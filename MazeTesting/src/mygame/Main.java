package mygame;

import com.jme3.app.SimpleApplication;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.renderer.RenderManager;
import com.jme3.scene.Geometry;
import com.jme3.scene.shape.Box;
import com.jme3.material.Material;
import com.jme3.audio.AudioNode;
import com.jme3.input.MouseInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.MouseButtonTrigger;
import com.jme3.light.AmbientLight;
import com.jme3.light.DirectionalLight;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.debug.Grid;
import com.jme3.scene.debug.WireBox;
import com.jme3.scene.shape.Box;
import com.jme3.scene.shape.Sphere;
import com.jme3.shadow.DirectionalLightShadowRenderer;

/**
 * test
 *
 * @author normenhansen
 */
public class Main extends SimpleApplication {

    Material mat2;
    Material mat;
    private AudioNode audio_gun;
    private AudioNode background;
    SimpleApplication sa;
    GameCubeMaze maze = new GameCubeMaze();
    Node mazeNode = new Node();

    public static void main(String[] args) {
        Main app = new Main();
        app.start();
    }

    public Geometry attachWireBox(Vector3f pos, float size, ColorRGBA color) {
        Geometry g = new Geometry("wireframe cube", new WireBox(size, size, size));
        Material mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        mat.getAdditionalRenderState().setWireframe(true);
        mat.setColor("Color", color);
        g.setMaterial(mat);
        g.setLocalTranslation(pos);
        rootNode.attachChild(g);
        return g;
    }

    @Override
    public void simpleInitApp() {

        rootNode.attachChild(mazeNode);

        maze.generate(3, 3, 3);
        buildMaze();
        attachGrid(new Vector3f(0, 0, 0), 100, ColorRGBA.Cyan);
        flyCam.setMoveSpeed(20);
        Sphere s = new Sphere(32, 32, .1f);
        Geometry geom = new Geometry("Sphere", s);

        Material mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        mat.setColor("Color", ColorRGBA.Blue);
        geom.setMaterial(mat);

        rootNode.attachChild(geom);
        initLightandShadow();
        mazeNode.move(2, 2, 2);
        //initKeys();
        //initAudio();

    }

    private Geometry attachGrid(Vector3f pos, int size, ColorRGBA color) {
        Geometry g = new Geometry("wireframe grid", new Grid(size, size, 0.2f));
        Material mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        mat.getAdditionalRenderState().setWireframe(true);
        mat.setColor("Color", color);
        g.setMaterial(mat);
        g.center().move(pos);
        rootNode.attachChild(g);
        return g;
    }

    public void initAudio() {
        audio_gun = new AudioNode(assetManager, "Sounds/Gun.ogg", false);
        audio_gun.setPositional(false);
        audio_gun.setLooping(false);
        audio_gun.setVolume(2);
        rootNode.attachChild(audio_gun);

        background = new AudioNode(assetManager, "Sounds/back.ogg", false);
        background.setPositional(false);
        background.setLooping(true);
        background.setVolume(2);
        rootNode.attachChild(background);

        background.play();
    }

    public void initKeys() {
        inputManager.addMapping("Shoot", new MouseButtonTrigger(MouseInput.BUTTON_LEFT));
        inputManager.addListener(actionListener, "Shoot");
    }
    /**
     * Defining the "Shoot" action: Play a gun sound.
     */
    private ActionListener actionListener = new ActionListener() {
        @Override
        public void onAction(String name, boolean keyPressed, float tpf) {
            if (name.equals("Shoot") && !keyPressed) {
                audio_gun.playInstance(); // play each instance once!
            }
        }
    };

    private void initLightandShadow() {
        // Light1: white, directional
        DirectionalLight sun = new DirectionalLight();
        sun.setDirection((new Vector3f(-0.7f, -2.3f, 0.9f)).normalizeLocal());
        sun.setColor(ColorRGBA.Gray);
        rootNode.addLight(sun);

        // Light 2: Ambient, gray
        AmbientLight ambient = new AmbientLight();
        ambient.setColor(new ColorRGBA(7.9f, 7.9f, 7.9f, 50.0f));
        rootNode.addLight(ambient);

        // SHADOW
        // the second parameter is the resolution. Experiment with it! (Must be a power of 2)
        DirectionalLightShadowRenderer dlsr = new DirectionalLightShadowRenderer(assetManager, 4096, 2);
        dlsr.setLight(sun);
        viewPort.addProcessor(dlsr);
    }

    @Override
    public void simpleUpdate(float tpf) {
        listener.setLocation(cam.getLocation());
        listener.setRotation(cam.getRotation());
    }

    @Override
    public void simpleRender(RenderManager rm) {
        //TODO: add render code
    }

    public void buildMaze() {
        int depth = maze.getDepth();
        int width = maze.getWidth();
        int height = maze.getHeight();

        for (int i = 0; i < depth; i++) {
            for (int j = 0; j < width; j++) {
                for (int k = 0; k < height; k++) {
                    if (maze.hasSouthWall(i, j, k)) {
                        Wall temp = new Wall(sa, mat2);
                        temp.initGeometry(1);
                        temp.setPos(i, j, k);
                        mazeNode.attachChild(temp.wall);
                    }
                    if (maze.hasEastWall(i, j, k)) {
                        Wall temp = new Wall(sa, mat2);
                        temp.initGeometry(2);
                        temp.setPos(i, j, k);
                        mazeNode.attachChild(temp.wall);
                    }
                    if (maze.hasRoof(i, j, k)) {
                        Wall temp = new Wall(sa, mat2);
                        temp.initGeometry(3);
                        temp.setPos(i, j, k);
                        mazeNode.attachChild(temp.wall);
                    }
                }
            }
        }
    }
}