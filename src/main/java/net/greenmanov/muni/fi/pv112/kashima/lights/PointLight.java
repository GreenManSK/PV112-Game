package net.greenmanov.muni.fi.pv112.kashima.lights;

import org.joml.Vector3f;

import java.nio.FloatBuffer;

/**
 * Point light
 *
 * @author Lukáš Kurčík <lukas.kurcik@gmail.com>
 */
public class PointLight extends ALight {

    public static final int BUFFER_SIZE = 3 + 4 * 3;

    private Vector3f position;
    private float constant;
    private float linear;
    private float quadratic;

    public PointLight(Vector3f position, float constant, float linear, float quadratic,
                      Vector3f ambient, Vector3f diffuse, Vector3f specular) {
        this.position = position;
        this.constant = constant;
        this.linear = linear;
        this.quadratic = quadratic;
        this.ambient = ambient;
        this.diffuse = diffuse;
        this.specular = specular;
    }

    public Vector3f getPosition() {
        return position;
    }

    public void setPosition(Vector3f position) {
        this.position = position;
    }

    public float getConstant() {
        return constant;
    }

    public void setConstant(float constant) {
        this.constant = constant;
    }

    public float getLinear() {
        return linear;
    }

    public void setLinear(float linear) {
        this.linear = linear;
    }

    public float getQuadratic() {
        return quadratic;
    }

    public void setQuadratic(float quadratic) {
        this.quadratic = quadratic;
    }

    @Override
    public void toBuffer(FloatBuffer buffer) {
        position.get(buffer);
        buffer.position(buffer.position() + 3);
        buffer.put(constant);
        buffer.put(linear);
        buffer.put(quadratic);
        ambient.get(buffer);
        buffer.position(buffer.position() + 3);
        diffuse.get(buffer);
        buffer.position(buffer.position() + 3);
        specular.get(buffer);
        buffer.position(buffer.position() + 3);
    }
}
