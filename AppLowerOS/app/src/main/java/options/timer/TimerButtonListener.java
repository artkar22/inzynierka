package options.timer;

import android.view.View;

import karolakpochwala.apploweros.R;
import options.GlobalOptionsStates;

/**
 * Created by ArturK on 2016-12-13.
 */
public class TimerButtonListener implements View.OnClickListener {
//    private boolean timerButtonOn;
    private View buttonView;
    private final static int PICTURE_OFF = R.drawable.time;
    private final static int PICTURE_ON = R.drawable.timer_nasycony;

    public TimerButtonListener(View buttonView) {
//        timerButtonOn = false;
        this.buttonView = buttonView;

    }

    @Override
    public void onClick(View v) {
        if (GlobalOptionsStates.TIMER_BUTTON_STATE == false) {
            GlobalOptionsStates.TIMER_BUTTON_STATE = true;
            GlobalOptionsStates.FOR_LOOP_BUTTON_STATE = false;
            buttonView.setBackgroundResource(PICTURE_ON);
        } else if (GlobalOptionsStates.TIMER_BUTTON_STATE == true) {
            GlobalOptionsStates.TIMER_BUTTON_STATE = false;
            GlobalOptionsStates.FOR_LOOP_BUTTON_STATE = false;
            buttonView.setBackgroundResource(PICTURE_OFF);

        }
    }

//    public boolean getStatus() {
//        return timerButtonOn;
//    }
}
