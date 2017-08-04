package TriggerSimulets;


import com.rits.cloning.Cloner;

import org.eclipse.californium.core.WebLink;

import java.net.URI;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import ExceptionHandling.ExceptionCodes;
import Simulets.SimuletsState;
import dynamicGrid.mapGenerator.map.PlaceInMapDTO;

import static karolakpochwala.apploweros.ResourcesList.STATES_LIST_RESOURCE;
import static karolakpochwala.apploweros.ResourcesList.STATUS_RESOURCE_ID;

/**
 * Created by ArturK on 2016-12-27.
 */
public class EventSimulet {

    private String simuletClass;
    private String name;
    private URI triggersURI;
    private Set<WebLink> resources;

    private LinkedList<PlaceInMapDTO> myPlacesInMap;
    private List<SimuletsState> states;

    public EventSimulet(URI triggersURI) {
        this.triggersURI = triggersURI;
    }

    public String getSimuletClass() {
        return simuletClass;
    }

    public void setClass(String id) {
        this.simuletClass = id;
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

    public String getStatesListResource() {
        for (WebLink weblink : resources) {
            if (weblink.getURI().endsWith(STATES_LIST_RESOURCE)) {
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

    public void setStates(List<SimuletsState> states) {
        this.states = states;
    }
    public List<SimuletsState> getStates() {
        return states;
    }
}
