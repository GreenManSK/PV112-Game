package net.greenmanov.muni.fi.pv112.kashima.opengl;

import com.jogamp.opengl.GL4;
import com.jogamp.opengl.util.GLBuffers;
import net.greenmanov.muni.fi.pv112.kashima.opengl.camera.ICamera;
import net.greenmanov.muni.fi.pv112.kashima.opengl.drawable.IDrawable;
import net.greenmanov.muni.fi.pv112.kashima.opengl.program.CanvasProgram;
import net.greenmanov.muni.fi.pv112.kashima.opengl.program.Program;
import org.joml.Matrix4f;

import java.util.ArrayList;
import java.util.List;

/**
 * Canvas for rendering programs with the use of MVP
 *
 * @author Lukáš Kurčík <lukas.kurcik@gmail.com>
 */
public class MVPCanvas {

    public static final String MODEL_UNIFORM = "model";
    public static final String VIEW_UNIFORM = "view";
    public static final String PROJECTION_UNIFORM = "projection";

    private List<CanvasProgram> programs = new ArrayList<>();
    private ICamera camera;

    private float fov;
    private int width;
    private int height;
    private float near;
    private float far;

    private GL4 gl;

    /**
     * Create MVPCanvas
     * @param camera Camera
     * @param fov Fov in degress
     * @param width Width of view
     * @param height Height of view
     * @param near Near plane
     * @param far Far plane
     */
    public MVPCanvas(ICamera camera, float fov, int width, int height, float near, float far) {
        this.camera = camera;
        this.fov = fov;
        this.width = width;
        this.height = height;
        this.near = near;
        this.far = far;
    }

    /**
     * Render all programs in order that they were added
     */
    public void display(GL4 gl) {
        this.gl = gl;
        for (CanvasProgram program : programs) {
            gl.glUseProgram(program.getProgram().getName());
            displayProgram(program);
        }
    }

    private void displayProgram(CanvasProgram program) {
        int viewLoc = gl.glGetUniformLocation(program.getProgram().getName(), VIEW_UNIFORM);
        gl.glUniformMatrix4fv(viewLoc, 1, false, camera.getView().get(GLBuffers.newDirectFloatBuffer(16)));

        int projectLoc = gl.glGetUniformLocation(program.getProgram().getName(), PROJECTION_UNIFORM);
        Matrix4f projection = new Matrix4f().perspective((float) Math.toRadians(fov), (float) width / height, near, far);
        gl.glUniformMatrix4fv(projectLoc, 1, false, projection.get(GLBuffers.newDirectFloatBuffer(16)));

        program.display(gl);
    }

    /**
     * Change projection matrix based on new window size
     */
    public void reshape(GL4 gl, int width, int height) {
        this.width = width;
        this.height = height;
    }

    /**
     * Dispose of all used programs.
     */
    public void dispose(GL4 gl) {
        programs.forEach(p -> p.dispose(gl));
    }

    /**
     * Add program to canvas.
     * Vertex shader should have three mat4 uniforms specified by {@link MVPCanvas#MODEL_UNIFORM},
     * {@link MVPCanvas#VIEW_UNIFORM}, {@link MVPCanvas#PROJECTION_UNIFORM}
     */
    public void addProgram(CanvasProgram program) {
        program.setBeforeDrawListener(this::setObjectModel);
        programs.add(program);
    }

    /**
     * Set model for the object
     */
    private void setObjectModel(Program program, IDrawable object) {
        int modelLoc = gl.glGetUniformLocation(program.getName(), MODEL_UNIFORM);
        gl.glUniformMatrix4fv(modelLoc, 1, false, object.getModel().get(GLBuffers.newDirectFloatBuffer(16)));
    }

    /**
     * Remove program from canvas
     */
    public void removeProgram(CanvasProgram program) {
        programs.remove(program);
    }

    public float getFov() {
        return fov;
    }

    public void setFov(float fov) {
        this.fov = fov;
    }
}
