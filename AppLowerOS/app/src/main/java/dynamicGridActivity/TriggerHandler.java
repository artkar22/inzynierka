package dynamicGridActivity;

import android.util.Pair;

import org.eclipse.californium.core.CoapHandler;
import org.eclipse.californium.core.CoapResponse;

import TriggerSimulets.TriggerSimulet;
import TriggerSimulets.TriggerWrapper;

/**
 * Created by Inni on 2017-06-16.
 */

public class TriggerHandler implements CoapHandler{

    private TriggerWrapper wrapper;
    private TriggerSimulet trigger;

    public TriggerHandler(TriggerWrapper wrapper, TriggerSimulet trigger){
            this.wrapper = wrapper;
            this.trigger = trigger;
        }

        @Override
        public void onLoad(CoapResponse response) {
            if (!response.getResponseText().equals("no_action")) {
                if(!wrapper.getTriggerActionThread().isInProcessing(response.getResponseText())) {
                    wrapper.getTriggerActionThread().addToQueue(new Pair<TriggerSimulet, String>(trigger, response.getResponseText()));
                    wrapper.getTriggerActionThread().run();
                }
            }
        }

        @Override
        public void onError() {

        }
}
