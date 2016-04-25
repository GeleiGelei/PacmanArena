
package client;

import com.jme3.app.SimpleApplication;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.shape.Box;
import com.jme3.scene.shape.Sphere;

/*
 * THIS FILE HOLDS THE CHEESE CLASS, PACMAN'S OBJECTIVE. THERE ARE TWO TYPES OF 
 * CHEESE: SMALL AND LARGE. THESE WILL BE DISPERSED THROUGHOUT THE GAME MAZE 
 * UNIFORMLY.
 */

public class Cheese extends Node{
    
    SimpleApplication sa;
    public Node cheeseNode;
    public Geometry geom;
    
    public Cheese(SimpleApplication sa){
        this.sa = sa;
        
        createCheese();
    }
    
    public void createCheese(){
        
        cheeseNode = new Node("cheeseNode");
        //Box b = new Box(Vector3f.ZERO, .6f, .6f, .6f);
        Sphere s = new Sphere(20, 20, .6f);
        geom = new Geometry("Sphere", s);
        
        Material mat = new Material(sa.getAssetManager(), "Common/MatDefs/Misc/Unshaded.j3md");
        mat.setColor("Color", ColorRGBA.Yellow);
        geom.setMaterial(mat);
        geom.setLocalTranslation(0, 1, 0);
        
        cheeseNode.attachChild(geom);
        this.attachChild(cheeseNode);
    }
    
}
