package dynamicGrid.mapGenerator.map;

import android.util.Pair;

import java.util.ArrayList;
import java.util.LinkedList;

/**
 * Created by ArturK on 2016-09-24.
 */
public class MapDTO {

    private String mapID;
    private int numberOfColums;
    private int numberOfRows;
    private int numberOfStatesRows;
    private ArrayList<String> placesTypes;//zczytywanie idków gdzie mapaa
    private LinkedList<PlaceInMapDTO> placesInMap;
    private LinkedList<Integer> triggersIndexes;
    private LinkedList<Integer> arrowsIndexes;
    private LinkedList<Integer> spaceBeetweenIndexes;

    public String getMapID() {
        return mapID;
    }

    public void setMapID(final String mapID) {
        this.mapID = mapID;
    }

    public int getNumberOfColums() {
        return numberOfColums;
    }

    public void setNumberOfColums(final int numberOfColums) {
        this.numberOfColums = numberOfColums;
    }

    public int getNumberOfRows() {
        return numberOfRows;
    }

    public void setNumberOfRows(final int numberOfRows) {
        this.numberOfRows = numberOfRows;
    }

    public LinkedList<PlaceInMapDTO> getPlacesInMap() {
        return placesInMap;
    }

    public void setPlacesInMap(final LinkedList<PlaceInMapDTO> placesInMap) {
        this.placesInMap = placesInMap;
    }

    public void setNumberOfStatesRows(int numberOfStatesRows) {
        this.numberOfStatesRows = numberOfStatesRows;
    }

    public LinkedList<Integer> getTriggersIndexes() {
        return triggersIndexes;
    }

    public LinkedList<Integer> getArrowsIndexes() {
        return arrowsIndexes;
    }

    public void setTriggersIndexes(final LinkedList<Integer> triggersIndexes) {
        this.triggersIndexes = triggersIndexes;
    }

    public void setArrowsIndexes(final LinkedList<Integer> arrowsIndexes) {
        this.arrowsIndexes = arrowsIndexes;
    }

    public boolean checkIfTriggersIndexesContainsValue(final int val) {
        if (triggersIndexes != null) {
            for (Integer ind : this.triggersIndexes) {
                if(ind.intValue() == val){
                    return true;
                }
            }
        }
        return false;
    }

    public boolean checkIfArrowsIndexesContainsValue(final int val) {
        if (arrowsIndexes != null) {
            for (Integer ind : this.arrowsIndexes) {
                if(ind.intValue() == val){
                    return true;
                }
            }
        }
        return false;
    }

    public boolean checkIfSpaceBeetweenIndexesContainsValue(final int val){
        if (spaceBeetweenIndexes != null) {
            for (Integer ind : this.spaceBeetweenIndexes) {
                if(ind.intValue() == val){
                    return true;
                }
            }
        }
        return false;
    }
    public ArrayList<String> getPlacesTypes() {
        return placesTypes;
    }

    public void setPlacesTypes(final ArrayList<String> placesTypes) {
        this.placesTypes = placesTypes;
    }

    public LinkedList<Integer> getSpaceBeetweenIndexes() {
        return spaceBeetweenIndexes;
    }

    public void setSpaceBeetweenIndexes(LinkedList<Integer> spaceBeetweenIndexes) {
        this.spaceBeetweenIndexes = spaceBeetweenIndexes;
    }
}
