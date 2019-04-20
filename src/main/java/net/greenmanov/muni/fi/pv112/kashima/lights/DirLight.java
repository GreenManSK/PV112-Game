package net.greenmanov.muni.fi.pv112.kashima.lights;

import org.joml.Vector3f;

import java.nio.FloatBuffer;

/**
 * Directional light
 *
 * @author Lukáš Kurčík <lukas.kurcik@gmail.com>
 */
public class DirLight extends ALight {

    public static final int BUFFER_SIZE = 4 * 3;

    private Vector3f direction;

    public DirLight(Vector3f direction, Vector3f ambient, Vector3f diffuse, Vector3f specular) {
        this.direction = direction;
        this.ambient = ambient;
        this.diffuse = diffuse;
        this.specular = specular;
    }

    public Vector3f getDirection() {
        return direction;
    }

    public void setDirection(Vector3f direction) {
        this.direction = direction;
    }

    @Override
    public void toBuffer(FloatBuffer buffer) {
        direction.get(buffer);
        buffer.position(buffer.position() + 3);
        ambient.get(buffer);
        buffer.position(buffer.position() + 3);
        diffuse.get(buffer);
        buffer.position(buffer.position() + 3);
        specular.get(buffer);
        buffer.position(buffer.position() + 3);
    }
}
