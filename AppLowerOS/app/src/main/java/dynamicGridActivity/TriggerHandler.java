package dynamicGridActivity;

import android.util.Pair;

import org.eclipse.californium.core.CoapHandler;
import org.eclipse.californium.core.CoapResponse;

import TriggerSimulets.EventSimulet;
import TriggerSimulets.TriggerWrapper;

/**
 * Created by Inni on 2017-06-16.
 */

public class TriggerHandler implements CoapHandler{

    private TriggerWrapper wrapper;
    private EventSimulet trigger;

    public TriggerHandler(TriggerWrapper wrapper, EventSimulet trigger){
            this.wrapper = wrapper;
            this.trigger = trigger;
        }

        @Override
        public void onLoad(CoapResponse response) {
            if (!response.getResponseText().equals("no_action") && !"INITIAL".equals(response.getResponseText())) {
                if(!wrapper.getTriggerActionThread().isInProcessing(response.getResponseText())) {
                    wrapper.getTriggerActionThread().addToQueue(new Pair<EventSimulet, String>(trigger, response.getResponseText()));
                    wrapper.getTriggerActionThread().run();
                }
            }
        }

        @Override
        public void onError() {

        }
}
