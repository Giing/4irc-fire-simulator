package module;

import java.util.ArrayList;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import org.json.JSONArray;
import org.junit.Assert;
import org.junit.Test;

import module.json.JsonMapper;
import module.model.Coord;
import module.model.Sensor;

public class SerializerTest {

    private JsonMapper mapper = JsonMapper.getInstance();

    @Test
    public void TestSensorSerialization() {
        ArrayList<Sensor> sensors = new ArrayList<Sensor>();
        sensors.add(new Sensor("0", new Coord(45.0, 4.0), 100));
        sensors.add(new Sensor("1", new Coord(47.0, -4.0), 10));
    }

}
