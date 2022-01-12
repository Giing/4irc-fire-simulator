package module.api.services;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;

import module.api.Http;
import module.model.Team;

public class TeamService extends ApiService{

    public TeamService(Http client) {
        super(client);
    }

    @Override
    public List<Team> getAll() {
        try {
            String result = this.client.get("/teams");
            return this.mapper.team.fromJson(new JSONArray(result));
        } catch (JSONException | IOException e) {
            // TODO Auto-generated catch block
            // e.printStackTrace();
            return new ArrayList<Team>();
        }
    }

    @Override
    public List<Team> createOrUpdate(List teams) {
        try {
            String result = this.client.post("/teams", this.mapper.team.toJson(teams));
            return this.mapper.team.fromJson(new JSONArray(result));
        } catch (IOException | JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return new ArrayList<Team>();
        }
    }

    public List<Team> getAllByStation(String stationId) {
        try {
            String result = this.client.get("/stations/" + stationId + "/teams");
            return this.mapper.team.fromJson(new JSONArray(result));
        } catch (JSONException | IOException e) {
            // TODO Auto-generated catch block
            // e.printStackTrace();
            return new ArrayList<Team>();
        }
    }

    public List<Team> getAllByEmergency(String emergencyId) {
        try {
            String result = this.client.get("/teams/emergency/" + emergencyId);
            return this.mapper.team.fromJson(new JSONArray(result));
        } catch (JSONException | IOException e) {
            // TODO Auto-generated catch block
            // e.printStackTrace();
            return new ArrayList<Team>();
        }
    }

    public Team reset(String id) {
        try {
            String result = this.client.get("/teams/reset/" + id);
            return this.mapper.team.fromJson(new JSONArray("[" + result + "]")).get(0);
        } catch (JSONException | IOException e) {
            // TODO Auto-generated catch block
            // e.printStackTrace();
            return null;
        }
    }
}
