package module.api.services;

import java.io.IOException;
import java.util.ArrayList;
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
            return new ArrayList<Emergency>();
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
            return new ArrayList<Emergency>();
        }
    }

    public void delete(Emergency emergency) {
        try {
            String result = this.client.delete("/emergencies/" + emergency.getId());
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public Emergency getById(String emergencyId) {
        try {
            String result = this.client.get("/emergencies/" + emergencyId);
            List<Emergency> emergencies = this.mapper.emergency.fromJson(new JSONArray("[" + result + "]"));
            return emergencies.get(0);
        } catch (IOException | JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;
        }
    }
}
