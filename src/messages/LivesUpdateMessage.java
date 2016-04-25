
package messages;

import com.jme3.network.AbstractMessage;
import com.jme3.network.serializing.Serializable;

/*
 * Responsible for updating pacman's lives count
 */

@Serializable
public class LivesUpdateMessage extends AbstractMessage {
    private int lives;  //new total -> replaces previous total
    private int playerId; //id of the pacman client
    
    public LivesUpdateMessage() {
        //nothing here
    }
    
    public LivesUpdateMessage(int lives, int playerId) {
        this.playerId = playerId;
        this.lives = lives;
    }
    
    public int getLives() {
        return this.lives;
    }
    
    public int getId() {
        return this.playerId;
    }
}
