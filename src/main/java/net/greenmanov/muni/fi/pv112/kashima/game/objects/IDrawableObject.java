package net.greenmanov.muni.fi.pv112.kashima.game.objects;

import net.greenmanov.muni.fi.pv112.kashima.opengl.drawable.Object3D;

/**
 * Object that can be rendered by {@link net.greenmanov.muni.fi.pv112.kashima.game.GameController}
 *
 * @author Lukáš Kurčík <lukas.kurcik@gmail.com>
 */
public interface IDrawableObject {
    Object3D getObject3D();
}
