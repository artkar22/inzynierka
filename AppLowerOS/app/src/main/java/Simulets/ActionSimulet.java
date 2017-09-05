package Simulets;

import java.net.URI;
import java.util.List;
import java.util.Set;

import org.eclipse.californium.core.WebLink;

import ExceptionHandling.ExceptionCodes;

import static karolakpochwala.apploweros.ResourcesList.STATES_LIST_RESOURCE;
import static karolakpochwala.apploweros.ResourcesList.STATUS_RESOURCE_ID;


public class ActionSimulet {

    private String simuletClass;
    private URI simuletsURI;
    private Set<WebLink> resources;
    private List<SimuletsState> states;


    public ActionSimulet(URI simuletsURI) {
        this.simuletsURI = simuletsURI;
    }

    public String getSimuletClass() {
        return simuletClass;
    }

    public void setSimuletClass(final String simuletClass) {
        this.simuletClass = simuletClass;
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

    public String getStatesListResource() {
        for (WebLink weblink : resources) {
            if (weblink.getURI().endsWith(STATES_LIST_RESOURCE)) {
                return simuletsURI + weblink.getURI();
            }
        }
        throw new RuntimeException(ExceptionCodes.NO_SUCH_RESOURCE);
    }

    public List<SimuletsState> getStates() {
        return states;
    }

    public void setStates(List<SimuletsState> states) {
        this.states = states;
    }
}
