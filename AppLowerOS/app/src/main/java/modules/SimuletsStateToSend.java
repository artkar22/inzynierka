package modules;


import java.io.Serializable;

/**
 * Created by Artur Karolak on 2017-02-27.
 */
public class SimuletsStateToSend implements Serializable {
    private static final long serialVersionUID = 140605814607823206L;
    private final String StateId;
    private final byte[] miniature;
    private final byte[] highlightedMiniature;

    public SimuletsStateToSend(final String StateId, final byte[] miniature, final byte[] highlightedMiniature) {
        this.StateId = StateId;
        this.miniature = miniature;
        this.highlightedMiniature = highlightedMiniature;
    }

    public String getStateId() {
        return StateId;
    }

    public byte[] getMiniature() {
        return miniature;
    }

    public byte[] getHighlightedMiniature() {
        return highlightedMiniature;
    }
}

