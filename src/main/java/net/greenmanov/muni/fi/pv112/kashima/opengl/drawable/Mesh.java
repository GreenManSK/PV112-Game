package net.greenmanov.muni.fi.pv112.kashima.opengl.drawable;

import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL4;
import com.jogamp.opengl.util.GLBuffers;
import net.greenmanov.muni.fi.pv112.kashima.opengl.program.Program;
import org.joml.Matrix4f;

import java.nio.Buffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

/**
 * Model Mesh
 *
 * @author Lukáš Kurčík <lukas.kurcik@gmail.com>
 */
public class Mesh implements IDrawable {

    // Arrays with vectors
    private float[] vertices;
    private float[] normals;
    private float[] textureCoords;
    private int[] indices;

    // Layout locations
    private int verticesLocation;
    private int normalsLocation;
    private int textureCoordsLocation;

    // Rendering mode
    private int mode = GL.GL_TRIANGLES;

    // Buffers
    private IntBuffer vao;
    private IntBuffer verticesBuffer;
    private IntBuffer normalsBuffer;
    private IntBuffer textureCoordsBuffer;
    private IntBuffer indicesBuffer;

    // Mesh info
    private boolean build = false;

    public Mesh() {
    }

    public void addVerticies(float[] vertices, int location) {
        this.vertices = vertices;
        verticesLocation = location;
    }

    public void addNormals(float[] normals, int location) {
        this.normals = normals;
        normalsLocation = location;
    }

    public void addTextureCoords(float[] textureCoords, int location) {
        this.textureCoords = textureCoords;
        textureCoordsLocation = location;
    }

    public void addIndices(int[] indices) {
        this.indices = indices;
    }

    /**
     * Create VAO and buffers
     */
    public void build(GL4 gl) {
        vao = IntBuffer.allocate(1);
        gl.glCreateVertexArrays(1, vao);

        if (vertices != null) {
            verticesBuffer = IntBuffer.allocate(1);
            buildBuffer(gl, verticesBuffer, vertices, verticesLocation, 3);
        }

        if (normals != null) {
            normalsBuffer = IntBuffer.allocate(1);
            buildBuffer(gl, normalsBuffer, normals, normalsLocation, 3);
        }

        if (textureCoords != null) {
            textureCoordsBuffer = IntBuffer.allocate(1);
            buildBuffer(gl, textureCoordsBuffer, textureCoords, textureCoordsLocation, 2);
        }

        if (indices != null) {
            indicesBuffer = IntBuffer.allocate(1);
            gl.glCreateBuffers(1, indicesBuffer);
            IntBuffer data = GLBuffers.newDirectIntBuffer(indices);
            glNamedBufferStorage(gl, indicesBuffer.get(0), data.capacity() * Integer.BYTES, data, GL4.GL_DYNAMIC_STORAGE_BIT);
            gl.glVertexArrayElementBuffer(vao.get(0), indicesBuffer.get(0));
        }
        build = true;
    }

    private void buildBuffer(GL4 gl, IntBuffer buffer, float[] values, int location, int size) {
        gl.glCreateBuffers(1, buffer);
        FloatBuffer data = GLBuffers.newDirectFloatBuffer(values);
        glNamedBufferStorage(gl, buffer.get(0), data.capacity() * Float.BYTES, data, GL4.GL_DYNAMIC_STORAGE_BIT);

        gl.glVertexArrayVertexBuffer(vao.get(0), location, buffer.get(0), 0, size * Float.BYTES);
        gl.glEnableVertexArrayAttrib(vao.get(0), location);
        gl.glVertexArrayAttribFormat(vao.get(0), location, size, GL.GL_FLOAT, false, 0);
        gl.glVertexArrayAttribBinding(vao.get(0), location, location);
    }

    /**
     * GL4.glNamedBufferStorage is not supported in jogamp
     */
    private void glNamedBufferStorage(GL4 gl, int buffer, long size, Buffer data, int flags) {
        gl.glBindBuffer(GL4.GL_ARRAY_BUFFER, buffer);
        gl.glBufferStorage(GL4.GL_ARRAY_BUFFER, size, data, flags);
        gl.glBindBuffer(GL4.GL_ARRAY_BUFFER, 0);
    }

    @Override
    public void draw(GL4 gl, Program program) {
        if (!build) {
            throw new IllegalStateException("Mesh not build before drawing");
        }

        gl.glBindVertexArray(vao.get(0));
        if (indicesBuffer != null) {
            gl.glDrawElements(mode, indices.length, GL.GL_UNSIGNED_INT, 0);
        } else {
            gl.glDrawArrays(mode, 0, vertices.length);
        }
    }

    @Override
    public Matrix4f getModel() {
        return new Matrix4f();
    }

    @Override
    public void dispose(GL4 gl) {
        if (vao != null) {
            gl.glDeleteVertexArrays(1, vao);
        }
        if (verticesBuffer != null) {
            gl.glDeleteBuffers(1, verticesBuffer);
        }
        if (normalsBuffer != null) {
            gl.glDeleteBuffers(1, normalsBuffer);
        }
        if (textureCoordsBuffer != null) {
            gl.glDeleteBuffers(1, textureCoordsBuffer);
        }
        if (indicesBuffer != null) {
            gl.glDeleteBuffers(1, indicesBuffer);
        }
    }

    public int getMode() {
        return mode;
    }

    public void setMode(int mode) {
        this.mode = mode;
    }

    public boolean isBuild() {
        return build;
    }
}
