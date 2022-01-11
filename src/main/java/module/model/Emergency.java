package module.model;

import java.util.List;
import java.util.Timer;

import com.google.gson.JsonDeserializer;

public class Emergency {
    private final String id_emergency;
    private Coord location;
    private int intensity;
    private List<Sensor> sensors;

    public Boolean isHandled = false;

    public static JsonDeserializer<Emergency> jsonAdapter = new EmergencyAdapter();

    public List<Sensor> getSensors() {
        return sensors;
    }

    public void setSensors(List<Sensor> sensors) {
        this.sensors = sensors;
    }
    public void addSensor(Sensor sensor) {
        this.sensors.add(sensor);
    }

    public String getId() { return this.id_emergency; }
    public int getIntensity() { return this.intensity; }
    public void setIntensity(int intensity) {
        if(intensity < 0) intensity = 0;
        else if(intensity > 100) intensity = 100;
        this.intensity = intensity; 
    }
    public Coord getLocation() { return this.location; }

    public void setLocation(Coord position) {
        this.location = position;
    }

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
                ", intensity=" + this.intensity +
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

    public void computeIntensityFromSensors(List<Sensor> sensors) {
        this.intensity = 0;
        for (Sensor sensor : sensors) {
            if(this.intensity < sensor.getIntensity()) this.intensity = sensor.getIntensity();
        }
        // Sensor sensor = sensors.get(0);
        // double distance = Math.pow((this.location.getLatitude() - sensor.getLocation().getLatitude()), 2) + Math.pow((this.location.getLongitude() - sensor.getLocation().getLongitude()), 2);
        // double sensorRadius = Math.pow(sensor.getRadius(), 2);
        // // Calcul de l'intensité en fonction de la distance entre le centre du feu et le centre du sensor
        // this.intensity = (int)Math.ceil(sensor.getIntensity() * sensorRadius / (sensorRadius - distance) );
    }

    public void updateIntensity() {
        for (Sensor sensor : sensors) {
            double distance = this.getLocation().getDistance(sensor.getLocation());
            int intensitySensor = (int)Math.ceil((sensor.getRadius() - distance) * this.getIntensity() / sensor.getRadius());
            if (distance < sensor.getRadius()) {
                sensor.setIntensity(intensitySensor);
            }
        }
    } 

    @Override
    public boolean equals(Object obj) {
        boolean res = false;
        if (obj instanceof Emergency) {
            Emergency other = (Emergency) obj;
            res = this.id_emergency.equals(other.getId());
        }
        return res;
    }
}
