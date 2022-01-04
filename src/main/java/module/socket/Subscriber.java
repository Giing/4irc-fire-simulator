package module.socket;

import java.util.ArrayList;

import module.model.Emergency;
import module.model.Fire;
import module.model.Sensor;
import module.model.Station;

public class Subscriber {
    public void onUpdateSensors (ArrayList<Sensor> sensors) {}
    public void onUpdateEmergencies (ArrayList<Emergency> emergencies) {}
    public void onUpdateStations (ArrayList<Station> stations) {}
}
