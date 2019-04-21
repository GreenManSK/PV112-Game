package net.greenmanov.muni.fi.pv112.kashima.game;

import com.jogamp.newt.opengl.GLWindow;
import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL4;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLEventListener;
import net.greenmanov.muni.fi.pv112.kashima.GUI;
import net.greenmanov.muni.fi.pv112.kashima.game.controls.KeyboardControls;
import net.greenmanov.muni.fi.pv112.kashima.game.objects.IGameObject;
import net.greenmanov.muni.fi.pv112.kashima.models.Models;
import net.greenmanov.muni.fi.pv112.kashima.opengl.MVPCanvas;
import net.greenmanov.muni.fi.pv112.kashima.opengl.camera.ICamera;
import net.greenmanov.muni.fi.pv112.kashima.opengl.camera.SimpleCamera;
import net.greenmanov.muni.fi.pv112.kashima.opengl.program.CanvasProgram;
import net.greenmanov.muni.fi.pv112.kashima.opengl.program.Program;
import net.greenmanov.muni.fi.pv112.kashima.textures.Textures;
import org.joml.Vector3f;

import java.util.HashSet;
import java.util.logging.Logger;

import static com.jogamp.opengl.GL.GL_BLEND;
import static com.jogamp.opengl.GL.GL_ONE_MINUS_SRC_ALPHA;
import static com.jogamp.opengl.GL.GL_SRC_ALPHA;
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

    private HashSet<IGameObject> objects = new HashSet<>();

    private double deltaTime;
    private double lastFrame;

    private ICamera camera;
    private MVPCanvas mvpCanvas;
    private CanvasProgram guiCanvas;

    private GUI gui;

    public GameController(GLWindow window) {
        this.window = window;
        this.window.addKeyListener(new KeyboardControls(window));
    }

    public void addObject(IGameObject object) {
        objects.add(object);
    }

    public void removeObject(IGameObject object) {
        objects.remove(object);
    }

    @Override
    public void init(GLAutoDrawable drawable) {
        GL4 gl = drawable.getGL().getGL4();

        prepareGui(gl);
        prepareMvpCanvas(gl);

        // Enable depth test and multisampling
        gl.glEnable(GL.GL_DEPTH_TEST);
        gl.glEnable(GL.GL_MULTISAMPLE);

        // Enable blending
        gl.glEnable(GL_BLEND);
        gl.glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);

        // Build models and textures
        Models.buildModels(gl);
        Textures.setDefaultSettings(gl);

        recomputeDeltaTime();
        checkError(LOGGER, gl, "init");
    }

    private void prepareGui(GL4 gl) {
        Program guiProgram = new Program(gl, "shaders", "gui");
        guiCanvas = new CanvasProgram(guiProgram);
        gui = new GUI(window.getWidth(), window.getHeight(), gl);
        guiCanvas.getDrawables().add(gui);
    }

    private void prepareMvpCanvas(GL4 gl) {
        camera = new SimpleCamera(new Vector3f(5f, 0, 0), new Vector3f());
        mvpCanvas = new MVPCanvas(camera, MVP_FOV, window.getWidth(), window.getHeight(), MVP_NEAR, MVP_FAR);
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
        double deltaTime = recomputeDeltaTime();
        moveStep(deltaTime);
        //TODO: Collisions
        logicStep(deltaTime);
    }

    /**
     * Move all objects
     */
    private void moveStep(double deltaTime) {
        objects.forEach(obj -> obj.move(deltaTime));
    }

    /**
     * Trigger logic of each object
     */
    private void logicStep(double deltaTime) {
        objects.forEach(obj -> obj.logic(deltaTime));
    }

    private double recomputeDeltaTime() {
        double currentFrame = System.nanoTime() / 1000000000.0;
        deltaTime = currentFrame - lastFrame;
        lastFrame = currentFrame;
        return deltaTime;
    }

    @Override
    public void dispose(GLAutoDrawable drawable) {
        GL4 gl = drawable.getGL().getGL4();

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
