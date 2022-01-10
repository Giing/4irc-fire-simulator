package module.api;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import module.model.Coord;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class Map {

    final OkHttpClient client = new OkHttpClient();
    private String token;
    private String url;

    public Map(String tokenApi)
    {
        this.token = tokenApi;
        this.url = "http://www.mapquestapi.com/directions/v2/route?key=" + this.token;
    }

    public List<Coord> getDirection(Coord from, Coord to) throws IOException {
        System.out.println(this.url + "&from=" + from.toApi() + "&to=" + to.toApi());

        Request request = new Request.Builder()
            .url(this.url + "&from=" + from.toApi() + "&to=" + to.toApi())
            .build();

        List<Coord> legs = new ArrayList<Coord>();

        try (Response response = client.newCall(request).execute()) {
            JsonParser parser = new JsonParser();
            JsonObject object = (JsonObject) parser.parse(response.body().string());

            object.get("route").getAsJsonObject().get("legs").getAsJsonArray().forEach(leg -> {
                leg.getAsJsonObject().get("maneuvers").getAsJsonArray().forEach(maneuver -> {
                    JsonObject coord = maneuver.getAsJsonObject().get("startPoint").getAsJsonObject();
                    Coord point = new Coord(coord.get("lat").getAsDouble(), coord.get("lng").getAsDouble());
                    legs.add(point);
                });
            });

            // Exact coord just to be sure
            legs.add(to);
        } catch (Exception error) {
            legs.add(to);
            System.out.println(error);
        }

        // System.out.println("Map steps: ");
        // System.out.println(legs);

        return legs;
    }
}
