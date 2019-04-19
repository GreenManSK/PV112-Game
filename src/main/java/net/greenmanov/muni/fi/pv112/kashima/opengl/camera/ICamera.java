package net.greenmanov.muni.fi.pv112.kashima.opengl.camera;

import org.joml.Matrix4f;
import org.joml.Vector3f;

/**
 * Camera for MVP rendering model
 *
 * @author Lukáš Kurčík <lukas.kurcik@gmail.com>
 */
public interface ICamera {
    /**
     * Get view matrix for this camera
     */
    Matrix4f getView();

    /**
     * Get camera position
     */
    Vector3f getPosition();
}
