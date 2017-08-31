package TriggerSimulets;

import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.util.Pair;

import org.eclipse.californium.core.CoapClient;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import ApplicationData.ApplicationData;
import Simulets.SimuletsState;
import dynamicGrid.DynamicGridView;
import dynamicGrid.mapGenerator.map.MapDTO;
import dynamicGrid.mapGenerator.map.PlaceInMapDTO;
import dynamicGridActivity.GridActivity;
import mainUtils.Consts;

/**
 * Created by ArturK on 2016-12-29.
 */
public class TriggerActionThread implements Runnable {
    private static final long PAUSED_TIME_UNSET = 0L;
    private LinkedList<Pair<EventSimulet, String>> queue;
    private DynamicGridView gridView;
    private ApplicationData applicationData;
    private GridActivity gridActivity;
    private CoapClient client;
    private Handler delayHandler;
    private LinkedList<List<Message>> queueOfpendingMessages;
    private long pausedTime;
    private Map<Message, Long> messageTimeMap;
    private List<String> activeProcessess;

    public TriggerActionThread(final DynamicGridView gridView, final ApplicationData applicationData,
                               final GridActivity gridActivity, final CoapClient client) {
        this.gridView = gridView;
        this.applicationData = applicationData;
        this.gridActivity = gridActivity;
        this.client = client;
        activeProcessess = new LinkedList<>();
        queue = new LinkedList<>();
        queueOfpendingMessages = new LinkedList<>();
        messageTimeMap = new LinkedHashMap<>();
        if (delayHandler == null) {
            delayHandler = new Handler(new Handler.Callback() {
                @Override
                public boolean handleMessage(Message msg) {
                    for(List<Message> messages : queueOfpendingMessages){
                        if(messages.contains(msg)){
                            messages.remove(msg);
                        }
                    }
                    for(int x = 0; x < queueOfpendingMessages.size(); x++){
                        if(queueOfpendingMessages.get(x).isEmpty()){
                            queueOfpendingMessages.remove(x);
                        }
                    }
                    if (msg.obj instanceof Runnable) {
                        ((Runnable) msg.obj).run();
                    }
                    return true;
                }
            });
        }
    }

    @Override
    public void run() {

        gridActivity.runOnUiThread(new Runnable() {
            public void run() {
                while (queue.size() > 0) {
                    final Pair<EventSimulet, String> trigger = queue.remove(0);
                    final MapDTO currentMap = applicationData.getAllMaps().get(0);
                    final LinkedList<Integer> indexes = currentMap.getTriggersIndexes();
                    for (Integer index : indexes) {
                        final int indexVal = index.intValue();
                        final PlaceInMapDTO placeOfTrigger = currentMap.getPlacesInMap().get(indexVal);
                        if (placeOfTrigger.getSimuletState() != null && placeOfTrigger.getSimuletState().getStateId().equals(trigger.second)
                                && placeOfTrigger.getSimuletState().getSimuletsURI().equals(trigger.first.getUriOfTrigger())) {
                            activeProcessess.add(trigger.second);
                            executeSequenceForThatSimulet(indexVal, currentMap);
                        }
                    }
                }
            }
        });

    }

    private void executeSequenceForThatSimulet(final int indexVal, final MapDTO currentMap) {
        List<Message> myPendingMessages = new LinkedList<>();
        final int lastColumnIndex = indexVal + currentMap.getNumberOfColums();
        int index = indexVal + 1;
//        Handler handler1 = new Handler();
        int delay = 0;
//        int delay = executePreSequenceIconChange(handler1,index -1, currentMap);
        Message showPlayResumeBeforeSequence = Message.obtain();
        showPlayResumeBeforeSequence.obj = new PostDelayedPlayResumeButtonsManagment(gridActivity, true, this);
        myPendingMessages.add(showPlayResumeBeforeSequence);
        delayHandler.sendMessageDelayed(showPlayResumeBeforeSequence, delay);
        while (index < lastColumnIndex + 1) {
            Message mess = Message.obtain();
            if (currentMap.getPlacesInMap().size() > index && index != lastColumnIndex) {
                final PlaceInMapDTO dto = currentMap.getPlacesInMap().get(index);
                final SimuletsState currentSimulet = dto.getSimuletState();
                if (currentSimulet != null) {
                    delay = delay + getHowLongToWait(Consts.TIME_BEETWEEN_SIMULETS, false);
                    mess.obj = new PostDelayedRunnable(client, currentSimulet, index, gridView, currentMap, indexVal);
                    myPendingMessages.add(mess);
                    delayHandler.sendMessageDelayed(mess, delay);
//                        delayHandler.postDelayed(new PostDelayedRunnable(client,currentSimulet,index,gridView, currentMap, indexVal), delay);
                } else if(myPendingMessages.size()>0 && !myPendingMessages.get(myPendingMessages.size()-1).obj.getClass().getCanonicalName().equals(PostDelayedIconChange.class.getCanonicalName())) {
                    delay = delay + getHowLongToWait(Consts.TIME_BEETWEEN_SIMULETS, false);
                    mess.obj = new PostDelayedIconChange(index, gridView, currentMap, indexVal);
                    myPendingMessages.add(mess);
                    delayHandler.sendMessageDelayed(mess, delay);
//                        delayHandler.postDelayed(new PostDelayedIconChange(index, gridView, currentMap, indexVal), delay);
                }
            }
            else {
                delay = delay + getHowLongToWait(Consts.TIME_BEETWEEN_SIMULETS, false);
                mess.obj = new PostDelayedIconChange(index, gridView, currentMap, indexVal);
                myPendingMessages.add(mess);
                delayHandler.sendMessageDelayed(mess, delay);
//                    delayHandler.postDelayed(new PostDelayedIconChange(index, gridView, currentMap, indexVal), delay);
            }

            index++;
        }
        Message hidePlayResumeAfterSequence = Message.obtain();
        hidePlayResumeAfterSequence.obj = new PostDelayedPlayResumeButtonsManagment(gridActivity, false, this);
        myPendingMessages.add(hidePlayResumeAfterSequence);
        delayHandler.sendMessageDelayed(hidePlayResumeAfterSequence, delay);
        delayHandler.sendEmptyMessageAtTime(100, delay);
        queueOfpendingMessages.add(myPendingMessages);
//        hold();
    }

    private int executePreSequenceIconChange(final Handler handler, final int preindex, final MapDTO currentMap) {
        int delay = 0;
        final PlaceInMapDTO dto = currentMap.getPlacesInMap().get(preindex);
        final SimuletsState currentSimulet = dto.getSimuletState();
        if (currentSimulet != null) {
            delay = delay + getHowLongToWait(Consts.TIME_BEETWEEN_SIMULETS, false);
            handler.postDelayed(new PostDelayedIconChange(preindex, gridView, currentMap, 10000), delay);
        }
        return delay;
    }

    public void addToQueue(final Pair<EventSimulet, String> triggerAndState) {
        queue.add(triggerAndState);
    }
    public boolean isQueueEmpty(){
        if(queue.size() == 0) return true;
        return false;
    }

    private Pair<EventSimulet, String> removeFirst() {
        return queue.removeFirst();
    }

    private int getHowLongToWait(final int secs, final boolean simuletsTimerStatus) {
        if (simuletsTimerStatus) {
            return (Consts.TIME_BEETWEEN_SIMULETS_MULTIPLIER * secs * 1000);
        } else {
            return (secs * 1000);
        }
    }

    public void pause() {
        if (delayHandler != null && pausedTime == PAUSED_TIME_UNSET) {
            pausedTime = SystemClock.uptimeMillis();

            //need to copy the Messages because their data will get cleared in removeCallbacksAndMessages
            final LinkedList<List<Message>> copiedQueueOfpendingMessages = new LinkedList<>();
            for(List<Message> msgs : queueOfpendingMessages){
                List<Message> copiedMessages = new ArrayList<Message>();
                for (Message msg : msgs) {
                    Message copy = Message.obtain();
                    copy.obj = msg.obj;
                    messageTimeMap.put(copy, msg.getWhen()); //remember the time since unable to set directly on Message
                    copiedMessages.add(copy);
                }
                copiedQueueOfpendingMessages.add(copiedMessages);
            }

            //remove all messages from the handler
            delayHandler.removeCallbacksAndMessages(null);
            queueOfpendingMessages.clear();

            queueOfpendingMessages.addAll(copiedQueueOfpendingMessages);
        }
    }

    public void hold(){
        if (delayHandler != null && pausedTime != PAUSED_TIME_UNSET) {

            //need to copy the Messages because their data will get cleared in removeCallbacksAndMessages
            final LinkedList<List<Message>> copiedQueueOfpendingMessages = new LinkedList<>();
            for(List<Message> msgs : queueOfpendingMessages){
                List<Message> copiedMessages = new ArrayList<Message>();
                for (Message msg : msgs) {
                    Message copy = Message.obtain();
                    copy.obj = msg.obj;
                    messageTimeMap.put(copy, msg.getWhen()); //remember the time since unable to set directly on Message
                    copiedMessages.add(copy);
                }
                copiedQueueOfpendingMessages.add(copiedMessages);
            }

            //remove all messages from the handler
            delayHandler.removeCallbacksAndMessages(null);
            queueOfpendingMessages.clear();

            queueOfpendingMessages.addAll(copiedQueueOfpendingMessages);
        }
    }

    public void resume() {
        if (delayHandler != null && pausedTime != PAUSED_TIME_UNSET) {
            for(List<Message> msgs : queueOfpendingMessages){
                for (Message msg : msgs) {
                    long msgWhen = messageTimeMap.get(msg);
                    long timeLeftForMessage = msgWhen - pausedTime;
                    delayHandler.sendMessageDelayed(msg, timeLeftForMessage);
                }
            }
            messageTimeMap.clear();
            pausedTime = PAUSED_TIME_UNSET;
        }
    }

    public void nextStep() {
        if (delayHandler != null && pausedTime != PAUSED_TIME_UNSET && !queueOfpendingMessages.isEmpty()) {
            for(List<Message> msgs : queueOfpendingMessages){
                if(!msgs.isEmpty()) {
                    final Message nextStep = msgs.remove(0);
                    long msgWhen = messageTimeMap.remove(nextStep);
//            mapMessagesToNewTime();
                    long timeLeftForMessage = msgWhen - pausedTime;
                    delayHandler.sendMessageDelayed(nextStep, timeLeftForMessage);
                }
            }
            if(queueOfpendingMessages.size()>0 && queueOfpendingMessages.getFirst().size()>0){
                pausedTime = SystemClock.uptimeMillis();
            } else {
                resetPauseTime();
                gridActivity.resetPauseResumeButtons();
            }
        }
    }

    public boolean isInProcessing(String newProcess){
        return activeProcessess.contains(newProcess);
    }
    public boolean isProcessing() {
        return activeProcessess.size()>0;
    }

    public void removeFirstInProcessing() {
        if(activeProcessess.size()>0)
            activeProcessess.remove(0);
    }

    public void resetPauseTime() {
        pausedTime = PAUSED_TIME_UNSET;
    }

    public boolean isPausedTimeUnset() {
        return pausedTime == PAUSED_TIME_UNSET;
    }
//    private void mapMessagesToNewTime() {
//        final Map<Message, Long> newTimeMap = new LinkedHashMap<>();
//        for (Message msg : pendingMessages) {
//            newTimeMap.put(msg, messageTimeMap.get(msg) - pausedTime);
//        }
//        messageTimeMap = newTimeMap;
//    }
}
