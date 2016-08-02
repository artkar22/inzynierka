package main;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Enumeration;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.xml.ws.Endpoint;

import modules.Simulet;
import modules.listOfAvailableModules;
import modules.Budzik.Budzik;
import modules.Lampka.Lampka;
import modules.Lampka.LampkaActionListener;

import org.eclipse.californium.core.CoapResource;
import org.eclipse.californium.core.CoapServer;
import org.eclipse.californium.core.coap.CoAP.ResponseCode;
import org.eclipse.californium.core.network.CoapEndpoint;
import org.eclipse.californium.core.network.config.NetworkConfig;
import org.eclipse.californium.core.server.resources.CoapExchange;

import serverResources.BudzikResource;
import serverResources.LampkaResource;
import configParser.ConfigParser;

public class Menu extends JFrame implements ActionListener {

	private String nameOfSimulet;
	private int serverPort;
	private ArrayList<BufferedImage> images;
	private JButton button;
	private Lampka lampka;
	private Budzik budzik;
	private CoapServer server;
	
	public Menu(String nameOfSimulet)
	{
	this.nameOfSimulet=nameOfSimulet;
	configurateWindow();
	loadConfiguration();
	createCOAPServer();
	startModule();
	server.start();
	}

	private void configurateWindow() {
//		button = new JButton("testButton");
		this.setBounds(0, 0, 1024, 768);
		this.setLayout(new GridLayout());
//		this.add(button);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setTitle(nameOfSimulet);
		this.setVisible(true);
	}
	
	private void loadConfiguration() {
		ConfigParser parser = new ConfigParser(nameOfSimulet);
		parser.Parse();
		serverPort = parser.getPort();
	}
	
	private void createCOAPServer() 
	{
		InetAddress addr;
		try {
			addr = InetAddress.getByName(getIPofCurrentMachine());
			InetSocketAddress adr = new InetSocketAddress(addr,serverPort);
			CoapEndpoint endpoint = new CoapEndpoint(adr);
			server = new CoapServer(nameOfSimulet);
			server.addEndpoint(endpoint);
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
	}

	private String getIPofCurrentMachine()
	{
		try {
		    Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
		    while (interfaces.hasMoreElements()) {
		        NetworkInterface iface = interfaces.nextElement();
		        if (iface.isLoopback() || !iface.isUp() || iface.isVirtual() || iface.isPointToPoint())
		            continue;

		        Enumeration<InetAddress> addresses = iface.getInetAddresses();
		        while(addresses.hasMoreElements()) {
		            InetAddress addr = addresses.nextElement();

		            final String ip = addr.getHostAddress();
		            if(Inet4Address.class == addr.getClass()) return ip;
		        }
		    }
		} catch (SocketException e) {
		    throw new RuntimeException(e);
		}
		return null;
	}
	
	private void startModule() {
		if(nameOfSimulet.equals(listOfAvailableModules.LAMPKA))
		{
			
			lampka = new Lampka(nameOfSimulet);
//			LampkaActionListener listener = new LampkaActionListener(lampka,this);
//			button.addActionListener(listener);
			this.add(lampka);
			this.pack();
			//images = lampka.getImages();
			LampkaResource lampkaResource = new LampkaResource(lampka,this);
			server.add(lampkaResource);
			}
		if(nameOfSimulet.equals(listOfAvailableModules.BUDZIK))
		{
			
			budzik = new Budzik(nameOfSimulet);
			this.add(budzik);
			this.pack();
			//images = lampka.getImages();
			BudzikResource budzikResource = new BudzikResource(budzik,this);
			server.add(budzikResource);
			}
	}
	
	@Override
	public void actionPerformed(ActionEvent arg0) 
	{
	}
}
