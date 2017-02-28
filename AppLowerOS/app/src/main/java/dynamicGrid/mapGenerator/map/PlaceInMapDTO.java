package dynamicGrid.mapGenerator.map;

import Simulets.Simulet;
import Simulets.SimuletsState;

/**
 * Created by ArturK on 2016-09-24.
 */
public class PlaceInMapDTO {

    private int placeInMapId;
//    private boolean dropAllowed;
//    private boolean alreadyDropped;
    private boolean isItMap;//if not then it is Items container
    private SimuletsState simuletsState; //stateBindedToThatPlace

    public int getPlaceInMapId() {
        return placeInMapId;
    }

    public void setPlaceInMapId(final int placeInMapId) {
        this.placeInMapId = placeInMapId;
    }

//    public boolean isDropAllowed() {
//        return dropAllowed;
//    }
//
//    public void setDropAllowed(final boolean dropAllowed) {
//        this.dropAllowed = dropAllowed;
//    }
//
//    public boolean isAlreadyDropped() {
//        return alreadyDropped;
//    }
//
//    public void setAlreadyDropped(final boolean alreadyDropped) {
//        this.alreadyDropped = alreadyDropped;
//    }

    public boolean isItMap() {
        return isItMap;
    }

    public void setIsItMap(final boolean isItMap) {
        this.isItMap = isItMap;
    }

    public void setSimuletState(final SimuletsState simuletState) {
        this.simuletsState = simuletState;
    }

    public void unbindSimuletState() {
        simuletsState = null;
    }

    public SimuletsState getSimuletState() {
        return simuletsState;
    }

}
