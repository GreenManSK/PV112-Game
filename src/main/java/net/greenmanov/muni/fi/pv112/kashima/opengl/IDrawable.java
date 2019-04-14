package net.greenmanov.muni.fi.pv112.kashima.opengl;

import com.jogamp.opengl.GL3;

/**
 * Objects that are able to be rendered by the game
 *
 * @author Lukáš Kurčík <lukas.kurcik@gmail.com>
 */
public interface IDrawable {
    void draw(GL3 gl);
    void dispose(GL3 gl);
}
