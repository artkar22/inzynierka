package dynamicGrid.mapGenerator.map;

import java.util.ArrayList;

/**
 * Created by ArturK on 2016-09-24.
 */
public class MapDTO {

    private String mapID;
    private int numberOfColums;
    private int numberOfRows;
    private ArrayList<PlaceInMapDTO> placesInMap;

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

    public ArrayList<PlaceInMapDTO> getPlacesInMap() {
        return placesInMap;
    }

    public void setPlacesInMap(final ArrayList<PlaceInMapDTO> placesInMap) {
        this.placesInMap = placesInMap;
    }

}
