package module.model;

import com.google.gson.*;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class EmergencyAdapter implements JsonDeserializer<Emergency>, JsonSerializer<Emergency> {
    public Emergency deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject jsonObject = json.getAsJsonObject();

        Coord coord = new Coord(
            jsonObject.get("longitude").getAsDouble(),
            jsonObject.get("latitude").getAsDouble()
        );

        return new Emergency(
            jsonObject.get("id_emergency").getAsString(),
            coord,
            new ArrayList<Sensor>()
        );
    }

    @Override
    public JsonElement serialize(Emergency src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject obj = new JsonObject();
        obj.addProperty("id", src.getId());
        obj.addProperty("intensity", src.getIntensity());
        obj.addProperty("longitude", src.getLocation().getLongitude());
        obj.addProperty("longitude", src.getLocation().getLatitude());
        return obj;
    }
}