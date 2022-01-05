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
    private final Api api = Api.getInstance();

    public FireManager() {

    }

    @Override
    public void onUpdateSensors(List<Sensor> sensors) {
        this.sensors = sensors;
        this.detectNewFire();
    }
    
    private void detectNewFire() {
        List<Sensor> trigerredSensors = this.filterSensors();
        
        if(trigerredSensors.size() > 2 && trigerredSensors.size() < 5) {
            NonLinearLeastSquaresSolver solver = new NonLinearLeastSquaresSolver(new TrilaterationFunction(getSensorsPosition(trigerredSensors), getSensorsDistance(trigerredSensors)), new LevenbergMarquardtOptimizer());
            Optimum optimum = solver.solve();
            
            double[] centroid = optimum.getPoint().toArray();
            
            Coord fireCoordinates = new Coord(centroid[0], centroid[1]);
            
            Emergency fire = new Emergency("1", fireCoordinates, trigerredSensors);
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
            positions[i] = sensor.getRadius() * sensor.getIntensity() / 100;
        }

        return positions;
    }
}