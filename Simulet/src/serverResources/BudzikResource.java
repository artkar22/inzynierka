package serverResources;

import main.Menu;
import modules.Budzik.Budzik;

import org.eclipse.californium.core.CoapResource;
import org.eclipse.californium.core.coap.CoAP.Code;
import org.eclipse.californium.core.coap.CoAP.ResponseCode;
import org.eclipse.californium.core.server.resources.CoapExchange;

import Protocol.Comm_Protocol;

public class BudzikResource extends CoapResource {

	private Budzik budzik;
	private Menu menu;
	public BudzikResource(Budzik budzik, Menu menu) {
		super(budzik.getNameOfSimulet());
		this.budzik=budzik;
		this.menu = menu;
	}

	@Override
	public void handleGET(CoapExchange exchange) 
	{
		if(budzik.getCurrentStatus()==Budzik.LAMP_SWITCHED_ON)
		{
			exchange.respond(ResponseCode.CONTENT, Integer.toString(Budzik.LAMP_SWITCHED_ON));
		}
		else if(budzik.getCurrentStatus()==Budzik.LAMP_SWITCHED_OFF)
		{
			exchange.respond(ResponseCode.CONTENT, Integer.toString(Budzik.LAMP_SWITCHED_OFF));
		}
	}
	@Override
	public void handlePUT(CoapExchange exchange) 
	{
		if(exchange.getRequestText().equals(Comm_Protocol.SWITCHED_ON)){
		budzik.switchOn();
		menu.repaint();
		exchange.respond(ResponseCode.CHANGED);
		
		}
		else if(exchange.getRequestText().equals(Comm_Protocol.SWITCHED_OFF))
		{
			budzik.switchOff();
			menu.repaint();
			exchange.respond(ResponseCode.CHANGED);

		}
	}
}
