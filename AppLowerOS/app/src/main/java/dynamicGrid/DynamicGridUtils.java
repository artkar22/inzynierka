package dynamicGrid;

import java.net.URI;

import android.view.View;

import java.util.ArrayList;
import java.util.LinkedList;

import Simulets.Simulet;
import Simulets.SimuletsState;
import TriggerSimulets.TriggerSimulet;
import dynamicGrid.mapGenerator.map.MapDTO;
import dynamicGrid.mapGenerator.map.MapDTOBuilder;
import dynamicGrid.mapGenerator.map.PlaceInMapDTO;

/**
 * Author: alex askerov
 * Date: 9/7/13
 * Time: 10:14 PM
 */
public class DynamicGridUtils {
    private static final String TRIGGER = "TRIGGER";
    private static final String SIMULET = "SIMULET";
    private static final String NONE = "NONE";

    /**
     * Delete item in <code>list</code> from position <code>indexFrom</code> and insert it to <code>indexTwo</code>
     *
     * @param list
     * @param indexFrom
     * @param indexTwo
     */
    public static void reorder(LinkedList<PlaceInMapDTO> list, int indexFrom, int indexTwo) {
//        if (list.get(indexTwo).isDropAllowed()) {
        PlaceInMapDTO obj = list.remove(indexFrom);
        list.add(indexTwo, obj);
//        }
    }

    /**
     * Swap item in <code>list</code> at position <code>firstIndex</code> with item at position <code>secondIndex</code>
     *
     * @param list        The list in which to swap the items.
     * @param firstIndex  The position of the first item in the list.
     * @param secondIndex The position of the second item in the list.
     */
    public static void swap(LinkedList<PlaceInMapDTO> list, MapDTO map,
                            ArrayList<Simulet> listOfSimulets, ArrayList<TriggerSimulet> triggers,
                            int firstIndex, int secondIndex) {
        final ArrayList placesTypes = map.getPlacesTypes();
        final String firstType = checkType(list.get(firstIndex), triggers, listOfSimulets);
        final String secondType = checkType(list.get(secondIndex), triggers, listOfSimulets);
        if ((placesTypes.get(firstIndex).equals(MapDTOBuilder.TRIGGER_PLACE) && placesTypes.get(secondIndex).equals(MapDTOBuilder.TRIGGER_PLACE) && !firstType.equals(SIMULET)&& !secondType.equals(SIMULET)) ||
                (placesTypes.get(firstIndex).equals(MapDTOBuilder.SIMULET_PLACE) && placesTypes.get(secondIndex).equals(MapDTOBuilder.SIMULET_PLACE) && !firstType.equals(TRIGGER)&& !secondType.equals(TRIGGER))) {
            //Trigger podmien z triggerem lub simulet z simuletem
            PlaceInMapDTO secondObject = list.get(secondIndex);
//            list.set(firstIndex, secondObject);
            list.set(secondIndex, list.set(firstIndex, secondObject));
        } else if ((placesTypes.get(firstIndex).equals(MapDTOBuilder.CONTAINER) && placesTypes.get(secondIndex).equals(MapDTOBuilder.TRIGGER_PLACE) && firstType.equals(TRIGGER)) ||
                (placesTypes.get(firstIndex).equals(MapDTOBuilder.CONTAINER) && placesTypes.get(secondIndex).equals(MapDTOBuilder.SIMULET_PLACE) && firstType.equals(SIMULET))) {

            PlaceInMapDTO secondObject = list.get(secondIndex);
            PlaceInMapDTO firstObject = list.get(firstIndex);
            SimuletsState copy = new SimuletsState(firstObject.getSimuletState().getStateId(),
                    firstObject.getSimuletState().getMiniature(),
                    firstObject.getSimuletState().getHighlightedMiniature(),
                    firstObject.getSimuletState().getSimuletsURI());
            secondObject.setSimuletState(copy);
            list.set(secondIndex, list.set(firstIndex, secondObject));
        }

}

    public static float getViewX(View view) {
        return Math.abs((view.getRight() - view.getLeft()) / 2);
    }

    public static float getViewY(View view) {
        return Math.abs((view.getBottom() - view.getTop()) / 2);
    }

    private static String checkType(final PlaceInMapDTO place, ArrayList<TriggerSimulet> triggers, ArrayList<Simulet> simulets) {
        if (isSimulet(place, simulets)) {
            return SIMULET;
        } else if (isTrigger(place, triggers)) {
            return TRIGGER;
        } else {
            return NONE;
        }
    }

    private static boolean isTrigger(final PlaceInMapDTO place, ArrayList<TriggerSimulet> triggers) {
        if(place.getSimuletState()==null) return false;
        final URI triggersUri = place.getSimuletState().getSimuletsURI();
        for (TriggerSimulet trigger : triggers) {
            if (trigger.getUriOfTrigger().equals(triggersUri)) return true;
        }
        return false;
    }

    private static boolean isSimulet(final PlaceInMapDTO place, ArrayList<Simulet> simulets) {
        if(place.getSimuletState()==null) return false;
        final URI simuletsUri = place.getSimuletState().getSimuletsURI();
        for (Simulet simulet : simulets) {
            if (simulet.getUriOfSimulet().equals(simuletsUri)) return true;
        }
        return false;
    }
}
