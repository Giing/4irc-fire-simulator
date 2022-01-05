package managers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
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
    private List<Emergency> potentialNewFire = new ArrayList<Emergency>();
    private final Api api = Api.getInstance();

    public FireManager() {

    }

    @Override
    public void onUpdateSensors(List<Sensor> sensors) {
        this.sensors = sensors;
        for (Sensor sensor : sensors) {
            this.detectPotentialFire(sensor);
        }
    }

    private void detectPotentialFire(Sensor sensor) {
        // TODO replace "0" with null for production
        if(Integer.parseInt(sensor.getEmergencyId()) == 0 && sensor.getIntensity() > 1) {
            List<Emergency> relationWithSensor =  this.potentialNewFire.stream().filter(fire -> sensor.isInRadius(fire.getLocation())).collect(Collectors.toList());
            
            if(relationWithSensor.size() == 1) {
                Emergency fire = relationWithSensor.get(0);
                fire.addSensor(sensor);
                this.computeFirePosition(fire);
            } else if(relationWithSensor.size() == 0) {
                List<Sensor> newSensors = new ArrayList<Sensor>();
                newSensors.add(sensor);
                this.potentialNewFire.add(new Emergency("2", sensor.getLocation(), newSensors));
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
            
            fire.setLocation(fireCoordinates);
            api.emergency.createOrUpdate(Arrays.asList(fire));
        }
    }

    private List<Sensor> filterSensors() {
        return sensors.stream().filter(p -> p.getIntensity() > 1).collect(Collectors.toList());
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