package app;

import org.junit.Assert;
import org.junit.Test;

import module.model.Coord;
import module.model.Emergency;
import module.model.Sensor;

import java.util.*;

public class EmergencyTest {

    @Test
    public void TestIsThereSensorInFire() {
        // Sensor in fire
        Sensor sensor = new Sensor("0", new Coord(45.0, 4.0), 100);
        // Sensor not in fire
        Sensor s = new Sensor("1", new Coord(46.0, 4.0), 0);
        List<Sensor> sensors = new ArrayList<>();
        sensors.add(sensor);
        Emergency emergency = new Emergency(0, new Coord(4.0, 45.0), sensors);
        Assert.assertTrue(emergency.isThereSensorInFire(sensor));
        Assert.assertFalse(emergency.isThereSensorInFire(s));
    }
}

