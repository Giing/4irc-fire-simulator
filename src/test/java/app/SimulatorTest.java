package app;

import org.junit.Assert;
import org.junit.Test;

import module.model.Emergency;

public class SimulatorTest {

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

}
