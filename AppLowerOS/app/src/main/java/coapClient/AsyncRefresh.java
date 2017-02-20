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
import Simulets.IpsoDigitalOutput;
import Simulets.IpsoLightControl;
import Simulets.Simulet;
import TriggerSimulets.TriggerActionThread;
import TriggerSimulets.TriggerSimulet;
import dynamicGrid.DynamicGridView;
import dynamicGrid.mapGenerator.map.MapDTO;
import dynamicGridActivity.CheeseDynamicAdapter;
import dynamicGridActivity.GridActivity;
import dynamicGridActivity.OptionButtonsUtils;
import karolakpochwala.apploweros.MainActivity;
import karolakpochwala.apploweros.R;

import static ipsoConfig.ipsoDefinitions.IPSO_DIGITAL_INPUT;
import static ipsoConfig.ipsoDefinitions.IPSO_DIGITAL_OUTPUT;
import static ipsoConfig.ipsoDefinitions.IPSO_LIGHT_CONTROL;

/**
 * Created by ArturK on 2017-01-10.
 */
public class AsyncRefresh extends AsyncTask<Void, Void, Void> {

    private ArrayList<Simulet> simulets;
    private ArrayList<TriggerSimulet> triggers;
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
        this.simulets = applicationData.getSimulets();
        this.triggers = applicationData.getTriggers();
        this.client = client;
        this.allMaps = applicationData.getAllMaps();
        this.triggerActionThread = triggerActionThread;
        this.gridActivity = gridActivity;
    }

    @Override
    protected void onPostExecute(Void result) {
        OptionButtonsUtils.createMapForFirstTrigger(triggers, allMaps.get(0));
        final DynamicGridView gridView = (DynamicGridView) gridActivity.findViewById(R.id.dynamic_grid);
        gridView.setAdapter(new CheeseDynamicAdapter(gridView.getContext(),
                simulets,
                triggers.get(0),
                allMaps.get(0),
                true));
        OptionButtonsUtils.createMapForEachTrigger(triggers);
        OptionButtonsUtils.setInitialStatusForSimulets(applicationData, client, triggerActionThread);
        OptionButtonsUtils.createOptionButtons(gridActivity, gridView, applicationData, client);
        dialog.dismiss();
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

    @Override
    protected Void doInBackground(Void... params) {
        discoverDevices();
        discoverResourcesOfEachDevice();
        return null;
    }
}
