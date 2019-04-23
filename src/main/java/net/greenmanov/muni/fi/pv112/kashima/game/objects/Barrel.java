package net.greenmanov.muni.fi.pv112.kashima.game.objects;

import net.greenmanov.muni.fi.pv112.kashima.game.GameController;
import net.greenmanov.muni.fi.pv112.kashima.lights.PointLight;
import net.greenmanov.muni.fi.pv112.kashima.materials.Materials;
import net.greenmanov.muni.fi.pv112.kashima.models.Models;
import net.greenmanov.muni.fi.pv112.kashima.opengl.drawable.Object3D;
import net.greenmanov.muni.fi.pv112.kashima.textures.Textures;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector3fc;
import org.joml.Vector4f;

import java.awt.geom.AffineTransform;
import java.awt.geom.Area;
import java.awt.geom.Rectangle2D;

/**
 * Barrel of fuel
 *
 * @author Lukáš Kurčík <lukas.kurcik@gmail.com>
 */
public class Barrel implements IGameObject, IDrawableObject, ICollisionObject {

    public final static int CAPACITY = 25;
    public final static float Z_COORD = -0.9f;
    public final static float WIDTH = 0.4f, HEIGHT = 0.4f;

    private final static int ROTATION_SPEED = 90;
    private static final Vector3fc ROTATION_VECTOR = new Vector3f(.3f, 1f, .3f);

    private GameController gameController;

    private Object3D object3D;
    private PointLight light;

    public Barrel(Vector3f position, GameController gameController) {
        this.gameController = gameController;
        setObject3d(position);
        addLights(position);
    }

    private void setObject3d(Vector3f position) {
        object3D = new Object3D(Models.BARREL);
        object3D.setScale(.3f);
        object3D.setTexture(Textures.BARREL);
        object3D.setMaterial(Materials.SILVER);
        object3D.setModel(
                new Matrix4f()
                        .translate(position)
                        .translate(0,Z_COORD, 0)
        );
    }

    private void addLights(Vector3f position) {
        light = new PointLight(position,
                1f,
                0.7f,
                2.5f,
                new Vector3f(0f, 0.8f,0.1f),
                new Vector3f( 0f,  0.8f,0.1f),
                new Vector3f(0f, 0.8f,0.1f));
        gameController.getLightContainer().addLight(light);
    }

    @Override
    public Area getCollisionArea() {
        Vector4f coords = new Vector4f(0, 0, 0,1).mul(object3D.getModel());
        float x = coords.x;
        float y = coords.z;
        Area area = new Area(new Rectangle2D.Float(
                x - (WIDTH / 2),
                y - (HEIGHT / 2),
                WIDTH, HEIGHT));
        AffineTransform at = new AffineTransform();
        return area.createTransformedArea(at);
    }

    @Override
    public void onCollision(ICollisionObject object) {
        if (object instanceof Wrench || object instanceof Barrel)
            return;
        gameController.getLightContainer().removeLight(light);
        gameController.removeObject(this);
    }

    @Override
    public Object3D getObject3D() {
        return object3D;
    }

    @Override
    public void move(float deltaTime) {
        object3D.getModel()
                .rotate((float) Math.toRadians(ROTATION_SPEED) * deltaTime,ROTATION_VECTOR);
    }

    @Override
    public void logic(float deltaTime) {

    }

    public int getCapacity() {
        return CAPACITY;
    }
}
