package managers;

import java.util.ArrayList;
import java.util.List;

import app.PropertiesReader;
import module.api.Api;
import module.model.Sensor;
import module.socket.Events;
import module.socket.WebSocket;

public class Main {
    public static void main(String args[]) throws Exception {
        System.out.println("==================================");
        System.out.println("|| Bienvenue dans la gestion des managers ||");
        System.out.println("==================================");
        
        PropertiesReader prop = new PropertiesReader();
        Api api = new Api(prop.getProp().getProperty("EMERGENCY_BASE_URL"), prop.getProp().getProperty("EMERGENCY_API_KEY"));
        
        List<Sensor> simulatorSensors = new ArrayList<Sensor>();
        System.out.println("Pending ...");
        do
        {
            simulatorSensors = api.sensor.getAll();
        } while (simulatorSensors.isEmpty());
        
        System.out.println("Connection successful !");
        WebSocket ws = new WebSocket(prop.getProp().getProperty("EMERGENCY_BASE_URL"), prop.getProp().getProperty("EMERGENCY_WEBSOCKET_KEY"));


        FireManager fire = new FireManager(api);
        StationManager station = new StationManager(api);

        /***
         * Websocket
         */
        ws.subscribe(fire, Events.SENSORS.getEvent());
        ws.subscribe(fire, Events.EMERGENCIES.getEvent());
        ws.subscribe(station, Events.EMERGENCIES.getEvent());
        ws.subscribe(station, Events.TEAMS.getEvent());
    }
}
