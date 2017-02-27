package TriggerSimulets;

import android.graphics.Bitmap;

import com.rits.cloning.Cloner;

import org.eclipse.californium.core.WebLink;

import java.net.URI;
import java.util.LinkedList;
import java.util.Set;

import ExceptionHandling.ExceptionCodes;
import dynamicGrid.mapGenerator.map.PlaceInMapDTO;
import static karolakpochwala.apploweros.ResourcesList.STATUS_RESOURCE_ID;
import static karolakpochwala.apploweros.ResourcesList.MAIN_ICON_RESOURCE_ID;

/**
 * Created by ArturK on 2016-12-27.
 */
public class TriggerSimulet {
    private static final String NAME = "NAME";

    private boolean triggerOn;
    private String id;
    private String name;
    private URI triggersURI;
    private Set<WebLink> resources;
    private Bitmap mainIconBitmap;

    private LinkedList<PlaceInMapDTO> myPlacesInMap;

    public TriggerSimulet(URI triggersURI) {
        //this.nameOfSimulet = nameOfSimulet;
        this.triggersURI = triggersURI;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public URI getUriOfTrigger() {
        return triggersURI;
    }

    public void setResources(Set<WebLink> resources) {
        this.resources = resources;
    }

    public String getStatusResource() {
        for (WebLink weblink : resources) {
            if (weblink.getURI().endsWith(STATUS_RESOURCE_ID)) {
                return triggersURI + weblink.getURI();
            }
        }
        return null;
    }

    public String getNameResource() {
        for (WebLink weblink : resources) {
            if (weblink.getURI().endsWith(NAME)) {
                return triggersURI + weblink.getURI();
            }
        }
        return null;
    }

    public String getMainIconResource() {
        for (WebLink weblink : resources) {
            if (weblink.getURI().endsWith(MAIN_ICON_RESOURCE_ID)) {
                return triggersURI + weblink.getURI();
            }
        }
        throw new RuntimeException(ExceptionCodes.NO_SUCH_RESOURCE);
    }

    public LinkedList<PlaceInMapDTO> getMyPlacesInMap() {
        return myPlacesInMap;
    }

    public void setMyPlacesInMap(LinkedList<PlaceInMapDTO> placesInMap) {
        this.myPlacesInMap = placesInMap;
    }

    public void deepCopyOfPlacesInMap(LinkedList<PlaceInMapDTO> placesInMap) {
        Cloner cloner = new Cloner();
        myPlacesInMap = cloner.deepClone(placesInMap);

    }

    public Bitmap getMainIconBitmap() {
        return mainIconBitmap;
    }

    public void setMainIconBitmap(Bitmap mainIconBitmap) {
        this.mainIconBitmap = mainIconBitmap;
    }

}
