package net.greenmanov.muni.fi.pv112.kashima.game.objects;

import java.awt.geom.Area;

/**
 * Object capable of colliding with other objects
 *
 * @author Lukáš Kurčík <lukas.kurcik@gmail.com>
 */
public interface ICollisionObject {
    /**
     * Return area representing object
     */
    Area getCollisionArea();

    /**
     * Handler for collisions
     */
    void onCollision(ICollisionObject object);
}
