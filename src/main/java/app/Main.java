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

import static java.lang.Thread.sleep;

public class Main {

    public static void main(String args[]) throws Exception {
        System.out.println("==================================");
        System.out.println("|| Bienvenue dans le simulateur ||");
        System.out.println("==================================");

        Sensor sensorToPost = new Sensor("0", new Coord(45.0, 4.0), 50);
        Fire fireToPost = new Fire(0, new Coord(45.0, 4.0), 4);

        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(Sensor.class, new SensorDeserializer());
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
            /*IO.Options options = IO.Options.builder()
                    .setQuery("token=4739f58f-5e35-4235-8ac5-4fdba549d641")
                    .build();

            Socket socket = IO.socket("ws://localhost:3000", options);

            socket.on("onUpdateSensors", new Listener() {
                @Override
                public void call(Object... args) {
                    System.out.println("TEST");

                    JSONArray content = (JSONArray)args[0];
                    try {
                        Sensor sensor = gson.fromJson(content.get(0).toString(), Sensor.class);
                        System.out.println("Raw content " + content.get(0).toString());
                        System.out.println("Updated sensor : " + sensor.toJSON());
                    } catch (JSONException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            });

            socket.connect();
            */
        } catch (Exception e) {
            //TODO: handle exception
            throw new Exception("Failed to connect to websocket");
        }
        sleep(500);
        response = example.post("/sensors/", sensorToPost.toJSON());
        System.out.println(response);
    }
}
