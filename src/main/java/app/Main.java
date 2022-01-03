package app;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import model.Coord;
import model.Fire;
import model.Sensor;
import model.SensorDeserializer;
import org.json.JSONArray;
import org.json.JSONException;
import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter.Listener;
import socket.Events;
import socket.WebSocket;

import java.util.Random;

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
        Manager manager = new Manager();
        WebSocket ws = new WebSocket("ws://localhost:3000", "4739f58f-5e35-4235-8ac5-4fdba549d641");
        ws.subscribe(manager , Events.SENSORS.getEvent());

        // Boucle infinie de simulation
        while(true) {
            int delayBetweenTwoEmergencies = 30000 + (int)(Math.random() * ((45000 - 30000) + 1));
            Fire fire = sim.initFire();
            if (fire != null) {
                api.post("http://localhost:3000/api/emergencies/", fire.toJSON());
            }
            System.out.println("Attente de " + delayBetweenTwoEmergencies / 1000 + " secondes");
            sleep(delayBetweenTwoEmergencies);
        }

        /*sleep(500);
        response = example.post("http://localhost:3000/api/sensors/", sensorToPost.toJSON());
        System.out.println(response);*/
    }
}
