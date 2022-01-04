package module.socket;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter.Listener;
import module.json.Json;
import module.model.Fire;
import module.model.Sensor;
import module.model.SensorAdapter;
import module.model.Station;

import java.lang.reflect.Type;
import java.net.URISyntaxException;
import java.util.List;

import org.json.JSONArray;

public class WebSocket {
    private String[] EVENTS = {"onUpdateSensors", "onUpdateEmergencies", "onUpdateTeams", "onUpdateStations"};

    private String URL;
    private String WS_KEY;

    public Socket socket;

    public WebSocket(String url, String key) {
        URL = url;
        WS_KEY = key;

        init();
    }

    private void init() {
        IO.Options options = IO.Options.builder()
                .setQuery("token=" + WS_KEY)
                .build();

        try {
            socket = IO.socket(URL, options);
            socket.connect();
        } catch (URISyntaxException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void subscribe(Subscriber subscriber, String eventToSubscribe) {

        if(eventToSubscribe == null) {
            subscribeToEvent(subscriber, eventToSubscribe);
        } else {
            for (String event : EVENTS) {
                subscribeToEvent(subscriber, event);
            }
        }
    }

    private void subscribeToEvent(Subscriber subscriber, String event) {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(Sensor.class, new SensorAdapter());
        final Gson gson = gsonBuilder.create();

        Json<Sensor> sensorJson = new Json<>(Sensor.class);

        socket.on(event, new Listener() {
            @Override
            public void call(Object... args) {

                JSONArray content = (JSONArray)args[0];


                switch(event) {
                    case "onUpdateSensors":
                        subscriber.onUpdateSensors(sensorJson.fromJson(content));
                        break;
                    case "onUpdateEmergencies":
                        Type typeEmergencies = new TypeToken<List<Emergency>>(){}.getType();
                        subscriber.onUpdateEmergencies(gson.fromJson(content.toString(), typeEmergencies));
                        break;
                    case "onUpdateStations":
                        Type typeStations = new TypeToken<List<Station>>(){}.getType();
                        subscriber.onUpdateStations(gson.fromJson(content.toString(), typeStations));
                        break;
                }
            }
        });
    }
}