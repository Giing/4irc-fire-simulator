package module.model;

import com.google.gson.*;

import java.lang.reflect.Type;

public class SensorAdapter implements JsonDeserializer<Sensor>, JsonSerializer<Sensor> {
    public Sensor deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject jsonObject = json.getAsJsonObject();

        Coord coord = new Coord(
            jsonObject.get("longitude").getAsDouble(),
            jsonObject.get("latitude").getAsDouble()
        );

        return new Sensor(
            jsonObject.get("id").getAsString(),
            coord,
            jsonObject.get("intensity").getAsInt(),
            jsonObject.get("emergencyId").getAsString()
        );
    }

    @Override
    public JsonElement serialize(Sensor src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject obj = new JsonObject();
        obj.addProperty("id", src.getId());
        obj.addProperty("emergencyId", src.getEmergencyId());
        obj.addProperty("radius", src.getRadius());
        obj.addProperty("intensity", src.getIntensity());
        obj.addProperty("longitude", src.getLocation().getLongitude());
        obj.addProperty("longitude", src.getLocation().getLatitude());
        obj.addProperty("latitude", src.getRadius());
        return obj;
    }
}