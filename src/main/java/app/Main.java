package app;

import module.api.Api;
import module.api.Http;
import module.model.Coord;
import module.model.Emergency;
import module.model.Sensor;
import module.socket.Events;
import module.socket.WebSocket;


import static java.lang.Thread.sleep;

import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import managers.FireManager;

public class Main {

    public static void main(String args[]) throws Exception {
        System.out.println("==================================");
        System.out.println("|| Bienvenue dans le simulateur ||");
        System.out.println("==================================");
        PropertiesReader prop = new PropertiesReader();

        Api api = new Api(prop.getProp().getProperty("BASE_URL"), prop.getProp().getProperty("API_KEY"));


        List<Sensor> simulatorSensors = new ArrayList<Sensor>();
        System.out.println("Pending ...");
        do
        {
            simulatorSensors = api.sensor.getAll();
        } while (simulatorSensors.isEmpty());

        System.out.println("Connection successful !");


        System.out.println(simulatorSensors);
        Simulator sim = new Simulator(simulatorSensors, api);

        /***
         * Websocket
         */
        // Connection to the simulation
        WebSocket ws = new WebSocket(prop.getProp().getProperty("BASE_URL"), prop.getProp().getProperty("WEBSOCKET_KEY"));
        ws.subscribe(sim, Events.SENSORS.getEvent());
        ws.subscribe(sim, Events.EMERGENCIES.getEvent());

        // Connection to the emergency
        WebSocket wsEmeregncy = new WebSocket(prop.getProp().getProperty("BASE_URL"), prop.getProp().getProperty("WEBSOCKET_KEY"));
        wsEmeregncy.subscribe(sim, Events.TEAMS.getEvent());

        System.out.println("Simulator started !");

        // Boucle infinie de simulation
        while(true) {
            int delayBetweenTwoEmergencies = 30000 + (int)(Math.random() * ((45000 - 30000) + 1));
            Emergency emergency = sim.initEmergency();
            if (emergency != null) {
                api.emergency.createOrUpdate(Arrays.asList(emergency));
                for (Sensor sensor : emergency.getSensors()) {
                    api.sensor.createOrUpdate(Arrays.asList(sensor));
                }
                break;
            }
            // System.out.println("Attente de " + delayBetweenTwoEmergencies / 1000 + " secondes");
            // sleep(delayBetweenTwoEmergencies);
        }

        /*sleep(500);
        response = example.post("http://localhost:3000/api/sensors/", sensorToPost.toJSON());
        System.out.println(response);*/
    }
}
