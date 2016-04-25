package client;

import com.jme3.animation.AnimChannel;
import com.jme3.animation.AnimControl;
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
    private AnimChannel channel;
    private AnimControl control;
    
    public Ghost(SimpleApplication sa, Player p) {
        this.p = p;
        createGhost(sa);
    }
    
    private void createGhost(SimpleApplication sa){
        //to determine which ghost to make you can get the character index
        //with this.p.getCharacterIndex() -> they correspond to the indices on the
        //start screen
        
        
        
        
        
        if(p.getCharacterIndex() == 1){
            ghostNode = new Node("GhostNode");
            gNode = (Node) sa.getAssetManager().loadModel("Models/ghosts/orangeGhost.j3o");
            gNode.setMaterial(sa.getAssetManager().loadMaterial("Materials/Generated/orangeGhostMat.j3m"));
            ghostNode.attachChild(gNode);
        }else if(p.getCharacterIndex() == 2){
            ghostNode = new Node("GhostNode");
            gNode = (Node) sa.getAssetManager().loadModel("Models/ghosts/blueGhost.j3o");
            gNode.setMaterial(sa.getAssetManager().loadMaterial("Materials/Generated/blueGhostMat.j3m"));
            ghostNode.attachChild(gNode);
        }else if(p.getCharacterIndex() == 3){
            ghostNode = new Node("GhostNode");
            gNode = (Node) sa.getAssetManager().loadModel("Models/ghosts/redGhost.j3o");
            gNode.setMaterial(sa.getAssetManager().loadMaterial("Materials/Generated/redGhostMat.j3m"));
            ghostNode.attachChild(gNode);
        }else if(p.getCharacterIndex() == 4){
            ghostNode = new Node("GhostNode");
            gNode = (Node) sa.getAssetManager().loadModel("Models/ghosts/pinkGhost.j3o");
            gNode.setMaterial(sa.getAssetManager().loadMaterial("Materials/Generated/pinkGhostMat.j3m"));
            ghostNode.attachChild(gNode);
        }
        
        
        
        Node child = (Node) ghostNode.getChild(0);
        Node grandChild = (Node) child.getChild(0);
        control = grandChild.getChild(0).getControl(AnimControl.class);
        channel = control.createChannel();
        System.out.println(control.getAnimationNames());
        
        this.attachChild(ghostNode);
        sa.getRootNode().attachChild(this);
        
    }
    
    public void toggleVulnerability(boolean isVulnerable) {
        if(isVulnerable) {
            //turn the ghost blue 
        } else {
            //return the ghost back to its normal color
        }
    }
    
    public void walkGhost(){
        channel.setAnim("walk");
    }

    public int getClientId() {
        return this.p.getId();
    }
}
