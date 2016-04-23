
package messages;

import com.jme3.network.AbstractMessage;
import com.jme3.network.serializing.Serializable;

@Serializable
public class InitMazeMessage extends AbstractMessage {
    private byte[][] mazeData;
    
    public InitMazeMessage() {
        //nothing here
    }
    
    public InitMazeMessage(byte[][] mazeData) {
        this.mazeData = mazeData;
    }
    
    public byte[][] getMazeData() {
        return this.mazeData;
    }
}
