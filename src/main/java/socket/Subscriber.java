package socket;

import model.Fire;
import model.Sensor;
import model.Station;

import java.util.ArrayList;

public class Subscriber {
    public void onUpdateSensors (ArrayList<Sensor> sensors) {}
    public void onUpdateEmergencies (ArrayList<Fire> emergencies) {}
    public void onUpdateStations (ArrayList<Station> stations) {}
}
