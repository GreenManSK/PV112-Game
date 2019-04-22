package net.greenmanov.muni.fi.pv112.kashima.game.objects;

import net.greenmanov.muni.fi.pv112.kashima.game.GameController;
import net.greenmanov.muni.fi.pv112.kashima.game.enviroment.WaterPlane;
import net.greenmanov.muni.fi.pv112.kashima.lights.SpotLight;
import net.greenmanov.muni.fi.pv112.kashima.materials.Materials;
import net.greenmanov.muni.fi.pv112.kashima.models.Models;
import net.greenmanov.muni.fi.pv112.kashima.opengl.drawable.Object3D;
import net.greenmanov.muni.fi.pv112.kashima.textures.Textures;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;

import java.awt.geom.AffineTransform;
import java.awt.geom.Area;
import java.awt.geom.Rectangle2D;

/**
 * Rocket shoot by ships
 *
 * @author Lukáš Kurčík <lukas.kurcik@gmail.com>
 */
public class Rocket implements IGameObject, IDrawableObject, ICollisionObject  {
    public final static float Z_COORD = -0.9f;
    public final static float WIDTH = 0.44f, HEIGHT = 0.08f; //TODO

    private GameController gameController;

    private Object3D object3D;
    private SpotLight spotLight;

    private float speed;
    private float angle;

    public Rocket(Vector3f start, float angle, float speed, GameController gameController) {
        this.angle = angle;
        this.speed = speed;
        this.gameController = gameController;
        setObject3d(start);
        addLights();
    }

    private void setObject3d(Vector3f start) {
        object3D = new Object3D(Models.ROCKET);
        object3D.setScale(.05f);
        object3D.setTexture(Textures.ROCKET);
        object3D.setMaterial(Materials.SHIP);

        object3D.setModel(
                new Matrix4f()
                .translate(start)
                .translate(0,Z_COORD, 0)
                .rotateY((float) Math.toRadians(90) + angle)
        );
    }

    private void addLights() {
        spotLight = new SpotLight(
                new Vector3f(0, 0, 0),
                1f,
                0.7f,
                1.8f,
                new Vector3f(1f, 0.05f,0),
                new Vector3f( 1f,  0.05f,0),
                new Vector3f(1f, 0.05f,0),
                new Vector3f(-1,0,0).rotateY(angle),
                (float) Math.cos(Math.toRadians(12.5)),
                (float) Math.cos(Math.toRadians(25.5))
        );
        gameController.getLightContainer().addLight(spotLight);
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
        at.rotate(-angle, x, y);
        return area.createTransformedArea(at);
    }

    @Override
    public void onCollision(ICollisionObject object) {
        destroy();
    }

    private void destroy() {
        gameController.getLightContainer().removeLight(spotLight);
        gameController.removeObject(this);
    }

    @Override
    public Object3D getObject3D() {
        return object3D;
    }

    @Override
    public void move(float deltaTime) {
        Vector3f dd = new Vector3f(0,0,speed).mul(speed * deltaTime);
        object3D.getModel().translate(dd);

        Vector4f v = new Vector4f(0,-4f,0,1).mul(object3D.getModel());
        spotLight.setPosition(new Vector3f(v.x, v.y+.1f, v.z));
    }

    @Override
    public void logic(float deltaTime) {
        if (Math.abs(spotLight.getPosition().x) > WaterPlane.SIZE
                || Math.abs(spotLight.getPosition().z) > WaterPlane.SIZE) {
            destroy();
        }
    }
}
