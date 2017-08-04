package coapClient;


import android.app.ProgressDialog;
import android.util.Log;

import org.eclipse.californium.core.CoapClient;
import org.eclipse.californium.core.CoapResponse;
import org.eclipse.californium.core.network.CoapEndpoint;
import org.eclipse.californium.core.network.config.NetworkConfig;

import java.io.IOException;
import java.net.DatagramSocket;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.InterfaceAddress;
import java.net.NetworkInterface;
import java.net.ServerSocket;
import java.net.SocketException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Enumeration;

import Simulets.ActionSimulet;
import TriggerSimulets.EventSimulet;
import dynamicGridActivity.GridActivity;
import karolakpochwala.apploweros.MainActivity;
import mainUtils.NetworkUtils;

import static ipsoConfig.ipsoDefinitions.*;


/**
 * Created by Inni on 2016-03-15.
 */
public class CoapClientThread implements Runnable {

    private static final int MIN_PORT_NUMBER = 0;
    private static final int MAX_PORT_NUMBER = 12000;
    public static final int SEARCH_BEGINNING_PORT = 11110;
    public static final int SEARCH_END_PORT = 11120;
    private ArrayList<ActionSimulet> actionSimulets;
    private ArrayList<EventSimulet> eventSimulets;
    private CoapClient client;
    private MainActivity mainActivity;
    private GridActivity gridActivity;
    private ProgressDialog dialog;

    public CoapClientThread(final ArrayList<ActionSimulet> actionSimulets, final ArrayList<EventSimulet> eventSimulets,
                            final MainActivity mainActivity) {
        this.mainActivity = mainActivity;
        this.actionSimulets = actionSimulets;
        this.eventSimulets = eventSimulets;
        this.client = new CoapClient();
        dialog = new ProgressDialog(mainActivity);
        setCoap();
    }

    public CoapClientThread(final ArrayList<ActionSimulet> actionSimulets, final ArrayList<EventSimulet> eventSimulets,
                            final GridActivity gridActivity, final CoapClient client) {
        this.gridActivity = gridActivity;
        this.actionSimulets = actionSimulets;
        this.eventSimulets = eventSimulets;
        this.client = client;
        dialog = new ProgressDialog(gridActivity);
    }

    private void setCoap() {
        int port = NetworkUtils.PORT;
        //available(port);
        try {
            InetAddress addr = InetAddress.getByName(NetworkUtils.getIPofCurrentMachine());
            InetSocketAddress adr = new InetSocketAddress(addr, port);
            URI uri = new URI("coap://192.168.2.2:11111");
//	            URI uri = new URI("coap://192.168.2.2:11111/Lampka");
//	            //URI uri = new URI("coap://127.0.0.1:11111");
//            client = new CoapClient(uri);
            client.setURI(uri.toString());
            CoapEndpoint endpoint = new CoapEndpoint(adr, NetworkConfig.createStandardWithoutFile());
            endpoint.start();
            client = client.setEndpoint(endpoint);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void stopEndpoint() {
        client.getEndpoint().destroy();
    }

    public void run() {
        if (mainActivity != null) {
            mainActivity.runOnUiThread(new Runnable() {
                public void run() {
                    dialog.setMessage("Wyszukiwanie simuletów, proszę czekać ...");
                    dialog.setCancelable(false);
                    dialog.setInverseBackgroundForced(false);
                    dialog.show();
                }
            });
            discoverDevices();
            discoverResourcesOfEachDevice();
            stopEndpoint();
//            getMainIcons();
//            getStateLists();
            mainActivity.runOnUiThread(new Runnable() {
                public void run() {
                    dialog.dismiss();
                }
            });
        } else if (gridActivity != null) {
            dialog = new ProgressDialog(gridActivity);
            dialog.setMessage("Wyszukiwanie simuletów, proszę czekać ...");
            dialog.setCancelable(false);
            dialog.setInverseBackgroundForced(false);
            dialog.show();
        }
    }

    /**
     * Checks to see if a specific port is available.
     *
     * @param port the port to check for availability
     */
    public static boolean available(int port) {
        if (port < MIN_PORT_NUMBER || port > MAX_PORT_NUMBER) {
            throw new IllegalArgumentException("Invalid start port: " + port);
        }

        ServerSocket ss = null;
        DatagramSocket ds = null;
        try {
            ss = new ServerSocket(port);
            ss.setReuseAddress(true);
            ds = new DatagramSocket(port);
            ds.setReuseAddress(true);
            return true;
        } catch (IOException e) {
        } finally {
            if (ds != null) {
                ds.close();
            }

            if (ss != null) {
                try {
                    ss.close();
                } catch (IOException e) {
                /* should not be thrown */
                }
            }
        }

        return false;
    }


    private void discoverDevices() {
        try {
            Enumeration interfaces = NetworkInterface.getNetworkInterfaces();
            while (interfaces.hasMoreElements()) {
                NetworkInterface networkInterface = (NetworkInterface) interfaces.nextElement();
                if (networkInterface.isLoopback() || !networkInterface.isUp()) {
                    continue;
                }

                for (InterfaceAddress interfaceAddress : networkInterface.getInterfaceAddresses()) {
                    InetAddress broadcast = interfaceAddress.getBroadcast();
                    if (broadcast == null) {
                        continue;
                    }
                    if (broadcast.getClass().equals(Inet4Address.class)) {
                        searchForSimulets(broadcast);
                    }
                }
            }
        } catch (SocketException e) {
            e.printStackTrace();
        }
    }

    private void searchForSimulets(InetAddress broadcast) {
        try {
            int port = SEARCH_BEGINNING_PORT;
            while (port < SEARCH_END_PORT) {
                URI uriOfSimuletsClass =
                        new URI("coap:/" + broadcast + ":" + Integer.toString(port) + "/class");

                Log.i("uri", uriOfSimuletsClass.toString());

                client.setURI(uriOfSimuletsClass.toString());
                CoapResponse resp = client.get();
                if (resp != null && resp.isSuccess()) {
                    URI uriOfSimulet =
                            new URI("coap://" +
                                    resp.advanced().getSource().getHostAddress() +
                                    ":" + Integer.toString(port));

                    createSimulet(resp, uriOfSimulet);
                }
                port++;
            }
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    private void createSimulet(CoapResponse resp, URI uri) {
        if (resp.getResponseText().equals(ACTION_SIMULET)) {
            ActionSimulet simulet = new ActionSimulet(uri);
            simulet.setSimuletClass(resp.getResponseText());
            actionSimulets.add(simulet);
        } else if (resp.getResponseText().equals(EVENT_SIMULET)) {
            EventSimulet eventSimulet = new EventSimulet(uri);
            eventSimulet.setClass(resp.getResponseText());
            eventSimulets.add(eventSimulet);
        }
    }

    private void discoverResourcesOfEachDevice() {
        if (actionSimulets.size() > 0) {
            for (ActionSimulet actionSimulet : actionSimulets) {
                client.setURI(actionSimulet.getUriOfSimulet().toString());
                actionSimulet.setResources(client.discover());
            }
        }
        if (eventSimulets.size() > 0) {
            for (EventSimulet trigger : eventSimulets) {
                client.setURI(trigger.getUriOfTrigger().toString());
                trigger.setResources(client.discover());
            }
        }
    }
}
