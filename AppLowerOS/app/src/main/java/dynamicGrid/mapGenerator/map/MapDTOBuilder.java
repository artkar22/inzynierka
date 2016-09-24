package dynamicGrid.mapGenerator.map;

import java.util.ArrayList;

/**
 * Created by ArturK on 2016-09-24.
 */
public abstract class MapDTOBuilder {
    public static MapDTO buildMapDto(final String mapId, final int numberOfColumns, final int numberOfRows) {
        MapDTO dto = initializeMapDTO(mapId, numberOfColumns, numberOfRows);
        //TODO POCIĄGNĄĆ TO I ZAŁADOWAĆ MIEJSCA Z PLIKU MAPY;
        return dto;
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
            if (((numberOfColumns + 1) % currentPlaceInMapIndex) == 0 && currentPlaceInMapIndex != 0) {
                isItMapFlag = false;
            }
            final PlaceInMapDTO defaultPlaceInMap =
                    PlaceInMapDTOBuilder.buildPlaceInMapDto(currentPlaceInMapIndex, false, false, isItMapFlag);
            placesInMap.add(defaultPlaceInMap);
        }
        dto.setPlacesInMap(placesInMap);
        return dto;
    }

}
