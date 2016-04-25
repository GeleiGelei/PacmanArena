
package messages;

import com.jme3.math.Quaternion;
import com.jme3.network.AbstractMessage;
import com.jme3.network.serializing.Serializable;

/*
 * Contains player rotation data which updates in real time - we broadcast
 * this as a response to the triggering of the action listener in GameClient.
 */
@Serializable
public class RotationMessage extends AbstractMessage {
    private Quaternion rot;
    private int id;
    
    public RotationMessage() {
        //nothing here
    }
    
    public RotationMessage(Quaternion newRot, int clientId) {
        this.rot = newRot;
        this.id = clientId;
    }
    
    public Quaternion getRot() {
        return this.rot;
    }
    
    public int getId() {
        return this.id;
    }
}
