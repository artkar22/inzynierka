package modules;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

public abstract class Simulet extends JPanel {

	
	protected ArrayList<BufferedImage> images;
	protected BufferedImage currentImage;
	private String nameOfSimulet;
	
	public Simulet(String nameOfSimulet) 
	{
		this.nameOfSimulet=nameOfSimulet;
		loadPictures();
		setCurrentImage();
	}

	public String getNameOfSimulet()
	{
		return nameOfSimulet;
	}
	
	private void setCurrentImage() 
	{
		currentImage = images.get(0);
		Dimension dimension = new Dimension(currentImage.getWidth(), currentImage.getHeight());
		setPreferredSize(dimension);
	}

	private void loadPictures()
	{
		images =  new ArrayList<BufferedImage>();
		File directory = new File("pictures/"+nameOfSimulet);
		if(directory.isDirectory())
		{
			File[] files = directory.listFiles();
			for(File file: files)
			{
				try {
					images.add(ImageIO.read(file));
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	public ArrayList<BufferedImage> getImages()
	{
		return images;
	}
	
	@Override
	public void paintComponent(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;
		g2d.drawImage(currentImage, 0, 0, this);
	}
}
