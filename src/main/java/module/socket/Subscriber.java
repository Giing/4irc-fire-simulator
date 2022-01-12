package module.socket;

import java.util.List;

import module.model.Emergency;
import module.model.Sensor;
import module.model.Station;
import module.model.Team;

public class Subscriber {
    public void onUpdateSensors (List<Sensor> sensors) {}
    public void onUpdateEmergencies (List<Emergency> emergencies) {}
    public void onDeleteEmergencies (List<Emergency> emergencies) {}
    public void onUpdateStations (List<Station> stations) {}
    public void onUpdateTeams (List<Team> stations) {}
}
