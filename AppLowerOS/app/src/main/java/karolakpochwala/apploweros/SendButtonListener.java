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
        for(Integer specialPlaceId:currentMap.getSpecialPlacesIds()){
            PlaceInMapDTO dto = currentMap.getPlacesInMap().get(specialPlaceId.intValue());
            if (dto.getSimulet() != null) {
                client.setURI(dto.getSimulet().getStatusResource());
                CoapResponse get = client.get();
                if (get.getCode().equals(CoAP.ResponseCode.CONTENT) && get.getResponseText().equals(Comm_Protocol.SWITCHED_OFF)) {
                    CoapResponse put = client.put(Comm_Protocol.SWITCHED_ON, 0);
                    waitSomeSecs(3);
                } else if (get.getCode().equals(CoAP.ResponseCode.CONTENT) && get.getResponseText().equals(Comm_Protocol.SWITCHED_ON)) {
                    CoapResponse put = client.put(Comm_Protocol.SWITCHED_OFF, 0);
                    waitSomeSecs(3);
                }
            }
        }
    }
    private void waitSomeSecs(int secs){
        try {
            synchronized(this){
                wait(secs*1000);
            }
        }
        catch(InterruptedException ex){
        }
    }
}
