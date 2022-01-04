package module.socket;

import java.util.List;

import module.model.Emergency;
import module.model.Sensor;
import module.model.Station;

public class Subscriber {
    public void onUpdateSensors (List<Sensor> sensors) {}
    public void onUpdateEmergencies (List<Emergency> emergencies) {}
    public void onUpdateStations (List<Station> stations) {}
}
