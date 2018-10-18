package controllers;

/*import io.DataStore;
import io.PlayerData;
import io.socket.client.IO;
import io.socket.client.Socket;
import misc.Statistics;
import org.json.JSONException;
import org.json.JSONML;
import io.socket.emitter.Emitter;

import java.net.URISyntaxException;*/

public class WebsocketController {

    //private static Socket socket;

    public static void connect(String url) {
        /*try {
            socket = IO.socket("http://localhost");
            socket.connect();
            System.out.println("Connected to socket at "+url+":80.");
            registerEvents();
            sendPlayerData();
            sendStatistics();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }*/
    }

    private static void registerEvents() {
        /*System.out.println("Registering socket events...");
        socket.on("request player list", new Emitter.Listener() {
            public void call(Object... args) {
                sendPlayerData();
            }
        });
        socket.on("request stats", new Emitter.Listener() {
            public void call(Object... args) {
                sendStatistics();
            }
        });*/
    }

    public static void disconnect() {
        /*System.out.println("Disconnecting from socket!");
        socket.disconnect();*/
    }

    public static void sendPlayerData() {
        /*String playerString = "";
        for (PlayerData pd: DataStore.getPlayerData())
            playerString += pd.asPacket()+"\n";
        socket.emit("player list", playerString.trim());*/
    }

    public static void sendStatistics() {
        /*socket.emit("stats", Statistics.asPacket());(*/
    }

}
