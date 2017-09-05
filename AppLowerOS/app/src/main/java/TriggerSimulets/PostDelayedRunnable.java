package TriggerSimulets;

import android.widget.ImageView;
import android.widget.LinearLayout;

import org.eclipse.californium.core.CoapClient;
import org.eclipse.californium.core.CoapResponse;

import Simulets.SimuletsState;
import dynamicGrid.DynamicGridView;
import dynamicGrid.mapGenerator.map.MapDTO;

import static dynamicGrid.DynamicGridUtils.PAUSE_SIMULET;

public class PostDelayedRunnable implements Runnable {
    private CoapClient client;
    private SimuletsState currentSimuletState;
    private int index;
    private DynamicGridView gridView;
    private MapDTO currentMap;
    private int indexVal;

    public PostDelayedRunnable(final CoapClient client, final SimuletsState currentSimuletState,
                        final int index, final DynamicGridView gridView, final MapDTO currentMap, final int indexVal){
        this.client = client;
        this.currentSimuletState = currentSimuletState;
        this.index = index;
        this.gridView = gridView;
        this.currentMap = currentMap;
        this.indexVal = indexVal;
    }

    @Override
    public void run() {
//                        CoapResponse get = client.get();
//                        if (get.getCode().equals(CoAP.ResponseCode.CONTENT) && get.getResponseText().equals(Comm_Protocol.SWITCHED_OFF)) {
            client.setURI(currentSimuletState.getSimuletsURI() + "/current_state");//status_resource - tak będzie szybciej XD
            SimuletsState simuletInPreviousIter = currentMap.getPlacesInMap().get(index-1).getSimuletState();
            if(simuletInPreviousIter != null && index-1>= indexVal+1){
                ((ImageView) ((LinearLayout) gridView.getChildAt(index-1)).getChildAt(0)).setImageBitmap(simuletInPreviousIter.getMiniature());
            }
            ((ImageView) ((LinearLayout) gridView.getChildAt(index)).getChildAt(0)).setImageBitmap(currentSimuletState.getHighlightedMiniature());
        if(!PAUSE_SIMULET.equals(currentSimuletState.getStateId())){
            CoapResponse put = client.put(currentSimuletState.getStateId(), 0);
        }

//                            if (put.isSuccess()) {
//
//                            }
//                        }
//                        else if (get.getCode().equals(CoAP.ResponseCode.CONTENT) && get.getResponseText().equals(Comm_Protocol.SWITCHED_ON)) {
//                            CoapResponse put = client.put(Comm_Protocol.SWITCHED_OFF, 0);
//                            if (put.isSuccess()) {
//                                currentSimuletState.setSimuletOn(false);
//                                ((ImageView) ((LinearLayout) gridView.getChildAt(specialPlaceId.intValue())).getChildAt(0)).setImageResource(getPictureForSimulet(currentSimuletState));
//
//                            }
//                        }
    }
}
