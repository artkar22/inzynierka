package TriggerSimulets;

import android.view.View;

import ApplicationData.ApplicationData;
import dynamicGrid.DynamicGridView;

/**
 * Created by ArturK on 2016-12-13.
 */
public class TriggerSimuletButtonListener implements View.OnClickListener {
    //    private boolean timerButtonOn;
    private View buttonView;
    private DynamicGridView gridView;
    private TriggerSimulet triggerSimulet;
    private ApplicationData appData;

    public TriggerSimuletButtonListener(View buttonView, DynamicGridView gridView,
                                        TriggerSimulet triggerSimulet, ApplicationData appData) {
//        timerButtonOn = false;
        this.buttonView = buttonView;
        this.gridView = gridView;
        this.triggerSimulet = triggerSimulet;
        this.appData = appData;

    }

    @Override
    public void onClick(View v) {
//        gridView.setAdapter(new SimuletDynamicAdapter(gridView.getContext(),
//                appData.getSimulets(),
//                triggerSimulet,
//                appData.getAllMaps().get(0),
//                false));
//        gridView.switchView(triggerSimulet.getMyPlacesInMap());
    }

//    public boolean getStatus() {
//        return timerButtonOn;
//    }
}
