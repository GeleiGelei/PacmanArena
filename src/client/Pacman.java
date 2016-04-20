
package client;

import CodeUtils.Parser;
import com.jme3.math.Vector3f;
import com.jme3.network.AbstractMessage;
import com.jme3.network.serializing.Serializable;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.shape.Sphere;
import java.lang.annotation.Annotation;

/*
 * THIS IS THE ALL-IMPORTANT PACMAN CLASS. THIS HANDLES THE CREATION AND INITIALIZATION
 * OF THE PACMAN OBJECT (GEOMETRY, MATERIALS, CONTROLS, LIVES, ETC)
 */

@Serializable
public class Pacman extends Node implements Character {
    private int lives;
    private String characterName;
    private Vector3f location;
    private Vector3f spawnLocation;
    private int movementSpeed;
    private Geometry pacmanGeo;
    
    /*
     * physical data
     */
    private float radius = 2.0f;
    private int zSamples = 32;
    private int radialSamples = 256;
    
    public Pacman() {
        this.name = "";
        this.lives = 3;
        this.location = new Vector3f(0, 0, 0);
        this.spawnLocation = new Vector3f(0, 0, 0);
        this.movementSpeed = 5; // a random value, may need to be adjusted later
        
        // initialize geometry, materials, etc.
        //init();
    }
    
    public void init() {
        throw new UnsupportedOperationException("Not supported yet."); 
    }
    
    public void setLives(int lifeCount) {
        this.lives = lifeCount;
    }

    public int getLives() {
        return this.lives;
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
        this.lives--;
        
        if(this.lives == 0) {
            // death logic
            this.setLocation(this.spawnLocation);
        }
    }

    public void heal() {
        this.lives++;
    }

    public String getCharacterClass() {
        return Parser.parseForClassName(this.getClass().toString());
    }
    
    public void setCharacterName(String name) {
        this.name = "Pacman"; //always the same name for this
    }

    public void setCharacterName() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public Class serializer() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public short id() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public Class<? extends Annotation> annotationType() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
