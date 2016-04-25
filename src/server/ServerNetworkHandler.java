package server;

import com.jme3.network.ConnectionListener;
import com.jme3.network.Filters;
import com.jme3.network.HostedConnection;
import com.jme3.network.Message;
import com.jme3.network.MessageListener;
import com.jme3.network.Network;
import com.jme3.network.Server;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import messages.PlayerDisconnectMessage;
import messages.Registration;

public class ServerNetworkHandler implements MessageListener, ConnectionListener {

    public static int SERVERPORT = 6143;
    Server server;
    ServerNetworkListener gameServer;
    GameWorldData gwd;

    // -------------------------------------------------------------------------
    public ServerNetworkHandler(GameServer l, GameWorldData gwd) {
        gameServer = l;
        this.gwd = gwd;
        try {
            server = Network.createServer(SERVERPORT);
            Registration.registerMessages();
            server.addMessageListener(this);
            server.addConnectionListener(this);
            server.start();
        } catch (IOException ex) {
            Logger.getLogger(ServerNetworkHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    // -------------------------------------------------------------------------
    public void messageReceived(Object source, Message msg) {
        gameServer.messageReceived(msg);
    }

    // -------------------------------------------------------------------------
    public void connectionAdded(Server server, HostedConnection conn) {
        int connID = conn.getId();
        System.out.println("Client " + connID + " connected");
        Message m;
        
        try {
            // gameServer.newConnectionReceived throws an Exception
            // if the connection should not be accepted.
            m = gameServer.newConnectionReceived(connID);
            // if all is ok, broadcast gameServer-created message
            // this is usually an InitialClientMessage
            // (which is just not hard coded here to keep it customizable).
            broadcast(m);
        } catch (Exception e) {
            // Connection not accepted.
        }
    }

    // -------------------------------------------------------------------------
    public void sendToClient(int ID, Message msg) {
        try {
            HostedConnection hc = server.getConnection(ID);
            server.broadcast(Filters.in(hc), msg);
        } catch (Exception e) {
            System.out.println("ERROR in sendToClient() : " + e.getMessage());
        }
    }

    // -------------------------------------------------------------------------
    public void broadcast(Message m) {
        server.broadcast(m);
    }
    
    // -------------------------------------------------------------------------
    public void connectionRemoved(Server server, HostedConnection conn) {
        //remove the player from the game
        if(this.gwd.removePlayer(conn.getId())) {
            System.out.println("Player " + conn.getId() + " disconnected from the server");
            PlayerDisconnectMessage pdm = new PlayerDisconnectMessage(conn.getId());
            pdm.setNumPlayers(gwd.getData().size());
            broadcast(pdm);
        } else {
            System.out.println("Blocked new player from joining; server is full.");
        }
    }
}
