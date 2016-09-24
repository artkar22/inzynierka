package dynamicGrid.mapGenerator.map;

import java.util.ArrayList;

/**
 * Created by ArturK on 2016-09-24.
 */
public abstract class MapDTOBuilder {
    public static MapDTO buildMapDto(final String mapId, final int numberOfColumns,
                                     final int numberOfRows, final ArrayList<PlaceInMapDTO> specialPlacesInMap) {
        MapDTO dto = initializeMapDTO(mapId, numberOfColumns, numberOfRows);
        if (specialPlacesInMap.size() > 0) {
            setSpecialPlacesOnDTO(dto.getPlacesInMap(), specialPlacesInMap);
        }
        return dto;
    }

    private static void setSpecialPlacesOnDTO(ArrayList<PlaceInMapDTO> placesInDto, ArrayList<PlaceInMapDTO> specialPlacesInMap) {
        for(PlaceInMapDTO specialPlace : specialPlacesInMap){
            for(PlaceInMapDTO placeInDto:placesInDto){
                if(specialPlace.getPlaceInMapId() == placeInDto.getPlaceInMapId()){
                    placeInDto.setIsItMap(specialPlace.isItMap());
                    placeInDto.setDropAllowed(specialPlace.isDropAllowed());
                    placeInDto.setAlreadyDropped(specialPlace.isAlreadyDropped());
                    break;
                }
            }
        }
    }

    private static MapDTO initializeMapDTO(final String mapId, final int numberOfColumns, final int numberOfRows) {
        MapDTO dto = new MapDTO();
        dto.setMapID(mapId);
        dto.setNumberOfColums(numberOfColumns);
        dto.setNumberOfRows(numberOfRows);
        final int numberOfPlacesInMap = numberOfColumns * numberOfRows;
        final ArrayList<PlaceInMapDTO> placesInMap = new ArrayList<>();
        for (int currentPlaceInMapIndex = 0; currentPlaceInMapIndex < numberOfPlacesInMap; currentPlaceInMapIndex++) {
            boolean isItMapFlag = true;
            if (checkIfCurrentIndexShouldBeItemCollection(numberOfColumns, currentPlaceInMapIndex)) {
                isItMapFlag = false;
            }
            final PlaceInMapDTO defaultPlaceInMap =
                    PlaceInMapDTOBuilder.buildPlaceInMapDto(currentPlaceInMapIndex, false, false, isItMapFlag);
            placesInMap.add(defaultPlaceInMap);
        }
        dto.setPlacesInMap(placesInMap);
        return dto;
    }

    private static boolean checkIfCurrentIndexShouldBeItemCollection(final int numberOfColumns, final int currentPlaceInMapIndex) {
        if (currentPlaceInMapIndex + 1 >= numberOfColumns && ((currentPlaceInMapIndex + 1) % (numberOfColumns)) == 0) {
            return true;
        }
        return false;
    }
}
