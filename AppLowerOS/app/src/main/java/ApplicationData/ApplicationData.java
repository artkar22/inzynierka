package ApplicationData;

import java.util.ArrayList;

import Simulets.ActionSimulet;
import TriggerSimulets.EventSimulet;
import dynamicGrid.mapGenerator.map.MapDTO;


public class ApplicationData {

    private ArrayList<MapDTO> allMaps;
    private ArrayList<ActionSimulet> actionSimulets;
    private ArrayList<EventSimulet> eventSimulets;


    public ApplicationData() {
        allMaps = new ArrayList<>();
        actionSimulets = new ArrayList<>();
        eventSimulets = new ArrayList<>();
    }

    public void addMap(final MapDTO newMap) {
        if (newMap != null) {
            allMaps.add(newMap);
        }
    }

    public ArrayList<MapDTO> getAllMaps() {
        return allMaps;
    }

    public ArrayList<ActionSimulet> getActionSimulets() {
        return actionSimulets;
    }

    public void addSimulet(final ActionSimulet actionSimulet) { //TODO dodawanie simuletów powinno być tą metodą, zmiana na pozniej
        this.actionSimulets.add(actionSimulet);
    }

    public void removeSimulet(final String simuletId) {
        //TODO gdy utrace łączność usuwam simulet
    }

    public void removeAllSimulets() {
        actionSimulets = new ArrayList<>();
    }

    public ArrayList<EventSimulet> getEventSimulets() {
        return eventSimulets;
    }
}
