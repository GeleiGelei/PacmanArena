package client;

import messages.Player;
import server.GameCubeMaze;
import com.jme3.app.DebugKeysAppState;
import com.jme3.app.SimpleApplication;
import com.jme3.app.StatsAppState;
import com.jme3.font.BitmapText;
import com.jme3.input.ChaseCamera;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.AnalogListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.light.AmbientLight;
import com.jme3.light.DirectionalLight;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Vector3f;
import com.jme3.network.Message;
import com.jme3.niftygui.NiftyJmeDisplay;
import com.jme3.post.FilterPostProcessor;
import com.jme3.post.filters.BloomFilter;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Box;
import com.jme3.shadow.DirectionalLightShadowRenderer;
import com.jme3.system.AppSettings;
import com.jme3.util.SkyFactory;
import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.controls.Label;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;
import java.awt.Dimension;
import java.awt.TextField;
import java.awt.Toolkit;
import java.util.LinkedList;
import messages.InitMazeMessage;
import messages.NewClientFinalize;
import messages.NewClientMessage;
import messages.PlayerDisconnectMessage;
import messages.VulnerabilityMessage;

public class GameClient extends SimpleApplication implements ClientNetworkListener, ActionListener {
    
    protected ClientNetworkHandler networkHandler;
    private GameCubeMaze cube;
    Nifty nifty;
    Screen screen;
    Spatial sky;
    private Player player; 
    public static GameClient app;
    private int ID = -1;
    BitmapText text;
    int count = 0;
    float tpfSum;
    Node sNode, cameraTarget;
    Material mat, mat1;
    Geometry geomLarge, geomSmall, geomGround;
    Node mazeNode;
    Pacman pac;
    Ghost ghost;
    ChaseCamera camera;
    Maze clientMaze;
    boolean mazeCreated;
    
    //list of players and corresponding characters (pacman or ghost) 
    public LinkedList<Player> gamePlayers;
    public LinkedList<Character> gamePlayerCharacters;

    private MyStartScreen startScreen;

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

    public Player getPlayer() {
        return this.player;
    }
    
    // -------------------------------------------------------------------------
    public GameClient() {
        // this constructor has no fly cam!
        super();
        //super(new StatsAppState(), new DebugKeysAppState());
    }

    // -------------------------------------------------------------------------
    @Override
    public void simpleInitApp() {
        setPauseOnLostFocus(false);
        
        this.gamePlayers = new LinkedList<Player>();
        this.gamePlayerCharacters = new LinkedList<Character>();
        this.player = null;
        this.pac = null;
        //this.ghost = null;
        
        // Initialize the sky, lights, keys, and NiftyGui
        createSkybg();
        initLightandShadow();
        initMaterial();
        initKeys();
        initCam();
        initGui();
        initNifty();
        
        //generate a temporary maze until we figure out which one to use
        mazeCreated = false;
        clientMaze = new Maze(40, 30, false);
        
        //creates a new ClientNetworkHandler and connects to the server
        connect();
    }
    
    private void buildMaze() {
        mazeCreated = true;
        mazeNode = new Node();
        // create maze cells
        for (int r = 0; r < clientMaze.getRows(); r++) {
            for (int c = 0; c < clientMaze.getCols(); c++) {
                MazeCell mc = new MazeCell(clientMaze, r, c, this);
                mazeNode.attachChild(mc);
            }
        }
        
        int cols = clientMaze.getCols();
        int rows = clientMaze.getRows();
//        // add South Boundary
        Box south = new Box(cols * MazeCell.CELLSIZE / 2, MazeCell.WALLHEIGHT, MazeCell.WALLTHICKNESS);
        Geometry southWall = new Geometry("sw", south);
        southWall.setMaterial(mat);
        southWall.setLocalTranslation((cols - 1) * MazeCell.CELLSIZE / 2, MazeCell.WALLHEIGHT, rows * MazeCell.CELLSIZE - MazeCell.CELLSIZE / 2);
        mazeNode.attachChild(southWall);
//
//        // add East Boundary
        Box east = new Box(MazeCell.WALLTHICKNESS, MazeCell.WALLHEIGHT, rows * MazeCell.CELLSIZE / 2);
        Geometry eastWall = new Geometry("ew", east);
        eastWall.setMaterial(mat);
        eastWall.setLocalTranslation(cols * MazeCell.CELLSIZE - MazeCell.CELLSIZE / 2, MazeCell.WALLHEIGHT, (rows - 1) * MazeCell.CELLSIZE / 2);
        mazeNode.attachChild(eastWall);
//
//        // add floor
        Box groundMesh = new Box(cols * MazeCell.CELLSIZE / 2, MazeCell.CELLSIZE / 10, rows * MazeCell.CELLSIZE / 2);
        Geometry floor = new Geometry("floor", groundMesh);
        floor.setMaterial(mat1);
        floor.setLocalTranslation((cols - 1) * MazeCell.CELLSIZE / 2, -MazeCell.CELLSIZE / 10, (rows - 1) * MazeCell.CELLSIZE / 2);
        mazeNode.attachChild(floor);


        // translate maze such that center is in 0 0
//        float transX = cols * MazeCell.CELLSIZE / 2f;
//        float transZ = rows * MazeCell.CELLSIZE / 2f;
//        mazeNode.setLocalTranslation(-transX, 0f, -transZ);
        rootNode.attachChild(mazeNode);
    }

    // -------------------------------------------------------------------------
    // This client received its InitialClientMessage.
    private void initGame(NewClientMessage msg) {
        
        this.ID = msg.ID;
        System.out.println("My ID: " + this.ID);
        
        //update the player data
        gamePlayers = msg.gameWorldData;
        System.out.println("Updated player list: " + gamePlayers.toString());
        for(Player p : msg.gameWorldData) {
            
            if(p.getId() == msg.ID) {
                if(this.player != null) {
                    if(this.player.getId() == msg.ID) {
                        this.player = p;
                        System.out.println("Updated player: " + this.player.getId());
                        break;
                    }
                } else {
                    this.player = p;
                    System.out.println("New player: " + this.player.getId());
                    break;
                }
                
            }
        }
        
        if(this.player.getCharacter() != null) {
            System.out.println("Player finalized: " + this.player.getName() + " who is " + this.player.getCharacter() + " named " + this.player.getCharacterName());
        }
        
        //update player count on the start screen 
        if(this.nifty.getCurrentScreen().getScreenId().equals("start")) {
            nifty.getScreen("start").findNiftyControl("playersConnected", Label.class).setText(msg.gameWorldData.size() + " players connected");
        }
        
        //add new player models to the game if it hasn't been done already 
        for(Player temp : gamePlayers) {
            boolean create = true;
            for(int j = 0; j < gamePlayerCharacters.size(); j++) {
                if(gamePlayerCharacters.get(j).getClientId() == temp.getId()) {
                    create = false;
                } 
            }
            
            if(create) {
                switch(temp.getCharacterIndex()) {
                    case 0: //pacman
                        this.gamePlayerCharacters.add(new Pacman(this, temp));
                        break;
                    case 1: //clyde
                        this.gamePlayerCharacters.add(new Ghost(this, temp));
                        break;
                    case 2: //inky
                        this.gamePlayerCharacters.add(new Ghost(this, temp));
                        break;
                    case 3: //blinky
                        this.gamePlayerCharacters.add(new Ghost(this, temp));
                        break;
                    case 4: //pinky
                        this.gamePlayerCharacters.add(new Ghost(this, temp));
                        break;
                    default: System.out.println("Error in initGame");
                }
            }
        }
    }
    
    public void initPlayer(String playerName, String playerCharacter, String playerCharacterName, int characterIndex) {
        NewClientFinalize ncf = new NewClientFinalize(this.player.getId(), playerName, playerCharacter, playerCharacterName, characterIndex, clientMaze.getMazeBytes());
        
        System.out.println("player list: " + gamePlayers + ", currentPlayer: " + this.player.getId());
        if(playerCharacter.toLowerCase().equals("pacman")) {
            this.pac = new Pacman(this, this.player);
            //initialize pacman, create him
            
            //add to character list 
            gamePlayerCharacters.add(this.pac);
        } else {
            this.ghost = new Ghost(this, this.player);
            
            //initialize pacman, create him
            
            //add to character list 
            gamePlayerCharacters.add(this.ghost);
        }
        
        System.out.println("character list: " + gamePlayerCharacters);
        
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
        setDisplayFps(false);
        setDisplayStatView(false);
    }
    
    private void initCam(){
        this.getFlyByCamera().setMoveSpeed(30);
        //flyCam.setEnabled(false);
        //camera = new ChaseCamera(cam,pac.getGeo(),this.getInputManager());
        //camera.setDragToRotate(false);
        //camera.setEnabled(true);
    }
    
    private void initNifty(){
        startScreen = new MyStartScreen(this,nifty);
        stateManager.attach(startScreen);
        
        NiftyJmeDisplay niftyDisplay = new NiftyJmeDisplay(
                assetManager, inputManager, audioRenderer, guiViewPort);
        
        nifty = niftyDisplay.getNifty();
        //nifty.fromXml("Interface/start.xml", "start");
        guiViewPort.addProcessor(niftyDisplay);
        nifty.fromXml("Interface/start.xml", "start", startScreen);
        nifty.setDebugOptionPanelColors(false);

    }
    
    public void createSkybg(){
        sky = SkyFactory.createSky(assetManager, "Interface/spaceBg.jpg", true);
        rootNode.attachChild(sky);
    }

    // -------------------------------------------------------------------------
    private void initLightandShadow() {
        // Light1: white, directional
        DirectionalLight sun = new DirectionalLight();
        sun.setDirection((new Vector3f(-0.5f, -0.7f, 0.9f)).normalizeLocal());
        sun.setColor(ColorRGBA.White);
        rootNode.addLight(sun);

        // Light2: white, directional
        DirectionalLight sun2 = new DirectionalLight();
        sun2.setDirection((new Vector3f(0.5f, -0.7f, -0.9f)).normalizeLocal());
        sun2.setColor(ColorRGBA.White);
        rootNode.addLight(sun2);

        // Light 3: Ambient, gray
        AmbientLight ambient = new AmbientLight();
        ambient.setColor(new ColorRGBA(0.3f, 0.3f, 0.3f, 1.0f));
        rootNode.addLight(ambient);
    }
    
    private void initMaterial() {
        mat = new Material(assetManager, "Common/MatDefs/Light/Lighting.j3md");
        mat.setBoolean("UseMaterialColors", true);
        mat.setColor("Ambient", ColorRGBA.Black);
        mat.setColor("Diffuse", ColorRGBA.Blue);
        mat.setColor("Specular", ColorRGBA.White);
        mat.setFloat("Shininess", 12f); // shininess from 1-128

        mat1 = new Material(assetManager, "Common/MatDefs/Light/Lighting.j3md");
        mat1.setBoolean("UseMaterialColors", true);
        mat1.setColor("Ambient", ColorRGBA.Black);
        mat1.setColor("Diffuse", ColorRGBA.Black);
        mat1.setColor("Specular", ColorRGBA.Gray);
        mat1.setFloat("Shininess", 2f); // shininess from 1-128
    }

    // -------------------------------------------------------------------------
    
    // Keyboard input
    private void initKeys() {
        inputManager.addMapping("moveForward", new KeyTrigger(KeyInput.KEY_I));
        inputManager.addMapping("moveBackward", new KeyTrigger(KeyInput.KEY_K));
        inputManager.addMapping("moveLeft", new KeyTrigger(KeyInput.KEY_J));
        inputManager.addMapping("moveRight", new KeyTrigger(KeyInput.KEY_L));
    
        inputManager.addListener(analogListener,"moveForward", "moveBackward", "moveLeft", "moveRight");
    }
    
    private AnalogListener analogListener = new AnalogListener() {
    public void onAnalog(String name, float value, float tpf) {
        
        speed = 5;
        
        if (name.equals("moveForward")) {
            Vector3f v = pac.getLocalTranslation();
            pac.setLocalTranslation(v.x, v.y, v.z + value*speed);
        }
        if (name.equals("moveBackward")) {
            Vector3f v = pac.getLocalTranslation();
            pac.setLocalTranslation(v.x, v.y, v.z - value*speed);
        }
        if (name.equals("moveLeft")) {
            Vector3f v = pac.getLocalTranslation();
            pac.setLocalTranslation(v.x + value*speed, v.y, v.z);
        }
        if (name.equals("moveRight")) {
            Vector3f v = pac.getLocalTranslation();
            pac.setLocalTranslation(v.x - value*speed, v.y, v.z);
        }
    }
    };

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
        } else if(msg instanceof PlayerDisconnectMessage) {
            //update player count on the start screen 
            if(this.nifty.getCurrentScreen().getScreenId().equals("start")) {
                PlayerDisconnectMessage pdm = (PlayerDisconnectMessage)msg;
                nifty.getScreen("start").findNiftyControl("playersConnected", Label.class).setText(pdm.getNumPlayers() + " players connected");
                
                //find the disconnected player in the list of players
                int pIdx = -1;
                for(int i = 0; i < gamePlayers.size(); i++) {
                    if(gamePlayers.get(i).getId() == pdm.getId()) {
                        pIdx = i;
                        break;
                    }
                }
                
                //remove the player data and corresponding character object from the game 
                if(pIdx != -1) {
                    gamePlayers.remove(pIdx);
                    rootNode.detachChild((Node)gamePlayerCharacters.get(pIdx));
                    gamePlayerCharacters.remove(pIdx);
                }
            }
        } else if(msg instanceof VulnerabilityMessage) {
            VulnerabilityMessage vm = (VulnerabilityMessage)msg;
            if(this.player.getCharacter().toLowerCase().equals("ghost")) {
                this.player.setVulnerability(vm.getVulnerability());
                startScreen.toggleVulnerabilityGraphics(vm.getVulnerability());
                this.ghost.toggleVulnerability(vm.getVulnerability());
            }
        } else if(msg instanceof InitMazeMessage) {
            if(!mazeCreated) {
                InitMazeMessage imm = (InitMazeMessage)msg;
                this.clientMaze = new Maze(imm.getMazeData());
                buildMaze();
            }
        }
    }
    
    
    @Override
    public void simpleUpdate(float tpf) {
        //System.out.println(pac.getLocalTranslation());
    }
}