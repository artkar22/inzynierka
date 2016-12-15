package options;

/**
 * Created by ArturK on 2016-12-13.
 */

//this is class for simulets
public class OptionsStatus {
    //here you can add options for simulets, for now it is on/off logic
    private boolean timer;
    private boolean forLoop;

    public OptionsStatus() {
        //default set all false
        timer = false;
        forLoop = false;
    }

    public boolean isTimer() {
        return timer;
    }

    public void setTimer(boolean timer) {
        this.timer = timer;
    }

    public boolean isForLoop() {
        return forLoop;
    }

    public void setForLoop(boolean forLoop) {
        this.forLoop = forLoop;
    }

}
