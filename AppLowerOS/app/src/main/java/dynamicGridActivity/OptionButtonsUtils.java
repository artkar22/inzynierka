package dynamicGridActivity;

import android.view.View;

import org.eclipse.californium.core.CoapClient;

import java.util.ArrayList;

import ApplicationData.ApplicationData;
import TriggerSimulets.EventSimulet;
import TriggerSimulets.TriggerWrapper;
import dynamicGrid.DynamicGridView;
import dynamicGrid.mapGenerator.map.MapDTO;
import karolakpochwala.apploweros.R;

public class OptionButtonsUtils {


    public static void createOptionButtons(final GridActivity activity, final DynamicGridView gridView,
                                           final ApplicationData applicationData, final CoapClient client) {
    }

    private static void setPictureForOption(final EventSimulet currentTrigger, final View viewById) {
        if (currentTrigger.getName().equals("Trigger_1")) {
                viewById.setBackgroundResource(R.drawable.trigger_1_off);
        } else if (currentTrigger.getName().equals("Trigger_2")) {
            viewById.setBackgroundResource(R.drawable.trigger_2_off);
        }
    }

    public static void createMapForFirstTrigger(ArrayList<EventSimulet> triggers, MapDTO currentMap) {
        if (triggers.size() > 0) {
            triggers.get(0).deepCopyOfPlacesInMap(currentMap.getPlacesInMap());
        }
    }

    public static void createMapForEachTrigger(ArrayList<EventSimulet> triggers) {
        for (int x = 1; x < triggers.size(); x++) {
            triggers.get(x).deepCopyOfPlacesInMap(triggers.get(0).getMyPlacesInMap());
        }
    }

    public static void setInitialStatusForSimulets(final ArrayList<TriggerWrapper> triggerWrappers, final CoapClient client) {
//        final ArrayList<ActionSimulet> listOfSimulets = applicationData.getActionSimulets();
//        for (ActionSimulet simulet : listOfSimulets) {
//            client.setURI(simulet.getStatusResource());
//            CoapResponse get = client.get();
//            if (get.getCode().equals(CoAP.ResponseCode.CONTENT) && get.getResponseText().equals(Comm_Protocol.SWITCHED_ON)) {
//                simulet.setSimuletOn(true);
//            } else {
//                simulet.setSimuletOn(false);
//            }
//        }
        for (TriggerWrapper wrapper : triggerWrappers) {
            EventSimulet trigger = wrapper.getTrigger();
            client.setURI(trigger.getStatusResource());
            client.observe(new TriggerHandler(wrapper, trigger));
        }
    }
}
