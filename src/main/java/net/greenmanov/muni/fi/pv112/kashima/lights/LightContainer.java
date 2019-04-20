package net.greenmanov.muni.fi.pv112.kashima.lights;

import com.jogamp.opengl.GL4;
import com.jogamp.opengl.util.GLBuffers;
import net.greenmanov.muni.fi.pv112.kashima.opengl.program.Program;

import java.nio.Buffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Container used fro storing lights and putting them to program
 *0
 * @author Lukáš Kurčík <lukas.kurcik@gmail.com>
 */
public class LightContainer {

    public static final String DIR_LIGHTS_SIZE_UNIFORM_NAME = "DirLightsSize";
    public static final String POINT_LIGHTS_SIZE_UNIFORM_NAME = "PointLightsSize";
    public static final String SPOT_LIGHTS_SIZE_UNIFORM_NAME = "SpotLightsSize";

    public static final int DIR_LIGHTS_UNIFORM_POSITON = 1;
    public static final int POINT_LIGHTS_UNIFORM_POSITON = 2;
    public static final int SPOT_LIGHTS_UNIFORM_POSITON =3;

    private ArrayList<DirLight> dirLights = new ArrayList<>();
    private ArrayList<PointLight> pointLights = new ArrayList<>();
    private ArrayList<SpotLight> spotLights = new ArrayList<>();

    private boolean createdBuffers = false;
    private IntBuffer dirLightsBuffer;
    private IntBuffer pointLightsBuffer;
    private IntBuffer spotLightsBuffer;

    public LightContainer() {
    }

    /**
     * Bind lights to program
     */
    public void bind(GL4 gl, Program program) {
        if (!createdBuffers) {
            createBuffers(gl);
        }
        program.setUniform(gl, DIR_LIGHTS_SIZE_UNIFORM_NAME, dirLights.size());
        program.setUniform(gl, POINT_LIGHTS_SIZE_UNIFORM_NAME, pointLights.size());
        program.setUniform(gl, SPOT_LIGHTS_SIZE_UNIFORM_NAME, spotLights.size());

        gl.glBindBufferBase(GL4.GL_SHADER_STORAGE_BUFFER, DIR_LIGHTS_UNIFORM_POSITON, dirLightsBuffer.get(0));
        gl.glBindBufferBase(GL4.GL_SHADER_STORAGE_BUFFER, POINT_LIGHTS_UNIFORM_POSITON, pointLightsBuffer.get(0));
        gl.glBindBufferBase(GL4.GL_SHADER_STORAGE_BUFFER, SPOT_LIGHTS_UNIFORM_POSITON, spotLightsBuffer.get(0));

        if (dirLights.size() > 0) {
            FloatBuffer dirLightsArray = dirLightArrayBuffer();
            gl.glNamedBufferSubData(dirLightsBuffer.get(0), 0, dirLightsArray.capacity() * Float.BYTES, dirLightsArray);
        }
        if (pointLights.size() > 0) {
            FloatBuffer pointLightsArray = pointLightArrayBuffer();
            gl.glNamedBufferSubData(pointLightsBuffer.get(0), 0, pointLightsArray.capacity() * Float.BYTES, pointLightsArray);
        }
        if (spotLights.size() > 0) {
            FloatBuffer spotLightsArray = spotLightArrayBuffer();
            gl.glNamedBufferSubData(spotLightsBuffer.get(0), 0, spotLightsArray.capacity() * Float.BYTES, spotLightsArray);
        }
    }

    /**
     * Create buffers for lights
     */
    private void createBuffers(GL4 gl) {
        dirLightsBuffer = IntBuffer.allocate(1);
        gl.glCreateBuffers(1, dirLightsBuffer);
        pointLightsBuffer = IntBuffer.allocate(1);
        gl.glCreateBuffers(1, pointLightsBuffer);
        spotLightsBuffer = IntBuffer.allocate(1);
        gl.glCreateBuffers(1, spotLightsBuffer);

        if (dirLights.size() > 0) {
            FloatBuffer dirLightsArray = dirLightArrayBuffer();
            glNamedBufferStorage(gl, dirLightsBuffer.get(0), dirLightsArray.capacity() * Float.BYTES, dirLightsArray, GL4.GL_DYNAMIC_STORAGE_BIT);
        }

        if (pointLights.size() > 0) {
            FloatBuffer pointLightsArray = pointLightArrayBuffer();
            glNamedBufferStorage(gl, pointLightsBuffer.get(0), pointLightsArray.capacity() * Float.BYTES, pointLightsArray, GL4.GL_DYNAMIC_STORAGE_BIT);
        }
        if (spotLights.size() > 0) {
            FloatBuffer spotLightsArray = spotLightArrayBuffer();
            glNamedBufferStorage(gl, spotLightsBuffer.get(0), spotLightsArray.capacity() * Float.BYTES, spotLightsArray, GL4.GL_DYNAMIC_STORAGE_BIT);
        }
    }

    /**
     * GL4.glNamedBufferStorage is not supported in jogamp
     */
    private void glNamedBufferStorage(GL4 gl, int buffer, long size, Buffer data, int flags) {
        gl.glBindBuffer(GL4.GL_ARRAY_BUFFER, buffer);
        gl.glBufferStorage(GL4.GL_ARRAY_BUFFER, size, data, flags);
        gl.glBindBuffer(GL4.GL_ARRAY_BUFFER, 0);
    }

    private FloatBuffer dirLightArrayBuffer() {
        FloatBuffer buffer = GLBuffers.newDirectFloatBuffer(dirLights.size() * DirLight.BUFFER_SIZE);
        dirLights.forEach(l -> l.toBuffer(buffer));
        buffer.position(0);
        return buffer;
    }

    private FloatBuffer pointLightArrayBuffer() {
        FloatBuffer buffer = GLBuffers.newDirectFloatBuffer(pointLights.size() * PointLight.BUFFER_SIZE);
        pointLights.forEach(l -> l.toBuffer(buffer));
        buffer.position(0);
        return buffer;
    }

    private FloatBuffer spotLightArrayBuffer() {
        FloatBuffer buffer = GLBuffers.newDirectFloatBuffer(spotLights.size() * SpotLight.BUFFER_SIZE);
        spotLights.forEach(l -> l.toBuffer(buffer));
        buffer.position(0);
        return buffer;
    }

    /**
     * Dispose of created buffers
     */
    public void dispose(GL4 gl) {
        if (dirLightsBuffer != null) {
            gl.glDeleteBuffers(1, dirLightsBuffer);
            dirLightsBuffer = null;
        }
        if (spotLightsBuffer != null) {
            gl.glDeleteBuffers(1, spotLightsBuffer);
            spotLightsBuffer = null;
        }
        if (pointLightsBuffer != null) {
            gl.glDeleteBuffers(1, pointLightsBuffer);
            pointLightsBuffer = null;
        }

    }

    public void addLight(DirLight light) {
        dirLights.add(light);
    }

    public void addLight(PointLight light) {
        pointLights.add(light);
    }

    public void addLight(SpotLight light) {
        spotLights.add(light);
    }

    public void removeLight(DirLight light) {
        dirLights.remove(light);
    }

    public void removeLight(PointLight light) {
        pointLights.remove(light);
    }

    public void removeLight(SpotLight light) {
        spotLights.remove(light);
    }
}
