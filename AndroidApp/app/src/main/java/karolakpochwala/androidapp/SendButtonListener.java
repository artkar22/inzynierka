package karolakpochwala.androidapp;

import android.content.DialogInterface;
import android.view.View;
import android.widget.EditText;

import org.eclipse.californium.core.coap.CoAP;
import org.eclipse.californium.core.coap.Request;
import org.eclipse.californium.core.network.CoapEndpoint;

/**
 * Created by Inni on 2016-03-05.
 */
public class SendButtonListener implements View.OnClickListener {

    private EditText messageTextView;
    private CoapEndpoint endpoint;

    public SendButtonListener(View viewById, CoapEndpoint endpoint) {
        super();
        messageTextView= (EditText) viewById;
        this.endpoint=endpoint;
    }
    @Override
    public void onClick(View v)
    {

        Request request = new Request(CoAP.Code.GET);
        String uri = "coap://192.168.2.2:11111";
        request.setURI(uri);
        request.send(endpoint);
       // Response response = request.waitForResponse();
    }

}
