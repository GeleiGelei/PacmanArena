/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package client;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Iterator;
import static javax.swing.JFrame.EXIT_ON_CLOSE;

public class Maze {

    private final int DEFAULTCHAMBERSIZE = 14;
    //
    private byte[][] theMaze;    // the maze, containing chambers
    private JPanel mazePanel;
    private Container cp;
    private long count;

    // -------------------------------------------------------------------------
    // inner class MyPanel
    // -------------------------------------------------------------------------
    private class MyPanel extends JPanel {

        int chamberSize;

        public MyPanel() {
            // where to go now ?
            int noRows = theMaze.length;
            int noColumns = theMaze[0].length;

            chamberSize = DEFAULTCHAMBERSIZE;
            if (Math.max(noRows, noColumns) < 100) {
                chamberSize = DEFAULTCHAMBERSIZE * 2;
            } else if (Math.max(noRows, noColumns) > 200) {
                chamberSize = DEFAULTCHAMBERSIZE / 2;
            }

            setPreferredSize(new Dimension(noColumns * chamberSize, noRows * chamberSize));
        }

        public void paint(Graphics g) {
            int noRows = theMaze.length;
            int noColumns = theMaze[0].length;
            for (int r = 0; r < noRows; r++) {
                for (int c = 0; c < noColumns; c++) {
                    int w = theMaze[r][c];
                    int posX = c * chamberSize;
                    int posY = r * chamberSize;
                    if ((w & 0x20) != 0) {
                        g.setColor(Color.green);          // the exit
                    } else if ((w & 0x10) != 0) {
                        g.setColor(Color.red);            // path
                    } else {
                        g.setColor(Color.orange);         // default background
                    }
                    g.fillRect(posX, posY, chamberSize, chamberSize);

                    // the walls
                    g.setColor(Color.black);
                    int cs = chamberSize - 1;
                    if ((w & 1) == 1) {
                        g.drawLine(posX, posY, posX + cs, posY);
                    }
                    if ((w & 2) == 2) {
                        g.drawLine(posX, posY, posX, posY + cs);
                    }
                    if ((w & 4) == 4) {
                        g.drawLine(posX, posY + cs, posX + cs, posY + cs);
                    }
                    if ((w & 8) == 8) {
                        g.drawLine(posX + cs, posY, posX + cs, posY + cs);
                    }
                }
            }
        }
    }
    // -------------------------
    // Constructor
    // -------------------------

    public Maze(int r, int c, boolean show) {
        generate(r, c);  // generate maze
        if (show) {
            JFrame f = new JFrame("Maze2D");
            f.setDefaultCloseOperation(EXIT_ON_CLOSE);

            mazePanel = new MyPanel();
            f.add(mazePanel);
            f.pack();
            f.setVisible(true);
        }
    }

    public byte getMazeData(int r, int c) {
        return (theMaze[r][c]);
    }

    public boolean hasNorthWall(int r, int c) {
        return ((getMazeData(r, c) & 1) != 0);
    }

    public boolean hasWestWall(int r, int c) {
        return ((getMazeData(r, c) & 2) != 0);
    }

    public boolean hasSouthWall(int r, int c) {
        return ((getMazeData(r, c) & 4) != 0);
    }

    public boolean hasEastWall(int r, int c) {
        return ((getMazeData(r, c) & 8) != 0);
    }

    public boolean isExit(int r, int c) {
        return ((getMazeData(r, c) & 32) != 0);
    }

    public int getHeight() {
        return (theMaze.length);
    }

    public int getWidth() {
        return (theMaze[0].length);
    }

    // -------------------------
    // Maze Generation Entry
    // -------------------------
    public final void generate(int rows, int columns) {
        // initialize: all walls up, path=false => 0x0f
        theMaze = new byte[rows][columns];
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < columns; c++) {
                theMaze[r][c] = 0x0f;
            }
        }

        // pick a start point
        int startR = (int) Math.floor(Math.random() * rows);
        int startC = (int) Math.floor(Math.random() * rows);
        count = 0;

        // and generate
        generateRec(startR, startC, 0);
        // set exit point to 0,0
        theMaze[0][0] |= 0x20;      // 0x20: exit
        theMaze[0][0] &= 0xFE;      // remove North Wall   
        mazePanel = new MyPanel();      // create and add new panel
    }

    // -------------------------
    // Recursive Maze Generation
    // -------------------------
    private void generateRec(int r, int c, int direction) {
        // tear down wall towards source direction
        switch (direction) {
            case 1:
                theMaze[r][c] -= 4;
                break;
            case 2:
                theMaze[r][c] -= 8;
                break;
            case 4:
                theMaze[r][c] -= 1;
                break;
            case 8:
                theMaze[r][c] -= 2;
                break;
        }
        count++;        // another chamber processed.

        // where to go now ?
        int noRows = theMaze.length;
        int noColumns = theMaze[0].length;

        // base case 1: all chambers finished
        if (count == noRows * noColumns) {
            return;
        }

        // recursive case: while there are walkable directions: walk
        while (true) {
            // find walkable directions
            boolean dir1, dir2, dir4, dir8;
            dir1 = dir2 = dir4 = dir8 = false;
            if (r > 0 && (theMaze[r - 1][c] == 0x0f)) {
                dir1 = true;
            }
            if (c > 0 && (theMaze[r][c - 1] == 0x0f)) {
                dir2 = true;
            }
            if (r < noRows - 1 && (theMaze[r + 1][c] == 0x0f)) {
                dir4 = true;
            }
            if (c < noColumns - 1 && (theMaze[r][c + 1] == 0x0f)) {
                dir8 = true;
            }


            // base case 2: no walkable directions left
            if ((dir1 | dir2 | dir4 | dir8) == false) {
                break;
            }

            boolean picked = false;
            do {
                int d = (int) Math.floor(Math.random() * 4); // direction 0-3
                switch (d) {
                    case 0:
                        if (dir1) {
                            picked = true;
                            theMaze[r][c] -= 1;
                            generateRec(r - 1, c, 1);
                            dir1 = false;
                            break;
                        }
                    case 1:
                        if (dir2) {
                            picked = true;
                            theMaze[r][c] -= 2;
                            generateRec(r, c - 1, 2);
                            dir2 = false;
                            break;
                        }
                    case 2:
                        if (dir4) {
                            picked = true;
                            theMaze[r][c] -= 4;
                            generateRec(r + 1, c, 4);
                            dir4 = false;
                            break;
                        }
                    case 3:
                        if (dir8) {
                            picked = true;
                            theMaze[r][c] -= 8;
                            generateRec(r, c + 1, 8);
                            dir8 = false;
                            break;
                        }
                }
            } while (!picked);
        }
        // base case2n cont'd: no more walkable directions left
        return;
    }

    // ---------------------------
    // toString: returns maze data
    // ---------------------------
    public String toString() {
        int noRows = theMaze.length;
        int noColumns = theMaze[0].length;

        String s = "";
        for (int r = 0; r < noRows; r++) {
            for (int c = 0; c < noColumns; c++) {
                s = s + theMaze[r][c] + " ";
            }
            s = s + "\n";
        }
        return (s);
    }
}