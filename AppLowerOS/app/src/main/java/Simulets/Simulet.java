package Simulets;

import java.net.URI;
import java.util.Set;

import org.eclipse.californium.core.WebLink;

public class Simulet {

	private static final String STATUS = "on_off";
	private String nameOfSimulet;
	private URI simuletsURI;
	private Set<WebLink> resources;
	private int pictureNameOff;
	private int pictureNameOn;
	
	public Simulet( URI simuletsURI)
	{
		//this.nameOfSimulet = nameOfSimulet;
		this.simuletsURI = simuletsURI;
	}
	public String getNameOfSimulet()
	{
		return nameOfSimulet;
	}
	
	public URI getUriOfSimulet()
	{
		return simuletsURI;
	}
	public void setResources(Set<WebLink> resources) 
	{
		this.resources = resources;
	}
	public String getStatusResource()
	{
		for(WebLink weblink : resources)
		{
			if(weblink.getURI().endsWith(STATUS))
			{
				return simuletsURI+weblink.getURI();
			}
		}
		return null;
	}
	public void setPictures(final int nameOFF, final int nameON){
		pictureNameOff = nameOFF;
		pictureNameOn = nameON;
	}
	public int getPictureOff(){
		return pictureNameOff;
	}
	public int getPictureOn(){
		return pictureNameOn;
	}
}
