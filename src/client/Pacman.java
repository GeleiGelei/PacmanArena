
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
    
public class Pacman extends Node implements Character {
    
    private Player p; //the associated player with this character
    public Node pacNode;
    public Spatial pNode;
    private AnimChannel channel;
    private AnimControl control;
    private boolean moving;
    private boolean animSet;
    
    // Constructor
    Pacman(SimpleApplication sa, Player p){
        this.moving = false;
        this.animSet = false;
        this.p = p;
        createPacman(sa);
    }
    
    private void createPacman(SimpleApplication sa){
        pacNode = new Node("PacmanNode");
        pNode = (Node) sa.getAssetManager().loadModel("Models/pacman/pacman.j3o");
        pNode.setMaterial(sa.getAssetManager().loadMaterial("Materials/Generated/pacmanMat.j3m"));
        
        pacNode.attachChild(pNode);
        
        Node child = (Node) pacNode.getChild(0);
        Node grandChild = (Node) child.getChild(0);
        control = grandChild.getChild(0).getControl(AnimControl.class);
        channel = control.createChannel();
        System.out.println(control.getAnimationNames());
        
        this.attachChild(pacNode);
    }
    
    public void toggleMoveAnimation(){
        if(this.isMoving()) {
            if(!animSet) {
                channel.setAnim("mouth_move");
                animSet = true;
            }
            channel.setSpeed(0.5f);
        } else {
            animSet = false;
            channel.reset(false);
            channel.setSpeed(0);
        }
    }
    
    public int getClientId() {
        return this.p.getId();
    }
    
    public void setMoving(boolean moving) {
        this.moving = moving;
    }
    
    public boolean isMoving() {
        return this.moving;
    }
}
