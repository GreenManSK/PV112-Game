package net.greenmanov.muni.fi.pv112.kashima;

import com.jogamp.newt.event.WindowAdapter;
import com.jogamp.newt.event.WindowEvent;
import com.jogamp.newt.opengl.GLWindow;
import com.jogamp.opengl.*;
import com.jogamp.opengl.util.Animator;
import com.jogamp.opengl.util.GLBuffers;
import net.greenmanov.muni.fi.pv112.kashima.opengl.IDrawable;
import net.greenmanov.muni.fi.pv112.kashima.opengl.Program;
import net.greenmanov.muni.fi.pv112.kashima.opengl.SimpleObject;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;

import static com.jogamp.opengl.GL.*;
import static com.jogamp.opengl.GL.GL_INVALID_FRAMEBUFFER_OPERATION;
import static com.jogamp.opengl.GL.GL_OUT_OF_MEMORY;

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

    private Program testProgram;
    private List<IDrawable> objects;

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

        initPrograms(gl);
        initObjects(gl);

        gl.glEnable(GL.GL_DEPTH_TEST);
        gl.glEnable(GL.GL_MULTISAMPLE);

        gl.glEnable(GL.GL_CULL_FACE);
        gl.glCullFace(GL.GL_BACK);

        checkError(gl, "init");
    }

    private void initPrograms(GL3 gl) {
        testProgram = new Program(gl, "test", "main");
    }

    private void initObjects(GL3 gl) {
        objects = new ArrayList<>();
        objects.add(new SimpleObject(gl, new float[]{
                0.5f, 0.5f, 0.0f,  // top right
                0.5f, -0.5f, 0.0f,  // bottom right
                -0.5f, -0.5f, 0.0f,  // bottom left
                -0.5f, 0.5f, 0.0f   // top left
        }, new int[]{
                0, 2, 1,  // first Triangle
                3, 2, 0   // second Triangle
        }, new float[]{
                1f, 0f, 0f, 1f,
                0f, 1f, 0f, 1f,
                0f, 0f, 1f, 1f,
                0f, 0f, 0f, 1f,
        }));
    }

    /**
     * Called at the end to dispose of resources
     */
    public void dispose(GLAutoDrawable drawable) {
        GL3 gl = drawable.getGL().getGL3();
        testProgram.dispose(gl);
        for (IDrawable obj : objects) {
            obj.dispose(gl);
        }
    }

    /**
     * Render loop
     */
    public void display(GLAutoDrawable drawable) {
        GL3 gl = drawable.getGL().getGL3();
        gl.glClearColor(0.2f, 0.3f, 0.3f, 1.0f);
        gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);

        gl.glUseProgram(testProgram.getName());
        for (IDrawable obj : objects) {
            obj.draw(gl);
        }

        checkError(gl, "display");
    }

    /**
     * Called when window is resized
     */
    public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
        GL3 gl = drawable.getGL().getGL3();
        gl.glViewport(x, y, width, height);
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
