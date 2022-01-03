package model;

import com.google.gson.*;

import java.lang.reflect.Type;

public class SensorDeserializer implements JsonDeserializer<Sensor> {
    public Sensor deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject jsonObject = json.getAsJsonObject();

        Coord coord = new Coord(
                jsonObject.get("longitude").getAsDouble(),
                jsonObject.get("latitude").getAsDouble()
        );

        return new Sensor(
                jsonObject.get("id").getAsString(),
                coord,
                jsonObject.get("intensity").getAsInt()
        );
    }
}
