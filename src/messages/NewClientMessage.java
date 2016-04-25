package messages;

import com.jme3.network.AbstractMessage;
import com.jme3.network.serializing.Serializable;
import java.util.ArrayList;
import java.util.LinkedList;

@Serializable
public class NewClientMessage extends AbstractMessage {

    public int ID;
    public String err;
    public ArrayList gameWorldData;

    // -------------------------------------------------------------------------
    public NewClientMessage() {
    }

    // -------------------------------------------------------------------------
    public NewClientMessage(int ID, ArrayList gameWorldData) {
        super();
        this.ID = ID;
        this.gameWorldData = gameWorldData;
        this.err = "";
    }
    
    //sets the error string 
    public void setErr(String err) {
        this.err = err;
    } 
    
    //returns the error string; will be empty by default unless there is an error
    public String getErr() {
        return this.err;
    }
}
