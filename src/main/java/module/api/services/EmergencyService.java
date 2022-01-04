package module.api.services;

import java.io.IOException;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;

import module.api.Http;
import module.model.Emergency;

public class EmergencyService extends ApiService{

    public EmergencyService(Http client) {
        super(client);
    }

    @Override
    public List<Emergency> getAll() {
        try {
            String result = this.client.get("/emergencies");
            return this.mapper.emergency.fromJson(new JSONArray(result));
        } catch (JSONException | IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<Emergency> createOrUpdate(List emergencies) {
        try {
            String result = this.client.post("/emergencies", this.mapper.emergency.toJson(emergencies));
            return this.mapper.emergency.fromJson(new JSONArray(result));
        } catch (IOException | JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;
        }
    }
}
