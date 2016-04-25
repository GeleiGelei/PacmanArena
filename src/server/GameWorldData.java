
package server;

import com.jme3.math.ColorRGBA;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Random;
import messages.NewClientFinalize;
import messages.Player;

/*
 * Holds all of the data associated with the packman game world. 
 * This includes a list of up-to-date information about each player in the 
 * form of a list of PlayerData objects 
 */

public class GameWorldData {
    public ArrayList data;
    public byte[][] mazeData;
    public boolean mazeCreated;

    // -------------------------------------------------------------------------
    public GameWorldData() {
        data = new ArrayList();
        mazeCreated = false;
    }

    // -------------------------------------------------------------------------
    //creates and adds a new player to the server and returns it
    //on error, player is null
    public Player addElement(int id) {
        Player temp = null;
        
        //check to see if the server has the max number of players 
        if(data.size() == 2) {
            return temp;
        }
        
        //if we have space, make a newly initialized player and add them to the list
        temp = new Player(id);
        data.add(id, temp);
        
        return temp;
    }
    
    // removes the player with the given id from the game. This happens on connect 
    public boolean removePlayer(int id) {
        try {
            data.remove(id);
        } catch(Exception e) {
            System.out.println("Exception in removePlayer(): " + e.getMessage());
            return false;
        }
        return true;
    }
    
    // last step in getting a player set up for gameplay
    public void finalizePlayer(NewClientFinalize pData) {
        //save the maze data of the first person to connect
        if(!mazeCreated) {
            this.mazeData = pData.getMazeData();
            mazeCreated = true;
        }
        
        Player p = (Player)data.get(pData.getId());
        p.setName(pData.getName());
        p.setCharacter(pData.getGameChar());
        p.setCharacterName(pData.getGameCharName());
        p.setMovementSpeed(pData.getMovementSpeed());
        p.setCharacterIndex(pData.getCharacterIndex());
    }
    
    public ArrayList getData() {
        return this.data;
    }
    
    public void setVulnerable(boolean vulnerable) {
        for(int i = 0; i < data.size(); i++) {
            Player temp = (Player)data.get(i);
            if(temp.getCharacter().toLowerCase().equals("ghost")) {
                temp.setVulnerability(vulnerable);
            }
        }
    }
}
