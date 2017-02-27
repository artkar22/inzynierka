package Simulets;

import android.graphics.Bitmap;

/**
 * Created by ArturK on 2017-02-27.
 */

public class SimuletsState {

    private final String StateId;
    private final Bitmap miniature;

    public SimuletsState(final String StateId, final Bitmap miniature) {
        this.StateId = StateId;
        this.miniature = miniature;
    }

    public String getStateId() {
        return StateId;
    }

    public Bitmap getMiniature() {
        return miniature;
    }

}
