package modules;


import java.io.Serializable;

public class SimuletsStateToSend implements Serializable {
    private static final long serialVersionUID = 140605814607823206L;
    private final String StateId;
    private final byte[] miniature;
    private final byte[] highlightedMiniature;
    private final String eventType;

    public SimuletsStateToSend(final String StateId, final byte[] miniature, final byte[] highlightedMiniature, final String eventType) {
        this.StateId = StateId;
        this.miniature = miniature;
        this.highlightedMiniature = highlightedMiniature;
        this.eventType = eventType;
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

    public String getEventType() { return eventType; }
}

