/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package messages;

import com.jme3.network.AbstractMessage;
import com.jme3.network.serializing.Serializable;

@Serializable
public class CheeseConsumptionMessage extends AbstractMessage {
    int cheeseIdx;
    
    public CheeseConsumptionMessage() {
        //nope
    }
    
    public CheeseConsumptionMessage(int idx) {
        this.cheeseIdx = idx;
    }
    
    public int getCheeseIndex() {
        return this.cheeseIdx;
    }
}
