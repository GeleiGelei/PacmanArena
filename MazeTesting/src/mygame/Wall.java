package mygame;

import com.jme3.app.SimpleApplication;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.shape.Box;

/*
 * THIS FILE IS THE GAME'S WALL CLASS. THIS IS A COMPONENT OF THE GAME MAZE.
 * Yar Har fiddle dee-dee
 */
public class Wall extends Node{
    SimpleApplication sa;
    Material mat;
    Geometry wall;
            
    public Wall(SimpleApplication sa, Material mat) {
        this.sa = sa;
        this.mat = mat;
    }

    public void initGeometry(int angle) {
        Box b = new Box(1, 1, 1);
        wall = new Geometry("wall", b);
        wall.setMaterial(this.mat);
        if (angle == 1)
            wall.scale(2, .01f, 2);
        
        if (angle  == 2)
            wall.scale(.01f, 2, 2);
        
        if (angle == 3)
            wall.scale(2, 2, .01f);
    }
    
    public void setPos (int i, int j, int k){
        wall.setLocalTranslation(2*i, 2*j, 2*k);
    }
}
