package options.timer;

import android.view.View;

/**
 * Created by ArturK on 2016-12-13.
 */
public class TimerButtonListener implements View.OnClickListener {
    private Boolean timerButtonOn;

    public TimerButtonListener() {
        timerButtonOn = false;
    }

    @Override
    public void onClick(View v) {
        if (timerButtonOn == false) {
            timerButtonOn = true;
        } else if (timerButtonOn == true) {
            timerButtonOn = false;
        }
    }

    public boolean getStatus() {
        return timerButtonOn;
    }
}
