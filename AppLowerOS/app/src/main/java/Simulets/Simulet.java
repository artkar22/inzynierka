package Simulets;

import java.net.URI;
import java.util.Set;

import org.eclipse.californium.core.WebLink;

import options.OptionsStatus;

public class Simulet {

    private static final String STATUS = "on_off";
    private boolean simuletOn;
    private String nameOfSimulet;
    private URI simuletsURI;
    private Set<WebLink> resources;

    private int pictureNameOff;
    private int pictureNameOn;
    private int pictureNameOnPetla;
    private int pictureNameOnTimer;
    private int pictureNameOnPetlaTimer;

    private OptionsStatus optionsStatus;

    public Simulet(URI simuletsURI) {
        //this.nameOfSimulet = nameOfSimulet;
        this.simuletsURI = simuletsURI;
        optionsStatus = new OptionsStatus();
    }

    public String getNameOfSimulet() {
        return nameOfSimulet;
    }

    public URI getUriOfSimulet() {
        return simuletsURI;
    }

    public void setResources(Set<WebLink> resources) {
        this.resources = resources;
    }

    public String getStatusResource() {
        for (WebLink weblink : resources) {
            if (weblink.getURI().endsWith(STATUS)) {
                return simuletsURI + weblink.getURI();
            }
        }
        return null;
    }

    //get on or off
    public boolean isSimuletOn() {
        return simuletOn;
    }

    //set on or off
    public void setSimuletOn(boolean simuletOn) {
        this.simuletOn = simuletOn;
    }


    public void setPictures(final int nameOFF,
                            final int nameON,
                            final int pictureNameOnPetla,
                            final int pictureNameOnTimer,
                            final int pictureNameOnPetlaTimer) {
        pictureNameOff = nameOFF;
        pictureNameOn = nameON;
        this.pictureNameOnPetla = pictureNameOnPetla;
        this.pictureNameOnTimer = pictureNameOnTimer;
        this.pictureNameOnPetlaTimer = pictureNameOnPetlaTimer;
    }

    public int getPictureOff() {
        return pictureNameOff;
    }

    public int getPictureOn() {
        return pictureNameOn;
    }

    public int getPictureNameOnPetla() {
        return pictureNameOnPetla;
    }

    public int getPictureNameOnTimer() {
        return pictureNameOnTimer;
    }

    public int getPictureNameOnPetlaTimer() {
        return pictureNameOnPetlaTimer;
    }

    public OptionsStatus getOptionsStatus() {
        return optionsStatus;
    }
}
