package app;

import model.Coord;
import model.Fire;
import model.Sensor;

import java.util.*;

public class Simulator {

    private final double longitude_max    = 4.896831000000001;
    private final double latitude_min     = 45.723967;
    private final double longitude_min    = 4.797831;
    private final double latitude_max     = 45.773967;
    private final double longitude_step   = 0.11 / 10.0;
    private final double latitude_step    = 0.06 / 6.0;
    private List<Sensor> sensors = new ArrayList<>();
    private List<Fire> fires = new ArrayList<>();

    public double getLongitude_min() {
        return longitude_min;
    }

    public double getLongitude_max() {
        return longitude_max;
    }

    public double getLatitude_min() {
        return latitude_min;
    }

    public double getLatitude_max() {
        return latitude_max;
    }

    public double getLongitude_step() {
        return longitude_step;
    }

    public double getLatitude_step() {
        return latitude_step;
    }

    public List<Sensor> getSensors() {
        return this.sensors;
    }

    public Simulator initializeSimulation() {
        int id = 0;
        for (double i = 0; i < 10; i++) {
            for (double j = 0; j < 6; j++) {
                this.sensors.add(new Sensor(String.valueOf(id), new Coord((j * this.latitude_step) + this.latitude_min, (i * this.longitude_step) + this.longitude_min), 0));
                id++;
            }
        }
        return this;
    }

    public Fire initFire() {
        // Génération de la latitude/longitude du feu
        double fireLat = this.latitude_min + Math.random() * (this.latitude_max - this.latitude_min);
        double fireLong = this.longitude_min + Math.random() * (this.longitude_max - this.longitude_min);
        List<Sensor> fireSensors = new ArrayList<>();
        for (Sensor sensor : this.sensors) {
            double distance = Math.pow((fireLat - sensor.getLocation().getLatitude()), 2) + Math.pow((fireLong - sensor.getLocation().getLongitude()), 2);
            double sensorRadius = Math.pow(sensor.getRadius(), 2);
            // Calcul de l'intensité en fonction de la distance entre le centre du feu et le centre du sensor
            int intensity = (int)Math.round(distance * 100 / sensorRadius);
            if (distance < sensorRadius) {
                sensor.incrementIntensite(intensity);
                fireSensors.add(sensor);
            }
        }
        Fire fire = new Fire(this.fires.size(), new Coord(fireLat, fireLong), fireSensors);
        if (canDeclareFire(fire))
            this.fires.add(fire);
        return fire;
    }

    public boolean canDeclareFire(Fire fire) {
        boolean res = true;
        for(Sensor s : fire.getSensors()) {
            if(fire.isThereSensorInFire(s))
                res = false;
        }
        return res;
    }

    @Override
    public String toString() {
        String str = "========Simulator========\n";
        for(Sensor sensor : this.sensors)
            str += sensor.toJSON() + ",\n";
        str += "===================\n";
        return str;
    }
}
