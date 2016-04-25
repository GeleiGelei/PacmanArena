
package client;

import com.jme3.app.SimpleApplication;
import com.jme3.light.AmbientLight;
import com.jme3.light.DirectionalLight;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;

/**
 *
 * @author parth
 */
public class JmeInit {
    SimpleApplication sa;
    public static Material mat, mat1;
    
    public JmeInit(SimpleApplication sa){
        this.sa = sa;
        initGui();
        initMaterials();
        initLightandShadow();
    }
    
    private void initGui() {
        sa.setDisplayFps(false);
        sa.setDisplayStatView(false);
    }
    
    private void initMaterials() {
        mat = new Material(sa.getAssetManager(), "Common/MatDefs/Light/Lighting.j3md");
        mat.setBoolean("UseMaterialColors", true);
        mat.setColor("Ambient", ColorRGBA.Black);
        mat.setColor("Diffuse", ColorRGBA.Blue);
        mat.setColor("Specular", ColorRGBA.White);
        mat.setFloat("Shininess", 12f); // shininess from 1-128

        mat1 = new Material(sa.getAssetManager(), "Common/MatDefs/Light/Lighting.j3md");
        mat1.setBoolean("UseMaterialColors", true);
        mat1.setColor("Ambient", ColorRGBA.Black);
        mat1.setColor("Diffuse", ColorRGBA.Black);
        mat1.setColor("Specular", ColorRGBA.Gray);
        mat1.setFloat("Shininess", 2f); // shininess from 1-128
    }
    
    private void initLightandShadow() {
        // Light1: white, directional
        DirectionalLight sun = new DirectionalLight();
        sun.setDirection((new Vector3f(-0.5f, -0.7f, 0.9f)).normalizeLocal());
        sun.setColor(ColorRGBA.White);
        sa.getRootNode().addLight(sun);

        // Light2: white, directional
        DirectionalLight sun2 = new DirectionalLight();
        sun2.setDirection((new Vector3f(0.5f, -0.7f, -0.9f)).normalizeLocal());
        sun2.setColor(ColorRGBA.White);
        sa.getRootNode().addLight(sun2);

        // Light 3: Ambient, gray
        AmbientLight ambient = new AmbientLight();
        ambient.setColor(new ColorRGBA(0.3f, 0.3f, 0.3f, 1.0f));
        sa.getRootNode().addLight(ambient);
    }
}
