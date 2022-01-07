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
        Api api = new Api(prop.getProp().getProperty("BASE_URL"), prop.getProp().getProperty("API_KEY"));
        WebSocket ws = new WebSocket(prop.getProp().getProperty("BASE_URL"), prop.getProp().getProperty("WEBSOCKET_KEY"));

        List<Sensor> simulatorSensors = new ArrayList<Sensor>();
        System.out.println("Pending ...");
        do
        {
            simulatorSensors = api.sensor.getAll();
        } while (simulatorSensors.isEmpty());

        System.out.println("Connection successful !");


        FireManager test = new FireManager(api);

        /***
         * Websocket
         */
        ws.subscribe(test, Events.SENSORS.getEvent());
    }
}
