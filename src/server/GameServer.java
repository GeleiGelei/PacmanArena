/*
 * The Game Server contains the game logic
 */
package server;

import com.jme3.network.Message;
import messages.InitMazeMessage;
import messages.NewClientFinalize;
import messages.NewClientMessage;
import messages.Player;
import messages.PositionMessage;
import messages.RotationMessage;
import messages.VulnerabilityMessage;
public class GameServer implements ServerNetworkListener {

    ServerNetworkHandler networkHandler;
    GameWorldData gameWorld;
    GameCubeMaze cube;

    // -------------------------------------------------------------------------
    public static void main(String[] args) {
        System.out.println("Starting Game Server at port " + ServerNetworkHandler.SERVERPORT);
        GameServer gs = new GameServer();
        while (true) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException ex) {
            }
        }
    }

    // -------------------------------------------------------------------------
    public GameServer() {
        gameWorld = new GameWorldData();
        networkHandler = new ServerNetworkHandler(this, gameWorld);
        
        initCube();
    }


    // -------------------------------------------------------------------------
    // Builds the Cube
    public void initCube(){
        cube = new GameCubeMaze(10, 10, 10);
    }
            
    // -------------------------------------------------------------------------
    // Methods required by ServerNetworkHandler
    public void messageReceived(Message msg) {
        if(msg instanceof NewClientFinalize) {
            //player intends to update his information
            gameWorld.finalizePlayer((NewClientFinalize)msg);
            
            //broadcast the maze to the new player that just connected
            InitMazeMessage imm = new InitMazeMessage(gameWorld.mazeData);
            networkHandler.broadcast(imm);
            
            //broadcast the new player that just connected to all players
            NewClientMessage resp = new NewClientMessage(((NewClientFinalize)msg).getId(), gameWorld.data);
            networkHandler.broadcast(resp);
        } else if(msg instanceof VulnerabilityMessage) {
            VulnerabilityMessage vul = (VulnerabilityMessage)msg;
            gameWorld.setVulnerable(vul.getVulnerability());
            networkHandler.broadcast(vul);
        } else if(msg instanceof PositionMessage) {
            networkHandler.broadcast((PositionMessage)msg);
        } else if(msg instanceof RotationMessage) {
            networkHandler.broadcast((RotationMessage)msg);
        }
    }

    // -------------------------------------------------------------------------
    public Message newConnectionReceived(int connectionID) throws Exception {
        // put player on random gameWorld
        Player temp = gameWorld.addElement(connectionID);
        
        // send entire gameWorld to new client
        NewClientMessage iniCM = new NewClientMessage(connectionID, gameWorld.data);
        if (temp == null) {
            iniCM.setErr("Yikes! The server is already full... Please try connecting later.");
        }
        return (iniCM);
    }
}
