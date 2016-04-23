
package client;

import com.jme3.app.SimpleApplication;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.shape.Sphere;
import messages.Player;

public class Ghost extends Node implements Character {
    Player p; //the associated client id of the player who controls this character
    
    public Ghost(SimpleApplication sa, Player p) {
        this.p = p;
        createGhost(sa);
    }
    
    private void createGhost(SimpleApplication sa){
        //to determine which ghost to make you can get the character index
        //with this.p.getCharacterIndex() -> they correspond to the indices on the
        //start screen
    }
    
    public void toggleVulnerability(boolean isVulnerable) {
        if(isVulnerable) {
            //turn the ghost blue 
        } else {
            //return the ghost back to its normal color
        }
    }

    public int getClientId() {
        return this.p.getId();
    }
}
