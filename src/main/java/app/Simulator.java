package app;

import java.lang.reflect.Array;
import java.util.*;

import module.json.JsonMapper;
import module.model.Coord;
import module.model.Emergency;
import module.model.Sensor;
import module.socket.Subscriber;

public class Simulator extends Subscriber{

    private final double longitude_max    = 4.896831000000001;
    private final double latitude_min     = 45.723967;
    private final double longitude_min    = 4.797831;
    private final double latitude_max     = 45.773967;
    private final double longitude_step   = 0.11 / 10.0;
    private final double latitude_step    = 0.06 / 6.0;
    private List<Sensor> sensors = new ArrayList<>();

    public List<Emergency> getEmergencies() {
        return emergencies;
    }

    public void addEmergency(Emergency em) {
        this.emergencies.add(em);
    }

    public void setEmergencies(List<Emergency> emergencies) {
        this.emergencies = emergencies;
    }

    private List<Emergency> emergencies = new ArrayList<>();
    protected JsonMapper mapper = JsonMapper.getInstance();

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

    public Simulator(List<Sensor> sensors) {
        this.sensors = sensors;
    }

    public Simulator(){}

    @Deprecated
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

    private static Simulator INSTANCE = null;

    public static synchronized Simulator getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new Simulator();
        }
        return INSTANCE;
    }

    public Emergency initEmergency() {
        // Génération de la latitude/longitude du feu
        double fireLat = this.latitude_min + Math.random() * (this.latitude_max - this.latitude_min);
        double fireLong = this.longitude_min + Math.random() * (this.longitude_max - this.longitude_min);
        Coord fireCoord = new Coord(fireLat, fireLong);
        List<Sensor> fireSensors = new ArrayList<>();

        for (Sensor sensor : this.sensors) {
            double distance = Math.pow((fireLat - sensor.getLocation().getLatitude()), 2) + Math.pow((fireLong - sensor.getLocation().getLongitude()), 2);
            double sensorRadius = Math.pow(sensor.getRadius(), 2);

            // Calcul de l'intensité en fonction de la distance entre le centre du feu et le centre du sensor
            int intensity = (int)Math.round((sensorRadius - distance) * 100 / sensorRadius);
            if (distance < sensorRadius) {
                sensor.setIntensity(intensity);
                fireSensors.add(sensor);
            }
        }

        Emergency emergency = new Emergency(this.emergencies.size() , new Coord(fireLat, fireLong), fireSensors);
        if (canDeclareEmergency(emergency)) {
            System.out.println("Nouveau feu créé : " + emergency.toJSON() + "\n" + emergency.getSensors());
            for (Sensor s : emergency.getSensors())
                s.setEmergencyId(String.valueOf(emergency.getId()));
            this.emergencies.add(emergency);
            return emergency;
        } else
            return null;
    }

    public boolean canDeclareEmergency(Emergency emergency) {
        boolean res = true;
        // TODO : enlever la condition qui teste si une emergency est détectée par au moins 3 capteurs
        res = emergency.getSensors().size() >=3;
        for(Sensor s : emergency.getSensors()) {
            for(Emergency f : this.emergencies) {
                if(f.isThereSensorInFire(s))
                    res = false;
            }
        }
        return res;
    }

    public Sensor getOneSensor(String idSensor) {
        Sensor s = null;
        for (Sensor sensor : this.sensors) {
            if (sensor.getId().equals(idSensor))
                s = sensor;
        }
        return s;
    }

    @Override
    public String toString() {
        String str = "========Simulator========\n";
        str += "Simulateur d'urgences\n";
        str += "========Sensors========\n";
        for(Sensor sensor : this.sensors)
            str += sensor.toJSON() + ",\n";
        str += "=========Fires==========\n";
        for (Emergency f : this.emergencies) {
            str += f.toJSON() + "\n";
            for (Sensor s : f.getSensors()) {
                str += "\t=> " + s.toJSON() + "\n";
            }
        }
        return str;
    }

    @Override
    public void onUpdateSensors(List<Sensor> sensors) {
        for (Sensor sensor : this.sensors) {
            for (Sensor s : sensors) {
                if (s.equals(sensor)) {
                    this.sensors.set(this.sensors.indexOf(sensor), s);
                }
            }
        }
        //super.onUpdateSensors(sensors);
        // System.out.println(this);
        // System.out.println("MàJ des sensors " + sensors.toString());
    }

    @Override
    public  void onUpdateEmergencies(List<Emergency> emergencies) {
        for (Emergency em : this.emergencies) {
            for (Emergency emergencyToUpdate : emergencies) {
                if (em.equals(emergencyToUpdate)) {
                    this.emergencies.set(this.emergencies.indexOf(em), emergencyToUpdate);
                }
            }
        }
    }
}