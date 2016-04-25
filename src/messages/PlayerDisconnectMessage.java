
package messages;

import com.jme3.network.AbstractMessage;
import com.jme3.network.serializing.Serializable;

/*
 * Class to alert players of disconnect 
 */
@Serializable
public class PlayerDisconnectMessage extends AbstractMessage {
    private int playerId;
    private int numPlayers;
    
    public PlayerDisconnectMessage() {}
    
    public PlayerDisconnectMessage(int id) {
        this.playerId = id;
    }
    
    public int getId() {
        return this.playerId;
    }
    
    public void setNumPlayers(int numPlayers) {
        this.numPlayers = numPlayers;
    }
    
    public int getNumPlayers() {
        return this.numPlayers;
    }
}
