package module.model;

import com.google.gson.*;

import java.lang.reflect.Type;

public class SensorAdapter implements JsonDeserializer<Sensor>, JsonSerializer<Sensor> {
    public Sensor deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject jsonObject = json.getAsJsonObject();

        Coord coord = new Coord(
            jsonObject.get("latitude").getAsDouble(),
            jsonObject.get("longitude").getAsDouble()
        );

        Sensor sensor = new Sensor(
                jsonObject.get("id").getAsString(),
                coord,
                jsonObject.get("intensity").getAsInt()
        );

        // Si il existe un emergencyId on l'ajoute à l'objet sensor
        if (!jsonObject.get("emergencyId").isJsonNull())
            sensor.setEmergencyId(jsonObject.get("emergencyId").getAsString());

        return sensor;
    }

    @Override
    public JsonElement serialize(Sensor src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject obj = new JsonObject();
        obj.addProperty("id", src.getId());
        if(src.getEmergencyId() != null) {
            obj.addProperty("emergencyId", src.getEmergencyId());
        }
        obj.addProperty("radius", src.getRadius());
        obj.addProperty("intensity", src.getIntensity());
        obj.addProperty("longitude", src.getLocation().getLongitude());
        obj.addProperty("latitude", src.getLocation().getLatitude());
        return obj;
    }
}