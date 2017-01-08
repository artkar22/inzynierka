package dynamicGridActivity;


import android.view.MotionEvent;
import android.view.View;
import android.widget.ListAdapter;

import org.eclipse.californium.core.CoapClient;

import java.util.ArrayList;

import ApplicationData.ApplicationData;
import Simulets.Simulet;
import TriggerSimulets.TriggerActionThread;
import TriggerSimulets.TriggerSimulet;
import coapClient.CoapClientThread;
import dynamicGrid.DynamicGridView;
import dynamicGrid.mapGenerator.map.MapDTO;
import dynamicGrid.mapGenerator.map.PlaceInMapDTO;
import karolakpochwala.apploweros.MainActivity;
import karolakpochwala.apploweros.R;

/**
 * Created by ArturK on 2017-01-04.
 */
public class RefreshButtonListener implements View.OnClickListener {
    private CoapClientThread coapClientThread;
    private Thread refreshThread;
    private ApplicationData applicationData;
    private GridActivity gridActivity;
    private CoapClient client;
    private TriggerActionThread triggerActionThread;

    public RefreshButtonListener(final ApplicationData applicationData, final GridActivity gridActivity,
                                 final CoapClient client, final TriggerActionThread triggerActionThread) {
        this.applicationData = applicationData;
        this.gridActivity = gridActivity;
        this.client = client;
        this.triggerActionThread = triggerActionThread;
        coapClientThread = new CoapClientThread(applicationData.getSimulets(), applicationData.getTriggers(), gridActivity, client);
        refreshThread = new Thread(coapClientThread);
    }

    @Override
    public void onClick(View v) {
        final ArrayList<Simulet> simulets = applicationData.getSimulets();
        final ArrayList<TriggerSimulet> triggers = applicationData.getTriggers();
        final ArrayList<MapDTO> allMaps = applicationData.getAllMaps();
        simulets.clear();
        triggers.clear();
        refreshThread.run();
        OptionButtonsUtils.createMapForFirstTrigger(triggers, allMaps.get(0));
        final DynamicGridView gridView = (DynamicGridView) gridActivity.findViewById(R.id.dynamic_grid);
        gridView.setAdapter(new CheeseDynamicAdapter(gridView.getContext(),
                simulets,
                triggers.get(0),
                allMaps.get(0),
                true));
        OptionButtonsUtils.createMapForEachTrigger(triggers);
        OptionButtonsUtils.setInitialStatusForSimulets(applicationData, client, triggerActionThread);
        OptionButtonsUtils.createOptionButtons(gridActivity, gridView, applicationData);
//        ((PlaceInMapDTO)((DynamicGridView) gridActivity.findViewById(R.id.dynamic_grid)).getAdapter().getItem(5)).setSimulet(null);
//        ((DynamicGridView) gridActivity.findViewById(R.id.dynamic_grid));

//        ((DynamicGridView) gridActivity.findViewById(R.id.dynamic_grid)).stopEditMode();
    }

}
