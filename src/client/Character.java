
package client;

import com.jme3.math.Vector3f;
//import com.jme3.network.serializing.Serializable;

/*
 * INTERFACE FOR GHOST/PACMAN DATA. THIS WILL HOLD ALL 'PHYSICAL' PLAYER DATA 
 * INCLUDING LIVES, LOCATION, AND GAME STATS LIKE MOVEMENT SPEED.
 */

public interface Character {
    // initializes phyisical character properties
    public void init();
    
    // set the lives of the player to lifecount 
    public void setLives(int lifeCount);    
    
    // returns the current lives of the player 
    public int getLives();
    
    // returns a vector with the x, y, and z position of the player
    public Vector3f getLocation();
    
    // sets the location of the player - could be useful to initialize the game 
    public void setLocation(Vector3f loc);
    
    // sets the movement speed of the character
    public void setMovementSpeed(int speed);
    
    // gets the movment speed of the character
    public int getMovementSpeed();
    
    // decrements life count by one 
    public void takeDamage();
    
    // increments life count by one 
    public void heal();
    
    // returns the class of the instantiated character - Ghost or Pacman
    public String getCharacterClass();
    
    // sets the character name 
    public void setCharacterName(String name);
}
