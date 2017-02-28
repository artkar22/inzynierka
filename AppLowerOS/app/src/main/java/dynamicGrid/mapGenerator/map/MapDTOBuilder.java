package dynamicGrid.mapGenerator.map;

import java.util.ArrayList;
import java.util.LinkedList;

/**
 * Created by ArturK on 2016-09-24.
 */
public abstract class MapDTOBuilder {
    public static MapDTO buildMapDto(final String mapId, final int numberOfColumns,
                                     final int numberOfRows, final int numberOfStateRows) {
        MapDTO dto = initializeMapDTO(mapId, numberOfColumns, numberOfRows, numberOfStateRows);
//        dto.setSpecialPlacesIds(specialPlacesInMap);
//        if (specialPlacesInMap.size() > 0) {
//            setSpecialPlacesOnDTO(dto.getPlacesInMap(), specialPlacesInMap);
//        }
        return dto;
    }

//    private static void setSpecialPlacesOnDTO(LinkedList<PlaceInMapDTO> placesInDto, ArrayList<PlaceInMapDTO> specialPlacesInMap) {
//        for (PlaceInMapDTO specialPlace : specialPlacesInMap) {
//            for (PlaceInMapDTO placeInDto : placesInDto) {
//                if (specialPlace.getPlaceInMapId() == placeInDto.getPlaceInMapId()) {
//                    placeInDto.setIsItMap(true);
//                    placeInDto.setDropAllowed(specialPlace.isDropAllowed());
//                    placeInDto.setAlreadyDropped(specialPlace.isAlreadyDropped());
//                    break;
//                }
//            }
//        }
//    }

    private static MapDTO initializeMapDTO(final String mapId, final int numberOfColumns,
                                           final int numberOfRows, final int numberOfStateRows) {
        final MapDTO dto = new MapDTO();
        dto.setMapID(mapId);
        dto.setNumberOfColums(numberOfColumns);
        dto.setNumberOfRows(numberOfRows);
        dto.setNumberOfStatesRows(numberOfStateRows);

        final int numberOfPlacesInMap = numberOfColumns * numberOfRows;
        final int numberOfPlacesForAnItemContainer = numberOfStateRows * numberOfColumns;
        final LinkedList<PlaceInMapDTO> placesInMap = new LinkedList<>();

        for (int currentPlaceInMapIndex = 0; currentPlaceInMapIndex < numberOfPlacesInMap; currentPlaceInMapIndex++) {
            boolean isItMapFlag = true;
            if (currentPlaceInMapIndex < numberOfPlacesForAnItemContainer) {
                isItMapFlag = false;
            }
            final PlaceInMapDTO defaultPlaceInMap =
                    PlaceInMapDTOBuilder.buildPlaceInMapDto(currentPlaceInMapIndex, isItMapFlag);
            placesInMap.add(defaultPlaceInMap);
        }
        dto.setPlacesInMap(placesInMap);
        return dto;
    }

//    private static boolean checkIfCurrentIndexShouldBeItemCollection(final int numberOfColumns, final int numberOfRows, final int currentPlaceInMapIndex) {
//        final int halfOfTheMap = (numberOfColumns * numberOfRows) / 2;
//        if (currentPlaceInMapIndex < halfOfTheMap) {
//            return true;
//        }
//        return false;
//    }
}
