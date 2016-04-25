
package messages;

import com.jme3.math.Vector3f;
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
    //can't figure out how to serialize and transfer interfaces here, so we can't 
    //use polymorphism. Instead the player will have both, and we'll just do checks 
    //to see which one they are based on if it's null or not... this sucks. 
    private String playerCharacter; //determines whether we are pacman or ghost 
    
    /*character-specific data*/
    private String characterName; //name of character; pacman, blinky, inky, etc.
    private Vector3f location;    //current location
    private Vector3f spawnLocation;//spawn location 
    private int movementSpeed;    //used as a multiplier for movement
    private int characterIndex;   //index of the character in the start screen menu
    
    /*Ghost-specific data*/
    private boolean isVulnerable; // changes when ghost becomes 'blue'
    
    /*Pacman-specific data*/
    private int lives;          //lives - decreases whenever ghost touches pacman
    
    public Player(int id) {
        this.id = id;
        this.name = "";
        this.points = 0;
        this.playerCharacter = null;
        this.characterName = null;
        this.location = new Vector3f(0, 0, 0);
        this.spawnLocation = new Vector3f(0, 0, 0);
        this.isVulnerable = false;
        this.lives = 3;
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
    
    public void setPoints(int newPoints) {
        this.points = newPoints;
    }
    
    // sets the players character 
    public void setCharacter(String c) {
        this.playerCharacter = c;
    }
    
    // gets the player's character
    public String getCharacter() {
        return this.playerCharacter;
    }
    
    public void setCharacterName(String charName) {
        this.characterName = charName;
    }
    
    public String getCharacterName() {
        return this.characterName;
    }
    
    public Vector3f getPos() {
        return this.location;
    }
    
    public void setPos(Vector3f newPos) {
        this.location = newPos;
    }
    
    public Vector3f getSpawnLocation() {
        return this.spawnLocation;
    }
    
    public void setSpawnLocation(Vector3f loc) {
        this.spawnLocation = loc;
    }
    
    public void setMovementSpeed(int speed) {
        this.movementSpeed = speed;
    } 
    
    public void setLives(int newLives) {
        this.lives = newLives;
    }
    
    //returns true if player is dead; false otherwise
    public boolean takeDamage() {
        this.lives--;
        return this.lives == 0;
    }
    
    public void heal() {
        this.lives++;
    }
    
    public void setVulnerability(boolean vulnerable) {
        this.isVulnerable = vulnerable;
    }
    
    public void setCharacterIndex(int idx) {
        this.characterIndex = idx;
    }
    
    public int getCharacterIndex() {
        return this.characterIndex;
    }
}
