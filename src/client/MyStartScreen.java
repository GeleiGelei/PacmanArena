
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
import de.lessvoid.nifty.controls.ImageSelect;
import de.lessvoid.nifty.controls.ImageSelectSelectionChangedEvent;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.elements.render.ImageRenderer;
import de.lessvoid.nifty.elements.render.TextRenderer;
import de.lessvoid.nifty.render.NiftyImage;
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
    public static String connectedPlayers;
    private int indexOfSelectedCharacter;
    private SimpleApplication app;
    float timer;
    
    
    String[] charImages = {"Interface/pacman.png","Interface/clyde.png","Interface/inky.png","Interface/blinky.png","Interface/pinky.png"};
    
    public MyStartScreen(GameClient main, Nifty nifty){
        
        this.nifty = nifty;
        this.main = main;
        inputName = "";
        connectedPlayers = "";
        indexOfSelectedCharacter = 0;
        
    }
    
    @Override
    public void initialize(AppStateManager stateManager, Application app) {
        super.initialize(stateManager, app);
        this.app=(SimpleApplication)app;
    }
    
   public void goToScreen(String nextScreen){
        //from start screen to start of game
        nifty.gotoScreen(nextScreen);
    }
    
    public void initPlayerData() {
        inputName = nifty.getScreen("start").findNiftyControl("input", TextField.class).getText();
        
        String selected;
        String selectedName;
        
        switch(indexOfSelectedCharacter) {
            case 0: //pacman
                selected = "Pacman";
                selectedName = "Pacman";
                break;
            case 1: //clyde
                selected = "Ghost";
                selectedName = "Clyde";
                break;
            case 2: //inky
                selected = "Ghost";
                selectedName = "Inky";
                break;
            case 3: //blinky
                selected = "Ghost";
                selectedName = "Blinky";
                break;
            case 4: //pinky 
                selected = "Ghost";
                selectedName = "Pinky";
                break;
            default: System.out.println("Error");
                selected = null;
                selectedName = null;
        }
        
        System.out.println("inputName: " + inputName);
        //creates an instance of NewClientFinalize and sends message to server
        main.initPlayer(inputName, selected, selectedName);
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
            connectedPlayers = nifty.getScreen("start").findNiftyControl("playersConnected", Label.class).getText();
            //System.out.println("Connected players = " + connectedPlayers);
        }
        if(screen.getScreenId().equals("hud")){
            System.out.println("=== on the HUD screen ===");
            
            // Finds the name the user input
            inputName = nifty.getScreen("start").findNiftyControl("input", TextField.class).getText();
            
            // Sets the text on the HUD screen to the input name
            Label l1 = screen.findNiftyControl("HUDtext", Label.class);
            l1.setText(inputName);
            System.out.println("inputName: " + inputName);
            
            // Finds which character the user selected from the start screen
            ImageSelect userCharacter = nifty.getScreen("start").findNiftyControl("imS", ImageSelect.class);
            indexOfSelectedCharacter = userCharacter.getSelectedImageIndex();
            
            // Post the character they selected to the HUD screen
            NiftyImage img = nifty.createImage(charImages[indexOfSelectedCharacter], false);
            Element niftyElement = nifty.getCurrentScreen().findElementByName("HUDcharImage");
            niftyElement.getRenderer(ImageRenderer.class).setImage(img);
            
            initPlayerData();
        }
    }
    
    public void onEndScreen() {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    public void setName(String inputName) {
        this.inputName = inputName;
        System.out.println(this.inputName);
    }
    
    public void updateCharSelector(){
        
    }
    
    // Quits the game entirely
    public void quitGame(){
        app.stop();
    }
    
    @Override
    public void update(float tpf) {

        if (screen.getScreenId().equals("hud")) {
            timer += tpf;
            String formattedString = String.format("%.02f", timer);
            Element niftyElement = nifty.getCurrentScreen().findElementByName("timer");
            niftyElement.getRenderer(TextRenderer.class).setText("Time: " + (formattedString) + ""); 
        }
    }
}