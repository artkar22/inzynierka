package karolakpochwala.apploweros;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;

import org.eclipse.californium.core.CoapClient;
import org.eclipse.californium.core.CoapResponse;
import org.eclipse.californium.core.network.CoapEndpoint;
import org.eclipse.californium.core.network.config.NetworkConfig;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.InterfaceAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Enumeration;

import Simulets.Simulet;
import coapClient.CoapClientThread;

public class MainActivity extends AppCompatActivity {

    private ArrayList<Simulet> simulets;
  //  private CoapClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        simulets = new ArrayList<Simulet>();
        Button sendButton = (Button) findViewById(R.id.button1);
        Thread CoapClient = new Thread(new CoapClientThread(sendButton,simulets));
        CoapClient.start();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
//
//    private void discoverDevices() {
//
//        simulets = new ArrayList<Simulet>();
//        try {
//            Enumeration interfaces = NetworkInterface.getNetworkInterfaces();
//            while(interfaces.hasMoreElements())
//            {
//                NetworkInterface networkInterface = (NetworkInterface) interfaces.nextElement();
//                if(networkInterface.isLoopback() || !networkInterface.isUp())
//                {
//                    continue;
//                }
//
//                for(InterfaceAddress interfaceAddress : networkInterface.getInterfaceAddresses()) {
//                    InetAddress broadcast = interfaceAddress.getBroadcast();
//                    if (broadcast == null) {
//                        continue;
//                    }
////                    String address = "/192.168.2";
////                    for (int x = 0; x < 255; x++){
//                        int port = 11110;
//                    while (port < 11115) {
//                        URI uri = new URI("coap:/" + broadcast + ":" + Integer.toString(port));
//                        Log.i("uri", uri.toString());
//
//                        client.setURI(uri.toString());
//                        CoapResponse resp = client.get();
//                        if (resp != null) {
//                            Log.i("response", resp.getResponseText());
//                            Simulet simulet = new Simulet(resp.getResponseText(), uri);
//                            simulets.add(simulet);
//                        }
//                        port++;
//                    }
////                }
//                }
//            }
//        } catch (SocketException e) {
//            e.printStackTrace();
//        } catch (URISyntaxException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        }
//    }
//    private void discoverResourcesOfEachDevice()
//    {
//        if(simulets.size()>0)
//        {
//            for(Simulet simulet : simulets)
//            {
//                client.setURI(simulet.getUriOfSimulet().toString());
//                simulet.setResources(client.discover());
//            }
//        }
//    }


}
