package app;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.net.URI;
import java.net.URISyntaxException;

public class Main {

    static Timer timer = new Timer();
    public Simulator sim;

    static class FireGenerator extends TimerTask {
        private final int fireFrequency = 1;
        private Simulator sim;

        public FireGenerator(Simulator sim) {
            this.sim = sim;
        }

        @Override
        public void run() {
            /*while(true) {
                int delay = this.fireFrequency * 60000 + new Random().nextInt(5) * 60000;
                try {
                    wait(delay);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }*/
        }
    }

    public static void main(String args[]) {
        System.out.println("==================================");
        System.out.println("|| Bienvenue dans le simulateur ||");
        System.out.println("==================================");

        Simulator sim = new Simulator();
        sim.initializeSimulation();
        try {
            // open websocket
            final WebsocketClientEndpoint clientEndPoint = new WebsocketClientEndpoint(new URI("ws://localhost:3000"));

            // add listener
            clientEndPoint.addMessageHandler(new WebsocketClientEndpoint.MessageHandler() {
                public void handleMessage(String message) {
                    System.out.println(message);
                }
            });

            // send message to websocket
            clientEndPoint.sendMessage("{'event':'addChannel','channel':'ok_btccny_ticker'}");

            // wait 5 seconds for messages from websocket
            Thread.sleep(5000);

        } catch (InterruptedException ex) {
            System.err.println("InterruptedException exception: " + ex.getMessage());
        } catch (URISyntaxException ex) {
            System.err.println("URISyntaxException exception: " + ex.getMessage());
        }
        //new FireGenerator(sim).run();

    }
}
