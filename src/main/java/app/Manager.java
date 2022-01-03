package app;

import model.Sensor;
import socket.Subscriber;

import java.util.ArrayList;

public class Manager extends Subscriber {

    @Override
    public void onUpdateSensors(ArrayList<Sensor> sensors) {
        //super.onUpdateSensors(sensors);
        System.out.println("Update des sensors");
        System.out.println(sensors);
    }
}
