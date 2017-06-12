package TriggerSimulets;

import android.util.Pair;
import android.view.View;

import java.util.LinkedList;

import dynamicGridActivity.GridActivity;
import karolakpochwala.apploweros.R;

/**
 * Created by Inni on 2017-06-11.
 */

public class PostDelayedPlayResumeButtonsManagment implements Runnable {

    private GridActivity gridActivity;
    private boolean flag;
    private TriggerActionThread processing;

    public PostDelayedPlayResumeButtonsManagment(final GridActivity gridActivity, final boolean flag, TriggerActionThread processing){
        this.gridActivity = gridActivity;
        this.flag = flag;
        this.processing = processing;
    }

    @Override
    public void run() {
        if(flag == true){
            (gridActivity.findViewById(R.id.playButton)).setVisibility(View.VISIBLE);
            (gridActivity.findViewById(R.id.resumeButton)).setVisibility(View.VISIBLE);
        } else {
            this.processing.setProcessing(false);
            boolean isAnyThreadProccessing = false;
            for(TriggerWrapper wrapper : gridActivity.getTriggerWrappers()){
                if(wrapper.getTriggerActionThread().isProcessing()){
                    isAnyThreadProccessing = true;
                    break;
                };
            }
            if(!isAnyThreadProccessing){
                gridActivity.resetPauseResumeButtons();
                (gridActivity.findViewById(R.id.playButton)).setVisibility(View.INVISIBLE);
                (gridActivity.findViewById(R.id.resumeButton)).setVisibility(View.INVISIBLE);
            }
        }
    }
}

