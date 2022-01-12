package module.model;

import com.google.gson.*;

import java.lang.reflect.Type;

public class StationAdapter implements JsonDeserializer<Station>, JsonSerializer<Station> {
    public Station deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject jsonObject = json.getAsJsonObject();

        Coord coord = new Coord(
            jsonObject.get("latitude").getAsDouble(),
            jsonObject.get("longitude").getAsDouble()
        );

        Station station = new Station(
            jsonObject.get("id").getAsString(),
            coord,
            jsonObject.get("name").getAsString(),
            jsonObject.get("street").getAsString(),
            jsonObject.get("cp").getAsInt(),
            jsonObject.get("radius").getAsDouble()
        );

        return station;
    }

    @Override
    public JsonElement serialize(Station src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject obj = new JsonObject();
        obj.addProperty("id", src.getId());
        obj.addProperty("longitude", src.getLocation().getLongitude());
        obj.addProperty("latitude", src.getLocation().getLatitude());
        obj.addProperty("name", src.getName());
        obj.addProperty("streetName", src.getStreetName());
        obj.addProperty("cp", src.getCp());
        obj.addProperty("radius", src.getRadius());
        return obj;
    }
}