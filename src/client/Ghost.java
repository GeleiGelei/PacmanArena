
package client;

import CodeUtils.Parser;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.shape.Cylinder;
import java.util.logging.Level;
import java.util.logging.Logger;

/*
 * THIS IS THE GHOST CLASS - EVIL AS IT IS. HERE IS THE CREATION AND INITIALIZATION
 * OF THE GHOST OBJECT (GEOMETRIES, MATERIALS, STATS, CONTROLS, ETC.)
 */

public class Ghost extends Node implements Character {
    private String characterName;
    private boolean isVulnerable; // changes when ghost becomes 'blue'
    private Vector3f location;
    private Vector3f spawnLocation;
    private int movementSpeed;
    private Geometry ghostGeo;
    
    /*
     * physical data
     */
    private float radius = 2.0f;
    private float height = 3.0f;
    private int axisSamples = 32;
    private int radialSamples = 256;
    
    public Ghost() {
        this.isVulnerable = false; 
        this.location = new Vector3f(0, 0, 0);
        this.spawnLocation = new Vector3f(0, 0, 0);
        this.movementSpeed = 3; // a random value, may need to be adjusted later
     
        // initialize geometry, materials, etc.
        init();
    }
    
    public void init() {
        throw new UnsupportedOperationException("Not supported yet."); 
    }
    
    public void setLives(int lifeCount) {
        try {
            // method does not apply to the ghost 
            throw new IllegalOperationException("Operation not supported for " + this.getClass());
        } catch (IllegalOperationException ex) {
            Logger.getLogger(Ghost.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public int getLives() {
        try {
            // method does not apply to the ghost 
            throw new IllegalOperationException("Operation not supported for " + this.getClass());
        } catch (IllegalOperationException ex) {
            Logger.getLogger(Ghost.class.getName()).log(Level.SEVERE, null, ex);
            return -1;
        }
    }

    public Vector3f getLocation() {
        return this.location;
    }

    public void setLocation(Vector3f loc) {
        this.location = loc;
    }

    public void setMovementSpeed(int speed) {
        this.movementSpeed = speed;
    }

    public int getMovementSpeed() {
        return this.movementSpeed;
    }
    
    public void takeDamage() {
        // kills the ghost and forces it to go back to its spawn area
        this.isVulnerable = false;
        // adjust the x, y, z coordinates for the spawn location of this player
        this.setLocation(this.spawnLocation); 
    }

    public void heal() {
        try {
            // method does not apply to the ghost 
            throw new IllegalOperationException("Operation not supported for " + this.getClass());
        } catch (IllegalOperationException ex) {
            Logger.getLogger(Ghost.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public String getCharacterClass() {
        return Parser.parseForClassName(this.getClass().toString());
    }

    public void setCharacterName(String name) {
        this.characterName = name;
    }
}
