package net.greenmanov.muni.fi.pv112.kashima.opengl.program;

import com.jogamp.opengl.GL4;
import com.jogamp.opengl.util.GLBuffers;
import com.jogamp.opengl.util.glsl.ShaderCode;
import com.jogamp.opengl.util.glsl.ShaderProgram;
import net.greenmanov.muni.fi.pv112.kashima.Main;
import org.joml.Matrix4f;
import org.joml.Vector3f;

import static com.jogamp.opengl.GL2ES2.GL_FRAGMENT_SHADER;
import static com.jogamp.opengl.GL2ES2.GL_VERTEX_SHADER;

/**
 * OpenGL program
 *
 * @author Lukáš Kurčík <lukas.kurcik@gmail.com>
 */
public class Program {
    private final ShaderProgram program;

    public Program(GL4 gl, String vertex) {
        this(gl, "", vertex, vertex);
    }

    public Program(GL4 gl, String path, String vertex) {
        this(gl, path, vertex, vertex);
    }

    public Program(GL4 gl, String path, String vertex, String fragment) {
        ShaderCode vertShader = ShaderCode.create(gl, GL_VERTEX_SHADER, Main.class, path, null, vertex,
                "vert", null, true);
        ShaderCode fragShader = ShaderCode.create(gl, GL_FRAGMENT_SHADER, Main.class, path, null, fragment,
                "frag", null, true);

        program = new ShaderProgram();

        program.add(vertShader);
        program.add(fragShader);

        program.init(gl);
        program.link(gl, System.err);

        vertShader.destroy(gl);
        fragShader.destroy(gl);
    }

    public int getName() {
        return program.program();
    }

    public void dispose(GL4 gl) {
        gl.glDeleteProgram(program.program());
    }

    public void setUniform(GL4 gl, String name, Vector3f vector3f) {
        int loc = gl.glGetUniformLocation(getName(), name);
        gl.glUniform3fv(loc, 1, vector3f.get(GLBuffers.newDirectFloatBuffer(3)));
    }

    public void setUniform(GL4 gl, String name, Matrix4f matrix4f) {
        int loc = gl.glGetUniformLocation(getName(), name);
        gl.glUniformMatrix4fv(loc, 1, false, matrix4f.get(GLBuffers.newDirectFloatBuffer(16)));
    }

    public void setUniform(GL4 gl, String name, float number) {
        int loc = gl.glGetUniformLocation(getName(), name);
        gl.glUniform1f(loc, 0.6f);
    }
}
