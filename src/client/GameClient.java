package client;

import server.GameCubeMaze;
import com.jme3.app.DebugKeysAppState;
import com.jme3.app.SimpleApplication;
import com.jme3.app.StatsAppState;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.light.AmbientLight;
import com.jme3.light.DirectionalLight;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.network.Message;
import com.jme3.post.FilterPostProcessor;
import com.jme3.post.filters.BloomFilter;
import com.jme3.scene.Geometry;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Sphere;
import com.jme3.shadow.DirectionalLightShadowRenderer;
import com.jme3.system.AppSettings;
import com.jme3.util.SkyFactory;
import java.awt.Dimension;
import java.awt.Toolkit;
import messages.NewClientMessage;
import server.FieldData;

public class GameClient extends SimpleApplication implements ClientNetworkListener, ActionListener {

    private int ID = -1;
    protected ClientNetworkHandler networkHandler;
    private GameCubeMaze maze;
    SimpleApplication sa;

    // -------------------------------------------------------------------------
    public static void main(String[] args) {
        System.out.println("Starting Client");
        //
        AppSettings aps = getAppSettings();
        //
        GameClient app = new GameClient();

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
        //
        // CONNECT TO SERVER!
        networkHandler = new ClientNetworkHandler(this);
        //

        initGui();
        initCam();
        initLightandShadow();
        initKeys();

    }

    // -------------------------------------------------------------------------
    // This client received its InitialClientMessage.
    private void initGame(NewClientMessage msg) {
        System.out.println("Received initial message from server. Initializing playfield.");
        //
        // store ID
        this.ID = msg.ID;
        maze = msg.maze;
        //buildMaze();


        /**
         * *********************
         *
         * For Testing Purposes Begin
         *
         **********************
         */
        Sphere center = new Sphere(32, 32, 0.1f);
        Material mat = new Material(sa.getAssetManager(), "Common/MatDefs/Light/Lighting.j3md");
        mat.setBoolean("UseMaterialColors", true);
        mat.setColor("Ambient", ColorRGBA.Red.mult(0.1f));
        mat.setColor("Diffuse", ColorRGBA.White);
        mat.setColor("Specular", ColorRGBA.White);
        mat.setTexture("DiffuseMap", sa.getAssetManager().loadTexture("Textures/textureOldTable.jpg"));
        mat.setFloat("Shininess", 10f);
        Geometry cenGeo = new Geometry("center", center);
        cenGeo.setMaterial(mat);
        rootNode.attachChild(cenGeo);
        /**
         * *********************
         *
         * For Testing Purposes End
         *
         **********************
         */
    }
    // -------------------------------------------------------------------------

    public void buildMaze() {
        int depth = maze.getDepth();
        int width = maze.getWidth();
        int height = maze.getHeight();

        for (int i = 0; i < depth; i++) {
            for (int j = 0; j < width; j++) {
                for (int k = 0; k < height; k++) {
                    if (maze.hasSouthWall(i, j, k)) {
                        Wall temp = new Wall(sa);
                        temp.initGeometry(1);
                        temp.initMat();
                        rootNode.attachChild(temp);
                    }
                    if (maze.hasEastWall(i, j, k)) {
                        Wall temp = new Wall(sa);
                        temp.initGeometry(2);
                        temp.initMat();
                        rootNode.attachChild(temp);
                    }
                    if (maze.hasRoof(i, j, k)) {
                        Wall temp = new Wall(sa);
                        temp.initGeometry(3);
                        temp.initMat();
                        rootNode.attachChild(temp);
                    }
                }
            }
        }

    }

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
        return (aps);
    }

    // -------------------------------------------------------------------------
    private void initGui() {
        setDisplayFps(true);
        setDisplayStatView(false);
    }

    // -------------------------------------------------------------------------
    private void initLightandShadow() {
//        UNCOMMENT THIS IF WE DECIDE WE WANT THIS TYPE OF LIGHTING, NOT NECESSARY FOR NOW
        // Light1: white, directional
        DirectionalLight sun = new DirectionalLight();
        sun.setDirection((new Vector3f(-0.7f, -1.3f, -0.9f)).normalizeLocal());
        sun.setColor(ColorRGBA.Gray);
        rootNode.addLight(sun);

        // Light 2: Ambient, gray
        AmbientLight ambient = new AmbientLight();
        ambient.setColor(new ColorRGBA(0.7f, 0.7f, 0.7f, 1.0f));
        rootNode.addLight(ambient);

        // SHADOW
        // the second parameter is the resolution. Experiment with it! (Must be a power of 2)
        DirectionalLightShadowRenderer dlsr = new DirectionalLightShadowRenderer(assetManager, 1024, 1);
        dlsr.setLight(sun);
        viewPort.addProcessor(dlsr);
    }

    // -------------------------------------------------------------------------
    private void initCam() {
        flyCam.setEnabled(true);
        cam.setLocation(new Vector3f(2f, 3f, 4f));
        cam.lookAt(new Vector3f(0.5f, 1, -1f), Vector3f.UNIT_Y);
    }

    // -------------------------------------------------------------------------
    // Keyboard input
    private void initKeys() {
    }

    // key action
    public void onAction(String name, boolean isPressed, float tpf) {
        if (isPressed) {
        }
    }

    // -------------------------------------------------------------------------
    // message received
    public void messageReceived(Message msg) {

        System.out.println("messageReceived: initGame");
        if (msg instanceof NewClientMessage) {
            NewClientMessage ncm = (NewClientMessage) msg;
            if (this.ID == -1) {
                //starting the game fresh
                initGame(ncm);

//System.out.println("Maze[0][0][0] = " + maze.getMazeData(0, 0, 0));
            } else {
            }
        }
    }
}
