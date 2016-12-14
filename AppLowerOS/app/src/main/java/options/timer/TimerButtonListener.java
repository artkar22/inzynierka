package options.timer;

import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ViewGroup;

import karolakpochwala.apploweros.R;

/**
 * Created by ArturK on 2016-12-13.
 */
public class TimerButtonListener implements View.OnClickListener {
    private boolean timerButtonOn;
    private View buttonView;
    private final static int PICTURE_OFF = R.drawable.time;
    private final static int PICTURE_ON = R.drawable.timer_nasycony;

    public TimerButtonListener(View buttonView) {
        timerButtonOn = false;
        this.buttonView = buttonView;

    }

    @Override
    public void onClick(View v) {
        if (timerButtonOn == false) {
            timerButtonOn = true;
            buttonView.setBackgroundResource(PICTURE_ON);
        } else if (timerButtonOn == true) {
            timerButtonOn = false;
            buttonView.setBackgroundResource(PICTURE_OFF);

        }
    }

    public boolean getStatus() {
        return timerButtonOn;
    }
}
