
package messages;

import client.Character;
import com.jme3.network.AbstractMessage;
import com.jme3.network.serializing.Serializable;

/*
 * The data for a single player - is a part of the overall list created in 
 * GameWorldData.java which holds a list of all Player objects that gets 
 * passed back and forth between the server 
 */
@Serializable
public class Player extends AbstractMessage {
    /*
     * Player Data 
     */
    private int id;             /* player id given by the server */
    private String name;        /* 'username' the player gives himself */
    private int points;         /*
                                 * points player earns during this round 
                                 * calculated differently for ghosts and pacman;
                                 * pacman recieves points for eating cheese and 
                                 * ghosts; ghosts recieve points for eating 
                                 * pacman. The values will differ. 
                                 */
    private Character c;        /* Pacman or Ghost */
    
    public Player(int id) {
        this.id = id;
        this.name = "";
        this.points = 0;
        this.c = null;
    }

    public Player() {
        //nothing here
    }
    
    // sets the id of the player 
    public void setId(int id) {
        this.id = id;
    }
    
    // gets the player's id 
    public int getId() {
        return this.id;
    }
    
    // sets the player's name
    public void setName(String name) {
        this.name = name;
    }
    
    // gets the player's name
    public String getName() {
        return this.name;
    }
    
    // gets the players current point total 
    public int getPoints() {
        return this.points;
    }
    
    // sets the players character 
    public void setCharacter(Character c) {
        this.c = c;
    }
    
    // gets the player's character
    public Character getCharacter() {
        return this.c;
    }
}
