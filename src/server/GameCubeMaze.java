
package server;

import com.jme3.network.serializing.Serializable;

/*
 * THIS FILE SERVES AS THE GAME MAZE OBJECT - IT WILL HANDLE CREATION AND INITIALIZATION
 * OF THE 3D GAME MAZE.
 */
@Serializable
public class GameCubeMaze {
    private final int DEFAULTCHAMBERSIZE = 8;
    private byte [][][] theMaze;    // the maze, containing chambers
    private int count;
    
    // -------------------------
    // Constructor
    // -------------------------

    public GameCubeMaze(){
        
    }
    
    public final void generate(int rows, int columns, int height) {
        // initialize: all walls up, path=false => 0x0f
        theMaze = new byte[rows][columns][height];
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < columns; c++) {
                for (int h = 0; h < height; h++){
                    theMaze[r][c][h] = 0x3f;
                }
            }
        }

        // pick a start point
        int startR = (int) Math.floor(Math.random() * rows);
        //int startC = (int) Math.floor(Math.random() * rows);
        int startC = (int) Math.floor(Math.random() * columns);
        int startH = (int) Math.floor(Math.random() * height);
        
        count = 0;

        // and generate
        generateRec(startR, startC, startH, 0);
        
        /***************************************
         * This part needs to be modified to create the prison/prisons for 
         *  the ghosts, the pacman start, and the warp walls;
         *
        // set exit point to 0,0
        theMaze[0][0][0] |= 0x20;      // 0x20: exit
        theMaze[0][0] &= 0xFE;      // remove North Wall   
        mazePanel = new MyPanel();      // create and add new panel
        *
        * 
        *****************************************/
    }

    private void generateRec(int r, int c, int h, int direction) {
        // tear down wall towards source direction
        switch (direction) {
            case 1:
                theMaze[r][c][h] -= 4;
                break;
            case 2:
                theMaze[r][c][h] -= 8;
                break;
            case 4:
                theMaze[r][c][h] -= 1;
                break;
            case 8:
                theMaze[r][c][h] -= 2;
                break;
            case 16:
                theMaze[r][c][h] -= 32;
                break;
            
            case 32:
                theMaze[r][c][h] -= 16;
                break;
        }
        count++;        // another chamber processed.

        // where to go now ?
        int noRows = theMaze.length;
        int noColumns = theMaze[0].length;
        int noHeight = theMaze[0][0].length;
        
        // base case 1: all chambers finished
        if (count == noRows * noColumns * noHeight) {
            return;
        }

        // recursive case: while there are walkable directions: walk
        while (true) {
            // find walkable directions
            boolean dir1, dir2, dir4, dir8, dir16, dir32;
            dir1 = dir2 = dir4 = dir8 = dir16 = dir32 = false;
            if (r > 0 && (theMaze[r - 1][c][h] == 0x3f)) {
                dir1 = true;
            }
            if (c > 0 && (theMaze[r][c - 1][h] == 0x3f)) {
                dir2 = true;
            }
            if (r < noRows - 1 && (theMaze[r + 1][c][h] == 0x3f)) {
                dir4 = true;
            }
            if (c < noColumns - 1 && (theMaze[r][c + 1][h] == 0x3f)) {
                dir8 = true;
            }
            if (h > 0 && (theMaze[r][c][h-1] == 0x3f)){
                dir16 = true;
            }
            if (h < noHeight - 1 && (theMaze[r][c][h + 1] == 0x3f)) {
                dir32 = true;
            }


            // base case 2: no walkable directions left
            if ((dir1 | dir2 | dir4 | dir8 | dir16 | dir32) == false) {
                break;
            }

            boolean picked = false;
            do {
                int d = (int) Math.floor(Math.random() * 6); // direction 0-5
                switch (d) {
                    case 0:
                        if (dir1) {
                            picked = true;
                            theMaze[r][c][h] -= 1;
                            generateRec(r - 1, c, h, 1);
                            dir1 = false;
                            break;
                        }
                    case 1:
                        if (dir2) {
                            picked = true;
                            theMaze[r][c][h] -= 2;
                            generateRec(r, c - 1, h, 2);
                            dir2 = false;
                            break;
                        }
                    case 2:
                        if (dir4) {
                            picked = true;
                            theMaze[r][c][h] -= 4;
                            generateRec(r + 1, c, h, 4);
                            dir4 = false;
                            break;
                        }
                    case 3:
                        if (dir8) {
                            picked = true;
                            theMaze[r][c][h] -= 8;
                            generateRec(r, c + 1, h, 8);
                            dir8 = false;
                            break;
                        }
                    case 4:
                        if (dir16) {
                            picked = true;
                            theMaze[r][c][h] -=16;
                            generateRec(r, c, h - 1, 16);
                            dir16 = false;
                            break;
                        }
                    case 5:
                        if (dir32) {
                            picked = true;
                            theMaze[r][c][h] -= 32;
                            generateRec(r, c, h + 1, 32);
                            dir32 = false;
                            break;
                        }
                }
            } while (!picked);
        }
        // base case2n cont'd: no more walkable directions left
        return;
    }
    
     // toString: returns maze data
    // ---------------------------
    public String toString() {
        int noRows = theMaze.length;
        int noColumns = theMaze[0].length;
        int noHeight = theMaze[0][0].length;
        
        String s = "";
        for (int r = 0; r < noRows; r++) {
            for (int c = 0; c < noColumns; c++) {
                for (int h = 0; h < noHeight; h++){
                    s = s + theMaze[r][c][h] + " ";
                }
                s = s + "\n";
            }
            s = s + "..";
        }
        return (s);
    }   
    
    //Utility Functions
    public byte getMazeData(int r, int c, int h) {
        return (theMaze[r][c][h]);
    }
    
    public boolean hasNorthWall(int r, int c, int h) {
        return ((getMazeData(r, c, h) & 1) != 0);
    }
    
    public boolean hasWestWall(int r, int c, int h) {
        return ((getMazeData(r, c, h) & 2) != 0);
    }
    
    public boolean hasSouthWall(int r, int c, int h) {
        return ((getMazeData(r, c, h) & 4) != 0);
    }
    
    public boolean hasEastWall(int r, int c, int h) {
        return ((getMazeData(r, c, h) & 8) != 0);
    }
    
    public boolean hasRoof (int r, int c, int h){
        return ((getMazeData(r, c, h) & 16) != 0);
    }
    
    public boolean hasFloor (int r, int c, int h){
        return ((getMazeData(r, c, h) & 32) != 0);
    }
    
    public boolean isPrison(int r, int c, int h) {
        return ((getMazeData(r, c, h) & 64) != 0);
        
    }
    
    public boolean isPacStart(int r, int c, int h){
        return ((getMazeData(r, c, h) & 128) != 0);
    }
    
    public boolean isWarpWall(int r, int c, int h){
        return ((getMazeData(r,c,h) & 256) != 0);
    }
    
    public int getDepth() {
        return (theMaze.length);
    }
    
    public int getWidth() {
        return (theMaze[0].length);
    }
    
    public int getHeight() {
        return (theMaze[0][0].length);
    }
}