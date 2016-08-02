package karolakpochwala.apploweros;

import android.util.Log;
import android.view.View;
import android.widget.EditText;

import org.eclipse.californium.core.CoapClient;
import org.eclipse.californium.core.CoapResponse;
import org.eclipse.californium.core.coap.CoAP;
import org.eclipse.californium.core.coap.Request;
import org.eclipse.californium.core.network.CoapEndpoint;

import java.util.ArrayList;

import Protocol.Comm_Protocol;
import Simulets.Simulet;

/**
 * Created by Inni on 2016-03-05.
 */
public class SendButtonListener implements View.OnClickListener {

    private EditText messageTextView;
    private CoapClient client;
    private ArrayList<Simulet> simulets;
    public SendButtonListener(CoapClient client,ArrayList<Simulet> simulets) {
        super();
        this.client=client;
        this.simulets=simulets;
    }
    @Override
    public void onClick(View v)
    {
        Log.i("HOWMANYSIMULETS", Integer.toString(simulets.size()));
//		Set<WebLink> set = client.discover();
//		System.out.println(set.size());
        for(Simulet simulet:simulets){
            client.setURI(simulet.getStatusResource());
            CoapResponse get = client.get();
            if(get.getCode().equals(CoAP.ResponseCode.CONTENT)&&get.getResponseText().equals(Comm_Protocol.SWITCHED_OFF))
            {
                CoapResponse put = client.put(Comm_Protocol.SWITCHED_ON, 0);
            }
            else if(get.getCode().equals(CoAP.ResponseCode.CONTENT)&&get.getResponseText().equals(Comm_Protocol.SWITCHED_ON))
            {
                CoapResponse put = client.put(Comm_Protocol.SWITCHED_OFF, 0);
            }
        }
    }

}
