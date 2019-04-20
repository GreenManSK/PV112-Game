package net.greenmanov.muni.fi.pv112.kashima;

import com.jogamp.newt.event.WindowAdapter;
import com.jogamp.newt.event.WindowEvent;
import com.jogamp.newt.opengl.GLWindow;
import com.jogamp.opengl.*;
import com.jogamp.opengl.util.Animator;
import net.greenmanov.muni.fi.pv112.kashima.lights.DirLight;
import net.greenmanov.muni.fi.pv112.kashima.lights.PointLight;
import net.greenmanov.muni.fi.pv112.kashima.lights.SpotLight;
import net.greenmanov.muni.fi.pv112.kashima.materials.Materials;
import net.greenmanov.muni.fi.pv112.kashima.models.Models;
import net.greenmanov.muni.fi.pv112.kashima.opengl.MVPCanvas;
import net.greenmanov.muni.fi.pv112.kashima.opengl.camera.MovingCamera;
import net.greenmanov.muni.fi.pv112.kashima.opengl.drawable.Object3D;
import net.greenmanov.muni.fi.pv112.kashima.opengl.program.CanvasProgram;
import net.greenmanov.muni.fi.pv112.kashima.opengl.program.Program;
import net.greenmanov.muni.fi.pv112.kashima.textures.Textures;
import org.joml.Matrix4f;
import org.joml.Vector3f;

import java.util.logging.Level;
import java.util.logging.Logger;

import static com.jogamp.opengl.GL.*;

/**
 * Class Main
 *
 * @author Lukáš Kurčík <lukas.kurcik@gmail.com>
 */
public class Main implements GLEventListener {

    private static final Logger LOGGER = Logger.getLogger(Main.class.getName());

    private static final String WINDOW_TITLE = "Project Kashima";
    private static final int WIDTH = 800, HEIGHT = 600;

    private GLWindow window;
    private Animator animator;

    private CameraController cameraController;
    private MVPCanvas canvas;

    private CanvasProgram guiCanvas;
    private GUI gui;

    private double deltaTime;
    private double lastFrame;

    public static void main(String[] args) {
        /*Rectangle2D.Float a = new Rectangle2D.Float(0,0,50,50);
        Rectangle2D.Float b = new Rectangle2D.Float(45f,45f,50,50);
        Area aArea = new Area(a);
        Area bArea = new Area(b);

        AffineTransform af = new AffineTransform();
        af.rotate(Math.toRadians(45), 25+45f, 25+45f);
        bArea = bArea.createTransformedArea(af);*/
        new Main().setup();
    }

    /**
     * Setup the window
     */
    private void setup() {
        GLProfile glProfile = GLProfile.get(GLProfile.GL4);
        GLCapabilities glCapabilities = new GLCapabilities(glProfile);

        window = GLWindow.create(glCapabilities);

        window.setTitle(WINDOW_TITLE);
        window.setSize(WIDTH, HEIGHT);

        window.setContextCreationFlags(GLContext.CTX_OPTION_DEBUG);
        window.setVisible(true);
        window.requestFocus();
        window.confinePointer(true);

        window.setPointerVisible(false);

        cameraController = new CameraController(window);

        window.addGLEventListener(this);
        window.addKeyListener(cameraController);
        window.addMouseListener(cameraController);


        animator = new Animator(window);
        animator.setRunAsFastAsPossible(true);
        animator.start();

        window.addWindowListener(new WindowAdapter() {
            @Override
            public void windowDestroyed(WindowEvent e) {
                animator.stop();
                System.exit(1);
            }
        });
    }

    /**
     * Called at the start to initialize everything needed for rendering
     */
    public void init(GLAutoDrawable drawable) {
        GL4 gl = drawable.getGL().getGL4();

        initCanvas();
        initPrograms(gl);

        gl.glEnable(GL.GL_DEPTH_TEST);
        gl.glEnable(GL.GL_MULTISAMPLE);

        gl.glEnable(GL_BLEND);
        gl.glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);

        Models.buildModels(gl);
        Textures.setDefaultSettings(gl);

        checkError(gl, "init");
    }

    SpotLight spotLight;

    private void initCanvas() {
        MovingCamera camera = new MovingCamera(new Vector3f(1.0f, 0.0f, 1.0f), new Vector3f(0.0f, 1.0f, 0.0f), -90.0f, 0f);
        camera.setMovementSpeed(20);
        cameraController.setCamera(camera);
        canvas = new MVPCanvas(camera, 45f, WIDTH, HEIGHT, 0.1f, 1000f);
        camera.setZoomChangeListener((zoom) -> canvas.setFov(zoom));

        canvas.getLightContainer().addLight(new DirLight(
                new Vector3f(-0.2f, -1.0f, -0.3f),
                new Vector3f(0.05f, 0.05f, 0.05f),
                new Vector3f( 0.5f, 0.5f, 0.5f),
                new Vector3f(1.0f, 1.0f, 1.0f)
        ));

        PointLight pointLight = new PointLight(
                new Vector3f(1f, 0f, 1f),
                1f,
                0.09f,
                0.032f,
                new Vector3f(0.2f, 0.2f, 0.2f),
                new Vector3f( 0.5f, 0.5f, 0.5f),
                new Vector3f(1.0f, 1.0f, 1.0f)
        );
//        canvas.getLightContainer().addLight(pointLight);

        spotLight = new SpotLight(
                new Vector3f(5f, 5f, 5f),
                1f,
                0.09f,
                0.032f,
                new Vector3f(0.2f, 0.2f, 0.2f),
                new Vector3f( 0.5f, 0.5f, 0.5f),
                new Vector3f(1.0f, 1.0f, 1.0f),
                new Vector3f(1f, 0f, 1f),
                (float) Math.cos(Math.toRadians(12.5)),
                (float) Math.cos(Math.toRadians(17.5))
            );
        canvas.getLightContainer().addLight(spotLight);
    }

    private void initPrograms(GL4 gl) {
        Program guiProgram = new Program(gl, "shaders", "gui");
        guiCanvas = new CanvasProgram(guiProgram);
        gui = new GUI(WIDTH, HEIGHT, gl);
        guiCanvas.getDrawables().add(gui);

        Program program = new Program(gl, "shaders", "main");
        CanvasProgram canvasProgram = new CanvasProgram(program);
        canvas.addProgram(canvasProgram);

        Object3D ship1 = new Object3D(Models.KING_GORGE_SHIP);
        ship1.setTexture(Textures.KING_GEORGE_SHIP);
        ship1.setMaterial(Materials.SHIP);
        ship1.setModel(new Matrix4f().rotate((float) Math.toRadians(-90.0), 1, 0, 0).translate(new Vector3f(5f, 0, 0)));
        canvasProgram.getDrawables().add(ship1);

        Object3D ship2 = new Object3D(Models.CHENG_KUNG_FRIGATE);
        ship2.setScale(.001f);
        ship2.setTexture(Textures.CHENG_KUNG_FRIGATE);
        ship2.setMaterial(Materials.SHIP);
        ship2.setModel(new Matrix4f().translate(new Vector3f(0, 0, 5f)));
        canvasProgram.getDrawables().add(ship2);

        Object3D ship3 = new Object3D(Models.BATTLESHIP_C);
        ship3.setScale(.0001f);
        ship3.setTexture(Textures.BATTLESHIP_C);
        ship3.setMaterial(Materials.SHIP);
        ship3.setModel(new Matrix4f().rotate((float) Math.toRadians(-90.0), 1, 0, 0).translate(new Vector3f(-5f, 0, 0)));
        canvasProgram.getDrawables().add(ship3);

        Object3D barrel = new Object3D(Models.BARREL);
        barrel.setScale(.5f);
        barrel.setTexture(Textures.BARREL);
        barrel.setMaterial(Materials.SILVER);
        barrel.setModel(new Matrix4f().translate(new Vector3f(-2f, 0, 0)));
        canvasProgram.getDrawables().add(barrel);

        Object3D rocket = new Object3D(Models.ROCKET);
        rocket.setScale(.05f);
        rocket.setTexture(Textures.ROCKET);
        rocket.setMaterial(Materials.SILVER);
        rocket.setModel(new Matrix4f().translate(new Vector3f(3f, 0, 0)));
        canvasProgram.getDrawables().add(rocket);
        
        Program waterProgram = new Program(gl, "shaders", "water", "main");
        CanvasProgram waterCanvas = new CanvasProgram(waterProgram);
        canvas.addProgram(waterCanvas);

        WaterPlane waterPlane = new WaterPlane(gl);
        waterCanvas.getDrawables().add(waterPlane);
    }

    /**
     * Called at the end to dispose of resources
     */
    public void dispose(GLAutoDrawable drawable) {
        GL4 gl = drawable.getGL().getGL4();
        canvas.dispose(gl);
        Textures.disposeTextures(gl);
    }

    /**
     * Render loop
     */
    public void display(GLAutoDrawable drawable) {
        double currentFrame = System.nanoTime() / 1000000000.0;
        deltaTime = currentFrame - lastFrame;
        lastFrame = currentFrame;

        cameraController.setDeltaTime(deltaTime);

        GL4 gl = drawable.getGL().getGL4();
        gl.glClearColor(0.02f, 0.05f, 0.1f, 1.0f);
        gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);

        if (cameraController.getCamera() != null) {
            spotLight.setPosition(cameraController.getCamera().getPosition());
            spotLight.setDirection(cameraController.getCamera().getFront());
        }

        canvas.display(gl);
        guiCanvas.display(gl);

        checkError(gl, "display");
    }

    /**
     * Called when window is resized
     */
    public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
        GL4 gl = drawable.getGL().getGL4();
        gl.glViewport(x, y, width, height);
        canvas.reshape(gl, width, height);
        if (gui != null)
            gui.reshape(width, height);
    }

    private void checkError(GL gl, String location) {
        int error = gl.glGetError();
        if (error != GL_NO_ERROR) {
            String errorString;
            switch (error) {
                case GL_INVALID_ENUM:
                    errorString = "GL_INVALID_ENUM";
                    break;
                case GL_INVALID_VALUE:
                    errorString = "GL_INVALID_VALUE";
                    break;
                case GL_INVALID_OPERATION:
                    errorString = "GL_INVALID_OPERATION";
                    break;
                case GL_INVALID_FRAMEBUFFER_OPERATION:
                    errorString = "GL_INVALID_FRAMEBUFFER_OPERATION";
                    break;
                case GL_OUT_OF_MEMORY:
                    errorString = "GL_OUT_OF_MEMORY";
                    break;
                default:
                    errorString = "UNKNOWN";
                    break;
            }
            LOGGER.log(Level.SEVERE, "OpenGL Error(" + errorString + "): " + location);
        }
    }
}
