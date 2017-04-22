package dynamicGridActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import org.apache.commons.lang3.SerializationUtils;
import org.eclipse.californium.core.CoapClient;
import org.eclipse.californium.core.CoapResponse;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import Simulets.Simulet;
import Simulets.SimuletsState;
import TriggerSimulets.TriggerSimulet;
import modules.SimuletsStateToSend;

/**
 * Created by ArturK on 2017-02-28.
 */

public class GraphicalResourcesService {

    public void refreshSimuletsAndTriggersGraphics(final CoapClient client, final List<TriggerSimulet> triggers, final List<Simulet> simulets){
//        getMainIcons(client, triggers, simulets);
        getStateLists(client, triggers, simulets);
    }

//    private void getMainIcons(final CoapClient client,
//                              final List<TriggerSimulet> triggers,
//                              final List<Simulet> simulets) {
//        if (simulets.size() > 0) {
//            for (Simulet simulet : simulets) {
//                client.setURI(simulet.getMainIconResource());
//                CoapResponse resp = client.get();
//                BitmapFactory.Options opt = new BitmapFactory.Options();
//                opt.inDither = true;
//                opt.inPreferredConfig = Bitmap.Config.ARGB_8888;
//                byte[] imageByteArray = resp.getPayload();
//                Bitmap bitmap = BitmapFactory.decodeByteArray(imageByteArray, 0, imageByteArray.length, opt);
//                simulet.setMainIconBitmap(bitmap);
//
//            }
//        }
//        if (triggers.size() > 0) {
//            for (TriggerSimulet trigger : triggers) {
//                client.setURI(trigger.getMainIconResource());
//                CoapResponse resp = client.get();
//                BitmapFactory.Options opt = new BitmapFactory.Options();
//                opt.inDither = true;
//                opt.inPreferredConfig = Bitmap.Config.ARGB_8888;
//                byte[] imageByteArray = resp.getPayload();
//                Bitmap bitmap = BitmapFactory.decodeByteArray(imageByteArray, 0, imageByteArray.length, opt);
//                trigger.setMainIconBitmap(bitmap);
//            }
//        }
//    }

    private void getStateLists(CoapClient client, List<TriggerSimulet> triggers, List<Simulet> simulets) {
        if (simulets.size() > 0) {
            for (Simulet simulet : simulets) {
                client.setURI(simulet.getStatesListResource());
                CoapResponse resp = client.get();
                final SimuletsStateToSend[] recieved = SerializationUtils.deserialize(resp.getPayload());
                simulet.setStates(createListOfStates(recieved, simulet.getUriOfSimulet()));
            }
        }
        if (triggers.size() > 0) {
            for (TriggerSimulet trigger : triggers) {
                client.setURI(trigger.getStatesListResource());
                CoapResponse resp = client.get();
                final SimuletsStateToSend[] recieved = SerializationUtils.deserialize(resp.getPayload());
                trigger.setStates(createListOfStates(recieved, trigger.getUriOfTrigger()));
            }
        }
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
}
