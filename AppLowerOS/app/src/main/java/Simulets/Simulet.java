package Simulets;

import android.graphics.Bitmap;

import java.net.URI;
import java.util.List;
import java.util.Set;

import org.eclipse.californium.core.WebLink;

import ExceptionHandling.ExceptionCodes;
import options.OptionsStatus;

import static karolakpochwala.apploweros.ResourcesList.STATES_LIST_RESOURCE;
import static karolakpochwala.apploweros.ResourcesList.STATUS_RESOURCE_ID;
import static karolakpochwala.apploweros.ResourcesList.MAIN_ICON_RESOURCE_ID;


public class Simulet {

    private boolean simuletOn;
    private String id;
    private URI simuletsURI;
    private Set<WebLink> resources;

    private Bitmap mainIconBitmap;
    private List<SimuletsState> states;

    private int pictureNameOff;
    private int pictureNameOffPetla;
    private int pictureNameOffTimer;
    private int pictureNameOffPetlaTimer;
    private int pictureNameOn;
    private int pictureNameOnPetla;
    private int pictureNameOnTimer;
    private int pictureNameOnPetlaTimer;

    private OptionsStatus optionsStatus;

    public Simulet(URI simuletsURI) {
        //this.id = id;
        this.simuletsURI = simuletsURI;
        optionsStatus = new OptionsStatus();
    }

    public String getId() {
        return id;
    }

    public void setId(final String id) {
        this.id = id;
    }

    public URI getUriOfSimulet() {
        return simuletsURI;
    }

    public void setResources(Set<WebLink> resources) {
        this.resources = resources;
    }

    public String getStatusResource() {
        for (WebLink weblink : resources) {
            if (weblink.getURI().endsWith(STATUS_RESOURCE_ID)) {
                return simuletsURI + weblink.getURI();
            }
        }
        throw new RuntimeException(ExceptionCodes.NO_SUCH_RESOURCE);
    }

    public String getMainIconResource() {
        for (WebLink weblink : resources) {
            if (weblink.getURI().endsWith(MAIN_ICON_RESOURCE_ID)) {
                return simuletsURI + weblink.getURI();
            }
        }
        throw new RuntimeException(ExceptionCodes.NO_SUCH_RESOURCE);
    }

    public String getStatesListResource() {
        for (WebLink weblink : resources) {
            if (weblink.getURI().endsWith(STATES_LIST_RESOURCE)) {
                return simuletsURI + weblink.getURI();
            }
        }
        throw new RuntimeException(ExceptionCodes.NO_SUCH_RESOURCE);
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
                            final int pictureNameOffPetla,
                            final int pictureNameOffTimer,
                            final int pictureNameOffPetlaTimer,
                            final int nameON,
                            final int pictureNameOnPetla,
                            final int pictureNameOnTimer,
                            final int pictureNameOnPetlaTimer) {
        pictureNameOff = nameOFF;
        pictureNameOn = nameON;
        this.pictureNameOffPetla = pictureNameOffPetla;
        this.pictureNameOffTimer = pictureNameOffTimer;
        this.pictureNameOffPetlaTimer = pictureNameOffPetlaTimer;
        this.pictureNameOnPetla = pictureNameOnPetla;
        this.pictureNameOnTimer = pictureNameOnTimer;
        this.pictureNameOnPetlaTimer = pictureNameOnPetlaTimer;
    }

    public Bitmap getMainIconBitmap() {
        return mainIconBitmap;
    }

    public void setMainIconBitmap(Bitmap mainIconBitmap) {
        this.mainIconBitmap = mainIconBitmap;
    }

    public List<SimuletsState> getStates() {
        return states;
    }

    public void setStates(List<SimuletsState> states) {
        this.states = states;
    }

    public int getPictureOff() {
        return pictureNameOff;
    }

    public int getPictureOn() {
        return pictureNameOn;
    }

    public int getPictureNameOffPetla() {
        return pictureNameOffPetla;
    }

    public int getPictureNameOffPetlaTimer() {
        return pictureNameOffPetlaTimer;
    }

    public int getPictureNameOffTimer() {
        return pictureNameOffTimer;
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
