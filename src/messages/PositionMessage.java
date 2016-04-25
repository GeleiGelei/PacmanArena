
package messages;

import com.jme3.math.Vector3f;
import com.jme3.network.AbstractMessage;
import com.jme3.network.serializing.Serializable;

/*
 * Contains player position data which updates in real time - we broadcast
 * this as a response to the triggering of the Analog listener in GameClient.
 */
@Serializable
public class PositionMessage extends AbstractMessage {
    private Vector3f pos;
    private int id;
    
    public PositionMessage() {
        //nothing here
    }
    
    public PositionMessage(Vector3f newPos, int clientId) {
        this.pos = newPos;
        this.id = clientId;
    }
    
    public Vector3f getPos() {
        return this.pos;
    }
    
    public int getId() {
        return this.id;
    }
}
