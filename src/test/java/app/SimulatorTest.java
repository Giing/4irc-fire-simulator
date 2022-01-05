package app;

import module.api.Api;
import module.model.Coord;
import module.model.Sensor;
import org.junit.Assert;
import org.junit.Test;

import module.model.Emergency;

import java.util.ArrayList;
import java.util.List;

public class SimulatorTest {

    private Api api = Api.getInstance();
    private Simulator sim = new Simulator();

    @Test
    public void TestFireInRangeOfSensors() {
        // Initialisation de la simulation
        sim.initializeSimulation();

        boolean allFireHaveSensors = true;
        int iteration = 1000;
        Emergency emergencyToTest;

        for (int i = 0; i < iteration; i++) {
            emergencyToTest = sim.initEmergency();
            if (emergencyToTest != null) {
                allFireHaveSensors = emergencyToTest.getSensors().size() > 0 && allFireHaveSensors;
                if(emergencyToTest.getSensors().size() <= 0)
                    System.out.println(emergencyToTest);
            }
        }
        Assert.assertTrue(allFireHaveSensors);
    }

    @Test
    public void TestFireInRangeOfGrid() {
        boolean allFiresAreInRange = true;
        int iteration = 1000;
        Emergency emergencyToTest;

        for (int i = 0; i < iteration; i++) {
            emergencyToTest = sim.initEmergency();
            if (emergencyToTest != null) {
                if (emergencyToTest.getLocation().getLatitude() < this.sim.getLatitude_min() ||
                    emergencyToTest.getLocation().getLatitude() > this.sim.getLatitude_max() ||
                    emergencyToTest.getLocation().getLongitude() < this.sim.getLongitude_min() ||
                    emergencyToTest.getLocation().getLongitude() > this.sim.getLongitude_max()) {
                    allFiresAreInRange = false;
                }
            }
        }
        Assert.assertTrue(allFiresAreInRange);
    }

    @Test
    public void TestTwoEmergenciesCannotGroupTheSameSensors() {

        // Initialisation du simulateur
        List<Sensor> sensors = this.api.sensor.getAll();
        Simulator simu = new Simulator(sensors);

        // Initialisation de la première emergency
        List<Sensor> sensorsEmergencyOne = new ArrayList<Sensor>();
        sensorsEmergencyOne.add(simu.getSensors().get(0));
        sensorsEmergencyOne.add(simu.getSensors().get(1));
        sensorsEmergencyOne.add(simu.getSensors().get(2));
        Emergency emergencyOne = new Emergency("1", new Coord(100.0, 100.0), sensorsEmergencyOne, 0);
        simu.addEmergency(emergencyOne);

        // Initialisation de la deuxième emergency
        List<Sensor> sensorsEmergencyTwo = new ArrayList<Sensor>();
        sensorsEmergencyOne.add(simu.getSensors().get(0));
        sensorsEmergencyOne.add(simu.getSensors().get(1));
        sensorsEmergencyOne.add(simu.getSensors().get(2));
        Emergency emergencyTwo = new Emergency("1", new Coord(100.0, 100.0), sensorsEmergencyOne, 0);

        // Should be false
        boolean canDeclareEmergency = simu.canDeclareEmergency(emergencyTwo);
        Assert.assertFalse(canDeclareEmergency);

    }

}
