package modules.Lampka;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import main.Menu;

public class LampkaActionListener implements ActionListener {

	private Lampka lampka;
	private Menu menu;
	public LampkaActionListener(Lampka lampka, Menu menu) {
		this.lampka = lampka;
		this.menu = menu;
	}

	@Override
	public void actionPerformed(ActionEvent arg0) 
	{
		lampka.switchOn();
		menu.repaint();
	}

}
