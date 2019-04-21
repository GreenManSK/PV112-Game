package net.greenmanov.muni.fi.pv112.kashima.game;

import net.greenmanov.muni.fi.pv112.kashima.game.objects.ICollisionObject;

import java.awt.geom.AffineTransform;
import java.awt.geom.Area;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

/**
 * Collision detector for objects {@link net.greenmanov.muni.fi.pv112.kashima.game.objects.ICollisionObject}
 *
 * @author Lukáš Kurčík <lukas.kurcik@gmail.com>
 */
public class CollisionDetector {

    private HashSet<ICollisionObject> objects = new HashSet<>();

    public CollisionDetector() {
    }

    public void addObject(ICollisionObject object) {
        objects.add(object);
    }

    public void removeObject(ICollisionObject object) {
        objects.remove(object);
    }

    public void detect() {
        HashMap<ICollisionObject, Area> areas = new HashMap<>();
        ArrayList<ICollisionObject> objectsList = new ArrayList<>(objects);

        AffineTransform af = new AffineTransform();
        af.scale(100, 100);
        objects.forEach(obj -> areas.put(obj, obj.getCollisionArea().createTransformedArea(af)));

        ICollisionObject a, b;
        for (int i = 0; i < objectsList.size(); i++) {
            a = objectsList.get(i);
            for (int j = i + 1; j < objectsList.size(); j++) {
                b = objectsList.get(j);
                if (isCollision(areas.get(a), areas.get(b))) {
                    a.onCollision(b);
                    b.onCollision(a);
                }
            }
        }
    }

    private boolean isCollision(Area a, Area b) {
        return a.intersects(b.getBounds()) && b.intersects(a.getBounds());
    }
}
