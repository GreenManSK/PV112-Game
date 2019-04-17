package net.greenmanov.muni.fi.pv112.kashima.opengl.program;

import com.jogamp.opengl.GL4;
import com.jogamp.opengl.util.glsl.ShaderCode;
import com.jogamp.opengl.util.glsl.ShaderProgram;
import net.greenmanov.muni.fi.pv112.kashima.Main;

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
}
