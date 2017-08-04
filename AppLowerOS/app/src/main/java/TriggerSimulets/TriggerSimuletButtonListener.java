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
    private EventSimulet eventSimulet;
    private ApplicationData appData;

    public TriggerSimuletButtonListener(View buttonView, DynamicGridView gridView,
                                        EventSimulet eventSimulet, ApplicationData appData) {
//        timerButtonOn = false;
        this.buttonView = buttonView;
        this.gridView = gridView;
        this.eventSimulet = eventSimulet;
        this.appData = appData;

    }

    @Override
    public void onClick(View v) {
//        gridView.setAdapter(new SimuletDynamicAdapter(gridView.getContext(),
//                appData.getActionSimulets(),
//                eventSimulet,
//                appData.getAllMaps().get(0),
//                false));
//        gridView.switchView(eventSimulet.getMyPlacesInMap());
    }

//    public boolean getStatus() {
//        return timerButtonOn;
//    }
}
