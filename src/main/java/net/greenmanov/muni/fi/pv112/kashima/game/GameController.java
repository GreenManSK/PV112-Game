package net.greenmanov.muni.fi.pv112.kashima.game;

import com.jogamp.newt.opengl.GLWindow;
import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL4;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLEventListener;
import net.greenmanov.muni.fi.pv112.kashima.GUI;
import net.greenmanov.muni.fi.pv112.kashima.game.controls.KeyboardControls;
import net.greenmanov.muni.fi.pv112.kashima.game.enviroment.WaterPlane;
import net.greenmanov.muni.fi.pv112.kashima.game.objects.*;
import net.greenmanov.muni.fi.pv112.kashima.lights.DirLight;
import net.greenmanov.muni.fi.pv112.kashima.models.Models;
import net.greenmanov.muni.fi.pv112.kashima.opengl.MVPCanvas;
import net.greenmanov.muni.fi.pv112.kashima.opengl.camera.SimpleCamera;
import net.greenmanov.muni.fi.pv112.kashima.opengl.program.CanvasProgram;
import net.greenmanov.muni.fi.pv112.kashima.opengl.program.Program;
import net.greenmanov.muni.fi.pv112.kashima.textures.Textures;
import org.joml.Vector3f;

import java.util.HashSet;
import java.util.logging.Logger;

import static com.jogamp.opengl.GL.*;
import static net.greenmanov.muni.fi.pv112.kashima.opengl.OpenGLHelper.checkError;

/**
 * Controller for the whole game loop
 *
 * @author Lukáš Kurčík <lukas.kurcik@gmail.com>
 */
public class GameController implements GLEventListener {

    private static final Logger LOGGER = Logger.getLogger(GameController.class.getName());

    private static final float MVP_FOV = 45f, MVP_NEAR = 0.1f, MVP_FAR = 100f;
    private static final Vector3f BACKGROUND_COLOR = new Vector3f(0.2f, 0, 0.2f);

    private GLWindow window;

    private CollisionDetector collisionDetector;
    private HashSet<IGameObject> objects = new HashSet<>();

    private double deltaTime;
    private double lastFrame;

    private SimpleCamera camera;
    private MVPCanvas mvpCanvas;
    private CanvasProgram mainProgram;
    private CanvasProgram guiCanvas;
    private CanvasProgram waterCanvas;

    private GUI gui;

    private KeyboardControls keyboardControls;

    private Player player;

    public GameController(GLWindow window) {
        this.window = window;
        this.collisionDetector = new CollisionDetector();
    }

    public void addObject(IGameObject object) {
        objects.add(object);
        if (object instanceof IDrawableObject) {
            mainProgram.getDrawables().add(((IDrawableObject) object).getObject3D());
        }
        if (object instanceof ICollisionObject) {
            collisionDetector.addObject((ICollisionObject) object);
        }
    }

    public void removeObject(IGameObject object) {
        objects.remove(object);
        if (object instanceof IDrawableObject) {
            mainProgram.getDrawables().remove(((IDrawableObject) object).getObject3D());
        }
        if (object instanceof ICollisionObject) {
            collisionDetector.removeObject((ICollisionObject) object);
        }
    }

    @Override
    public void init(GLAutoDrawable drawable) {
        GL4 gl = drawable.getGL().getGL4();

        prepareGui(gl);
        prepareMvpCanvas(gl);
        prepareWaterCanvas(gl);
        prepareGameObjects();

        // Enable depth test and multisampling
        gl.glEnable(GL.GL_DEPTH_TEST);
        gl.glEnable(GL.GL_MULTISAMPLE);

        // Enable blending
        gl.glEnable(GL_BLEND);
        gl.glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);

        // Build models and textures
        Models.buildModels(gl);
        Textures.setDefaultSettings(gl);

        // Enable controls
        enableControls();

        recomputeDeltaTime();
        checkError(LOGGER, gl, "init");
    }

    private void enableControls() {
        keyboardControls = new KeyboardControls(window, player);
        this.window.addKeyListener(keyboardControls);
    }

    private void prepareGameObjects() {
        player = new Player(this, new KingGorgeShip(0,0,this), camera);
        addObject(player);
        AShip ship = new KingGorgeShip(KingGorgeShip.WIDTH, 0, this);
        addObject(ship);
    }

    private void prepareGui(GL4 gl) {
        Program guiProgram = new Program(gl, "shaders", "gui");
        guiCanvas = new CanvasProgram(guiProgram);
        gui = new GUI(window.getWidth(), window.getHeight(), gl);
        guiCanvas.getDrawables().add(gui);
    }

    private void prepareWaterCanvas(GL4 gl) {
        Program waterProgram = new Program(gl, "shaders", "water", "main");
        waterCanvas = new CanvasProgram(waterProgram);
        mvpCanvas.addProgram(waterCanvas);
        WaterPlane waterPlane = new WaterPlane(gl);
        waterCanvas.getDrawables().add(waterPlane);
    }

    private void prepareMvpCanvas(GL4 gl) {
        camera = new SimpleCamera(new Vector3f(0, 10f, 0), new Vector3f());
        mvpCanvas = new MVPCanvas(camera, MVP_FOV, window.getWidth(), window.getHeight(), MVP_NEAR, MVP_FAR);

        Program program  = new Program(gl, "shaders", "main");
        mainProgram = new CanvasProgram(program);
        mvpCanvas.addProgram(mainProgram);

        // TODO: Temporary
        mvpCanvas.getLightContainer().addLight(new DirLight(
                new Vector3f(-0.2f, -1.0f, -0.3f),
                new Vector3f(0.05f, 0.05f, 0.05f),
                new Vector3f( 0.5f, 0.5f, 0.5f),
                new Vector3f(1.0f, 1.0f, 1.0f)
        ));
    }

    @Override
    public void display(GLAutoDrawable drawable) {
        GL4 gl = drawable.getGL().getGL4();

        gameLoopStep(gl);
        drawStep(gl);

        checkError(LOGGER, gl, "display");
    }

    /**
     * Render game
     */
    private void drawStep(GL4 gl) {
        gl.glClearColor(BACKGROUND_COLOR.x, BACKGROUND_COLOR.y, BACKGROUND_COLOR.z, 1.0f);
        gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);
        mvpCanvas.display(gl);
        guiCanvas.display(gl);
    }

    /**
     * Do game logic
     */
    private void gameLoopStep(GL4 gl) {
        float deltaTime = recomputeDeltaTime();
        moveStep(deltaTime);
        collisionDetector.detect();
        logicStep(deltaTime);
    }

    /**
     * Move all objects
     */
    private void moveStep(float deltaTime) {
        objects.forEach(obj -> obj.move(deltaTime));
    }

    /**
     * Trigger logic of each object
     */
    private void logicStep(float deltaTime) {
        objects.forEach(obj -> obj.logic(deltaTime));
    }

    private float recomputeDeltaTime() {
        double currentFrame = System.nanoTime() / 1000000000.0;
        deltaTime = currentFrame - lastFrame;
        lastFrame = currentFrame;
        return (float) deltaTime;
    }

    @Override
    public void dispose(GLAutoDrawable drawable) {
        GL4 gl = drawable.getGL().getGL4();

        mvpCanvas.dispose(gl);
        guiCanvas.dispose(gl);

        checkError(LOGGER, gl, "dispose");
    }

    @Override
    public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
        GL4 gl = drawable.getGL().getGL4();

        gl.glViewport(x, y, width, height);
        if (mvpCanvas != null) {
            mvpCanvas.reshape(gl, width, height);
        }
        if (gui != null) {
            gui.reshape(width, height);
        }

        checkError(LOGGER, gl, "reshape");
    }
}
