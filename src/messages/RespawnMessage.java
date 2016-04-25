/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package messages;

import com.jme3.math.Vector3f;
import com.jme3.network.AbstractMessage;
import com.jme3.network.serializing.Serializable;

@Serializable
public class RespawnMessage extends AbstractMessage {
    Vector3f respawnLoc;
    int clientId;
    
    public RespawnMessage() {
        //nothing here
    }
    
    public RespawnMessage(Vector3f respawnLoc, int id) {
        this.respawnLoc = respawnLoc;
        this.clientId = id;
    }
    
    public int getClientId() {
        return this.clientId;
    }
    
    public Vector3f getRespawnLoc() {
        return this.respawnLoc;
    }
}
