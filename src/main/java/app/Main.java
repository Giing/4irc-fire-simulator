package app;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import module.API;
import module.model.Coord;
import module.model.Fire;
import module.model.Sensor;
import module.model.SensorAdapter;
import module.socket.Events;
import module.socket.WebSocket;

import org.json.JSONArray;
import org.json.JSONException;
import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter.Listener;

import static java.lang.Thread.sleep;

public class Main {

    public static void main(String args[]) throws Exception {
        System.out.println("==================================");
        System.out.println("|| Bienvenue dans le simulateur ||");
        System.out.println("==================================");

        Sensor sensorToPost = new Sensor("0", new Coord(45.0, 4.0), 50);
        Fire fireToPost = new Fire(0, new Coord(45.0, 4.0), 4);

        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(Sensor.class, new SensorAdapter());
        final Gson gson = gsonBuilder.create();
        API example = new API();
        String response = example.get("http://localhost:3000");

        try {
            Simulator sim = new Simulator();
            sim.initializeSimulation();

            String json = gson.toJson(sim.getSensors());
            //System.out.println(json);

            /***
             * Requête API
             */




            /***
             * Websocket
             */
            Manager manager = new Manager();
            WebSocket ws = new WebSocket("ws://localhost:3000", "4739f58f-5e35-4235-8ac5-4fdba549d641");
            ws.subscribe(manager , Events.SENSORS.getEvent());

        } catch (Exception e) {
            //TODO: handle exception
            throw new Exception("Failed to connect to websocket");
        }
        sleep(500);
        response = example.post("/sensors/", sensorToPost.toJSON());
        System.out.println(response);
    }
}
