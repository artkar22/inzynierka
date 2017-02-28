package dynamicGrid.mapGenerator.map;

/**
 * Created by ArturK on 2016-09-24.
 */
public abstract class PlaceInMapDTOBuilder {
//    public static PlaceInMapDTO buildPlaceInMapDto(final int placeInMapId, final boolean dropAllowed,
//                                                   final boolean alreadyDropped, final boolean isItMap) {
//        final PlaceInMapDTO placeDTO = new PlaceInMapDTO();
//        placeDTO.setPlaceInMapId(placeInMapId);
//        placeDTO.setDropAllowed(dropAllowed);
//        placeDTO.setAlreadyDropped(alreadyDropped);
//        placeDTO.setIsItMap(isItMap);
//        return placeDTO;
//    }

    public static PlaceInMapDTO buildPlaceInMapDto(final int placeInMapId, final boolean isItMap) {
        final PlaceInMapDTO placeDTO = new PlaceInMapDTO();
        placeDTO.setPlaceInMapId(placeInMapId);
        placeDTO.setIsItMap(isItMap);
        return placeDTO;
    }
}
