package dynamicGridActivity;


import android.app.ProgressDialog;
import android.view.View;

import org.eclipse.californium.core.CoapClient;

import java.util.ArrayList;

import ApplicationData.ApplicationData;
import Simulets.ActionSimulet;
import TriggerSimulets.TriggerActionThread;
import TriggerSimulets.EventSimulet;
import coapClient.AsyncRefresh;

/**
 * Created by ArturK on 2017-01-04.
 */
public class RefreshButtonListener implements View.OnClickListener {
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
    }

    @Override
    public void onClick(View v) {
        final ArrayList<ActionSimulet> actionSimulets = applicationData.getActionSimulets();
        final ArrayList<EventSimulet> triggers = applicationData.getEventSimulets();
        actionSimulets.clear();
        triggers.clear();
        ProgressDialog dialog = new ProgressDialog(gridActivity);
        dialog.setMessage("Wyszukiwanie simuletów, proszę czekać ...");
        dialog.setCancelable(false);
        dialog.setInverseBackgroundForced(false);
        dialog.show();
        AsyncRefresh asyncRefresh = new AsyncRefresh(applicationData, triggerActionThread, gridActivity, dialog, client);
        asyncRefresh.execute();
    }

}
