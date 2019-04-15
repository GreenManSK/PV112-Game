package net.greenmanov.muni.fi.pv112.kashima.opengl.drawable;

import com.jogamp.opengl.GL3;
import org.joml.Matrix4f;

/**
 * Objects that are able to be rendered by the game
 *
 * @author Lukáš Kurčík <lukas.kurcik@gmail.com>
 */
public interface IDrawable {
    /**
     * Draw the object
     */
    void draw(GL3 gl);

    /**
     * Get the object model
     */
    Matrix4f getModel();

    /**
     * Dispose of the object resources
     */
    void dispose(GL3 gl);
}
