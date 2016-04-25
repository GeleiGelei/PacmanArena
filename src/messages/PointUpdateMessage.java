
package messages;

import com.jme3.network.AbstractMessage;
import com.jme3.network.serializing.Serializable;

@Serializable
public class PointUpdateMessage extends AbstractMessage {
    private int playerId;  //id of player whose points need to be updated 
    private int points;    //new point total -> replaces current total
                           //calculation is done on client based on what happened 
    
    public PointUpdateMessage () {
        //nothing here 
    }
    
    public PointUpdateMessage (int newPoints, int playerId) {
        this.points = newPoints;
        this.playerId = playerId;
    }
    
    public int getPoints() {
        return this.points;
    }
    
    public int getId() {
        return this.playerId;
    }
}
