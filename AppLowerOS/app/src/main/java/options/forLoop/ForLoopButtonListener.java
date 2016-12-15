package options.forLoop;


import android.view.View;

import karolakpochwala.apploweros.R;
import options.GlobalOptionsStates;

/**
 * Created by ArturK on 2016-12-14.
 */
public class ForLoopButtonListener implements View.OnClickListener {
//    private boolean forLoopButtonOn;
    private View buttonView;
    private final static int PICTURE_OFF = R.drawable.petla;
    private final static int PICTURE_ON = R.drawable.petla_nasycony;

    public ForLoopButtonListener(View buttonView) {
//        forLoopButtonOn = false;
        this.buttonView = buttonView;

    }

    @Override
    public void onClick(View v) {
        if (GlobalOptionsStates.FOR_LOOP_BUTTON_STATE == false) {
            GlobalOptionsStates.FOR_LOOP_BUTTON_STATE = true;
            GlobalOptionsStates.TIMER_BUTTON_STATE = false;
            buttonView.setBackgroundResource(PICTURE_ON);
        } else if (GlobalOptionsStates.FOR_LOOP_BUTTON_STATE == true) {
            GlobalOptionsStates.FOR_LOOP_BUTTON_STATE = false;
            GlobalOptionsStates.TIMER_BUTTON_STATE = false;
            buttonView.setBackgroundResource(PICTURE_OFF);

        }
    }

//    public boolean getStatus() {
//        return forLoopButtonOn;
//    }
}
