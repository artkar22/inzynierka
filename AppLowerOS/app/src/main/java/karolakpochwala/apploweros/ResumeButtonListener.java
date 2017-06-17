package karolakpochwala.apploweros;

import android.view.View;

import java.util.ArrayList;

import TriggerSimulets.TriggerWrapper;
import dynamicGridActivity.GridActivity;

/**
 * Created by Inni on 2017-05-16.
 */

public class ResumeButtonListener implements View.OnClickListener {

    private ArrayList<TriggerWrapper> triggerWrappers;

    public ResumeButtonListener(final ArrayList<TriggerWrapper> triggerWrappers) {
        super();
        this.triggerWrappers = triggerWrappers;
    }
    @Override
    public void onClick(View v) {
        for (TriggerWrapper wrapper: triggerWrappers) {
            wrapper.getTriggerActionThread().nextStep();
        }
    }
}
