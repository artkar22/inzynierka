package dynamicGridActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;

import com.google.gson.Gson;

import org.eclipse.californium.core.CoapClient;
import org.eclipse.californium.core.CoapHandler;
import org.eclipse.californium.core.CoapResponse;
import org.eclipse.californium.core.coap.CoAP;
import org.eclipse.californium.core.network.CoapEndpoint;
import org.eclipse.californium.core.network.config.NetworkConfig;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.UnknownHostException;
import java.util.ArrayList;

import ApplicationData.ApplicationData;
import Protocol.Comm_Protocol;
import Simulets.Simulet;
import TriggerSimulets.TriggerActionThread;
import TriggerSimulets.TriggerSimulet;
import TriggerSimulets.TriggerSimuletButtonListener;
import dynamicGrid.DynamicGridView;
import dynamicGrid.mapGenerator.MapGenerator;
import dynamicGrid.mapGenerator.map.MapDTO;
import dynamicGrid.mapGenerator.map.PlaceInMapDTO;
import karolakpochwala.apploweros.R;
import karolakpochwala.apploweros.SendButtonListener;
import mainUtils.Consts;
import mainUtils.NetworkUtils;
import options.GlobalOptionsStates;
import options.timer.TimerButtonListener;

public class GridActivity extends Activity {

    private static final String TAG = GridActivity.class.getName();

    private DynamicGridView gridView;
    private ApplicationData applicationData;
    private MapGenerator mapGenerator;
    private Gson gSON;
    private CoapClient client;
    private TimerButtonListener timerButton;
    private TriggerActionThread triggerActionThread;
    private Thread triggerThread;
//    private ForLoopButtonListener forLoopButton;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grid);
        gSON = new Gson();
        String ApplicationDataJSON = getIntent().getStringExtra(Consts.APPLICATION_DATA);
        applicationData = gSON.fromJson(ApplicationDataJSON, ApplicationData.class);
        applicationData.addMap(mapGenerator.loadMap(getAssets(), "map0.json"));//TODO SYSTEM WCZYTYWANIA MAP - POBIERANIE NAZW MAP Z KATALOGU I SYSTEM WCZYTYWANIA KOLEJNYCH
        gridView = (DynamicGridView) findViewById(R.id.dynamic_grid);

        gridView.setNumColumns(applicationData.getAllMaps().get(0).getNumberOfColums());
        createMapForFirstTrigger(applicationData.getTriggers(), applicationData.getAllMaps().get(0));
        if(applicationData.getTriggers().size()>0){
            gridView.setAdapter(new CheeseDynamicAdapter(this,
                    applicationData.getSimulets(),
                    applicationData.getTriggers().get(0),
                    applicationData.getAllMaps().get(0),
                    true)); //TODO TYLKO PIERWSZA MAPA NA RAZIE
        } else{
            gridView.setAdapter(new CheeseDynamicAdapter(this,
                    applicationData.getSimulets(),
                    null,
                    applicationData.getAllMaps().get(0),
                    true)); //TODO TYLKO PIERWSZA MAPA NA RAZIE
        }
//        add callback to stop edit mode if needed
        createMapForEachTrigger(applicationData.getTriggers(), applicationData.getAllMaps().get(0));
        this.createNewClient();
        this.setInitialStatusForSimulets();
        Button playButton = (Button) findViewById(R.id.playButton);
        SendButtonListener listener = new SendButtonListener(client, applicationData.getAllMaps().get(0), gridView);//TODO WIECEJ MAPÓW BO TERA TYLKO PIERWSZA
        playButton.setOnClickListener(listener);
        this.createOptionButtons();
        triggerActionThread = new TriggerActionThread(gridView, applicationData, this, client);
        triggerThread = new Thread(triggerActionThread);
        triggerThread.start();
        gridView.setOnDropListener(new DynamicGridView.OnDropListener() {
            @Override
            public void onActionDrop() {
                gridView.handleDrop();
                gridView.stopEditMode();
            }
        });
        gridView.setOnDragListener(new DynamicGridView.OnDragListener() {
            @Override
            public void onDragStarted(int position) {
                Log.d(TAG, "drag started at position " + position);
            }

            @Override
            public void onDragPositionsChanged(int oldPosition, int newPosition) {
                Log.d(TAG, String.format("drag item position changed from %d to %d", oldPosition, newPosition));
            }
        });
        gridView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                gridView.startEditMode(position);
                return true;
            }
        });

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                Toast.makeText(GridActivity.this, parent.getAdapter().getItem(position).toString(),
//                        Toast.LENGTH_SHORT).show();
                PlaceInMapDTO placeInMap = (PlaceInMapDTO) parent.getAdapter().getItem(position);
                if (placeInMap.getSimulet() != null) {
                    View optionsLayout = ((View) view.getParent().getParent()).findViewById(R.id.simulet_options);
//                    optionsLayout.setBackgroundColor(setBackgroundForOptions(placeInMap.getSimulet()));
//                    optionsLayout.setVisibility(View.VISIBLE);
                    this.simuletsOptionsLogicExecution(placeInMap.getSimulet(), (ImageView) ((ViewGroup) view).getChildAt(0));
                }
            }

            private void simuletsOptionsLogicExecution(final Simulet simulet, final ImageView view) {
                if (GlobalOptionsStates.TIMER_BUTTON_STATE) {//ustawiam timerbutton na simulecie
                    timerButtonLogic(simulet, view);
                }
//                else if (GlobalOptionsStates.FOR_LOOP_BUTTON_STATE) { ////NO_LONGER_ACTIVE
//                    forLoopButtonLogic(simulet, view);
//                }

            }

            private void timerButtonLogic(final Simulet simulet, final ImageView view) {
                if (simulet.isSimuletOn()) {
                    if (!simulet.getOptionsStatus().isTimer() && !simulet.getOptionsStatus().isForLoop()) {//gdy timer i loop nieustawione włacz timer bez loop
                        view.setImageResource(simulet.getPictureNameOnTimer());
                        simulet.getOptionsStatus().setTimer(true);
                    } else if (!simulet.getOptionsStatus().isTimer() && simulet.getOptionsStatus().isForLoop()) {//gdy timer nieustawiony a loop tak to włącz timer i wciąż z loop
                        view.setImageResource(simulet.getPictureNameOnPetlaTimer());
                        simulet.getOptionsStatus().setTimer(true);
                    } else if (simulet.getOptionsStatus().isTimer() && !simulet.getOptionsStatus().isForLoop()) {//gdy timer juz ustawiony a loop nie to wyłącz timer i wciąż bez loop
                        view.setImageResource(simulet.getPictureOn());
                        simulet.getOptionsStatus().setTimer(false);
                    } else if (simulet.getOptionsStatus().isTimer() && simulet.getOptionsStatus().isForLoop()) {//gdy timer juz ustawiony a loop tez ustawion to wyłącz timer i wciąż z loop
                        view.setImageResource(simulet.getPictureNameOnPetla());
                        simulet.getOptionsStatus().setTimer(false);
                    }
                } else { //tu wszystko jak wyżej tylko dla wyłączonego simuletu
                    if (!simulet.getOptionsStatus().isTimer() && !simulet.getOptionsStatus().isForLoop()) {
                        view.setImageResource(simulet.getPictureNameOffTimer());
                        simulet.getOptionsStatus().setTimer(true);
                    } else if (!simulet.getOptionsStatus().isTimer() && simulet.getOptionsStatus().isForLoop()) {//gdy timer nieustawiony a loop tak to włącz timer i wciąż z loop
                        view.setImageResource(simulet.getPictureNameOffPetlaTimer());
                        simulet.getOptionsStatus().setTimer(true);
                    } else if (simulet.getOptionsStatus().isTimer() && !simulet.getOptionsStatus().isForLoop()) {//gdy timer juz ustawiony a loop nie to wyłącz timer i wciąż bez loop
                        view.setImageResource(simulet.getPictureOff());
                        simulet.getOptionsStatus().setTimer(false);
                    } else if (simulet.getOptionsStatus().isTimer() && simulet.getOptionsStatus().isForLoop()) {//gdy timer juz ustawiony a loop tez ustawion to wyłącz timer i wciąż z loop
                        view.setImageResource(simulet.getPictureNameOffPetla());
                        simulet.getOptionsStatus().setTimer(false);
                    }
                }
            }

//            private void forLoopButtonLogic(final Simulet simulet, final ImageView view) {
//                if (simulet.isSimuletOn()) {
//                    if (!simulet.getOptionsStatus().isForLoop() && !simulet.getOptionsStatus().isTimer()) {//gdy loop i timer nieustawione włacz loop bez timer
//                        view.setImageResource(simulet.getPictureNameOnPetla());
//                        simulet.getOptionsStatus().setForLoop(true);
//                    } else if (!simulet.getOptionsStatus().isForLoop() && simulet.getOptionsStatus().isTimer()) {//gdy loop nieustawiony a timer tak to włącz loop i wciąż z timer
//                        view.setImageResource(simulet.getPictureNameOnPetlaTimer());
//                        simulet.getOptionsStatus().setForLoop(true);
//                    } else if (simulet.getOptionsStatus().isForLoop() && !simulet.getOptionsStatus().isTimer()) {//gdy loop juz ustawiony a timer nie to wyłącz loopi wciąż bez timer
//                        view.setImageResource(simulet.getPictureOn());
//                        simulet.getOptionsStatus().setForLoop(false);
//                    } else if (simulet.getOptionsStatus().isForLoop() && simulet.getOptionsStatus().isTimer()) {//gdy loop juz ustawiony a timer tez ustawion to wyłącz loop i wciąż z timer
//                        view.setImageResource(simulet.getPictureNameOnTimer());
//                        simulet.getOptionsStatus().setForLoop(false);
//                    }
//                } else { //tu wszystko jak wyżej tylko dla wyłączonego simuletu
//                    if (!simulet.getOptionsStatus().isForLoop() && !simulet.getOptionsStatus().isTimer()) {//gdy loop i timer nieustawione włacz loop bez timer
//                        view.setImageResource(simulet.getPictureNameOffPetla());
//                        simulet.getOptionsStatus().setForLoop(true);
//                    } else if (!simulet.getOptionsStatus().isForLoop() && simulet.getOptionsStatus().isTimer()) {//gdy loop nieustawiony a timer tak to włącz loop i wciąż z timer
//                        view.setImageResource(simulet.getPictureNameOffPetlaTimer());
//                        simulet.getOptionsStatus().setForLoop(true);
//                    } else if (simulet.getOptionsStatus().isForLoop() && !simulet.getOptionsStatus().isTimer()) {//gdy loop juz ustawiony a timer nie to wyłącz loopi wciąż bez timer
//                        view.setImageResource(simulet.getPictureOff());
//                        simulet.getOptionsStatus().setForLoop(false);
//                    } else if (simulet.getOptionsStatus().isForLoop() && simulet.getOptionsStatus().isTimer()) {//gdy loop juz ustawiony a timer tez ustawion to wyłącz loop i wciąż z timer
//                        view.setImageResource(simulet.getPictureNameOffTimer());
//                        simulet.getOptionsStatus().setForLoop(false);
//                    }
//                }
//            }

        });


    }

    private void createMapForFirstTrigger(ArrayList<TriggerSimulet> triggers, MapDTO currentMap) {
        if (triggers.size() > 0) {
            triggers.get(0).setMyPlacesInMap(currentMap.getPlacesInMap());
        }
    }

    private void createMapForEachTrigger(ArrayList<TriggerSimulet> triggers, MapDTO currentMap) {
        for (int x = 1; x < triggers.size(); x++) {
            triggers.get(x).deepCopyOfPlacesInMap(triggers.get(0).getMyPlacesInMap());
        }
    }

    private void setInitialStatusForSimulets() {
        final ArrayList<Simulet> listOfSimulets = applicationData.getSimulets();
        for (Simulet simulet : listOfSimulets) {
            client.setURI(simulet.getStatusResource());
            CoapResponse get = client.get();
            if (get.getCode().equals(CoAP.ResponseCode.CONTENT) && get.getResponseText().equals(Comm_Protocol.SWITCHED_ON)) {
                simulet.setSimuletOn(true);
            } else {
                simulet.setSimuletOn(false);
            }
        }
        for (final TriggerSimulet trigger : applicationData.getTriggers()) {
            client.setURI(trigger.getStatusResource());
            client.observe(new CoapHandler() {
                @Override
                public void onLoad(CoapResponse response) {
//                    response.getResponseText();
                    if (!response.getResponseText().equals("no_action")) {
                        triggerActionThread.addToQueue(trigger);
                        triggerActionThread.run();
//                        if(triggerThread.getState().equals(Thread.State.TERMINATED)){
//                            triggerThread.start();
//                        }
//                        gridView.setAdapter(new CheeseDynamicAdapter(gridView.getContext(),
//                                applicationData.getSimulets(),
//                                trigger,
//                                applicationData.getAllMaps().get(0),
//                                false));
                    }
                }

                @Override
                public void onError() {

                }
            });
        }
    }

    private void createOptionButtons() {
        for (int x = 0; x < this.applicationData.getTriggers().size(); x++) {//TODO bezsensowne rozwiązanie ale nie mam chwilowo pomysłu
            if (x == 0) {
                TriggerSimuletButtonListener listener = new TriggerSimuletButtonListener(findViewById(R.id.trigger0),
                        gridView, this.applicationData.getTriggers().get(x), this.applicationData);
                (findViewById(R.id.trigger0)).setOnClickListener(listener);
                findViewById(R.id.trigger0).setVisibility(View.VISIBLE);
            } else if (x == 1) {
                TriggerSimuletButtonListener listener = new TriggerSimuletButtonListener(findViewById(R.id.trigger1),
                        gridView, this.applicationData.getTriggers().get(x), this.applicationData);
                (findViewById(R.id.trigger1)).setOnClickListener(listener);
                findViewById(R.id.trigger1).setVisibility(View.VISIBLE);
            }

        }
        this.timerButton = new TimerButtonListener(findViewById(R.id.buttonTime));//add next options
        (findViewById(R.id.buttonTime)).setOnClickListener(timerButton);

//        this.forLoopButton = new ForLoopButtonListener(findViewById(R.id.buttonFor));
//        (findViewById(R.id.buttonFor)).setOnClickListener(forLoopButton);
    }

    @Override
    public void onBackPressed() {
        if (gridView.isEditMode()) {

            gridView.stopEditMode();
        } else {
            Intent intent = new Intent();
            setResult(0, intent);
            finish();//finishing activity
//            super.onBackPressed();
        }
    }

    private void createNewClient() {
        client = new CoapClient();
        try {
            InetAddress addr = InetAddress.getByName(NetworkUtils.getIPofCurrentMachine());
            InetSocketAddress adr = new InetSocketAddress(addr, NetworkUtils.PORT);
            URI uri = new URI("coap://192.168.2.2:11111");
//	            URI uri = new URI("coap://192.168.2.2:11111/Lampka");
//	            //URI uri = new URI("coap://127.0.0.1:11111");
//            client = new CoapClient(uri);
            client.setURI(uri.toString());
            CoapEndpoint endpoint = new CoapEndpoint(adr, NetworkConfig.createStandardWithoutFile());
            endpoint.start();
            client = client.setEndpoint(endpoint);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
