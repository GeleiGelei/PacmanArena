
package client;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.controls.ImageSelect;
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
        //from start screen to start of game
        if(nextScreen.equals("hud")) {
            initPlayerData();
        }
        
        //nifty.gotoScreen(nextScreen);
    }
    
    public void initPlayerData() {
        System.out.println("initPlayerData called");
        
        TextField text = screen.findNiftyControl("input", TextField.class);
        inputName = text.getText();
        
        ImageSelect gameChar = screen.findNiftyControl("imageSelect", ImageSelect.class);
        int idx = gameChar.getSelectedImageIndex();
        
        Character selected;
        
        switch(idx) {
            case 0: //pacman
                selected = new Pacman();
                selected.setCharacterName("Pacman");
                break;
            case 1: //clyde
                selected = new Ghost();
                selected.setCharacterName("Clyde");
                break;
            case 2: //inky
                selected = new Ghost();
                selected.setCharacterName("Inky");
                break;
            case 3: //blinky
                selected = new Ghost();
                selected.setCharacterName("Blinky");
                break;
            case 4: //pinky 
                selected = new Ghost();
                selected.setCharacterName("Pinky");
                break;
            default: System.out.println("Error");
                selected = null;
        }
        
        System.out.println("We got this far...");
        //creates an instance of NewClientFinalize and sends message to server
        //main.initPlayer(inputName, selected);
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