package dynamicGridActivity;

import android.app.Activity;
import android.graphics.Color;
import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;

import com.google.gson.Gson;

import org.eclipse.californium.core.CoapClient;
import org.eclipse.californium.core.network.CoapEndpoint;
import org.eclipse.californium.core.network.config.NetworkConfig;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.UnknownHostException;

import ApplicationData.ApplicationData;
import Simulets.Simulet;
import dynamicGrid.DynamicGridView;
import dynamicGrid.mapGenerator.MapGenerator;
import dynamicGrid.mapGenerator.map.PlaceInMapDTO;
import karolakpochwala.apploweros.R;
import karolakpochwala.apploweros.SendButtonListener;
import mainUtils.Consts;
import mainUtils.NetworkUtils;
import options.timer.TimerButtonListener;

public class GridActivity extends Activity {

    private static final String TAG = GridActivity.class.getName();

    private DynamicGridView gridView;
    private ApplicationData applicationData;
    private MapGenerator mapGenerator;
    private Gson gSON;
    private CoapClient client;
    private TimerButtonListener timerButton;

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
        gridView.setAdapter(new CheeseDynamicAdapter(this,
                applicationData.getSimulets(),
                applicationData.getAllMaps().get(0))); //TODO TYLKO PIERWSZA MAPA NA RAZIE
//        add callback to stop edit mode if needed
        this.createNewClient();
        Button playButton = (Button) findViewById(R.id.playButton);
        SendButtonListener listener = new SendButtonListener(client, applicationData.getAllMaps().get(0));//TODO WIECEJ MAPÓW BO TERA TYLKO PIERWSZA
        playButton.setOnClickListener(listener);
        this.createOptionButtons();
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
                    optionsLayout.setBackgroundColor(setBackgroundForOptions(placeInMap.getSimulet()));
//                    optionsLayout.setVisibility(View.VISIBLE);
                    this.simuletsOptionsLogicExecution(placeInMap.getSimulet(), (ImageView)((ViewGroup) view).getChildAt(0));
                }
            }

            private void simuletsOptionsLogicExecution(final Simulet simulet,final ImageView view) {
                if(timerButton.getStatus()){
                    view.setImageResource(simulet.getPictureNameOnTimer());
                    simulet.getOptionsStatus().setTimer(true);
                } else{
                    view.setImageResource(simulet.getPictureOff());
                    simulet.getOptionsStatus().setTimer(false);
                }
            }

            private int setBackgroundForOptions(Simulet simulet) {
                if (simulet.getPictureOff() == R.drawable.wiatraczek_off) {
                    return Consts.OPTIONS_BACKGROUND_COLOR_WIATRACZEK;
                } else if (simulet.getPictureOff() == R.drawable.zarowka_off) {
                    return Consts.OPTIONS_BACKGROUND_COLOR_LAMPKA;
                } else if (simulet.getPictureOff() == R.drawable.samochod_off) {
                    return Consts.OPTIONS_BACKGROUND_COLOR_SAMOCHOD;
                } else if (simulet.getPictureOff() == R.drawable.radio_off) {
                    return Consts.OPTIONS_BACKGROUND_COLOR_RADIO;
                }
                return Color.GRAY;
            }
        });


    }

    private void createOptionButtons() {
        this.timerButton = new TimerButtonListener();//add next options
        Button buttonView = (Button) findViewById(R.id.buttonTime);
        buttonView.setOnClickListener(timerButton);
    }

    @Override
    public void onBackPressed() {
        if (gridView.isEditMode()) {

            gridView.stopEditMode();
        } else {
            super.onBackPressed();
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
