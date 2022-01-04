package app;

import module.json.JsonMapper;
import module.model.Sensor;
import module.socket.Subscriber;

import java.util.ArrayList;

public class Manager extends Subscriber {

    @Override
    public void onUpdateSensors(ArrayList<Sensor> sensors) {
        JsonMapper mapper = JsonMapper.getInstance();
        System.out.println(mapper.sensor.toJson(sensors));
    }
}