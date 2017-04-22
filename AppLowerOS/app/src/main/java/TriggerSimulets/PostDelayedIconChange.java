package TriggerSimulets;

import android.widget.ImageView;
import android.widget.LinearLayout;

import org.eclipse.californium.core.CoapClient;

import Simulets.SimuletsState;
import dynamicGrid.DynamicGridView;
import dynamicGrid.mapGenerator.map.MapDTO;

/**
 * Created by ArturK on 2017-04-22.
 */

public class PostDelayedIconChange implements Runnable {

    private int index;
    private DynamicGridView gridView;
    private MapDTO currentMap;
    private int indexVal;

    public PostDelayedIconChange(final int index, final DynamicGridView gridView,
                                 final MapDTO currentMap, final int indexVal){
        this.index = index;
        this.gridView = gridView;
        this.currentMap = currentMap;
        this.indexVal = indexVal;
    }
    @Override
    public void run() {
        SimuletsState simuletInPreviousIter = currentMap.getPlacesInMap().get(index-1).getSimuletState();
        if(simuletInPreviousIter != null && index-1>= indexVal+1){
            ((ImageView) ((LinearLayout) gridView.getChildAt(index-1)).getChildAt(0)).setImageBitmap(simuletInPreviousIter.getMiniature());
        }
    }
}
