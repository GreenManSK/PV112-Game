package net.greenmanov.muni.fi.pv112.kashima.opengl.drawable;

import com.google.common.primitives.Floats;
import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL4;
import com.jogamp.opengl.util.GLBuffers;
import org.joml.Matrix4f;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.Arrays;
import java.util.stream.IntStream;

import static com.jogamp.opengl.GL.GL_FLOAT;
import static com.jogamp.opengl.GL.GL_TRIANGLES;

/**
 * Simple object that only has color
 *
 * @author Lukáš Kurčík <lukas.kurcik@gmail.com>
 */
public class SimpleObject implements IDrawable {
    private float[] vertices;
    private int[] indices;
    private float[] colors;

    private Matrix4f model;

    private final IntBuffer VBO = IntBuffer.allocate(1), VAO = IntBuffer.allocate(1), EBO = IntBuffer.allocate(1);

    public SimpleObject(GL4 gl, float[] vertices, float[] colors) {
        this(gl, vertices, IntStream.rangeClosed(0, vertices.length/3 - 1).toArray(), colors);
    }

    /**
     * Constructor
     * @param gl GL4
     * @param vertices Array containing vertices with xyz coordinates
     * @param indices Order of vertices to be drawn
     * @param colors Array containing RGBA for each vertex
     */
    public SimpleObject(GL4 gl, float[] vertices, int[] indices, float[] colors) {
        this.vertices = vertices;
        this.indices = indices;
        this.colors = colors;
        createBuffers(gl);
    }

    private void createBuffers(GL4 gl) {
        gl.glGenVertexArrays(1, VAO);
        gl.glGenBuffers(1, VBO);
        gl.glGenBuffers(1, EBO);

        gl.glBindVertexArray(VAO.get(0));

        gl.glBindBuffer(GL.GL_ARRAY_BUFFER, VBO.get(0));

        FloatBuffer vertexBuffer = GLBuffers.newDirectFloatBuffer(Floats.concat(colors, vertices));
        gl.glBufferData(GL.GL_ARRAY_BUFFER, vertexBuffer.capacity() * Float.BYTES, vertexBuffer, GL.GL_STATIC_DRAW);

        gl.glBindBuffer(GL.GL_ELEMENT_ARRAY_BUFFER, EBO.get(0));
        IntBuffer indeciesBuffer = GLBuffers.newDirectIntBuffer(indices);
        gl.glBufferData(GL.GL_ELEMENT_ARRAY_BUFFER, indeciesBuffer.capacity() * Integer.BYTES, indeciesBuffer, GL.GL_STATIC_DRAW);

        gl.glVertexAttribPointer(0, 3, GL_FLOAT, false, 3 * Float.BYTES, colors.length * Float.BYTES);
        gl.glEnableVertexAttribArray(0);

        gl.glVertexAttribPointer(1, 4, GL_FLOAT, false, 4 * Float.BYTES, 0);
        gl.glEnableVertexAttribArray(1);

        gl.glBindBuffer(GL.GL_ARRAY_BUFFER, 0);
        gl.glBindVertexArray(0);
    }

    @Override
    public void draw(GL4 gl) {
        gl.glBindVertexArray(VAO.get(0));
        gl.glDrawElements(GL_TRIANGLES, indices.length, GL.GL_UNSIGNED_INT, 0);
    }

    @Override
    public Matrix4f getModel() {
        return model;
    }

    public void setModel(Matrix4f model) {
        this.model = model;
    }

    @Override
    public void dispose(GL4 gl) {
        gl.glDeleteVertexArrays(1, VAO);
        gl.glDeleteBuffers(1, VBO);
        gl.glDeleteBuffers(1, EBO);
    }
}
