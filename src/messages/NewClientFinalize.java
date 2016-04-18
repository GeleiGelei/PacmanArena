
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
    private client.Character gameCharacter;
    public NewClientFinalize() {
        //nothing here
    }
    
    public NewClientFinalize(int id, String name, client.Character character) {
        this.clientId = id;
        this.name = name;
        this.gameCharacter = character;
    }
    
    public int getId() {
        return this.clientId;
    }
    
    public String getName() {
        return this.name;
    }
    
    public client.Character getGameChar() {
        return this.gameCharacter;
    }
}
