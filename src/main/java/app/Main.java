package app;

import module.api.Api;
import module.api.Http;
import module.model.Coord;
import module.model.Emergency;
import module.model.Sensor;
import module.socket.Events;
import module.socket.WebSocket;


import static java.lang.Thread.sleep;

import java.util.Arrays;
import java.util.List;

import managers.FireManager;

public class Main {

    public static void main(String args[]) throws Exception {
        System.out.println("==================================");
        System.out.println("|| Bienvenue dans le simulateur ||");
        System.out.println("==================================");

        Api api = Api.getInstance();
        List<Sensor> simulatorSensors = api.sensor.getAll();
        System.out.println(simulatorSensors);
        Simulator sim = new Simulator(simulatorSensors);

        /***
         * Websocket
         */
        WebSocket ws = new WebSocket("ws://api-simulation:3000", "4739f58f-5e35-4235-8ac5-4fdba549d641");
        ws.subscribe(sim, Events.SENSORS.getEvent());
        ws.subscribe(sim, Events.EMERGENCIES.getEvent());

        FireManager test = new FireManager();
        ws.subscribe(test, Events.SENSORS.getEvent());

        // Boucle infinie de simulation
        while(true) {
            int delayBetweenTwoEmergencies = 30000 + (int)(Math.random() * ((45000 - 30000) + 1));
            Emergency emergency = sim.initEmergency();
            if (emergency != null) {
                // api.emergency.createOrUpdate(Arrays.asList(emergency));
                for (Sensor sensor : emergency.getSensors()) {
                    api.sensor.createOrUpdate(Arrays.asList(sensor));
                }
                break;
            }
            System.out.println("Attente de " + delayBetweenTwoEmergencies / 1000 + " secondes");
            // sleep(delayBetweenTwoEmergencies);
        }

        /*sleep(500);
        response = example.post("http://localhost:3000/api/sensors/", sensorToPost.toJSON());
        System.out.println(response);*/
    }
}
