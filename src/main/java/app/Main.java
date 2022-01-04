package app;

import model.Emergency;
import model.Sensor;
import socket.Events;
import socket.WebSocket;

import static java.lang.Thread.sleep;

public class Main {

    public static void main(String args[]) throws Exception {
        System.out.println("==================================");
        System.out.println("|| Bienvenue dans le simulateur ||");
        System.out.println("==================================");

        API api = new API();
        Simulator sim = new Simulator();
        sim.initializeSimulation();
        System.out.println(sim.getSensors());

        /***
         * Websocket
         */
        WebSocket ws = new WebSocket("ws://localhost:3000", "4739f58f-5e35-4235-8ac5-4fdba549d641");
        ws.subscribe(sim , Events.SENSORS.getEvent());

        // Boucle infinie de simulation
        while(true) {
            int delayBetweenTwoEmergencies = 30000 + (int)(Math.random() * ((45000 - 30000) + 1));
            Emergency emergency = sim.initEmergency();
            if (emergency != null) {
                api.post("http://localhost:3000/api/emergencies/", emergency.toJSON());
                for (Sensor s : emergency.getSensors()) {
                    api.post("http://localhost:3000/api/sensors/", s.toJSON());
                }
            }
            System.out.println("Attente de " + delayBetweenTwoEmergencies / 1000 + " secondes");
            sleep(delayBetweenTwoEmergencies);
        }

        /*sleep(500);
        response = example.post("http://localhost:3000/api/sensors/", sensorToPost.toJSON());
        System.out.println(response);*/
    }
}
