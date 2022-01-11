package app;

import java.lang.reflect.Array;
import java.util.*;
import java.util.stream.Collectors;

import module.api.Api;
import module.json.JsonMapper;
import module.model.Coord;
import module.model.Emergency;
import module.model.Sensor;
import module.model.Team;
import module.socket.Subscriber;

public class Simulator extends Subscriber {

    private final double longitude_max    = 4.896831000000001;
    private final double latitude_min     = 45.723967;
    private final double longitude_min    = 4.797831;
    private final double latitude_max     = 45.773967;
    private final double longitude_step   = 0.11 / 10.0;
    private final double latitude_step    = 0.06 / 6.0;
    private List<Sensor> sensors = new ArrayList<>();

    public Map<String, Emergency> emergencies = new HashMap<String, Emergency>();

    public Collection<Emergency> getEmergencies() {
        return emergencies.values();
    }

    public void addEmergency(Emergency em) {
        this.emergencies.put(em.getId(), em);
    }

    protected JsonMapper mapper = JsonMapper.getInstance();
    private Api api;
    private Api apiEmergency;

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

    public int getNumberOfEmergency() {
        return this.emergencies.size();
    }

    public Simulator(List<Sensor> sensors, Api api, Api apiEmergency) {
        this.sensors = sensors;
        this.api = api;
        this.apiEmergency = apiEmergency;
    }

    public void init() {
        this.ensureInitOneEmergency();
        this.ensureInitOneEmergency();
    }

    public void ensureInitOneEmergency() {
        while(this.initEmergency() == null);
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
            int intensity = (int)Math.ceil((sensorRadius - distance) * 100 / sensorRadius);
            if (distance < sensorRadius) {
                if(sensor.getEmergencyId() != null) return null;
                sensor.setIntensity(intensity);
                fireSensors.add(sensor);
            }
        }

        Emergency emergency = new Emergency(java.util.UUID.randomUUID().toString(), new Coord(fireLat, fireLong), fireSensors, 100);
        if (canDeclareEmergency(emergency)) {
            System.out.println("Nouveau feu créé : " + emergency.toJSON() + "\n" + emergency.getSensors());

            // TODO Remove inproduction
            this.apiEmergency.sensor.createOrUpdate(emergency.getSensors());
                        
            this.api.emergency.createOrUpdate(Arrays.asList(emergency));
            for (Sensor sensor : emergency.getSensors()) {
                sensor.setEmergencyId(emergency.getId());
            }
            this.api.sensor.createOrUpdate(emergency.getSensors());

            this.addEmergency(emergency);
            return emergency;
        } else
            return null;
    }

    public boolean canDeclareEmergency(Emergency emergency) {
        boolean res = true;
        // TODO : enlever la condition qui teste si une emergency est détectée par au moins 3 capteurs
        res = emergency.getSensors().size() == 3;
        for(Sensor s : emergency.getSensors()) {
            for(Emergency f : this.getEmergencies()) {
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
        for (Emergency f : this.getEmergencies()) {
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
        System.out.println("MàJ des sensors " + sensors.toString());
    }

    @Override
    public  void onUpdateEmergencies(List<Emergency> newEmergencies) {
        for (Emergency emergency : newEmergencies) {
            Emergency oldEmergency = emergencies.get(emergency.getId());
            if(oldEmergency != null) {
                emergency.isHandled = oldEmergency.isHandled;
            }
            emergency.setSensors(this.api.sensor.getAllByEmergency(emergency.getId()));
            emergencies.put(emergency.getId(), emergency);
            if(emergency.getIntensity() == 0) {
                this.emergencies.remove(emergency.getId());
                for (Sensor sensor : emergency.getSensors()) {
                    Sensor reset = this.getOneSensor(sensor.getId());
                    reset.setEmergencyId(null);
                }
            }
        }
    }

    @Override
    public void onUpdateTeams(List<Team> teams) {
        for (Team team : teams) {


            Emergency emergencyHandledByTeam = this.getEmergencies().stream()
                .filter(emergency -> team.isHandlingFromCoord(emergency.getLocation()))
                .findFirst()
                .orElse(null);

            Api api = this.api;
            Api apiEmergency = this.apiEmergency;
            Map<String, Emergency> emergencies = this.emergencies;
            
            if(emergencyHandledByTeam != null && !emergencyHandledByTeam.isHandled) {
                System.out.println("Launch task for: " + emergencyHandledByTeam);
                emergencyHandledByTeam.isHandled = true;
                new Timer().scheduleAtFixedRate(new TimerTask(){
                    @Override
                    public void run(){
                        emergencyHandledByTeam.setSensors(api.sensor.getAllByEmergency(emergencyHandledByTeam.getId()));
                        emergencyHandledByTeam.setIntensity(emergencyHandledByTeam.getIntensity() - team.getLevel());
                        emergencyHandledByTeam.updateIntensity();
                        
                        api.emergency.createOrUpdate(Arrays.asList(emergencyHandledByTeam));
                        api.sensor.createOrUpdate(emergencyHandledByTeam.getSensors());
                        
                        // TODO Remove in production
                        for (Sensor sensor : emergencyHandledByTeam.getSensors()) {
                            sensor.setEmergencyId(null);
                            apiEmergency.sensor.createOrUpdate(Arrays.asList(sensor));
                            sensor.setEmergencyId(emergencyHandledByTeam.getId());
                        }
                        
                        System.out.println(emergencyHandledByTeam);
                        System.out.println(emergencyHandledByTeam.getSensors());
                        

                        if(emergencyHandledByTeam.getIntensity() == 0) {
                            for (Sensor sensor :  emergencyHandledByTeam.getSensors()) {
                                sensor.setEmergencyId(null);
                            }
                            System.out.println("Delete: " + emergencyHandledByTeam.getId());
                            emergencies.remove(emergencyHandledByTeam.getId());
                            api.emergency.delete(emergencyHandledByTeam);
                            this.cancel();
                            return;
                        }
                    }
                }, 0, 1000);
            }
        }
    }
}