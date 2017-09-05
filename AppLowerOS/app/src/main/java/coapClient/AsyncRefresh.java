package coapClient;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Log;

import org.eclipse.californium.core.CoapClient;
import org.eclipse.californium.core.CoapResponse;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.InterfaceAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Enumeration;

import ApplicationData.ApplicationData;
import Simulets.ActionSimulet;
import TriggerSimulets.TriggerActionThread;
import TriggerSimulets.EventSimulet;
import dynamicGrid.mapGenerator.map.MapDTO;
import dynamicGridActivity.GridActivity;

import static ipsoConfig.ipsoDefinitions.EVENT_SIMULET;
import static ipsoConfig.ipsoDefinitions.ACTION_SIMULET;

public class AsyncRefresh extends AsyncTask<Void, Void, Void> {

    private ArrayList<ActionSimulet> actionSimulets;
    private ArrayList<EventSimulet> eventSimulets;
    private CoapClient client;
    private ProgressDialog dialog;
    private ArrayList<MapDTO> allMaps;
    private ApplicationData applicationData;
    private TriggerActionThread triggerActionThread;
    private GridActivity gridActivity;


    public AsyncRefresh(final ApplicationData applicationData, final TriggerActionThread triggerActionThread,
                        final GridActivity gridActivity, final ProgressDialog dialog, final CoapClient client) {
        this.dialog = dialog;
        this.applicationData = applicationData;
        this.actionSimulets = applicationData.getActionSimulets();
        this.eventSimulets = applicationData.getEventSimulets();
        this.client = client;
        this.allMaps = applicationData.getAllMaps();
        this.triggerActionThread = triggerActionThread;
        this.gridActivity = gridActivity;
    }

    @Override
    protected void onPostExecute(Void result) {
//        OptionButtonsUtils.createMapForFirstTrigger(eventSimulets, allMaps.get(0));
//        final DynamicGridView gridView = (DynamicGridView) gridActivity.findViewById(R.id.dynamic_grid);
//        gridView.setAdapter(new SimuletDynamicAdapter(gridView.getContext(),
//                actionSimulets,
//                eventSimulets.get(0),
//                allMaps.get(0),
//                true));
//        OptionButtonsUtils.createMapForEachTrigger(eventSimulets);
//        OptionButtonsUtils.setInitialStatusForSimulets(applicationData, client, triggerActionThread);
//        OptionButtonsUtils.createOptionButtons(gridActivity, gridView, applicationData, client);
//        dialog.dismiss();
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
        if (resp.getResponseText().equals(ACTION_SIMULET)) {
            ActionSimulet simulet = new ActionSimulet(uri);
            actionSimulets.add(simulet);
        } else if (resp.getResponseText().equals(EVENT_SIMULET)) {
            EventSimulet eventSimulet = new EventSimulet(uri);
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

    @Override
    protected Void doInBackground(Void... params) {
        discoverDevices();
        discoverResourcesOfEachDevice();
        return null;
    }
}
