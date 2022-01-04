package model;

import java.util.List;

import module.model.Coord;

public class Emergency {
    private final int id_fire;
    private final Coord location;
    private int intensity;
    private List<Sensor> sensors;

    public List<Sensor> getSensors() {
        return sensors;
    }

    public void setSensors(List<Sensor> sensors) {
        this.sensors = sensors;
    }

    public int getId() { return this.id_fire; }

    public boolean isThereSensorInFire(Sensor sensor) {
        boolean res = false;
        for (Sensor s : this.sensors) {
            if(s == sensor)
                res = true; break;
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

    public String toJSON() {
        return "{\"id\":" + this.id_fire +
                ", \"latitude\":" + this.location.getLatitude() +
                ", \"longitude\":" + this.location.getLongitude() +
                ", \"intensity\":" + this.intensity +
                "}";
    }

    public Emergency(int id_fire, Coord location, List<Sensor> sensors, int intensity) {
        this.id_fire = id_fire;
        this.location = location;
        this.sensors = sensors;
        this.intensity = intensity;
    }

    public Emergency(int id_fire, Coord location, List<Sensor> sensors) {
        this.id_fire = id_fire;
        this.location = location;
        this.sensors = sensors;
    }

    public Emergency(int id_fire, Coord location, int intensity) {
        this.id_fire = id_fire;
        this.location = location;
        this.intensity = intensity;
    }
}
