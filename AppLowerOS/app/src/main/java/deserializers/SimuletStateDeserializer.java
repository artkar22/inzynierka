package deserializers;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;


import modules.SimuletsStateToSend;

/**
 * Created by Inni on 2017-05-23.
 */

public class SimuletStateDeserializer implements JsonDeserializer<SimuletsStateToSend> {
    @Override
    public SimuletsStateToSend deserialize(JsonElement json, Type typeOfT,
                                           JsonDeserializationContext context) throws JsonParseException {

        JsonObject object = json.getAsJsonObject();
        String a = object.get("StateId").getAsString();
        JsonArray b = object.get("miniature").getAsJsonArray();
        JsonArray c = object.get("highlightedMiniature").getAsJsonArray();
        if(object.has("eventType")){
            String d = object.get("eventType").getAsString();
            return new SimuletsStateToSend(a,transformToBytes(b),transformToBytes(c), d);
        }
        return new SimuletsStateToSend(a,transformToBytes(b),transformToBytes(c), null);

    }
    private byte[] transformToBytes(JsonArray array) {
        byte [] bytesy = new byte[array.size()];
        for(int x = 0; x < array.size(); x++){
            bytesy[x] = array.get(x).getAsByte();
        }
        return bytesy;
    }
}
