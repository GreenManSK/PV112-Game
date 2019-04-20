package net.greenmanov.muni.fi.pv112.kashima.lights;

import org.joml.Vector3f;

import java.nio.FloatBuffer;

/**
 * Spot light
 *
 * @author Lukáš Kurčík <lukas.kurcik@gmail.com>
 */
public class SpotLight extends PointLight {

    public static final int BUFFER_SIZE = PointLight.BUFFER_SIZE + 3 + 2;

    private Vector3f direction;
    private float cutOff;
    private float outerCutOff;

    public SpotLight(Vector3f position, float constant, float linear, float quadratic, Vector3f ambient, Vector3f diffuse, Vector3f specular, Vector3f direction, float cutOff, float outerCutOff) {
        super(position, constant, linear, quadratic, ambient, diffuse, specular);
        this.direction = direction;
        this.cutOff = cutOff;
        this.outerCutOff = outerCutOff;
    }

    public Vector3f getDirection() {
        return direction;
    }

    public void setDirection(Vector3f direction) {
        this.direction = direction;
    }

    public float getCutOff() {
        return cutOff;
    }

    public void setCutOff(float cutOff) {
        this.cutOff = cutOff;
    }

    public float getOuterCutOff() {
        return outerCutOff;
    }

    public void setOuterCutOff(float outerCutOff) {
        this.outerCutOff = outerCutOff;
    }

    @Override
    public void toBuffer(FloatBuffer buffer) {
        super.toBuffer(buffer);
        direction.get(buffer);
        buffer.position(buffer.position() + 3);
        buffer.put(cutOff);
        buffer.put(outerCutOff);
    }
}
