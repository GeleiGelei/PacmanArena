
package client;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;
import sun.applet.Main;

/**
 *
 * @author parth
 */
public class MyStartScreen implements ScreenController{
    
    private Nifty nifty;
    private Screen screen;
    private GameClient main;
    
    public MyStartScreen(GameClient main, Nifty nifty){
        this.nifty = nifty;
        this.main = main;
    }
    
    public void nextScreen(String nextScreen){
        nifty.gotoScreen(nextScreen);
    }
    
    public void startGame(){
        nifty.exit();
    }

    public void bind(Nifty nifty, Screen screen) {
        this.nifty = nifty;
        this.screen = screen;
    }

    public void onStartScreen() {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void onEndScreen() {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    public void quitGame(){
        System.exit(0);
    }
    
}
