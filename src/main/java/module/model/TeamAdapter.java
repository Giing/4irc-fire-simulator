package module.model;

import com.google.gson.*;

import java.lang.reflect.Type;

public class TeamAdapter implements JsonDeserializer<Team>, JsonSerializer<Team> {
    public Team deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject jsonObject = json.getAsJsonObject();

        Coord coord = new Coord(
            jsonObject.get("latitude").getAsDouble(),
            jsonObject.get("longitude").getAsDouble()
        );

        Team team = new Team(
            jsonObject.get("id").getAsString(),
            coord,
            jsonObject.get("level").getAsInt(),
            jsonObject.get("stationId").getAsString()
        );

        // Si il existe un emergencyId on l'ajoute à l'objet sensor
        if (jsonObject.has("emergencyId") && !jsonObject.get("emergencyId").isJsonNull())
            team.setEmergencyId(jsonObject.get("emergencyId").getAsString());

        return team;
    }

    @Override
    public JsonElement serialize(Team src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject obj = new JsonObject();
        obj.addProperty("id", src.getId());
        obj.addProperty("longitude", src.getLocation().getLongitude());
        obj.addProperty("latitude", src.getLocation().getLatitude());
        obj.addProperty("stationId", src.getStationId());
        obj.addProperty("emergencyId", src.getEmergencyId());
        return obj;
    }
}