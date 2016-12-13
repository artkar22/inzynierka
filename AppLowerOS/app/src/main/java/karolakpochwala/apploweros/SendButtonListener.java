package karolakpochwala.apploweros;

import android.util.Log;
import android.view.View;
import android.widget.EditText;

import org.eclipse.californium.core.CoapClient;
import org.eclipse.californium.core.CoapResponse;
import org.eclipse.californium.core.coap.CoAP;

import java.util.ArrayList;

import Protocol.Comm_Protocol;
import Simulets.Simulet;
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

    public SendButtonListener(CoapClient client, MapDTO currentMap) {
        super();
        this.client = client;
        this.currentMap = currentMap;
    }

    @Override
    public void onClick(View v) {
//        Log.i("HOWMANYSIMULETS", Integer.toString(simulets.size())); //TODO przeiterować po mapie, tam gdzie znacznik simuleta wysłać on na simuleta
//		Set<WebLink> set = client.discover();
//		System.out.println(set.size());
//        for (PlaceInMapDTO dto : currentMap.getPlacesInMap()) {
        for (Integer specialPlaceId : currentMap.getSpecialPlacesIds()) {
            final PlaceInMapDTO dto = currentMap.getPlacesInMap().get(specialPlaceId.intValue());
            final Simulet currentSimulet = dto.getSimulet();
            if (currentSimulet != null) {
                client.setURI(currentSimulet.getStatusResource());
                CoapResponse get = client.get();
                if (get.getCode().equals(CoAP.ResponseCode.CONTENT) && get.getResponseText().equals(Comm_Protocol.SWITCHED_OFF)) {
                    waitSomeSecs(Consts.TIME_BEETWEEN_SIMULETS, currentSimulet.getOptionsStatus().isTimer());
                    CoapResponse put = client.put(Comm_Protocol.SWITCHED_ON, 0);
                    if(put.isSuccess()){
                        currentSimulet.setSimuletOn(true);
                    }
                } else if (get.getCode().equals(CoAP.ResponseCode.CONTENT) && get.getResponseText().equals(Comm_Protocol.SWITCHED_ON)) {
                    waitSomeSecs(Consts.TIME_BEETWEEN_SIMULETS, currentSimulet.getOptionsStatus().isTimer());
                    CoapResponse put = client.put(Comm_Protocol.SWITCHED_OFF, 0);
                    if(put.isSuccess()){
                        currentSimulet.setSimuletOn(false);
                    }
                }
            }
        }
    }

    private void waitSomeSecs(final int secs, final boolean simuletsTimerStatus) {
        try {
            synchronized (this) {
                if(simuletsTimerStatus){
                    wait(Consts.TIME_BEETWEEN_SIMULETS_MULTIPLIER * secs * 1000);
                } else {
                    wait(secs * 1000);
                }
            }
        } catch (InterruptedException ex) {
        }
    }
}
