package TriggerSimulets;

/**
 * Created by ArturK on 2017-05-04.
 */

public class TriggerWrapper {
    private TriggerSimulet trigger;
    private Thread triggerThread;
    private TriggerActionThread triggerActionThread;

    public TriggerWrapper(final TriggerSimulet trigger, final TriggerActionThread triggerActionThread){
        this.trigger =  trigger;
        this.triggerActionThread = triggerActionThread;
        this.triggerThread =  new Thread(this.triggerActionThread);
        this.triggerThread.start();
    }
    public TriggerSimulet getTrigger() {
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
