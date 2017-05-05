package dynamicGridActivity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.graphics.drawable.DrawableUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

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
import TriggerSimulets.TriggerWrapper;
import dynamicGrid.DynamicGridView;
import dynamicGrid.mapGenerator.MapGenerator;
import dynamicGrid.mapGenerator.map.MapDTO;
import dynamicGrid.mapGenerator.map.MapDTOBuilder;
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
    private ArrayList<TriggerWrapper> triggerWrappers;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grid);
        gSON = new Gson();
        final String ApplicationDataJSON = getIntent().getStringExtra(Consts.APPLICATION_DATA);
        applicationData = gSON.fromJson(ApplicationDataJSON, ApplicationData.class);
        applicationData.addMap(mapGenerator.loadMap(getAssets(), "map0.json"));//TODO SYSTEM WCZYTYWANIA MAP - POBIERANIE NAZW MAP Z KATALOGU I SYSTEM WCZYTYWANIA KOLEJNYCH
        gridView = (DynamicGridView) findViewById(R.id.dynamic_grid);
        gridView.setNumColumns(applicationData.getAllMaps().get(0).getNumberOfColums());
        this.createNewClient();
        final GraphicalResourcesService resourcesService = new GraphicalResourcesService();
        resourcesService.refreshSimuletsAndTriggersGraphics(client, applicationData.getTriggers(), applicationData.getSimulets());
//        OptionButtonsUtils.createMapForFirstTrigger(applicationData.getTriggers(), applicationData.getAllMaps().get(0));
//        if (applicationData.getTriggers().size() > 0) {
//            gridView.setAdapter(new CheeseDynamicAdapter(this,
//                    applicationData.getSimulets(),
//                    applicationData.getTriggers().get(0),
//                    applicationData.getAllMaps().get(0),
//                    true)); //TODO TYLKO PIERWSZA MAPA NA RAZIE
//        } else {
        gridView.setAdapter(new CheeseDynamicAdapter(this,
                applicationData.getSimulets(),
                applicationData.getTriggers(),
                applicationData.getAllMaps().get(0),
                true)); //TODO TYLKO PIERWSZA MAPA NA RAZIE
//        }
////        add callback to stop edit mode if needed
//        OptionButtonsUtils.createMapForEachTrigger(applicationData.getTriggers());
        this.createNewClient();
        Button playButton = (Button) findViewById(R.id.playButton);
        SendButtonListener listener = new SendButtonListener(client, applicationData.getAllMaps().get(0), gridView);//TODO WIECEJ MAPÓW BO TERA TYLKO PIERWSZA
        playButton.setOnClickListener(listener);

        createTriggersHandling();
        OptionButtonsUtils.setInitialStatusForSimulets(triggerWrappers, client);
        OptionButtonsUtils.createOptionButtons(this, gridView, applicationData, client);

//        refreshButtonHandling();
//        createSimuletsAndTriggersBar();


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
                if (!applicationData.getAllMaps().get(0).getPlacesInMap().get(position).getTypeOfPlace()
                        .equals(MapDTOBuilder.ARROW_PLACE) && !applicationData.getAllMaps().get(0).getPlacesInMap().get(position).getTypeOfPlace()
                        .equals(MapDTOBuilder.SPACE_BEETWEEN)) {
                    gridView.startEditMode(position);
                }
                return true;
            }
        });

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                Toast.makeText(GridActivity.this, parent.getAdapter().getItem(position).toString(),
//                        Toast.LENGTH_SHORT).show();
//                PlaceInMapDTO placeInMap = (PlaceInMapDTO) parent.getAdapter().getItem(position);
//                if (placeInMap.getSimulet() != null) {
//                    View optionsLayout = ((View) view.getParent().getParent()).findViewById(R.id.simulet_options);
////                    optionsLayout.setBackgroundColor(setBackgroundForOptions(placeInMap.getSimulet()));
////                    optionsLayout.setVisibility(View.VISIBLE);
//                    this.simuletsOptionsLogicExecution(placeInMap.getSimulet(), (ImageView) ((ViewGroup) view).getChildAt(0));
//                }
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

//    private void createSimuletsAndTriggersBar() {
//        LinearLayout simuletsBar = (LinearLayout) findViewById(R.id.simulet_options);
//        for (int i = 0; i < this.applicationData.getTriggers().size(); i++) {
//            ImageView ii = new ImageView(this);
//            ii.setImageBitmap(this.applicationData.getTriggers().get(i).getMainIconBitmap());
//            simuletsBar.addView(ii);
//        }
//        for (int i = 0; i < this.applicationData.getSimulets().size(); i++) {
//            ImageView ii = new ImageView(this);
//            ii.setImageBitmap(this.applicationData.getSimulets().get(i).getMainIconBitmap());
//            simuletsBar.addView(ii);
//        }
//    }

//    private void refreshButtonHandling() {
//        final Button refreshButton = (Button) findViewById(R.id.refresh);
//        if (applicationData.getSimulets().size() < 4 || applicationData.getTriggers().size() < 2) {
//            refreshButton.setVisibility(View.VISIBLE);
//        }
//        refreshButton.setVisibility(View.VISIBLE);//TODO usunąć, na potrzeby testów
//        refreshButton.setOnClickListener(new RefreshButtonListener(applicationData, this, client, triggerActionThread));
//    }

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

    private void createTriggersHandling() {
        triggerWrappers = new ArrayList<>();
        final ArrayList<TriggerSimulet> triggers = applicationData.getTriggers();
        for (final TriggerSimulet trigger : triggers) {
            final TriggerWrapper wrapper = new TriggerWrapper(trigger, new TriggerActionThread(gridView, applicationData, this, client));
            triggerWrappers.add(wrapper);
//
// trigger.createTriggerThread(new TriggerActionThread(gridView, applicationData, this, client));
        }
    }
}
