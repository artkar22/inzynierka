package dynamicGridActivity;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.eclipse.californium.core.CoapClient;
import org.eclipse.californium.core.CoapResponse;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import Simulets.Simulet;
import Simulets.SimuletsState;
import TriggerSimulets.TriggerSimulet;
import deserializers.SimuletStateDeserializer;
import dynamicGrid.mapGenerator.map.MapDTO;
import modules.SimuletsStateToSend;

/**
 * Created by ArturK on 2017-02-28.
 */

public class GraphicalResourcesService extends AsyncTask {
    private ProgressDialog dialog;
    private CoapClient client;
    private List<TriggerSimulet> triggers;
    private List<Simulet> simulets;
    private GridActivity gridActivity;
    private MapDTO mapDTO;
    private SimuletDynamicAdapter adapter;

    public GraphicalResourcesService(final CoapClient client, final List<TriggerSimulet> triggers, final List<Simulet> simulets, final GridActivity gridActivity, MapDTO mapDTO, SimuletDynamicAdapter adapter){
//        getMainIcons(client, triggers, simulets);
        this.client = client;
        this.triggers = triggers;
        this.simulets = simulets;
        this.gridActivity = gridActivity;
        this.mapDTO = mapDTO;
        this.adapter = adapter;
        dialog = new ProgressDialog(gridActivity);
    }



    private void getStateLists(CoapClient client, List<TriggerSimulet> triggers, List<Simulet> simulets, GridActivity gridActivity) {
        gridActivity.runOnUiThread(new Runnable() {
            public void run() {
                dialog.setMessage("Ładowanie grafik ...");
                dialog.setCancelable(false);
                dialog.setInverseBackgroundForced(false);
                dialog.show();
            }
        });
        final Gson gsonDeserializer = new GsonBuilder().registerTypeAdapter(SimuletsStateToSend.class,
                new SimuletStateDeserializer()).create();
        if (simulets.size() > 0) {
            for (Simulet simulet : simulets) {
                client.setURI(simulet.getStatesListResource());
                client.setTimeout(0);
                CoapResponse resp = client.get();
                final SimuletsStateToSend[] recieved = gsonDeserializer.fromJson(resp.getResponseText(),SimuletsStateToSend[].class);
                simulet.setStates(createListOfStates(recieved, simulet.getUriOfSimulet()));
            }
        }
        if (triggers.size() > 0) {
            for (TriggerSimulet trigger : triggers) {
                client.setURI(trigger.getStatesListResource());
                client.setTimeout(0);
                CoapResponse resp = client.get();
                final SimuletsStateToSend[] recieved = gsonDeserializer.fromJson(resp.getResponseText(),SimuletsStateToSend[].class);
                trigger.setStates(createListOfStates(recieved, trigger.getUriOfTrigger()));
            }
        }
        gridActivity.runOnUiThread(new Runnable() {
            public void run() {
                dialog.dismiss();
            }
        });
    }

    private List<SimuletsState> createListOfStates(final SimuletsStateToSend[] recieved, final URI uriOfSimulet) {
        final List<SimuletsState> states = new ArrayList<>();
        for (int x = 0; x < recieved.length; x++) {
            SimuletsState state = new SimuletsState(recieved[x].getStateId(), convertMiniatureToBitmap(recieved[x].getMiniature()), convertMiniatureToBitmap(recieved[x].getHighlightedMiniature()), uriOfSimulet);
            states.add(state);
        }
        return states;
    }

    private Bitmap convertMiniatureToBitmap(final byte[] miniature) {
        BitmapFactory.Options opt = new BitmapFactory.Options();
        opt.inDither = true;
        opt.inPreferredConfig = Bitmap.Config.ARGB_8888;
        return BitmapFactory.decodeByteArray(miniature, 0, miniature.length, opt);
    }

    @Override
    protected Object doInBackground(Object[] params) {

        getStateLists(client, triggers, simulets, gridActivity);
        adapter.bindPlacesInMapToTriggers(triggers, mapDTO);
        adapter.bindPlacesInMapToSimulets(simulets, mapDTO, triggers.size());
        adapter.bindPlaceInMapToPauseSimulet(mapDTO);
        gridActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                adapter.notifyDataSetChanged();
            }
        });
        return null;
    }
}
