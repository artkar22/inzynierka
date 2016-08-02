package modules.Budzik;

import java.awt.Dimension;

import modules.Simulet;

public class Budzik extends Simulet {

	public static final int LAMP_SWITCHED_ON = 1;
	public static final int LAMP_SWITCHED_OFF = 0;
	private int currentStatus;
	public Budzik(String nameOfSimulet) 
	{
		super(nameOfSimulet);
		setStatus(LAMP_SWITCHED_OFF);
	}
	public void switchOn()
	{
		setStatus(LAMP_SWITCHED_ON);
		currentImage = images.get(LAMP_SWITCHED_ON);
		Dimension dimension = new Dimension(currentImage.getWidth(), currentImage.getHeight());
		setPreferredSize(dimension);
	}
	public void switchOff()
	{
		setStatus(LAMP_SWITCHED_OFF);
		currentImage = images.get(LAMP_SWITCHED_OFF);
		Dimension dimension = new Dimension(currentImage.getWidth(), currentImage.getHeight());
		setPreferredSize(dimension);
	}
	public int getCurrentStatus()
	{
		return currentStatus;
	}
	private void setStatus(int newStatus)
	{
		currentStatus = newStatus;
	}
}
