package coapClient;


import android.util.Log;
import android.widget.Button;

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

import Simulets.IpsoLightControl;
import Simulets.Simulet;
import karolakpochwala.apploweros.SendButtonListener;

import static ipsoConfig.ipsoDefinitions.*;


/**
 * Created by Inni on 2016-03-15.
 */
public class CoapClientThread implements Runnable {

    private static final int MIN_PORT_NUMBER = 0;
    private static final int MAX_PORT_NUMBER = 12000;
    private Button sendButton;
    private ArrayList<Simulet> simulets;
    private CoapClient client;

    public CoapClientThread(Button sendButton, ArrayList<Simulet> simulets) {
        //   this.client=client;
        this.simulets = simulets;
        this.sendButton = sendButton;
    }

    public void run() {
        int port = 8080;
        //available(port);
        try {
            InetAddress addr = InetAddress.getByName(getIPofCurrentMachine());
            InetSocketAddress adr = new InetSocketAddress(addr, port);
            URI uri = new URI("coap://192.168.2.2:11111");
//	            URI uri = new URI("coap://192.168.2.2:11111/Lampka");
//	            //URI uri = new URI("coap://127.0.0.1:11111");
            client = new CoapClient(uri);
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


        discoverDevices();
        discoverResourcesOfEachDevice();
        SendButtonListener listener = new SendButtonListener(client, simulets);
        sendButton.setOnClickListener(listener);


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

    private String getIPofCurrentMachine() {
        try {
            Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
            while (interfaces.hasMoreElements()) {
                NetworkInterface iface = interfaces.nextElement();
                if (iface.isLoopback() || !iface.isUp() || iface.isVirtual() || iface.isPointToPoint())
                    continue;

                Enumeration<InetAddress> addresses = iface.getInetAddresses();
                while (addresses.hasMoreElements()) {
                    InetAddress addr = addresses.nextElement();

                    final String ip = addr.getHostAddress();
                    if (Inet4Address.class == addr.getClass()) return ip;
                }
            }
        } catch (SocketException e) {
            throw new RuntimeException(e);
        }
        return null;
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
                    String address = "/192.168.2";
//                    for (int x = 0; x < 100; x++){
                    int port = 11110;
                    while (port < 11115) {
//                        URI uriOfSimuletsId = new URI("coap:/" + address +"."+ Integer.toString(x) + ":" + Integer.toString(port)+"/id");
                        URI uriOfSimuletsId = new URI("coap:/" + broadcast + ":" + Integer.toString(port)+"/id");
                        Log.i("uri", uriOfSimuletsId.toString());

                        client.setURI(uriOfSimuletsId.toString());
                        CoapResponse resp = client.get();
                        if (resp != null) {
//                            URI uriOfSimulet = new URI("coap:/" + address +"."+ Integer.toString(x) + ":" + Integer.toString(port));
                            URI uriOfSimulet = new URI("coap://" + resp.advanced().getSource().getHostAddress() + ":" + Integer.toString(port));

                            createSimulet(resp, uriOfSimulet);
                        }
                        port++;
                    }
//                }
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

        if (resp.getResponseText().equals(Integer.toString(IPSO_LIGHT_CONTROL)) )
        {
            IpsoLightControl simulet = new IpsoLightControl(uri);
            simulets.add(simulet);
        }
    }

    private void discoverResourcesOfEachDevice() {
        if (simulets.size() > 0) {
            for (Simulet simulet : simulets) {
                client.setURI(simulet.getUriOfSimulet().toString());
                simulet.setResources(client.discover());
            }
        }
    }

}
