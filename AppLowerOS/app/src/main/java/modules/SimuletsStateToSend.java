package modules;


import java.io.Serializable;

/**
 * Created by Artur Karolak on 2017-02-27.
 */
public class SimuletsStateToSend implements Serializable {
    private static final long serialVersionUID = 140605814607823206L;
    private final String StateId;
    private final byte[] miniature;

    public SimuletsStateToSend(final String StateId, final byte[] miniature) {
        this.StateId = StateId;
        this.miniature = miniature;
    }

    public String getStateId() {
        return StateId;
    }

    public byte[] getMiniature() {
        return miniature;
    }
}

