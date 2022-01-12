package module.api.services;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;

import module.api.Http;
import module.model.Station;

public class StationService extends ApiService{

    public StationService(Http client) {
        super(client);
    }

    @Override
    public List<Station> getAll() {
        try {
            String result = this.client.get("/stations");
            System.out.println(result);
            return this.mapper.station.fromJson(new JSONArray(result));
        } catch (JSONException | IOException e) {
            // TODO Auto-generated catch block
            // e.printStackTrace();
            return new ArrayList<Station>();
        }
    }

    @Override
    public List<Station> createOrUpdate(List stations) {
        try {
            String result = this.client.post("/stations", this.mapper.station.toJson(stations));
            return this.mapper.station.fromJson(new JSONArray(result));
        } catch (IOException | JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return new ArrayList<Station>();
        }
    }

    public Station getById(String id) {
        try {
            String result = this.client.get("/stations/" + id);
            List<Station> stations = this.mapper.station.fromJson(new JSONArray("[" + result + "]"));
            return stations.get(0);
        } catch (IOException | JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;
        }
    }
}
