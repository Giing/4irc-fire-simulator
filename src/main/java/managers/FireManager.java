package managers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.lemmingapex.trilateration.NonLinearLeastSquaresSolver;
import com.lemmingapex.trilateration.TrilaterationFunction;

import org.apache.commons.math3.fitting.leastsquares.LevenbergMarquardtOptimizer;
import org.apache.commons.math3.fitting.leastsquares.LeastSquaresOptimizer.Optimum;

import module.api.Api;
import module.model.Coord;
import module.model.Emergency;
import module.model.Sensor;
import module.socket.Subscriber;

public class FireManager extends Subscriber {
    private List<Sensor> sensors = new ArrayList<Sensor>();
    private List<Emergency> potentialNewFires = new ArrayList<Emergency>();
    private Map<String, Emergency> declaredFire = new HashMap<String, Emergency>();
    private Api api;

    public FireManager(Api api) {
        this.api = api;

        List<Emergency> fires = this.api.emergency.getAll();
        for (Emergency emergency : fires) {
            declaredFire.put(emergency.getId(), emergency);
        }
    }

    @Override
    public void onUpdateSensors(List<Sensor> sensors) {
        this.sensors = sensors;
        for (Sensor sensor : sensors) {
            this.detectPotentialFire(sensor);
        }
    }
    
    @Override
    public void onUpdateEmergencies(List<Emergency> fires) {
        for (Emergency emergency : fires) {
            if(emergency.getIntensity() == 0) {
                this.declaredFire.remove(emergency.getId());
                this.api.emergency.delete(emergency);
            } else {
                declaredFire.put(emergency.getId(), emergency);
            }
        }
    }
    
    private void detectPotentialFire(Sensor sensor) {

        if(sensor.getEmergencyId() == null && sensor.isTriggered()) {
            // Potential new Fire
            List<Emergency> relationWithSensor =  this.potentialNewFires.stream().filter(fire -> sensor.isInRadius(fire.getLocation())).collect(Collectors.toList());

            if(relationWithSensor.size() == 1) {
                Emergency fire = relationWithSensor.get(0);
                fire.addSensor(sensor);
                this.computeFirePosition(fire);
            } else if(relationWithSensor.size() == 0) {
                List<Sensor> newSensors = new ArrayList<Sensor>();
                newSensors.add(sensor);
                this.potentialNewFires.add(new Emergency(java.util.UUID.randomUUID().toString(), sensor.getLocation(), newSensors));
            }
        }

        if(sensor.getEmergencyId() != null) {
            try {
                // Update fire with new intensity
                List<Sensor> sensors = this.api.sensor.getAllByEmergency(sensor.getEmergencyId());
                Emergency fire = this.api.emergency.getById(sensor.getEmergencyId());

                Integer initialIntesity = fire.getIntensity();
                fire.computeIntensityFromSensors(sensors);

                if(initialIntesity != fire.getIntensity()) {
                    this.api.emergency.createOrUpdate(Arrays.asList(fire));
            }
            } catch(Error err) {
                System.out.println(sensor.getEmergencyId());
            }
        }

    }
    
    private void computeFirePosition(Emergency fire) {
        List<Sensor> trigerredSensors = fire.getSensors();
        
        if(trigerredSensors.size() > 2 && trigerredSensors.size() < 5) {
            double[][] positions = getSensorsPosition(trigerredSensors);
            double[] distances = getSensorsDistance(trigerredSensors);

            NonLinearLeastSquaresSolver solver = new NonLinearLeastSquaresSolver(new TrilaterationFunction(positions, distances), new LevenbergMarquardtOptimizer());
            Optimum optimum = solver.solve();
            
            double[] centroid = optimum.getPoint().toArray();
            
            Coord fireCoordinates = new Coord(centroid[0], centroid[1]);

            // apply new location to the fire
            fire.setLocation(fireCoordinates);
            fire.computeIntensityFromSensors(trigerredSensors);

            // push and remove the fire
            System.out.println("Add new fire !!!");
            api.emergency.createOrUpdate(Arrays.asList(fire));

            for (Sensor sensor : trigerredSensors) {
                sensor.setEmergencyId(fire.getId());
            }
            this.api.sensor.createOrUpdate(trigerredSensors);

            this.potentialNewFires.remove(fire);
        }
    }

    private double[][] getSensorsPosition(List<Sensor> newSensors) {
        double[][] positions = new double[newSensors.size()][2];

        for(int i = 0; i<newSensors.size(); i++) {
            Sensor sensor = newSensors.get(i);
            positions[i][0] = sensor.getLocation().getLatitude();
            positions[i][1] = sensor.getLocation().getLongitude();
        }

        return positions;
    }

    private double[] getSensorsDistance(List<Sensor> newSensors) {
        double[] positions = new double[newSensors.size()];

        for(int i = 0; i<newSensors.size(); i++) {
            Sensor sensor = newSensors.get(i);
            positions[i] = sensor.getRadius() - (sensor.getRadius() * sensor.getIntensity() / 100);
        }

        return positions;
    }
}