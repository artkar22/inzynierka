package coapClient;


import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;
import android.widget.Button;

import org.eclipse.californium.core.CoapClient;
import org.eclipse.californium.core.CoapHandler;
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

import Simulets.IpsoDigitalOutput;
import Simulets.IpsoLightControl;
import Simulets.Simulet;
import TriggerSimulets.TriggerSimulet;
import karolakpochwala.apploweros.MainActivity;
import karolakpochwala.apploweros.SendButtonListener;
import mainUtils.NetworkUtils;

import static ipsoConfig.ipsoDefinitions.*;


/**
 * Created by Inni on 2016-03-15.
 */
public class CoapClientThread implements Runnable {

    private static final int MIN_PORT_NUMBER = 0;
    private static final int MAX_PORT_NUMBER = 12000;
    private Button sendButton;
    private ArrayList<Simulet> simulets;
    private ArrayList<TriggerSimulet> triggers;
    private CoapClient client;
    private MainActivity mainActivity;
    private ProgressDialog dialog;

    public CoapClientThread(final Button sendButton, final ArrayList<Simulet> simulets,
                            final ArrayList<TriggerSimulet> triggers, final MainActivity mainActivity) {
        this.mainActivity = mainActivity;
        this.simulets = simulets;
        this.triggers = triggers;
        this.sendButton = sendButton;
        this.client = new CoapClient();
        dialog = new ProgressDialog(mainActivity);
        setCoap();
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

    public void run() {
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
        mainActivity.runOnUiThread(new Runnable() {
            public void run() {
                dialog.hide();
            }
        });
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

//    private String getIPofCurrentMachine() {
//        try {
//            Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
//            while (interfaces.hasMoreElements()) {
//                NetworkInterface iface = interfaces.nextElement();
//                if (iface.isLoopback() || !iface.isUp() || iface.isVirtual() || iface.isPointToPoint())
//                    continue;
//
//                Enumeration<InetAddress> addresses = iface.getInetAddresses();
//                while (addresses.hasMoreElements()) {
//                    InetAddress addr = addresses.nextElement();
//
//                    final String ip = addr.getHostAddress();
//                    if (Inet4Address.class == addr.getClass()) return ip;
//                }
//            }
//        } catch (SocketException e) {
//            throw new RuntimeException(e);
//        }
//        return null;
//    }


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
                        int port = 11110;
                        while (port < 11118) {
                            URI uriOfSimuletsId = new URI("coap:/" + broadcast + ":" + Integer.toString(port) + "/id");

                            Log.i("uri", uriOfSimuletsId.toString());

                            client.setURI(uriOfSimuletsId.toString());
                            CoapResponse resp = client.get();
                            if (resp != null) {
                                URI uriOfSimulet = new URI("coap://" + resp.advanced().getSource().getHostAddress() + ":" + Integer.toString(port));

                                createSimulet(resp, uriOfSimulet);
                            }
                            port++;
                        }
                    }
//                    }
                }
            }
        } catch (SocketException e) {
            e.printStackTrace();
        } catch (URISyntaxException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private void createSimulet(CoapResponse resp, URI uri) {

        if (resp.getResponseText().equals(Integer.toString(IPSO_LIGHT_CONTROL))) {
            IpsoLightControl simulet = new IpsoLightControl(uri);
            simulets.add(simulet);
        } else if (resp.getResponseText().equals(Integer.toString(IPSO_DIGITAL_OUTPUT))) {
            IpsoDigitalOutput simulet = new IpsoDigitalOutput(uri);
            simulets.add(simulet);
        } else if (resp.getResponseText().equals(Integer.toString(IPSO_DIGITAL_INPUT))) {
            TriggerSimulet trigger = new TriggerSimulet(uri);
            triggers.add(trigger);
        }
    }

    private void discoverResourcesOfEachDevice() {
        if (simulets.size() > 0) {
            for (Simulet simulet : simulets) {
                client.setURI(simulet.getUriOfSimulet().toString());
                simulet.setResources(client.discover());
            }
        }
        if (triggers.size() > 0) {
            for (TriggerSimulet trigger : triggers) {
                client.setURI(trigger.getUriOfTrigger().toString());
                trigger.setResources(client.discover());
            }
        }
    }

}
