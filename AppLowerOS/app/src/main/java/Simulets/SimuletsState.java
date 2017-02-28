package Simulets;

import android.graphics.Bitmap;

import java.net.URI;

/**
 * Created by ArturK on 2017-02-27.
 */

public class SimuletsState {

    private final String StateId;
    private final Bitmap miniature;
    private URI simuletsURI;

    public SimuletsState(final String StateId, final Bitmap miniature, final URI simuletsURI) {
        this.StateId = StateId;
        this.miniature = miniature;
        this.simuletsURI = simuletsURI;
    }

    public String getStateId() {
        return StateId;
    }

    public Bitmap getMiniature() {
        return miniature;
    }

    public URI getSimuletsURI() {
        return simuletsURI;
    }
}
