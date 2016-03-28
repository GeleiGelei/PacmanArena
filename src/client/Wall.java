package client;

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
    public Wall(SimpleApplication sa) {
        this.sa = sa;
    }

    public void initGeometry(int angle) {
        Box b = new Box(1, 1, 1);
        Geometry wall = new Geometry("wall", b);
        wall.setMaterial(initMat());
        
        if (angle == 1)
            wall.scale(1, .2f, 1);
        
        if (angle  == 2)
            wall.scale(.2f, 1, 1);
        
        if (angle == 3)
            wall.scale(1, 1, .2f);
    }

    public Material initMat() {
        Material mat = new Material(sa.getAssetManager(), "Common/MatDefs/Misc/Unshaded.j3md");
        mat.setColor("Color", ColorRGBA.Blue);
        return mat;
    }
}
