/*
 * The Game Server contains the game logic
 */
package server;

import com.jme3.network.Message;
import messages.NewClientMessage;
public class GameServer implements ServerNetworkListener {

    ServerNetworkHandler networkHandler;
    PlayField playfield;
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
        networkHandler = new ServerNetworkHandler(this);
        playfield = new PlayField();
        initCube();
        playfield.addMaze(cube);
    }


    // -------------------------------------------------------------------------
    // Builds the Cube
    public void initCube(){
        System.out.println("Creating Cube");
        cube = new GameCubeMaze();
        cube.generate(10, 10, 10);
        System.out.println("Cube Created");
    }
            
    // -------------------------------------------------------------------------
    // Methods required by ServerNetworkHandler
    public void messageReceived(Message msg) {

    }

    // -------------------------------------------------------------------------
    public Message newConnectionReceived(int connectionID) throws Exception {
        // put player on random playfield
        
        boolean ok = playfield.addElement(connectionID);
        if (!ok) {
            System.out.println("Max number of players!!");
            throw new Exception("Max number of players exceeded.");
        }
        // send entire playfield and maze to new client
        NewClientMessage iniCM = null;
        try {
            iniCM = new NewClientMessage(connectionID, playfield.maze, playfield.data);
        } catch (Exception e){
            System.out.println("ERROR: GameServer.newConnectionReceived //Failed to create NewClientMessage");
        }
        
/***********************
 * 
 * For Testing Purposes Begin
 * 
 ***********************/
 System.out.println("Arrived at the end of GameServer.newConnectionReceived");
/***********************
 * 
 * For Testing Purposes End
 * 
 ***********************/
        return (iniCM);
        
    }
}
