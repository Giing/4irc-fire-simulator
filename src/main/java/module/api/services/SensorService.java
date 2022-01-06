package module.api.services;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;

import module.api.Http;
import module.model.Sensor;

public class SensorService extends ApiService{

    public SensorService(Http client) {
        super(client);
    }

    @Override
    public List<Sensor> getAll() {
        try {
            String result = this.client.get("/sensors");
            return this.mapper.sensor.fromJson(new JSONArray(result));
        } catch (JSONException | IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return new ArrayList<Sensor>();
        }
    }

    @Override
    public List<Sensor> createOrUpdate(List sensors) {
        try {
            String result = this.client.post("/sensors", this.mapper.sensor.toJson(sensors));
            return this.mapper.sensor.fromJson(new JSONArray(result));
        } catch (IOException | JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return new ArrayList<Sensor>();
        }
    }
}
