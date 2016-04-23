
package server;

import com.jme3.math.ColorRGBA;
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
    public LinkedList<Player> data;

    // -------------------------------------------------------------------------
    public GameWorldData() {
        data = new LinkedList<Player>();
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
        data.addLast(temp);
        
        return temp;
    }
    
    // removes the player with the given id from the game. This happens on connect 
    public boolean removePlayer(int id) {
        try {
            boolean found = false;
            int idx = 0;
            for(Player p : data) {
                System.out.println("id: " + p.getId());
                if(p.getId() == id) {
                    found = true;
                    break;
                }
                idx++;
            };
            if(found) { 
                data.remove(idx);
            } else {
                System.out.println("Couldn't find player with the given id: " + id);
                return false;
            }
        } catch(Exception e) {
            System.out.println("Exception in removePlayer(): " + e.getMessage());
            return false;
        }
        return true;
    }
    
    // last step in getting a player set up for gameplay
    public void finalizePlayer(NewClientFinalize pData) {
        for(Player p : data) {
            if(p.getId() == pData.getId()) {
                System.out.println("Setting player character to: " + pData.getGameChar());
                p.setName(pData.getName());
                p.setCharacter(pData.getGameChar());
                p.setCharacterName(pData.getGameCharName());
                p.setMovementSpeed(pData.getMovementSpeed());
                p.setCharacterIndex(pData.getCharacterIndex());
                break;
            }
        }
    }
    
    public LinkedList getData() {
        return this.data;
    }
    
    public void setVulnerable(boolean vulnerable) {
        for(int i = 0; i < data.size(); i++) {
            Player temp = data.get(i);
            if(temp.getCharacter().toLowerCase().equals("ghost")) {
                temp.setVulnerability(vulnerable);
            }
        }
    }
}
