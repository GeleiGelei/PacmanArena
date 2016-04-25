
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

public class Pacman extends Node{
    
    public Node pacNode;
    public Spatial pNode;
    private AnimChannel channel;
    private AnimControl control;
    
    
    // Constructor
    Pacman(SimpleApplication sa){
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
        //sa.getRootNode().attachChild(pacNode);
    }
    
    public void movePacman(){
        channel.setAnim("mouth_move");
    }
    
}
