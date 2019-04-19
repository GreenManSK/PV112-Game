package net.greenmanov.muni.fi.pv112.kashima;

import com.jogamp.newt.event.WindowAdapter;
import com.jogamp.newt.event.WindowEvent;
import com.jogamp.newt.opengl.GLWindow;
import com.jogamp.opengl.*;
import com.jogamp.opengl.util.Animator;
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

    private double deltaTime;
    private double lastFrame;

    public static void main(String[] args) {
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

        Models.buildModels(gl);
        Textures.setDefaultSettings(gl);

        checkError(gl, "init");
    }

    private void initCanvas() {
        MovingCamera camera = new MovingCamera(new Vector3f(1.0f,0.0f,1.0f), new Vector3f(0.0f,1.0f,0.0f), -90.0f, 0f);
        camera.setMovementSpeed(50);
        cameraController.setCamera(camera);
        canvas = new MVPCanvas(camera, 45f, WIDTH, HEIGHT, 0.1f, 1000f);
        camera.setZoomChangeListener((zoom) -> canvas.setFov(zoom));
    }

    private void initPrograms(GL4 gl) {
        Program program = new Program(gl, "meshTest", "main");
        CanvasProgram canvasProgram = new CanvasProgram(program);
        canvas.addProgram(canvasProgram);

        Object3D cat = new Object3D(Models.CAT);
        cat.setScale(0.1f);
        cat.setTexture(Textures.LUKY);
        cat.setMaterial(Materials.NONE);
        canvasProgram.getDrawables().add(cat);

        Object3D light = new Object3D(Models.TEAPOT);
        light.setScale(0.1f);
        light.setMaterial(Materials.SILVER);
        light.setModel(new Matrix4f().translate(new Vector3f(5f,5f,5f)));
        canvasProgram.getDrawables().add(light);

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
        gl.glClearColor(0.2f, 0.3f, 0.3f, 1.0f);
        gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);

        canvas.display(gl);

        checkError(gl, "display");
    }

    /**
     * Called when window is resized
     */
    public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
        GL4 gl = drawable.getGL().getGL4();
        gl.glViewport(x, y, width, height);
        canvas.reshape(gl, width, height);
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
