package app;

import model.Fire;
import org.junit.Assert;
import org.junit.Test;

public class SimulatorTest {

    private Simulator sim = new Simulator();

    @Test
    public void TestFireInRangeOfSensors() {
        // Initialisation de la simulation
        sim.initializeSimulation();

        boolean allFireHaveSensors = true;
        int iteration = 1000;
        Fire fireToTest;

        for (int i = 0; i < iteration; i++) {
            fireToTest = sim.initFire();
            allFireHaveSensors = fireToTest.getSensors().size() > 0 && allFireHaveSensors;
            if(fireToTest.getSensors().size() <= 0)
                System.out.println(fireToTest);
        }
        Assert.assertTrue(allFireHaveSensors);
    }

}
