package dynamicGrid.mapGenerator;

import android.content.res.AssetManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import dynamicGrid.mapGenerator.map.MapDTO;
import dynamicGrid.mapGenerator.map.MapDTOBuilder;
import dynamicGrid.mapGenerator.map.PlaceInMapDTO;
import dynamicGrid.mapGenerator.map.PlaceInMapDTOBuilder;
import dynamicGrid.mapGenerator.utils.Consts;

/**
 * Created by ArturK on 2016-09-24.
 */
public class MapGenerator {

    public static MapDTO loadMap(final AssetManager assets, final String mapName) {
        final JSONObject reader;
        final String jsonString = loadJSONFromAsset(assets, mapName);
        try {
            reader = new JSONObject(jsonString);
            final String mapID = reader.getString(Consts.MAP_ID);
            final int numberOfColumns = reader.getInt(Consts.NUMBER_OF_COLUMNS);
            final int numberOfRows = reader.getInt(Consts.NUMBER_OF_ROWS);
            final JSONArray jsonMapPlaces = reader.getJSONArray(Consts.MAP);
            final ArrayList<PlaceInMapDTO> specialPlacesInMap = getSpecialPlacesFromJson(jsonMapPlaces);
            return MapDTOBuilder.buildMapDto(mapID, numberOfColumns, numberOfRows, specialPlacesInMap);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static String loadJSONFromAsset(final AssetManager assets, final String mapName) {
        String json = null;
        try {
            InputStream is = assets.open(mapName);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");

        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }

    private static ArrayList<PlaceInMapDTO> getSpecialPlacesFromJson(JSONArray jsonMapPlaces) {
        ArrayList<PlaceInMapDTO> places = new ArrayList<>();
        for (int x = 0; x < jsonMapPlaces.length(); x++) {
            try {
                JSONObject jsonPlace = jsonMapPlaces.getJSONObject(x);
                PlaceInMapDTO placeDto = PlaceInMapDTOBuilder
                        .buildPlaceInMapDto(jsonPlace.getInt(Consts.ID),
                                jsonPlace.getBoolean(Consts.DROP_ALLOWED),
                                jsonPlace.getBoolean(Consts.ALREADY_DROPPED),
                                false);
                places.add(placeDto);
            } catch (JSONException e) {
                e.printStackTrace();
                return places;
            }
        }
        return places;
    }
}
