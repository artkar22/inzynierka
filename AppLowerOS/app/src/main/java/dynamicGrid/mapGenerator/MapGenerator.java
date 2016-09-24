package dynamicGrid.mapGenerator;

import org.json.JSONException;
import org.json.JSONObject;

import dynamicGrid.mapGenerator.map.MapDTO;
import dynamicGrid.mapGenerator.map.MapDTOBuilder;
import dynamicGrid.mapGenerator.utils.Consts;

/**
 * Created by ArturK on 2016-09-24.
 */
public class MapGenerator {

    public static MapDTO loadMap(String mapName) {
        final JSONObject reader;
        try {
            reader = new JSONObject(Consts.PATH_TO_MAPS + mapName);
            final String mapID = reader.getString(Consts.MAP_ID);
            final int numberOfColumns = reader.getInt(Consts.NUMBER_OF_COLUMNS);
            final int numberOfRows = reader.getInt(Consts.NUMBER_OF_ROWS);

            return MapDTOBuilder.buildMapDto(mapID, numberOfColumns, numberOfRows);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

}
