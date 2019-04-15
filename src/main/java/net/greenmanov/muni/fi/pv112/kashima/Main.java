package net.greenmanov.muni.fi.pv112.kashima;

import com.jogamp.newt.event.WindowAdapter;
import com.jogamp.newt.event.WindowEvent;
import com.jogamp.newt.opengl.GLWindow;
import com.jogamp.opengl.*;
import com.jogamp.opengl.util.Animator;
import net.greenmanov.muni.fi.pv112.kashima.opengl.MVPCanvas;
import net.greenmanov.muni.fi.pv112.kashima.opengl.camera.ICamera;
import net.greenmanov.muni.fi.pv112.kashima.opengl.camera.SimpleCamera;
import net.greenmanov.muni.fi.pv112.kashima.opengl.drawable.SimpleObject;
import net.greenmanov.muni.fi.pv112.kashima.opengl.program.CanvasProgram;
import net.greenmanov.muni.fi.pv112.kashima.opengl.program.Program;
import org.joml.Matrix4f;
import org.joml.Vector3f;

import static com.jogamp.opengl.GL.*;

/**
 * Class Main
 *
 * @author Lukáš Kurčík <lukas.kurcik@gmail.com>
 */
public class Main implements GLEventListener {

    private static final String WINDOW_TITLE = "Project Kashima";
    private static final int WIDTH = 800, HEIGHT = 600;

    private GLWindow window;
    private Animator animator;

    private MVPCanvas canvas;

    public static void main(String[] args) {
        new Main().setup();
    }

    /**
     * Setup the window
     */
    private void setup() {
        GLProfile glProfile = GLProfile.get(GLProfile.GL3);
        GLCapabilities glCapabilities = new GLCapabilities(glProfile);

        window = GLWindow.create(glCapabilities);

        window.setTitle(WINDOW_TITLE);
        window.setSize(WIDTH, HEIGHT);

        window.setContextCreationFlags(GLContext.CTX_OPTION_DEBUG);
        window.setVisible(true);

        window.addGLEventListener(this);

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
        GL3 gl = drawable.getGL().getGL3();

        initCanvas();
        initPrograms(gl);

        gl.glEnable(GL.GL_DEPTH_TEST);
        gl.glEnable(GL.GL_MULTISAMPLE);

        checkError(gl, "init");
    }

    SimpleCamera camera;
    private void initCanvas() {
        camera = new SimpleCamera(new Vector3f(1f,0f,1f), new Vector3f(0f,0f,0f));
        canvas = new MVPCanvas(camera, 45f, WIDTH, HEIGHT, 0.1f, 100f);
    }

    private void initPrograms(GL3 gl) {
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
        rectangle.setModel(new Matrix4f());
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
        GL3 gl = drawable.getGL().getGL3();
        canvas.dispose(gl);
    }

    /**
     * Render loop
     */
    public void display(GLAutoDrawable drawable) {
        float radius = 10.0f;
        double time = System.nanoTime() / 1000000000.0;
        camera.setPosition(new Vector3f((float)Math.sin(time) * radius,(float)Math.cos(time) * radius,(float)Math.cos(time) * radius));

        GL3 gl = drawable.getGL().getGL3();
        gl.glClearColor(0.2f, 0.3f, 0.3f, 1.0f);
        gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);

        canvas.display(gl);

        checkError(gl, "display");
    }

    /**
     * Called when window is resized
     */
    public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
        GL3 gl = drawable.getGL().getGL3();
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
            throw new Error("OpenGL Error(" + errorString + "): " + location);
        }
    }
}
