
package messages;

import com.jme3.network.AbstractMessage;
import com.jme3.network.serializing.Serializable;

/*
 * NewClientFinalize is responsible for finishing the new player connection process.
 * This communicates the player's desired name and character class, updates the data in the server, and then 
 * broadcasts that info
 */

@Serializable
public class NewClientFinalize extends AbstractMessage {
    private int clientId;
    private String name; 
    private String playerChar;
    private String playerCharName;
    private int movementSpeed;
    private int characterIndex;
    private byte[][] mazeData;
    
    public NewClientFinalize() {
        //nothing here
    }
    
    public NewClientFinalize(int id, String name, String playerChar, String playerCharName, int characterIndex, byte[][] mazeData) {
        this.clientId = id;
        this.name = name;
        this.playerChar = playerChar;
        this.playerCharName = playerCharName;
        this.characterIndex = characterIndex;
        this.mazeData = mazeData;
        if(playerChar.toLowerCase().equals("pacman")) {
            this.movementSpeed = 10; //pacman speed
        } else {
            this.movementSpeed = 8;  //ghost speed
        }
    }
    
    public int getId() {
        return this.clientId;
    }
    
    public String getName() {
        return this.name;
    }
    
    public String getGameChar() {
        return this.playerChar;
    }
    
    public String getGameCharName() {
        return this.playerCharName;
    }
    
    public int getMovementSpeed() {
        return this.movementSpeed;
    }
    
    public int getCharacterIndex() {
        return this.characterIndex;
    }
    
    public byte[][] getMazeData() {
        return this.mazeData;
    }
}
