
package client;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;
import sun.applet.Main;
import de.lessvoid.nifty.controls.TextField;
/**
 *
 * @author parth
 */
public class MyStartScreen implements ScreenController{
    
    private Nifty nifty;
    private Screen screen;
    private GameClient main;
    private String inputName; 
    
    public MyStartScreen(GameClient main, Nifty nifty){
        this.nifty = nifty;
        this.main = main;
        inputName = "";
    }
    
    public void nextScreen(String nextScreen){
        nifty.gotoScreen(nextScreen);
        TextField text = screen.findNiftyControl("input", TextField.class);
        inputName = text.getText();
    }
    
    public void startGame(){
        nifty.exit();
    }

    public void bind(Nifty nifty, Screen screen) {
        this.nifty = nifty;
        this.screen = screen;
    }

    public void onStartScreen() {
        
    }

    public void setName(String inputName) {
        this.inputName = inputName;
        System.out.println(this.inputName);
    }
    
    public void onEndScreen() {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    public void quitGame(){
        System.out.println("The entered name was: " + this.inputName);
        System.exit(0);
    }
    
}