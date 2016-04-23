
package client;

import com.jme3.app.SimpleApplication;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.shape.Sphere;

public class Pacman extends Node{
    
    private static Sphere sphere;
    public Geometry pacGeo;
    
    // Constructor
    Pacman(SimpleApplication sa){
        createPacman(sa);
    }
    
    private void createPacman(SimpleApplication sa){
        
        Material matYellow = new Material(sa.getAssetManager(),"Common/MatDefs/Misc/Unshaded.j3md");
        matYellow.setColor("Color", ColorRGBA.Yellow);
        
        sphere = new Sphere(12, 12, .9f, true, false);
        sphere.setTextureMode(Sphere.TextureMode.Projected);
        
        pacGeo = new Geometry("pacman", sphere);
        pacGeo.setMaterial(matYellow);
        
        this.attachChild(pacGeo);
        sa.getRootNode().attachChild(this);
    }
    
    public Geometry getGeo(){
        return pacGeo;
    }
}
