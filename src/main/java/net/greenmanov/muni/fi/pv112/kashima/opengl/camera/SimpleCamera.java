package net.greenmanov.muni.fi.pv112.kashima.opengl.camera;

import org.joml.Matrix4f;
import org.joml.Vector3f;

/**
 * Simple camera with position and target
 *
 * @author Lukáš Kurčík <lukas.kurcik@gmail.com>
 */
public class SimpleCamera implements ICamera {

    private static final Vector3f UP = new Vector3f(0f,0f,1f);

    private Vector3f position;
    private Vector3f target;

    public SimpleCamera(Vector3f position, Vector3f target) {
        this.position = position;
        this.target = target;
    }

    /**
     * Get view matrix for this camera
     */
    @Override
    public Matrix4f getView() {
        return new Matrix4f().lookAt(position, target, UP);
    }

    @Override
    public Vector3f getPosition() {
        return position;
    }

    public void setPosition(Vector3f position) {
        this.position = position;
    }

    public Vector3f getTarget() {
        return target;
    }

    public void setTarget(Vector3f target) {
        this.target = target;
    }
}
