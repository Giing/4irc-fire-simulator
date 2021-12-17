package app;

import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.net.URI;
import java.net.URISyntaxException;

import com.google.gson.Gson;
import org.json.JSONArray;
import org.json.JSONException;
import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter.Listener;

public class Main {

    public static void main(String args[]) throws Exception {
        System.out.println("==================================");
        System.out.println("|| Bienvenue dans le simulateur ||");
        System.out.println("==================================");

        Sensor sensorToPost = new Sensor("0", new Coord(45.0, 4.0), 50);

        final Gson gson = new Gson();
        API example = new API();
        String response = example.get("http://localhost:3000");
        System.out.println(gson.toJson(sensorToPost));
        System.out.println(sensorToPost.toJSON());
        response = example.post("http://localhost:3000/api/sensors/", sensorToPost.toJSON());
        System.out.println(response);

        try {
            Simulator sim = new Simulator();
            sim.initializeSimulation();

            String json = gson.toJson(sim.getSensors());
            System.out.println(json);

            /***
             * Requête API
             */




            /***
             * Websocket
             */
            IO.Options options = IO.Options.builder()
                    .setQuery("token=4739f58f-5e35-4235-8ac5-4fdba549d641")
                    .build();

            Socket socket = IO.socket("ws://localhost:3000", options);

            socket.on("onUpdateSensors", new Listener() {
                @Override
                public void call(Object... args) {

                    JSONArray content = (JSONArray)args[0];
                    try {
                        Sensor sensor = gson.fromJson(content.get(0).toString(), Sensor.class);
                        System.out.println(sensor);
                    } catch (JSONException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            });

            socket.connect();
        } catch (Exception e) {
            //TODO: handle exception
            throw new Exception("Failed to connect to websocket");
        }
    }
}
