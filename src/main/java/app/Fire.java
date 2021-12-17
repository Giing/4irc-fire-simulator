package app;

import java.util.ArrayList;
import java.util.List;

public class Fire {
    private final int id_fire;
    private final Coord location;
    private List<Sensor> sensors;

    public Fire(int id_fire, Coord location, List<Sensor> sensors) {
        this.id_fire = id_fire;
        this.location = location;
        this.sensors = sensors;
    }

    public List<Sensor> getSensors() {
        return sensors;
    }

    public void setSensors(List<Sensor> sensors) {
        this.sensors = sensors;
    }

    public boolean isThereSensorInFire(Sensor sensor) {
        boolean res = false;
        for (Sensor s : this.sensors) {
            if(s == sensor)
                res = true;
        }
        return res;
    }


    @Override
    public String toString() {
        return "Fire{" +
                "id_fire=" + id_fire +
                ", location=" + this.location +
                "), sensors=\n" + sensors +
                '}';
    }
}
