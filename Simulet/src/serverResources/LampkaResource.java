package serverResources;

import main.Menu;
import modules.Lampka.Lampka;

import org.eclipse.californium.core.CoapResource;
import org.eclipse.californium.core.coap.CoAP.Code;
import org.eclipse.californium.core.coap.CoAP.ResponseCode;
import org.eclipse.californium.core.server.resources.CoapExchange;

import Protocol.Comm_Protocol;

public class LampkaResource extends CoapResource {

	private Lampka lampka;
	private Menu menu;
	public LampkaResource(Lampka lampka, Menu menu) {
		super(lampka.getNameOfSimulet());
		this.lampka=lampka;
		this.menu = menu;
	}

	@Override
	public void handleGET(CoapExchange exchange) 
	{
		if(lampka.getCurrentStatus()==Lampka.LAMP_SWITCHED_ON)
		{
			exchange.respond(ResponseCode.CONTENT, Integer.toString(Lampka.LAMP_SWITCHED_ON));
		}
		else if(lampka.getCurrentStatus()==Lampka.LAMP_SWITCHED_OFF)
		{
			exchange.respond(ResponseCode.CONTENT, Integer.toString(Lampka.LAMP_SWITCHED_OFF));
		}
	}
	@Override
	public void handlePUT(CoapExchange exchange) 
	{
		if(exchange.getRequestText().equals(Comm_Protocol.SWITCHED_ON)){
		lampka.switchOn();
		menu.repaint();
		exchange.respond(ResponseCode.CHANGED);
		
		}
		else if(exchange.getRequestText().equals(Comm_Protocol.SWITCHED_OFF))
		{
			lampka.switchOff();
			menu.repaint();
			exchange.respond(ResponseCode.CHANGED);

		}
	}
}
