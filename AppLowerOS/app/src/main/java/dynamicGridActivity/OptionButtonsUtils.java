package dynamicGridActivity;

import android.view.View;

import org.eclipse.californium.core.CoapClient;
import org.eclipse.californium.core.CoapHandler;
import org.eclipse.californium.core.CoapResponse;
import org.eclipse.californium.core.coap.CoAP;

import java.util.ArrayList;

import ApplicationData.ApplicationData;
import Protocol.Comm_Protocol;
import Simulets.Simulet;
import TriggerSimulets.TriggerActionThread;
import TriggerSimulets.TriggerSimulet;
import TriggerSimulets.TriggerSimuletButtonListener;
import dynamicGrid.DynamicGridView;
import dynamicGrid.mapGenerator.map.MapDTO;
import karolakpochwala.apploweros.R;
import options.timer.TimerButtonListener;

/**
 * Created by ArturK on 2017-01-08.
 */
public class OptionButtonsUtils {


    public static void createOptionButtons(final GridActivity activity, final DynamicGridView gridView,
                                           final ApplicationData applicationData, final CoapClient client) {
        for (int x = 0; x < applicationData.getTriggers().size(); x++) {//TODO bezsensowne rozwiązanie ale nie mam chwilowo pomysłu
            final TriggerSimulet currentTrigger = applicationData.getTriggers().get(x);
            client.setURI(currentTrigger.getNameResource());
            currentTrigger.setName(client.get().getResponseText());
            if (x == 0) {
                TriggerSimuletButtonListener listener = new TriggerSimuletButtonListener(activity.findViewById(R.id.trigger0),
                        gridView, currentTrigger, applicationData);
                (activity.findViewById(R.id.trigger0)).setOnClickListener(listener);
                activity.findViewById(R.id.trigger0).setVisibility(View.VISIBLE);
                setPictureForOption(currentTrigger, activity.findViewById(R.id.trigger0));
            } else if (x == 1) {
                TriggerSimuletButtonListener listener = new TriggerSimuletButtonListener(activity.findViewById(R.id.trigger1),
                        gridView, currentTrigger, applicationData);
                (activity.findViewById(R.id.trigger1)).setOnClickListener(listener);
                activity.findViewById(R.id.trigger1).setVisibility(View.VISIBLE);
                setPictureForOption(currentTrigger, activity.findViewById(R.id.trigger1));
            }

        }
        final TimerButtonListener timerButton = new TimerButtonListener(activity.findViewById(R.id.buttonTime));//add next options
        (activity.findViewById(R.id.buttonTime)).setOnClickListener(timerButton);

//        this.forLoopButton = new ForLoopButtonListener(findViewById(R.id.buttonFor));
//        (findViewById(R.id.buttonFor)).setOnClickListener(forLoopButton);
    }

    private static void setPictureForOption(final TriggerSimulet currentTrigger, final View viewById) {
        if (currentTrigger.getName().equals("Trigger_1")) {
                viewById.setBackgroundResource(R.drawable.trigger_1_off);
        } else if (currentTrigger.getName().equals("Trigger_2")) {
            viewById.setBackgroundResource(R.drawable.trigger_2_off);
        }
    }

    public static void createMapForFirstTrigger(ArrayList<TriggerSimulet> triggers, MapDTO currentMap) {
        if (triggers.size() > 0) {
            triggers.get(0).deepCopyOfPlacesInMap(currentMap.getPlacesInMap());
        }
    }

    public static void createMapForEachTrigger(ArrayList<TriggerSimulet> triggers) {
        for (int x = 1; x < triggers.size(); x++) {
            triggers.get(x).deepCopyOfPlacesInMap(triggers.get(0).getMyPlacesInMap());
        }
    }

    public static void setInitialStatusForSimulets(final ApplicationData applicationData, final CoapClient client,
                                                   final TriggerActionThread triggerActionThread) {
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
}
