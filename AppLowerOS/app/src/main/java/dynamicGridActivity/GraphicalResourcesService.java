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

import Simulets.ActionSimulet;
import Simulets.SimuletsState;
import TriggerSimulets.EventSimulet;
import deserializers.SimuletStateDeserializer;
import dynamicGrid.mapGenerator.map.MapDTO;
import modules.SimuletsStateToSend;

/**
 * Created by ArturK on 2017-02-28.
 */

public class GraphicalResourcesService extends AsyncTask {
    private ProgressDialog dialog;
    private CoapClient client;
    private List<EventSimulet> triggers;
    private List<ActionSimulet> actionSimulets;
    private GridActivity gridActivity;
    private MapDTO mapDTO;
    private SimuletDynamicAdapter adapter;

    public GraphicalResourcesService(final CoapClient client, final List<EventSimulet> triggers, final List<ActionSimulet> actionSimulets, final GridActivity gridActivity, MapDTO mapDTO, SimuletDynamicAdapter adapter){
        this.client = client;
        this.triggers = triggers;
        this.actionSimulets = actionSimulets;
        this.gridActivity = gridActivity;
        this.mapDTO = mapDTO;
        this.adapter = adapter;
        dialog = new ProgressDialog(gridActivity);
    }



    private void getStateLists(CoapClient client, List<EventSimulet> triggers, List<ActionSimulet> actionSimulets, GridActivity gridActivity) {
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
        if (actionSimulets.size() > 0) {
            for (ActionSimulet actionSimulet : actionSimulets) {
                client.setURI(actionSimulet.getStatesListResource());
                client.setTimeout(0);
                CoapResponse resp = client.get();
                final SimuletsStateToSend[] recieved = gsonDeserializer.fromJson(resp.getResponseText(),SimuletsStateToSend[].class);
                actionSimulet.setStates(createListOfStates(recieved, actionSimulet.getUriOfSimulet()));
            }
        }
        if (triggers.size() > 0) {
            for (EventSimulet trigger : triggers) {
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

        getStateLists(client, triggers, actionSimulets, gridActivity);
        adapter.bindPlacesInMapToTriggers(triggers, mapDTO);
        adapter.bindPlacesInMapToSimulets(actionSimulets, mapDTO, triggers.size());
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
