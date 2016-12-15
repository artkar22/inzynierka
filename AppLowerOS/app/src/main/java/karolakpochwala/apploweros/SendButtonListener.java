package karolakpochwala.apploweros;

import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import org.eclipse.californium.core.CoapClient;
import org.eclipse.californium.core.CoapResponse;
import org.eclipse.californium.core.coap.CoAP;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import Protocol.Comm_Protocol;
import Simulets.Simulet;
import dynamicGrid.DynamicGridView;
import dynamicGrid.mapGenerator.map.MapDTO;
import dynamicGrid.mapGenerator.map.PlaceInMapDTO;
import mainUtils.Consts;

/**
 * Created by Inni on 2016-03-05.
 */
public class SendButtonListener implements View.OnClickListener {

    private EditText messageTextView;
    private CoapClient client;
    private MapDTO currentMap;
    private DynamicGridView gridView;

    public SendButtonListener(CoapClient client, MapDTO currentMap, DynamicGridView gridView) {
        super();
        this.client = client;
        this.currentMap = currentMap;
        this.gridView = gridView;
    }

    @Override
    public void onClick(View v) {
        Handler handler1 = new Handler();
        int delay = 0;
        for (final Integer specialPlaceId : currentMap.getSpecialPlacesIds()) {
            final PlaceInMapDTO dto = currentMap.getPlacesInMap().get(specialPlaceId.intValue());
            final Simulet currentSimulet = dto.getSimulet();
            if (currentSimulet != null) {
                delay = delay + getHowLongToWait(Consts.TIME_BEETWEEN_SIMULETS, currentSimulet.getOptionsStatus().isTimer());
                handler1.postDelayed(new Runnable() {

                    @Override
                    public void run() {
                        client.setURI(currentSimulet.getStatusResource());

                        CoapResponse get = client.get();
                        if (get.getCode().equals(CoAP.ResponseCode.CONTENT) && get.getResponseText().equals(Comm_Protocol.SWITCHED_OFF)) {
                            CoapResponse put = client.put(Comm_Protocol.SWITCHED_ON, 0);
                            if (put.isSuccess()) {
                                currentSimulet.setSimuletOn(true);
                                ((ImageView) ((LinearLayout) gridView.getChildAt(specialPlaceId.intValue())).getChildAt(0)).setImageResource(getPictureForSimulet(currentSimulet));

                            }
                        } else if (get.getCode().equals(CoAP.ResponseCode.CONTENT) && get.getResponseText().equals(Comm_Protocol.SWITCHED_ON)) {
                            CoapResponse put = client.put(Comm_Protocol.SWITCHED_OFF, 0);
                            if (put.isSuccess()) {
                                currentSimulet.setSimuletOn(false);
                                ((ImageView) ((LinearLayout) gridView.getChildAt(specialPlaceId.intValue())).getChildAt(0)).setImageResource(getPictureForSimulet(currentSimulet));

                            }
                        }
                    }
                }, delay);
            }
        }
//        Handler handler1 = new Handler();
//        int delay = 0;
//        for (int a = 0; a < currentMap.getSpecialPlacesIds().size(); a++) {
//            final Integer specialPlaceId = currentMap.getSpecialPlacesIds().get(a);
//            final PlaceInMapDTO dto = currentMap.getPlacesInMap().get(specialPlaceId.intValue());
//            final Simulet currentSimulet = dto.getSimulet();
//            if (currentSimulet != null) {
//                delay = delay + getHowLongToWait(Consts.TIME_BEETWEEN_SIMULETS, currentSimulet.getOptionsStatus().isTimer());
//                handler1.postDelayed(new Runnable() {
//
//                    @Override
//                    public void run() {
//                        ((ImageView) ((LinearLayout) gridView.getChildAt(specialPlaceId.intValue())).getChildAt(0)).setImageResource(getPictureForSimulet(currentSimulet));
//
//                    }
//                }, delay);
//            }
//        }
//

    }

    private int getHowLongToWait(final int secs, final boolean simuletsTimerStatus) {
        if (simuletsTimerStatus) {
            return (Consts.TIME_BEETWEEN_SIMULETS_MULTIPLIER * secs * 1000);
        } else {
            return (secs * 1000);
        }
    }

    private void waitSomeSecs(final int secs, final boolean simuletsTimerStatus) {
        try {
            synchronized (this) {
                if (simuletsTimerStatus) {
                    wait(Consts.TIME_BEETWEEN_SIMULETS_MULTIPLIER * secs * 1000);
                } else {
                    wait(secs * 1000);
                }
            }
        } catch (InterruptedException ex) {
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
