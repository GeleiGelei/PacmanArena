
package client;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.controls.Label;
import de.lessvoid.nifty.controls.TextField;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;
import com.jme3.app.Application;
import com.jme3.app.SimpleApplication;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.elements.render.TextRenderer;
import de.lessvoid.nifty.tools.SizeValue;
/**
 *
 * @author parth
 */
public class MyStartScreen extends AbstractAppState implements ScreenController{
    
    private Nifty nifty;
    private Screen screen;
    private GameClient main;
    private String inputName; 
    private SimpleApplication app;
    
    public MyStartScreen(GameClient main, Nifty nifty){
        this.nifty = nifty;
        this.main = main;
        inputName = "";
    }
    
    @Override
    public void initialize(AppStateManager stateManager, Application app) {
        super.initialize(stateManager, app);
        this.app=(SimpleApplication)app;
    }
    

    public void nextScreen(String nextScreen){
        nifty.gotoScreen(nextScreen);
        TextField text = screen.findNiftyControl("input", TextField.class);
        
        inputName = text.getText();
        System.out.println("inputName: " + inputName);
    }
    
    
//    // This officially starts the actual game
//    public void startGame(){
//        nifty.exit();
//    }

    public void bind(Nifty nifty, Screen screen) {
        this.nifty = nifty;
        this.screen = screen;
    }

    public void onStartScreen() {
        if(screen.getScreenId().equals("start")) {
            System.out.println("=== on the start screen ===");
            TextField text = screen.findNiftyControl("input", TextField.class);
            inputName = text.getText();
        }
        if(screen.getScreenId().equals("hud")){
            System.out.println("=== on the HUD screen ===");
            //System.out.println(inputName);
            Label l1 = screen.findNiftyControl("HUDtext", Label.class);
            l1.setText(inputName);
        }
    }

    public void setName(String inputName) {
        this.inputName = inputName;
        System.out.println(this.inputName);
    }
    
    public void onEndScreen() {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    // Quits the game entirely
    public void quitGame(){
        System.out.println("The entered name was: " + this.inputName);
        System.exit(0);
    }
    
    @Override
    public void update(float tpf) {
        if (screen.getScreenId().equals("hud")) {
            System.out.println("Time: " + tpf);
        }
    }
}