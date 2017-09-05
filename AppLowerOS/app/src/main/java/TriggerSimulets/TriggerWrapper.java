package TriggerSimulets;


public class TriggerWrapper {
    private EventSimulet trigger;
    private Thread triggerThread;
    private TriggerActionThread triggerActionThread;

    public TriggerWrapper(final EventSimulet trigger, final TriggerActionThread triggerActionThread){
        this.trigger =  trigger;
        this.triggerActionThread = triggerActionThread;
        this.triggerThread =  new Thread(this.triggerActionThread);
        this.triggerThread.start();
    }
    public EventSimulet getTrigger() {
        return trigger;
    }
    public Thread getTriggerThread() {
        return triggerThread;
    }

    public void createTriggerThread(final TriggerActionThread triggerActionThread) {
        this.triggerActionThread = triggerActionThread;
        this.triggerThread =  new Thread(this.triggerActionThread);
        this.triggerThread.start();
    }
    public TriggerActionThread getTriggerActionThread() {
        return triggerActionThread;
    }
}
