package app;

import org.junit.Assert;
import org.junit.Test;

import module.model.Coord;
import module.model.Fire;
import module.model.Sensor;

import java.util.*;

public class FireTest {

    @Test
    public void TestIsThereSensorInFire() {
        // Sensor in fire
        Sensor sensor = new Sensor("0", new Coord(45.0, 4.0), 100);
        // Sensor not in fire
        Sensor s = new Sensor("1", new Coord(46.0, 4.0), 0);
        List<Sensor> sensors = new ArrayList<>();
        sensors.add(sensor);
        Fire fire = new Fire(0, new Coord(4.0, 45.0), sensors);
        Assert.assertTrue(fire.isThereSensorInFire(sensor));
        Assert.assertFalse(fire.isThereSensorInFire(s));
    }
}

