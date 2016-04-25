package client;

import com.jme3.app.SimpleApplication;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Sphere;
import messages.Player;

public class Ghost extends Node implements Character {
    Player p; //the associated client id of the player who controls this character
    public Node ghostNode;
    public Spatial gNode;
    
    public Ghost(SimpleApplication sa, Player p) {
        this.p = p;
        createGhost(sa);
    }
    
    private void createGhost(SimpleApplication sa){
        //to determine which ghost to make you can get the character index
        //with this.p.getCharacterIndex() -> they correspond to the indices on the
        //start screen
        
        ghostNode = new Node("GhostNode");
        gNode = (Node) sa.getAssetManager().loadModel("Models/ghosts/orangeGhost.j3o");
        gNode.setMaterial(sa.getAssetManager().loadMaterial("Materials/Generated/orangeGhostMat.j3m"));
        ghostNode.attachChild(gNode);
        
        this.attachChild(ghostNode);
        sa.getRootNode().attachChild(this);
        System.out.println("getting the ghost index " + p.getCharacterIndex());
        
        if(p.getCharacterIndex() == 1){
            System.out.println("hello");
        }
        
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
