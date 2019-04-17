package net.greenmanov.muni.fi.pv112.kashima;

import com.jogamp.newt.event.*;
import com.jogamp.newt.opengl.GLWindow;
import com.jogamp.opengl.*;
import com.jogamp.opengl.util.Animator;
import net.greenmanov.muni.fi.pv112.kashima.opengl.MVPCanvas;
import net.greenmanov.muni.fi.pv112.kashima.opengl.camera.MovingCamera;
import net.greenmanov.muni.fi.pv112.kashima.opengl.drawable.SimpleObject;
import net.greenmanov.muni.fi.pv112.kashima.opengl.program.CanvasProgram;
import net.greenmanov.muni.fi.pv112.kashima.opengl.program.Program;
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

        checkError(gl, "init");
    }

    private void initCanvas() {
        MovingCamera camera = new MovingCamera(new Vector3f(1.0f,0.0f,1.0f), new Vector3f(0.0f,1.0f,0.0f), -90.0f, 0f);
        camera.setMovementSpeed(5);
        cameraController.setCamera(camera);
        canvas = new MVPCanvas(camera, 45f, WIDTH, HEIGHT, 0.1f, 100f);
        camera.setZoomChangeListener((zoom) -> canvas.setFov(zoom));
    }

    Matrix4f matrix;

    private void initPrograms(GL4 gl) {
        Program program = new Program(gl, "test", "main");
        CanvasProgram canvasProgram = new CanvasProgram(program);
        canvas.addProgram(canvasProgram);

        SimpleObject rectangle = new SimpleObject(gl, new float[]{
                -0.5f, -0.5f, -0.5f,
                0.5f, -0.5f, -0.5f,
                0.5f,  0.5f, -0.5f,
                0.5f,  0.5f, -0.5f,
                -0.5f,  0.5f, -0.5f,
                -0.5f, -0.5f, -0.5f,

                -0.5f, -0.5f,  0.5f,
                0.5f, -0.5f,  0.5f,
                0.5f,  0.5f,  0.5f,
                0.5f,  0.5f,  0.5f,
                -0.5f,  0.5f,  0.5f,
                -0.5f, -0.5f,  0.5f,

                -0.5f,  0.5f,  0.5f,
                -0.5f,  0.5f, -0.5f,
                -0.5f, -0.5f, -0.5f,
                -0.5f, -0.5f, -0.5f,
                -0.5f, -0.5f,  0.5f,
                -0.5f,  0.5f,  0.5f,

                0.5f,  0.5f,  0.5f,
                0.5f,  0.5f, -0.5f,
                0.5f, -0.5f, -0.5f,
                0.5f, -0.5f, -0.5f,
                0.5f, -0.5f,  0.5f,
                0.5f,  0.5f,  0.5f,

                -0.5f, -0.5f, -0.5f,
                0.5f, -0.5f, -0.5f,
                0.5f, -0.5f,  0.5f,
                0.5f, -0.5f,  0.5f,
                -0.5f, -0.5f,  0.5f,
                -0.5f, -0.5f, -0.5f,

                -0.5f,  0.5f, -0.5f,
                0.5f,  0.5f, -0.5f,
                0.5f,  0.5f,  0.5f,
                0.5f,  0.5f,  0.5f,
                -0.5f,  0.5f,  0.5f,
                -0.5f,  0.5f, -0.5f,
        }, coolColor(36));
        matrix = new Matrix4f();
        rectangle.setModel(matrix);
        canvasProgram.getDrawables().add(rectangle);
    }

    private float[] coolColor(int colors) {
        float[] res = new float[colors * 4];
        for (int i = 0; i < colors; i++) {
            res[i * 4] = (float) Math.random();
            res[i * 4 + 1] = (float) Math.random();
            res[i * 4 + 2] = (float) Math.random();
            res[i * 4 + 3] = 1f;
        }
        return res;
    }

    /**
     * Called at the end to dispose of resources
     */
    public void dispose(GLAutoDrawable drawable) {
        GL4 gl = drawable.getGL().getGL4();
        canvas.dispose(gl);
    }

    /**
     * Render loop
     */
    public void display(GLAutoDrawable drawable) {
        double currentFrame = System.nanoTime() / 1000000000.0;
        deltaTime = currentFrame - lastFrame;
        lastFrame = currentFrame;

        cameraController.setDeltaTime(deltaTime);

        matrix.set(new Matrix4f().rotate((float) Math.toRadians(-55.0) * (float) currentFrame, 0.5f, 1.0f, 0.0f));

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
