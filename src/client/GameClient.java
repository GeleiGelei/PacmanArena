package client;

import messages.Player;
import server.GameCubeMaze;
import com.jme3.app.DebugKeysAppState;
import com.jme3.app.SimpleApplication;
import com.jme3.app.StatsAppState;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.light.AmbientLight;
import com.jme3.light.DirectionalLight;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.network.Message;
import com.jme3.niftygui.NiftyJmeDisplay;
import com.jme3.post.FilterPostProcessor;
import com.jme3.post.filters.BloomFilter;
import com.jme3.scene.Spatial;
import com.jme3.shadow.DirectionalLightShadowRenderer;
import com.jme3.system.AppSettings;
import com.jme3.util.SkyFactory;
import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;
import java.awt.Dimension;
import java.awt.TextField;
import java.awt.Toolkit;
import messages.NewClientFinalize;
import messages.NewClientMessage;

public class GameClient extends SimpleApplication implements ClientNetworkListener, ActionListener {
    
    protected ClientNetworkHandler networkHandler;
    private GameCubeMaze cube;
    Nifty nifty;
    Screen screen;
    private Player player; 
    public static GameClient app;

    // -------------------------------------------------------------------------
    public static void main(String[] args) {
        System.out.println("Starting Client");
        //
        AppSettings aps = getAppSettings();
        //
        app = new GameClient();
        app.setShowSettings(false);
        app.setSettings(aps);
        app.start();
    }

    // -------------------------------------------------------------------------
    public GameClient() {
        // this constructor has no fly cam!
        super(new StatsAppState(), new DebugKeysAppState());
    }

    // -------------------------------------------------------------------------
    @Override
    public void simpleInitApp() {
        setPauseOnLostFocus(false);
        
        this.player = null;
        
        //creates a new ClientNetworkHandler and connects to the server
        connect();
        
        initGui();
        initNifty();
        
        initCam();
        initLightandShadow();
        initKeys();
    }

    // -------------------------------------------------------------------------
    // This client received its InitialClientMessage.
    private void initGame(NewClientMessage msg) {
        for(Player p : msg.gameWorldData) {
            if(p.getId() == msg.ID) {
                this.player = p;
                break;
            }
        }
        
        if(this.player.getCharacter() != null) {
            System.out.println("Player registered: " + this.player.getName() + " who is " + this.player.getCharacter().getCharacterClass());
        }
    }
    
    public void initPlayer(String playerName, Character playerCharacter) {
        NewClientFinalize ncf = new NewClientFinalize(this.player.getId(), playerName, playerCharacter);
        
        //send over the new player info
        networkHandler.send(ncf);
    }
    // -------------------------------------------------------------------------

    public void SimpleUpdate(float tpf) {
    }

    // -------------------------------------------------------------------------
    // Initialization Methods
    // -------------------------------------------------------------------------
    private static AppSettings getAppSettings() {
        AppSettings aps = new AppSettings(true);
        Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
        screen.width *= 0.75;
        screen.height *= 0.75;
        aps.setResolution(screen.width, screen.height);
        aps.setTitle("3D Multiplayer PacMan");
        return (aps);
    }

    // -------------------------------------------------------------------------
    private void initGui() {
        setDisplayFps(true);
        setDisplayStatView(false);
    }
    
    private void initNifty(){
        NiftyJmeDisplay niftyDisplay = new NiftyJmeDisplay(
                assetManager, inputManager, audioRenderer, guiViewPort);
        
        nifty = niftyDisplay.getNifty();
        //nifty.fromXml("Interface/start.xml", "start");
        guiViewPort.addProcessor(niftyDisplay);
        nifty.fromXml("Interface/start.xml", "start", new MyStartScreen(this, nifty));
        nifty.setDebugOptionPanelColors(false);
    }

    // -------------------------------------------------------------------------
    private void initLightandShadow() {
//        UNCOMMENT THIS IF WE DECIDE WE WANT THIS TYPE OF LIGHTING, NOT NECESSARY FOR NOW
//        // Light1: white, directional
//        DirectionalLight sun = new DirectionalLight();
//        sun.setDirection((new Vector3f(-0.7f, -1.3f, -0.9f)).normalizeLocal());
//        sun.setColor(ColorRGBA.Gray);
//        rootNode.addLight(sun);
//
//        // Light 2: Ambient, gray
//        AmbientLight ambient = new AmbientLight();
//        ambient.setColor(new ColorRGBA(0.7f, 0.7f, 0.7f, 1.0f));
//        rootNode.addLight(ambient);
//
//        // SHADOW
//        // the second parameter is the resolution. Experiment with it! (Must be a power of 2)
//        DirectionalLightShadowRenderer dlsr = new DirectionalLightShadowRenderer(assetManager, 1024, 1);
//        dlsr.setLight(sun);
//        viewPort.addProcessor(dlsr);
    }

    // -------------------------------------------------------------------------
    private void initCam() {
        //flyCam.setEnabled(false);
    }
    
    // Keyboard input
    private void initKeys() {
    }

    // key action
    public void onAction(String name, boolean isPressed, float tpf) {
        if (isPressed) {
        }
    }

    //called after the user has clicked the 'connect' button in nifty gui
    public void connect() {
        // CONNECT TO SERVER!
        networkHandler = new ClientNetworkHandler(this);
    }
    
    // -------------------------------------------------------------------------
    // message received
    public void messageReceived(Message msg) {
        if (msg instanceof NewClientMessage) {
            NewClientMessage ncm = (NewClientMessage) msg;
            if(ncm.getErr().length() == 0) {
                //will be called twice - once for empty player object, once with real data
                initGame(ncm); 
            } else {
                //exit game, we can't connect
                if(this.player == null) {
                    System.out.println(ncm.getErr());
                    app.stop();
                    System.exit(1);
                }
            }
        }
    }

}