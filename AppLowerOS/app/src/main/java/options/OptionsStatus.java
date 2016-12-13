package options;

/**
 * Created by ArturK on 2016-12-13.
 */
public class OptionsStatus {
    //here you can add options for simulets, for now it is on/off logic
    private boolean timer;

    public OptionsStatus() {
        //default set all false
        timer = false;
    }

    public boolean isTimer() {
        return timer;
    }

    public void setTimer(boolean timer) {
        this.timer = timer;
    }

}
