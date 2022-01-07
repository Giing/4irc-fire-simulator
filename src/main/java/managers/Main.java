package managers;

import app.PropertiesReader;
import module.api.Api;
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


        FireManager test = new FireManager(api);

        /***
         * Websocket
         */
        ws.subscribe(test, Events.SENSORS.getEvent());
    }
}
