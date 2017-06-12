package karolakpochwala.apploweros;

import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;

import org.eclipse.californium.core.CoapClient;
import org.eclipse.californium.core.CoapResponse;
import org.eclipse.californium.core.coap.CoAP;

import java.util.ArrayList;

import Protocol.Comm_Protocol;
import Simulets.Simulet;
import TriggerSimulets.TriggerWrapper;
import dynamicGrid.DynamicGridView;
import dynamicGrid.mapGenerator.map.MapDTO;
import dynamicGrid.mapGenerator.map.PlaceInMapDTO;
import mainUtils.Consts;

/**
 * Created by Inni on 2016-03-05.
 */
public class SendButtonListener implements View.OnClickListener {

    private ArrayList<TriggerWrapper> triggerWrappers;
    private boolean isPauseButton = true;//if not, it is play button;

    public SendButtonListener(final ArrayList<TriggerWrapper> triggerWrappers) {
        super();
        this.triggerWrappers = triggerWrappers;
    }

    @Override
    public void onClick(View v) { //TODO SEND BUTTON
        if(isPauseButton){
            isPauseButton = false;
            v.setBackgroundResource(R.drawable.play_button_2);
            for (TriggerWrapper wrapper: triggerWrappers) {
                wrapper.getTriggerActionThread().pause();
            }
        } else {
            isPauseButton = true;
            v.setBackgroundResource(R.drawable.pause_3);
            for (TriggerWrapper wrapper: triggerWrappers) {
                wrapper.getTriggerActionThread().resume();
            }
        }
    }

    private int getHowLongToWait(final int secs, final boolean simuletsTimerStatus) {
        if (simuletsTimerStatus) {
            return (Consts.TIME_BEETWEEN_SIMULETS_MULTIPLIER * secs * 1000);
        } else {
            return (secs * 1000);
        }
    }

    private void waitSomeSecs(final int secs, final boolean simuletsTimerStatus) {
        try {
            synchronized (this) {
                if (simuletsTimerStatus) {
                    wait(Consts.TIME_BEETWEEN_SIMULETS_MULTIPLIER * secs * 1000);
                } else {
                    wait(secs * 1000);
                }
            }
        } catch (InterruptedException ex) {
        }
    }

    private int getPictureForSimulet(Simulet simulet) {
        if (simulet.isSimuletOn()) {
            if (simulet.getOptionsStatus().isTimer()) {
                return simulet.getPictureNameOnTimer();
            } else {
                return simulet.getPictureOn();
            }
        } else {
            if (simulet.getOptionsStatus().isTimer()) {
                return simulet.getPictureNameOffTimer();
            } else {
                return simulet.getPictureOff();
            }
        }

    }

    public void reset(View viewById) {

        isPauseButton = true;
        viewById.setBackgroundResource(R.drawable.pause_3);
    }
}
