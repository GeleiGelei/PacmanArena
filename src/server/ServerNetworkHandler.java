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
import messages.Registration;

public class ServerNetworkHandler implements MessageListener, ConnectionListener {

    public static int SERVERPORT = 6143;
    Server server;
    ServerNetworkListener gameServer;

    // -------------------------------------------------------------------------
    public ServerNetworkHandler(GameServer l) {
        gameServer = l;
        try {
            server = Network.createServer(SERVERPORT);
            Registration.registerMessages();
            server.addMessageListener(this);
            server.addConnectionListener(this);
            server.start();
        } catch (IOException ex) {
            System.out.println("ERROR: ServerNetworkHandler //Failed to create server");            
            Logger.getLogger(ServerNetworkHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    // -------------------------------------------------------------------------
    public void messageReceived(Object source, Message msg) {
        System.out.println("Received: " + msg);
        gameServer.messageReceived(msg);
    }

    // -------------------------------------------------------------------------
    public void connectionAdded(Server server, HostedConnection conn) {
        int connID = conn.getId();
        System.out.println("Client " + connID + " connecting");
        Message m;
        try {
            // gameServer.newConnectionReceived throws an Exception
            // if the connection should not be accepted.
            m = gameServer.newConnectionReceived(connID);
            // if all is ok, broadcast gameServer-created message
            // this is usually an InitialClientMessage
            // (which is just not hard coded here to keep it customizable).
/***********************
 * 
 * For Testing Purposes Begin
 * 
 ***********************/
System.out.println("Before Broadcasting: SererNetworkHandler.connectionAdded");
/***********************
 * 
 * For Testing Purposes End
 * 
 ***********************/
            broadcast(m);
/***********************
 * 
 * For Testing Purposes Begin
 * 
 ***********************/
System.out.println("After Broadcasting: SererNetworkHandler.connectionAdded");
/***********************
 * 
 * For Testing Purposes End
 * 
 ***********************/
        } catch (Exception e) {
            System.out.println("ERROR: Connection " + connID + " not completed//ServerNetworkHandler.connectionAdded");
        }
    }

    // -------------------------------------------------------------------------
    public void sendToClient(int ID, Message msg) {
        try {
            HostedConnection hc = server.getConnection(ID);
            server.broadcast(Filters.in(hc), msg);
        } catch (Exception e) {
            System.out.println("ERROR: ServerNetworkHandler.sendToClient //Getting connection or broadcasting");
        }
    }

    // -------------------------------------------------------------------------
    public void broadcast(Message m) {
        server.broadcast(m);
    }

    // -------------------------------------------------------------------------
    public void connectionRemoved(Server server, HostedConnection conn) {
        // TODO
    }
}
    