package module.model;

import java.util.List;

import com.google.gson.JsonDeserializer;

public class Emergency {
    private final String id_emergency;
    private final Coord location;
    private int intensity;
    private List<Sensor> sensors;

    public static JsonDeserializer<Emergency> jsonAdapter = new EmergencyAdapter();

    public List<Sensor> getSensors() {
        return sensors;
    }

    public void setSensors(List<Sensor> sensors) {
        this.sensors = sensors;
    }

    public String getId() { return this.id_emergency; }
    public int getIntensity() { return this.intensity; }
    public Coord getLocation() { return this.location; }

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
                "id_emergency=" + id_emergency +
                ", location=" + this.location +
                "), sensors=\n" + sensors +
                '}';
    }

    public String toJSON() {
        return "{\"id\":" + this.id_emergency +
                ", \"latitude\":" + this.location.getLatitude() +
                ", \"longitude\":" + this.location.getLongitude() +
                ", \"intensity\":" + this.intensity +
                "}";
    }

    public Emergency(String id_emergency, Coord location, List<Sensor> sensors, int intensity) {
        this.id_emergency = id_emergency;
        this.location = location;
        this.sensors = sensors;
        this.intensity = intensity;
    }

    public Emergency(String id_emergency, Coord location, List<Sensor> sensors) {
        this.id_emergency = id_emergency;
        this.location = location;
        this.sensors = sensors;
    }

    public Emergency(String id_emergency, Coord location, int intensity) {
        this.id_emergency = id_emergency;
        this.location = location;
        this.intensity = intensity;
    }

    public Emergency(int id_emergency, Coord location, List<Sensor> sensors, int intensity) {
        this.id_emergency = String.valueOf(id_emergency);
        this.location = location;
        this.sensors = sensors;
        this.intensity = intensity;
    }

    public Emergency(int id_emergency, Coord location, List<Sensor> sensors) {
        this.id_emergency = String.valueOf(id_emergency);
        this.location = location;
        this.sensors = sensors;
    }

    public Emergency(int id_emergency, Coord location, int intensity) {
        this.id_emergency = String.valueOf(id_emergency);
        this.location = location;
        this.intensity = intensity;
    }
}