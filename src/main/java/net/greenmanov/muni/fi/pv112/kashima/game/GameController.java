package net.greenmanov.muni.fi.pv112.kashima.game;

import com.jogamp.newt.event.KeyEvent;
import com.jogamp.newt.event.KeyListener;
import com.jogamp.newt.opengl.GLWindow;
import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL4;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLEventListener;
import net.greenmanov.muni.fi.pv112.kashima.GUI;
import net.greenmanov.muni.fi.pv112.kashima.game.controls.KeyboardControls;
import net.greenmanov.muni.fi.pv112.kashima.game.controls.MouseControls;
import net.greenmanov.muni.fi.pv112.kashima.game.enviroment.WaterPlane;
import net.greenmanov.muni.fi.pv112.kashima.game.objects.*;
import net.greenmanov.muni.fi.pv112.kashima.lights.DirLight;
import net.greenmanov.muni.fi.pv112.kashima.lights.LightContainer;
import net.greenmanov.muni.fi.pv112.kashima.models.Models;
import net.greenmanov.muni.fi.pv112.kashima.opengl.MVPCanvas;
import net.greenmanov.muni.fi.pv112.kashima.opengl.camera.MovingCamera;
import net.greenmanov.muni.fi.pv112.kashima.opengl.program.CanvasProgram;
import net.greenmanov.muni.fi.pv112.kashima.opengl.program.Program;
import net.greenmanov.muni.fi.pv112.kashima.textures.Textures;
import org.joml.Vector3f;

import java.awt.*;
import java.util.HashSet;
import java.util.Random;
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
    private static final Vector3f BACKGROUND_COLOR = new Vector3f(0.1f, 0, 0.1f);

    public static final int SCORE_PER_SECOND = 10;

    private GLWindow window;

    private CollisionDetector collisionDetector;
    private HashSet<IGameObject> objects = new HashSet<>();
    private HashSet<IGameObject> delete = new HashSet<>();

    private double deltaTime;
    private double lastFrame;

    private MovingCamera camera;
    private MVPCanvas mvpCanvas;
    private CanvasProgram mainProgram;
    private CanvasProgram guiCanvas;
    private CanvasProgram waterCanvas;

    private GUI gui;

    private KeyboardControls keyboardControls;
    private MouseControls mouseControls;

    private Player player;
    private float score;
    private boolean paused = false;

    public GameController(GLWindow window) {
        this.window = window;
        this.collisionDetector = new CollisionDetector();
    }

    public void addObject(IGameObject object) {
        // TODO: Add to buffer before realy adding
        objects.add(object);
        if (object instanceof IDrawableObject) {
            mainProgram.getDrawables().add(((IDrawableObject) object).getObject3D());
        }
        if (object instanceof ICollisionObject) {
            collisionDetector.addObject((ICollisionObject) object);
        }
    }

    /**
     * Add object to list of objects that should be deleted
     */
    public void removeObject(IGameObject object) {
        delete.add(object);
    }

    private void removeObjectReal(IGameObject object) {
        objects.remove(object);
        if (object instanceof IDrawableObject) {
            IDrawableObject drawable = (IDrawableObject) object;
            mainProgram.getDrawables().remove(drawable.getObject3D());
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
        try {
            keyboardControls = new KeyboardControls(window, player, this);
            this.window.addKeyListener(keyboardControls);
            mouseControls = new MouseControls(camera, window);
            this.window.addMouseListener(mouseControls);
        } catch (AWTException e) {
            throw new IllegalStateException(e);
        }
    }

    private void prepareGameObjects() {
        player = new Player(this, new KingGorgeShip(0,0,this), camera);
        gui.setPlayer(player);
        addObject(player);

        Random rand = new Random();
        for (int i = 0; i < 25; i++) {
            AShip ship = new KingGorgeShip(
                    (rand.nextBoolean() ? 1 : -1) * 50*rand.nextFloat(),
                    (rand.nextBoolean() ? 1 : -1) * 50*rand.nextFloat(),
                    this);
            ship.turn(rand.nextBoolean(), rand.nextInt(360));
            ship.accelerate(rand.nextBoolean(), rand.nextInt(5));
            addObject(ship);
        }
        for (int i = 0; i < 10; i++) {
            AShip ship = new ChengKungFrigate(
                    (rand.nextBoolean() ? 1 : -1) * 50*rand.nextFloat(),
                    (rand.nextBoolean() ? 1 : -1) * 50*rand.nextFloat(),
                    this);
            ship.turn(rand.nextBoolean(), rand.nextInt(360));
            ship.accelerate(rand.nextBoolean(), rand.nextInt(5));
            addObject(ship);
        }

        for (int i = 0; i < 30; i++) {
            Barrel barrel = new Barrel(
                    new Vector3f((rand.nextBoolean() ? 1 : -1) * 50*rand.nextFloat(),
                            0,
                            (rand.nextBoolean() ? 1 : -1) * 50*rand.nextFloat()
                    ), this);
            addObject(barrel);
        }

        for (int i = 0; i < 30; i++) {
            Wrench wrench = new Wrench(
                    new Vector3f((rand.nextBoolean() ? 1 : -1) * 50*rand.nextFloat(),
                            0,
                            (rand.nextBoolean() ? 1 : -1) * 50*rand.nextFloat()
                    ), this);
            addObject(wrench);
        }
    }

    private void prepareGui(GL4 gl) {
        Program guiProgram = new Program(gl, "shaders", "gui");
        guiCanvas = new CanvasProgram(guiProgram);
        gui = new GUI(window.getWidth(), window.getHeight(), gl);
        gui.setGameController(this);
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
        camera = new MovingCamera(new Vector3f(0,10f,0), new Vector3f(0,1,0), 0, -90);
        mvpCanvas = new MVPCanvas(camera, MVP_FOV, window.getWidth(), window.getHeight(), MVP_NEAR, MVP_FAR);
        camera.setZoomChangeListener((zoom) -> mvpCanvas.setFov(zoom));

        Program program  = new Program(gl, "shaders", "main");
        mainProgram = new CanvasProgram(program);
        mvpCanvas.addProgram(mainProgram);

//         Main light
        mvpCanvas.getLightContainer().addLight(new DirLight(
                new Vector3f(0, -15.0f, 0),
                new Vector3f(0.05f, 0.05f, 0.05f),
                new Vector3f( 0.3f, 0.3f, 0.3f),
                new Vector3f(0.5f, 0.5f, 0.5f)
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
        gui.redraw();
        guiCanvas.display(gl);
    }

    /**
     * Do game logic
     */
    private void gameLoopStep(GL4 gl) {
        float deltaTime = recomputeDeltaTime();
        if (!paused) {
            moveStep(deltaTime);
            collisionDetector.detect();
            logicStep(deltaTime);
            deleteObjectsStep();
        }
    }

    /**
     * Delete all object that should be deleted this step
     */
    private void deleteObjectsStep() {
        delete.forEach(this::removeObjectReal);
        delete.clear();
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
        addScore(SCORE_PER_SECOND * deltaTime);
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

    public void addScore(float score) {
        this.score += score;
        gui.setScore((int) this.score);
    }

    public void togglePause() {
        paused = !paused;
    }

    public boolean isPaused() {
        return paused;
    }

    public LightContainer getLightContainer() {
        return mvpCanvas.getLightContainer();
    }

    public void gameOver() {
        paused = true;
        this.window.removeKeyListener(keyboardControls);
        this.window.addKeyListener(new KeyListener() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyboardControls.QUIT_KEY)
                    new Thread(window::destroy).start();
            }

            @Override
            public void keyReleased(KeyEvent e) {

            }
        });
        this.window.removeMouseListener(mouseControls);
        gui.setGameOver(true);
    }
}
