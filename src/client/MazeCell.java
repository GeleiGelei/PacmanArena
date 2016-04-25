/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package client;

import com.jme3.app.SimpleApplication;
import com.jme3.bounding.BoundingBox;
import com.jme3.bullet.collision.shapes.BoxCollisionShape;
import com.jme3.bullet.control.GhostControl;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.bullet.util.CollisionShapeFactory;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.shape.Box;

class MazeCell extends Node {

    public static final float CELLSIZE = 10f;
    static final float WALLTHICKNESS = CELLSIZE / 10;
    static final float WALLHEIGHT = CELLSIZE / 3;
    public static Box sideWallH = new Box(CELLSIZE / 2, WALLHEIGHT, WALLTHICKNESS);
    public static Box sideWallV = new Box(WALLTHICKNESS, WALLHEIGHT, CELLSIZE / 2);
    Geometry nw;
    Geometry sw;
    SimpleApplication sa;
    
    protected MazeCell(Maze m, int row, int col, GameClient main, Cheese ch, SimpleApplication sa) {
        this.sa = sa;
        // northWall
        if (m.hasNorthWall(row, col)) {
            nw = new Geometry("W", sideWallH);
            nw.setMaterial(main.mat);
            nw.setLocalTranslation(0, WALLHEIGHT, -CELLSIZE / 2);
            attachChild(nw);
        }

        // westWall
        if (m.hasWestWall(row, col)) {
            sw = new Geometry("W", sideWallV);
            sw.setMaterial(main.mat);
            sw.setLocalTranslation(-CELLSIZE / 2, WALLHEIGHT, 0);
            attachChild(sw);
        }

        // set translation of entire cell
        setLocalTranslation(CELLSIZE * col, 0, CELLSIZE * row);
        ch.setLocalTranslation(CELLSIZE * col, 0, CELLSIZE * row);
    }    
}
