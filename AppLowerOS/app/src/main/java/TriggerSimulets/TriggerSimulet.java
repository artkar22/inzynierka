package TriggerSimulets;

import com.rits.cloning.Cloner;

import org.eclipse.californium.core.WebLink;

import java.net.URI;
import java.util.LinkedList;
import java.util.Set;

import dynamicGrid.mapGenerator.map.PlaceInMapDTO;

/**
 * Created by ArturK on 2016-12-27.
 */
public class TriggerSimulet {
    private static final String STATUS = "on_off";

    private boolean triggerOn;
    private String nameOfTrigger;
    private URI triggersURI;
    private Set<WebLink> resources;

    private LinkedList<PlaceInMapDTO> myPlacesInMap;

    public TriggerSimulet(URI triggersURI) {
        //this.nameOfSimulet = nameOfSimulet;
        this.triggersURI = triggersURI;
    }

    public URI getUriOfTrigger() {
        return triggersURI;
    }

    public void setResources(Set<WebLink> resources) {
        this.resources = resources;
    }

    public String getStatusResource() {
        for (WebLink weblink : resources) {
            if (weblink.getURI().endsWith(STATUS)) {
                return triggersURI + weblink.getURI();
            }
        }
        return null;
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
}
