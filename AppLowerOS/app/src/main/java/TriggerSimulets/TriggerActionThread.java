package TriggerSimulets;

import android.os.Handler;
import android.util.Pair;
import android.widget.ImageView;
import android.widget.LinearLayout;

import org.eclipse.californium.core.CoapClient;
import org.eclipse.californium.core.CoapResponse;
import org.eclipse.californium.core.coap.CoAP;

import java.util.LinkedList;

import ApplicationData.ApplicationData;
import Protocol.Comm_Protocol;
import Simulets.Simulet;
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

    private LinkedList<Pair<TriggerSimulet, String>> queue;
    private DynamicGridView gridView;
    private ApplicationData applicationData;
    private GridActivity gridActivity;
    private CoapClient client;

    public TriggerActionThread(final DynamicGridView gridView, final ApplicationData applicationData,
                               final GridActivity gridActivity, final CoapClient client) {
        this.gridView = gridView;
        this.applicationData = applicationData;
        this.gridActivity = gridActivity;
        this.client = client;
        queue = new LinkedList<>();
    }

    @Override
    public void run() {

        gridActivity.runOnUiThread(new Runnable() {
            public void run() {
                while (queue.size() > 0) {
                    final Pair<TriggerSimulet, String> trigger = removeFirst();
                    final MapDTO currentMap = applicationData.getAllMaps().get(0);
                    final LinkedList<Integer> indexes = currentMap.getTriggersIndexes();
                    for (Integer index : indexes) {
                        final int indexVal = index.intValue();
                        final PlaceInMapDTO placeOfTrigger = currentMap.getPlacesInMap().get(indexVal);
                        if (placeOfTrigger.getSimuletState() != null && placeOfTrigger.getSimuletState().getStateId().equals(trigger.second)
                                && placeOfTrigger.getSimuletState().getSimuletsURI().equals(trigger.first.getUriOfTrigger())) {
                            executeSequenceForThatSimulet(indexVal, currentMap);
                        }
                    }
//                    gridView.setAdapter(new CheeseDynamicAdapter(gridView.getContext(),
//                            applicationData.getSimulets(),
//                            trigger,
//                            currentMap,
//                            false));
//                    Handler handler1 = new Handler();
//                    int delay = 0;
//                    for (final Integer specialPlaceId : currentMap.getSpecialPlacesIds()) {
//                        final PlaceInMapDTO dto = trigger.getMyPlacesInMap().get(specialPlaceId.intValue());
//                        final Simulet currentSimulet = dto.getSimulet();
//                        if (currentSimulet != null) {
//                            delay = delay + getHowLongToWait(Consts.TIME_BEETWEEN_SIMULETS, currentSimulet.getOptionsStatus().isTimer());
//                            handler1.postDelayed(new Runnable() {
//
//                                @Override
//                                public void run() {
//                                    client.setURI(currentSimulet.getStatusResource());
//
//                                    CoapResponse get = client.get();
//                                    if (get.getCode().equals(CoAP.ResponseCode.CONTENT) && get.getResponseText().equals(Comm_Protocol.SWITCHED_OFF)) {
//                                        CoapResponse put = client.put(Comm_Protocol.SWITCHED_ON, 0);
//                                        if (put.isSuccess()) {
//                                            currentSimulet.setSimuletOn(true);
//                                            ((ImageView) ((LinearLayout) gridView.getChildAt(specialPlaceId.intValue())).getChildAt(0)).setImageResource(getPictureForSimulet(currentSimulet));
//
//                                        }
//                                    } else if (get.getCode().equals(CoAP.ResponseCode.CONTENT) && get.getResponseText().equals(Comm_Protocol.SWITCHED_ON)) {
//                                        CoapResponse put = client.put(Comm_Protocol.SWITCHED_OFF, 0);
//                                        if (put.isSuccess()) {
//                                            currentSimulet.setSimuletOn(false);
//                                            ((ImageView) ((LinearLayout) gridView.getChildAt(specialPlaceId.intValue())).getChildAt(0)).setImageResource(getPictureForSimulet(currentSimulet));
//
//                                        }
//                                    }
//                                }
//                            }, delay);
//                        }
//                    }
                }
            }
        });

    }

    private void executeSequenceForThatSimulet(final int indexVal, final MapDTO currentMap) {
        final int lastColumnIndex = indexVal + currentMap.getNumberOfColums();
        int index = indexVal + 1;
        Handler handler1 = new Handler();
        int delay = 0;
//        int delay = executePreSequenceIconChange(handler1,index -1, currentMap);
        while (index < lastColumnIndex+1) {
                if (currentMap.getPlacesInMap().size() > index) {
                    final PlaceInMapDTO dto = currentMap.getPlacesInMap().get(index);
                    final SimuletsState currentSimulet = dto.getSimuletState();
                    if(currentSimulet != null){
                        delay = delay + getHowLongToWait(Consts.TIME_BEETWEEN_SIMULETS, false);
                        handler1.postDelayed(new PostDelayedRunnable(client,currentSimulet,index,gridView, currentMap, indexVal), delay);
                    } else {
                        delay = delay + getHowLongToWait(Consts.TIME_BEETWEEN_SIMULETS, false);
                        handler1.postDelayed(new PostDelayedIconChange(index, gridView, currentMap, indexVal), delay);
                    }
                } else {
                    delay = delay + getHowLongToWait(Consts.TIME_BEETWEEN_SIMULETS, false);
                    handler1.postDelayed(new PostDelayedIconChange(index, gridView, currentMap, indexVal), delay);
                }

            index++;
        }
    }

    private int executePreSequenceIconChange(final Handler handler, final int preindex, final MapDTO currentMap) {
        int delay = 0;
        final PlaceInMapDTO dto = currentMap.getPlacesInMap().get(preindex);
        final SimuletsState currentSimulet = dto.getSimuletState();
        if(currentSimulet != null){
            delay = delay + getHowLongToWait(Consts.TIME_BEETWEEN_SIMULETS, false);
            handler.postDelayed(new PostDelayedIconChange(preindex,gridView, currentMap, 10000), delay);
        }
        return delay;
    }

    public void addToQueue(final Pair<TriggerSimulet, String> triggerAndState) {
        queue.add(triggerAndState);
    }

    private Pair<TriggerSimulet, String> removeFirst() {
        return queue.removeFirst();
    }

    private int getHowLongToWait(final int secs, final boolean simuletsTimerStatus) {
        if (simuletsTimerStatus) {
            return (Consts.TIME_BEETWEEN_SIMULETS_MULTIPLIER * secs * 1000);
        } else {
            return (secs * 1000);
        }
    }

    private int getPictureForSimulet(Simulet simulet) {
        if (simulet.isSimuletOn()) {
            if (simulet.getOptionsStatus().isTimer()) {
                return simulet.getPictureNameOnTimer();
            } else {
                return simulet.getPictureOn();
            }
        } else {
            if (simulet.getOptionsStatus().isTimer()) {
                return simulet.getPictureNameOffTimer();
            } else {
                return simulet.getPictureOff();
            }
        }

    }
}
