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

        Api api = new Api(prop.getProp().getProperty("SIMULATOR_BASE_URL"), prop.getProp().getProperty("SIMULATOR_API_KEY"));
        Api apiEmergency = new Api(prop.getProp().getProperty("EMERGENCY_BASE_URL"), prop.getProp().getProperty("EMERGENCY_API_KEY"));


        List<Sensor> simulatorSensors = new ArrayList<Sensor>();
        List<Sensor> emergencySensors = new ArrayList<Sensor>();
        System.out.println("Pending ...");
        do
        {
            simulatorSensors = api.sensor.getAll();
            emergencySensors = apiEmergency.sensor.getAll();
        } while (simulatorSensors.isEmpty() && emergencySensors.isEmpty());

        System.out.println("Connection successful !");


        System.out.println(simulatorSensors);
        Simulator sim = new Simulator(simulatorSensors, api, apiEmergency);

        /***
         * Websocket
         */
        // Connection to the simulation
        WebSocket ws = new WebSocket(prop.getProp().getProperty("SIMULATOR_BASE_URL"), prop.getProp().getProperty("SIMULATOR_WEBSOCKET_KEY"));
        ws.subscribe(sim, Events.SENSORS.getEvent());
        ws.subscribe(sim, Events.EMERGENCIES.getEvent());

        // Connection to the emergency
        WebSocket wsEmeregncy = new WebSocket(prop.getProp().getProperty("EMERGENCY_BASE_URL"), prop.getProp().getProperty("EMERGENCY_WEBSOCKET_KEY"));
        wsEmeregncy.subscribe(sim, Events.TEAMS.getEvent());

        System.out.println("Simulator started !");

        Integer i = 0;

        // Boucle infinie de simulation
        while(true) {
            int delayBetweenTwoEmergencies = 30000 + (int)(Math.random() * ((45000 - 30000) + 1));
            Emergency emergency = sim.initEmergency();
            if (emergency != null) {
                i++;
                // TODO Remove inproduction
                apiEmergency.sensor.createOrUpdate(emergency.getSensors());

                api.emergency.createOrUpdate(Arrays.asList(emergency));
                for (Sensor sensor : emergency.getSensors()) {
                    sensor.setEmergencyId(emergency.getId());
                }
                api.sensor.createOrUpdate(emergency.getSensors());


                if(i >= 2) {
                    break;
                }
            }
            // System.out.println("Attente de " + delayBetweenTwoEmergencies / 1000 + " secondes");
            // sleep(delayBetweenTwoEmergencies);
        }

        /*sleep(500);
        response = example.post("http://localhost:3000/api/sensors/", sensorToPost.toJSON());
        System.out.println(response);*/
    }
}
