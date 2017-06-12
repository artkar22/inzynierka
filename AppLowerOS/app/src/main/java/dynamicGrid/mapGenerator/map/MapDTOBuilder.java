package dynamicGrid.mapGenerator.map;

import android.util.Pair;

import java.util.ArrayList;
import java.util.LinkedList;

import dynamicGrid.mapGenerator.MapGenerator;

/**
 * Created by ArturK on 2016-09-24.
 */
public abstract class MapDTOBuilder {
    public static final String CONTAINER = "CONTAINER";
    public static final String TRIGGER_PLACE = "TRIGGER_PLACE";
    public static final String SIMULET_PLACE = "SIMULET_PLACE";
    public static final String PAUSE_SIMULET_PLACE = "PAUSE_SIMULET_PLACE";
    public static final String ARROW_PLACE = "ARROW_PLACE";//strzałeczki pomiedzy triggerami a sekwencja simuletow
    public static final String SPACE_BEETWEEN = "SPACE_BEETWEEN"; //miejsce miedzy simuletami a sekwencjami

    public static MapDTO buildMapDto(final String mapId, final int numberOfColumns,
                                     final int numberOfRows, final int numberOfStateRows) {
        MapDTO dto = initializeMapDTO(mapId, numberOfColumns, numberOfRows, numberOfStateRows);
//        dto.setSpecialPlacesIds(specialPlacesInMap);
//        if (specialPlacesInMap.size() > 0) {
//            setSpecialPlacesOnDTO(dto.getPlacesInMap(), specialPlacesInMap);
//        }
        return dto;
    }

    private static MapDTO initializeMapDTO(final String mapId, final int numberOfColumns,
                                           final int numberOfRows, final int numberOfStateRows) {
        final MapDTO dto = new MapDTO();
        dto.setMapID(mapId);
        dto.setNumberOfColums(numberOfColumns);
        dto.setNumberOfRows(numberOfRows);
        dto.setNumberOfStatesRows(numberOfStateRows);
        dto.setTriggersIndexes(calculateIndexesForTriggers(numberOfColumns, numberOfRows));
        dto.setArrowsIndexes(calculateIndexesForArrows(dto.getTriggersIndexes()));
        dto.setSpaceBeetweenIndexes(calculateIndexesForSpaceBeetween(numberOfColumns));
        final int numberOfPlacesInMap = numberOfColumns * numberOfRows;
        final int numberOfPlacesForAnItemContainer = numberOfStateRows * numberOfColumns;
        final LinkedList<PlaceInMapDTO> placesInMap = new LinkedList<>();
        final ArrayList<String> placesTypes = new ArrayList<>();

        for (int currentPlaceInMapIndex = 0; currentPlaceInMapIndex < numberOfPlacesInMap; currentPlaceInMapIndex++) {
            String type;
            if(currentPlaceInMapIndex == 6) {
                type = PAUSE_SIMULET_PLACE;
            }else if(currentPlaceInMapIndex == 13){
                type = SPACE_BEETWEEN;
            } else if (currentPlaceInMapIndex < numberOfPlacesForAnItemContainer) {
                type = CONTAINER;
            } else if (dto.checkIfTriggersIndexesContainsValue(currentPlaceInMapIndex)) {
                type = TRIGGER_PLACE;
            }else if (dto.checkIfArrowsIndexesContainsValue(currentPlaceInMapIndex)) {
                type = ARROW_PLACE;
            } else if(dto.checkIfSpaceBeetweenIndexesContainsValue(currentPlaceInMapIndex)){
              type = SPACE_BEETWEEN;
            } else {
                type = SIMULET_PLACE;
            }
            final PlaceInMapDTO defaultPlaceInMap =
                    PlaceInMapDTOBuilder.buildPlaceInMapDto(currentPlaceInMapIndex, type);
            placesInMap.add(defaultPlaceInMap);
            placesTypes.add(type);
        }
        dto.setPlacesInMap(placesInMap);
        dto.setPlacesTypes(placesTypes);
        return dto;
    }

    private static LinkedList<Integer> calculateIndexesForArrows(LinkedList<Integer> triggersIndexes) {
        final LinkedList<Integer> arrowsIndexes = new LinkedList<>();
        for(Integer index: triggersIndexes){
            arrowsIndexes.add(new Integer(index.intValue() + 1));
        }
        return arrowsIndexes;
    }

    private static LinkedList<Integer> calculateIndexesForTriggers(final int numberOfColumns, final int numberOfRows) {
        final LinkedList<Integer> triggersIndexes = new LinkedList<>();
        for (int x = 3; x < numberOfRows; x++) {
            triggersIndexes.add(new Integer(x * numberOfColumns));
        }
        return triggersIndexes;
    }

    private static LinkedList<Integer> calculateIndexesForSpaceBeetween(int numberOfColumns) {
        final LinkedList<Integer> spacesBeetween = new LinkedList<>();
        int lastIndex = 3*numberOfColumns;
        for(int firstIndex = 2*numberOfColumns; firstIndex<lastIndex; firstIndex++) {
            spacesBeetween.add(new Integer(firstIndex));
        }
        return spacesBeetween;
    }

//    private static boolean checkIfCurrentIndexShouldBeItemCollection(final int numberOfColumns, final int numberOfRows, final int currentPlaceInMapIndex) {
//        final int halfOfTheMap = (numberOfColumns * numberOfRows) / 2;
//        if (currentPlaceInMapIndex < halfOfTheMap) {
//            return true;
//        }
//        return false;
//    }
}
