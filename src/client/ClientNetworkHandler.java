package client;

import com.jme3.app.SimpleApplication;
import com.jme3.network.AbstractMessage;
import com.jme3.network.Client;
import com.jme3.network.Message;
import com.jme3.network.MessageListener;
import com.jme3.network.Network;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import messages.Registration;


public class ClientNetworkHandler implements MessageListener {
    private static final String IPADDRESS = "localhost";
    private static final int PORT = 6143;
    private Client client;
    private int ID = -1;
    ClientNetworkListener gameClient;

    // -------------------------------------------------------------------------
    public ClientNetworkHandler(ClientNetworkListener gc) {
        
        initNetwork();
        gameClient = gc;
    }

    // -------------------------------------------------------------------------
    private void initNetwork() {
        try {
            Registration.registerMessages();
            client = Network.connectToServer(IPADDRESS, PORT);
            client.start();
            client.addMessageListener(this);
        } catch (IOException ex) {
            System.out.println("Couldn't connect to the game");
            Logger.getLogger(ClientNetworkHandler.class.getName()).log(Level.SEVERE, "Could not connect to server", ex);
            ((SimpleApplication)gameClient).stop();
        }
    }
   
    // -------------------------------------------------------------------------
    public void messageReceived(Object source, Message msg) {    
System.out.println("Message Received by Handler");
            gameClient.messageReceived(msg);
    }
    
    // -------------------------------------------------------------------------
    public void send(AbstractMessage msg){
        client.send(msg);
    }
        
    // -------------------------------------------------------------------------
    public int getID(){
        if (this.ID == -1){
            ID = client.getId(); // could still be -1 if not transmitted from server yet
        }
        return(ID);
    }
}
