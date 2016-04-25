package client;

import messages.Player;
import server.GameCubeMaze;
import com.jme3.app.DebugKeysAppState;
import com.jme3.app.SimpleApplication;
import com.jme3.app.StatsAppState;
import com.jme3.bounding.BoundingVolume;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.collision.PhysicsCollisionListener;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.collision.CollisionResults;
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
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.network.Message;
import com.jme3.niftygui.NiftyJmeDisplay;
import com.jme3.post.FilterPostProcessor;
import com.jme3.post.filters.BloomFilter;
import com.jme3.scene.CameraNode;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.control.CameraControl;
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
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.concurrent.Callable;
import messages.CheeseConsumptionMessage;
import messages.InitMazeMessage;
import messages.NewClientFinalize;
import messages.NewClientMessage;
import messages.PlayerDisconnectMessage;
import messages.PositionMessage;
import messages.RespawnMessage;
import messages.RotationMessage;
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
    boolean rotateLeft = false;
    boolean rotateRight = false;
    Maze clientMaze;
    boolean mazeCreated;
    boolean initPlayerCalled = false;
    int posUpdate = 0;
    
    Cheese[] chs = new Cheese[1200];
    ArrayList mazeCells;
    
//    private RigidBodyControl wallPhysics;
    BulletAppState bullet;
    
    //list of players and corresponding characters (pacman or ghost) 
    public ArrayList gamePlayers;
    public ArrayList gamePlayerCharacters;

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
        
        this.mazeCells = new ArrayList();
        this.gamePlayers = new ArrayList();
        this.gamePlayerCharacters = new ArrayList();
        this.player = null;
        this.pac = null;
        this.ghost = null;
        
        bullet = new BulletAppState();
        stateManager.attach(bullet);
        
        // Initialize the sky, lights, keys, and NiftyGui
        createSkybg();
        initLightandShadow();
        initMaterial();
        initKeys();
        
        
        initGui();
        initNifty();
        
        //generate a temporary maze until we figure out which one to use
        mazeCreated = false;
        clientMaze = new Maze(40, 30, false);
        
        //creates a new ClientNetworkHandler and connects to the server
        connect();
        //buildMaze(10,5);
        
        //wallPhysics = new RigidBodyControl(0.0f);
        
    }
    
    private void buildMaze() {
        mazeNode = new Node();
        System.out.println("chs length " + chs.length);
        // create maze cells
        int i = 0;
        for (int r = 0; r < clientMaze.getRows(); r++) {
            for (int c = 0; c < clientMaze.getCols(); c++) {
                chs[i] = new Cheese(this);
                MazeCell mc = new MazeCell(clientMaze, r, c, this, chs[i], this);
                this.mazeCells.add(mc);
                mazeNode.attachChild(mc);
                mazeNode.attachChild(chs[i]);
                i++;
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

        rootNode.attachChild(mazeNode);
        mazeCreated = true;
    }

    // -------------------------------------------------------------------------
    // This client received its InitialClientMessage.
    private void initGame(NewClientMessage msg) {
        
        this.ID = msg.ID;
        System.out.println("My ID: " + this.ID);
        
        //update the player data
        gamePlayers = msg.gameWorldData;
        System.out.println("Updated player list: " + gamePlayers.toString());
        
        Player p = (Player)msg.gameWorldData.get(msg.ID);
        if(p.getId() == msg.ID) {
            if(this.player != null) {
                if(this.player.getId() == msg.ID) {
                    this.player = p;
                    System.out.println("Updated player: " + this.player.getId());
                }
            } else {
                this.player = p;
                System.out.println("New player: " + this.player.getId());
            }

        }
        
        if(this.player.getCharacter() != null) {
            System.out.println("Player finalized: " + this.player.getName() + " who is " + this.player.getCharacter() + " named " + this.player.getCharacterName());
        }
        
        //update player count on the start screen 
        if(this.nifty.getCurrentScreen().getScreenId().equals("start")) {
            nifty.getScreen("start").findNiftyControl("playersConnected", Label.class).setText(msg.gameWorldData.size() + " players connected");
        }
        
        if(initPlayerCalled) {
            if(this.player.getCharacter().toLowerCase().equals("pacman")) {
                if(this.pac == null) {
                    this.player.setMovementSpeed(14);

                    this.pac = new Pacman(this, this.player);
                    rootNode.attachChild(this.pac);

                    //add to character list 
                    gamePlayerCharacters.add(this.player.getId(), this.pac);
                }
            } else {
                if(this.ghost == null) {
                    this.player.setMovementSpeed(10);
                    this.ghost = new Ghost(this, this.player);
                    rootNode.attachChild(this.ghost);

                    //add to character list 
                    gamePlayerCharacters.add(this.player.getId(), this.ghost);
                }
            }
            initCam();
        }
        
        //add new player models to the game if it hasn't been done already 
        for(int k = 0; k < gamePlayers.size(); k++) {
            Player temp = (Player)gamePlayers.get(k);
            
            boolean create = true;
            for(int j = 0; j < gamePlayerCharacters.size(); j++) {
                if(((Character)gamePlayerCharacters.get(j)).getClientId() == temp.getId()) {
                    create = false;
                } 
            }
            
            if(temp.getCharacter() == null) {
                create = false;
            }
            
            if(create) {
                System.out.println("Creating new game object for: " + temp.getName());
                final Character newChar;
                switch(temp.getCharacterIndex()) {
                    case 0: //pacman
                        newChar = new Pacman(this, temp);
                        this.gamePlayerCharacters.add(newChar);
                        this.enqueue(new Callable<Spatial>() {
                            public Spatial call() throws Exception {
                                rootNode.attachChild((Node)newChar);
                                return (Node)newChar;
                            }
                        });
                        break;
                    case 1:
                    case 2:                      
                    case 3:                      
                    case 4:
                        newChar = new Ghost(this, temp);
                        this.gamePlayerCharacters.add(newChar);
                        this.enqueue(new Callable<Spatial>() {
                            public Spatial call() throws Exception {
                                rootNode.attachChild((Node)newChar);
                                return (Node)newChar;
                            }
                        });
                        break;
                    default: System.out.println("Error in initGame");
                }
            }
        }
    }
    
    public void initPlayer(String playerName, String playerCharacter, String playerCharacterName, int characterIndex) {
        NewClientFinalize ncf = new NewClientFinalize(this.player.getId(), playerName, playerCharacter, playerCharacterName, characterIndex, clientMaze.getMazeBytes());
        
        initPlayerCalled = true;
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
        screen.width *= 0.85;
        screen.height *= 0.85;
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
        this.getFlyByCamera().setEnabled(false);
        
        final CameraNode camNode = new CameraNode("Camera Node", cam);
        camNode.setControlDir(CameraControl.ControlDirection.SpatialToCamera);

        // Checks to see whether its pacman or ghost
        if(player.getCharacterIndex() == 0){
            this.enqueue(new Callable<Spatial>() {
                public Spatial call() throws Exception {
                    pac.pacNode.attachChild(camNode);
                    return null;
                }
            });
            
            camNode.setLocalTranslation(new Vector3f(0, 12, -12));
            camNode.lookAt(this.pac.getLocalTranslation(), Vector3f.UNIT_Y);
        }
        else {
            this.enqueue(new Callable<Spatial>() {
                public Spatial call() throws Exception {
                    ghost.ghostNode.attachChild(camNode);
                    return null;
                }
            });
            
            camNode.setLocalTranslation(new Vector3f(0, 12, -12));
            camNode.lookAt(this.ghost.getLocalTranslation(), Vector3f.UNIT_Y);
        }

        Quaternion XQuat = new Quaternion();
        XQuat.fromAngleAxis(35 * FastMath.DEG_TO_RAD, Vector3f.UNIT_X);
        camNode.setLocalRotation(XQuat);
        
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
        //mat.getAdditionalRenderState().setWireframe(true);

        mat1 = new Material(assetManager, "Common/MatDefs/Light/Lighting.j3md");
        mat1.setBoolean("UseMaterialColors", true);
        mat1.setColor("Ambient", ColorRGBA.Gray);
        mat1.setColor("Diffuse", ColorRGBA.Black);
        mat1.setColor("Specular", ColorRGBA.Gray);
        mat1.setFloat("Shininess", 2f); // shininess from 1-128
    }

    // -------------------------------------------------------------------------
    
    // Keyboard input
    private void initKeys() {
        inputManager.addMapping("moveForward", new KeyTrigger(KeyInput.KEY_I));
        inputManager.addMapping("moveLeft", new KeyTrigger(KeyInput.KEY_J));
        inputManager.addMapping("moveRight", new KeyTrigger(KeyInput.KEY_L));
    
        inputManager.addListener(analogListener,"moveForward");
        inputManager.addListener(actionListener, "moveLeft", "moveRight", "moveForward");
    }
    
    private ActionListener actionListener = new ActionListener() {
    public void onAction(String name, boolean keyPressed, float tpf) {
        
        if(name.equals("moveLeft")){
            if (keyPressed) {
                rotateLeft = true;
            } else {
                rotateLeft = false;
            }
        }
        
        if(name.equals("moveRight")){
            if (keyPressed) {
                rotateRight = true;
            } else {
                rotateRight = false;
            }
        }
        
        if(name.equals("moveForward")) {
            if(player.getCharacter().toLowerCase().equals("pacman") && !keyPressed) {
                pac.setMoving(false);
                pac.toggleMoveAnimation();
            }
        }
    }
    };
    
    private AnalogListener analogListener = new AnalogListener() {
        public void onAnalog(String name, float value, float tpf) {
            speed = player.getMovementSpeed();

            if (name.equals("moveForward")) {
                if(player.getCharacterIndex() == 0) {
                    Vector3f forward = pac.getLocalRotation().mult(Vector3f.UNIT_Z);
                    pac.move(forward.mult(tpf).mult(speed));
                    pac.setMoving(true);
                    pac.toggleMoveAnimation();

                    //build the position message and send to the server
                    PositionMessage pm = new PositionMessage((pac == null) ? ghost.getLocalTranslation() : 
                            pac.getLocalTranslation(), player.getId());
                    networkHandler.send(pm);
                } else {
                    Vector3f forward = ghost.getLocalRotation().mult(Vector3f.UNIT_Z);
                    ghost.move(forward.mult(tpf).mult(speed));
                    //ghost.moveGhost();

                    //build the position message and send to the server
                    PositionMessage pm = new PositionMessage((ghost == null) ? pac.getLocalTranslation() : 
                            ghost.getLocalTranslation(), player.getId());
                    networkHandler.send(pm);
                }   
            }
        }
    };

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
                    if(((Player)gamePlayers.get(i)).getId() == pdm.getId()) {
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
                final InitMazeMessage imm = (InitMazeMessage)msg;
                this.enqueue(new Callable<Spatial>() {
                    public Spatial call() throws Exception {
                        clientMaze = new Maze(imm.getMazeData());
                        buildMaze();
                        return null;
                    }
                });
            }
        } else if(msg instanceof PositionMessage) {
            //System.out.println("Position message recieved");
            if(mazeCreated) {
                final PositionMessage pm = (PositionMessage)msg;
                if(pm.getId() != player.getId()) {
                    this.enqueue(new Callable<Spatial>() {
                        public Spatial call() throws Exception {
                            ((Node)gamePlayerCharacters.get(pm.getId())).setLocalTranslation(pm.getPos());
                            if(((Player)gamePlayers.get(pm.getId())).getCharacter().toLowerCase().equals("pacman")) {
                                ((Pacman)gamePlayerCharacters.get(pm.getId())).setMoving(true);
                                ((Pacman)gamePlayerCharacters.get(pm.getId())).toggleMoveAnimation();
                            }
                            return ((Node)gamePlayerCharacters.get(pm.getId()));
                        }
                    });
                }
            }
        } else if(msg instanceof RotationMessage) {
            if(mazeCreated) {
                final RotationMessage rm = (RotationMessage)msg;
                if(rm.getId() != player.getId()) {
                    this.enqueue(new Callable<Spatial>() {
                        public Spatial call() throws Exception {
                            ((Node)gamePlayerCharacters.get(rm.getId())).setLocalRotation(rm.getRot());
                            return ((Node)gamePlayerCharacters.get(rm.getId()));
                        }
                    });

                }
            }
        } else if(msg instanceof RespawnMessage) {
            final RespawnMessage rm = (RespawnMessage)msg;
            if(mazeCreated && rm.getClientId() != this.player.getId()) {
                for(int i = 0; i < gamePlayerCharacters.size(); i++) {
                    if(((Character)gamePlayerCharacters.get(i)).getClientId() == rm.getClientId()) {
                        final Node temp = (Node)gamePlayerCharacters.get(i);
                        this.enqueue(new Callable<Spatial>() {
                            public Spatial call() throws Exception {
                                temp.setLocalTranslation(rm.getRespawnLoc());
                                return null;
                            }
                        });
                        
                        break;
                    }
                }
            }
        } else if(msg instanceof CheeseConsumptionMessage) {
            final CheeseConsumptionMessage ccm = (CheeseConsumptionMessage)msg;
            this.enqueue(new Callable<Spatial>() {
                public Spatial call() throws Exception {
                    mazeNode.detachChild(chs[ccm.getCheeseIndex()]);
                    return null;
                }
            });
        }
    }  
    
    @Override
    public void simpleUpdate(float tpf) {
        if(rotateLeft){
            if(this.player.getCharacter().toLowerCase().equals("pacman")) {
                pac.rotate(0, tpf/4, 0);
            } else {
                ghost.rotate(0, tpf/4, 0);
            }
            RotationMessage rm = new RotationMessage((this.pac == null) ? this.ghost.getLocalRotation() : 
                    this.pac.getLocalRotation(), this.player.getId());
            networkHandler.send(rm);
        }
        if(rotateRight){
            if(this.player.getCharacter().toLowerCase().equals("pacman")) {
                pac.rotate(0, -tpf/4, 0);
            } else {
                ghost.rotate(0, -tpf/4, 0);
            }
            RotationMessage rm = new RotationMessage((this.pac == null) ? this.ghost.getLocalRotation() : 
                    this.pac.getLocalRotation(), this.player.getId());
            networkHandler.send(rm);
        }
        
        if(mazeCreated && this.player.getCharacter().toLowerCase().equals("pacman")) {
            for(int i = 0; i < chs.length; i++) {
                Node temp = (Node)chs[i].getChild("cheeseNode");
                Vector3f tempLoc = temp.getWorldTranslation();
                float diff = tempLoc.distance(this.pac.getWorldTranslation());
                if(diff < 1.5f) {
                    CheeseConsumptionMessage ccm = new CheeseConsumptionMessage(i);
                    networkHandler.send(ccm);
                    mazeNode.detachChild(chs[i]);
                }
            }
        }
        
        if(mazeCreated) {
            for(int d = 0; d < gamePlayerCharacters.size(); d++) {
                if(this.pac != null) {
                    if(((Player)gamePlayers.get(d)).getCharacter().toLowerCase().equals("ghost")) {
                        Node temp = (Node)gamePlayerCharacters.get(d);
                        Vector3f tempLoc = temp.getLocalTranslation();
                        float diff = tempLoc.distance(this.pac.getLocalTranslation());
                        if(diff < 1.5f) {
                            if(((Player)gamePlayers.get(d)).hasVulnerability()) {
                                //increment pacman's points and send the ghost back to spawn
                            } else {
                                this.player.takeDamage();
                                //send pacman to spawn
                                this.pac.setLocalTranslation(0, 0, 0);
                                RespawnMessage rm = new RespawnMessage(new Vector3f(0, 0, 0), this.player.getId());
                                networkHandler.send(rm);
                                if(this.player.getLives() == 0) {
                                    //end screen + death message
                                }
                            }
                        }
                    }
                } else {
                    if(((Player)gamePlayers.get(d)).getCharacter().toLowerCase().equals("pacman")) {
                        Node temp = (Node)gamePlayerCharacters.get(d);
                        Vector3f tempLoc = temp.getLocalTranslation();
                        float diff = tempLoc.distance(this.ghost.getLocalTranslation());
                        if(diff < 1.5f) {
                            if(this.player.hasVulnerability()) {
                                //get sent to spawn
                            }
                        }
                    }
                }
            }
        }
//        if(mazeCreated) {
//            for(int l = 0; l < this.mazeCells.size(); l++) {
//                Node tempCell = (Node)mazeCells.get(l);
//                Vector3f tempCellLoc = tempCell.getWorldTranslation();
//                float cellDiff = tempCellLoc.distance((this.pac == null) ? 
//                        this.ghost.getLocalTranslation() : this.pac.getLocalTranslation());
//                if(cellDiff < 0.0005f) {
//                    System.out.println("Collided with: " + l + ", " + tempCellLoc);
//                }
//                //System.out.println("cell: " + tempCellLoc + ", pacman: " + this.pac.getLocalTranslation());
//            }
//        }
        //cheeseCollisionDetection();
    }

    public void onAction(String name, boolean isPressed, float tpf) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}