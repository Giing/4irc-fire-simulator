package app;

import module.model.Emergency;
import module.model.Sensor;
import module.model.Station;
import module.socket.Subscriber;

import java.util.List;

public class Manager extends Subscriber {

    public static void main(String args[]) throws Exception {
        System.out.println("Hello je suis le manager");
    }

    @Override
    public void onUpdateSensors(List<Sensor> sensors) {
        super.onUpdateSensors(sensors);
    }

    @Override
    public void onUpdateEmergencies(List<Emergency> emergencies) {
        super.onUpdateEmergencies(emergencies);
    }

    @Override
    public void onUpdateStations(List<Station> stations) {
        super.onUpdateStations(stations);
    }
}
