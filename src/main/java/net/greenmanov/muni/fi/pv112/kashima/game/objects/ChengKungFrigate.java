package net.greenmanov.muni.fi.pv112.kashima.game.objects;

import net.greenmanov.muni.fi.pv112.kashima.game.GameController;
import net.greenmanov.muni.fi.pv112.kashima.materials.Materials;
import net.greenmanov.muni.fi.pv112.kashima.models.Models;
import net.greenmanov.muni.fi.pv112.kashima.opengl.drawable.Object3D;
import net.greenmanov.muni.fi.pv112.kashima.textures.Textures;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;

/**
 * Class ChengKungFrigate
 *
 * @author Lukáš Kurčík <lukas.kurcik@gmail.com>
 */
public class ChengKungFrigate extends AShip  {

    public final static float WIDTH = 4.3f, HEIGHT = .55f;
    public final static float Z_COORD = -1f;

    public final static float MAX_SPEED = 5f;
    public final static float MAX_ACCELERATION = 1.5f;
    public final static float ACCELERATION_DELTA = 0.25f;
    public final static float TURN_DELTA = 0.02f;
    public final static int SCORE = 100;

    public final static int MAX_ROCKETS = 1;
    public final static float ROCKET_RELOADING = 1/3f;
    public final static float ROCKET_SPEED = 15f;

    private final static float SCALE = .0008f;

    public ChengKungFrigate(float x, float y, GameController gameController) {
        super(x, y, gameController);
        light.setAmbient(new Vector3f(1.0f, 0.5f, 1.0f));
    }

    @Override
    protected void setShipProperties() {
        width = WIDTH;
        height = HEIGHT;
        maxSpeed = MAX_SPEED;
        maxAcceleration = MAX_ACCELERATION;
        accelerationDelta = ACCELERATION_DELTA;
        turnDelta = TURN_DELTA;
        scoreValue = SCORE;
        maxRocket = MAX_ROCKETS;
        rocketReloading = ROCKET_RELOADING;
        rocketSpeed = ROCKET_SPEED;
        scale = SCALE;
    }

    @Override
    protected void setObject3d() {
        object3D = new Object3D(Models.CHENG_KUNG_FRIGATE);
        object3D.setTexture(Textures.CHENG_KUNG_FRIGATE);
        object3D.setMaterial(Materials.SHIP);
        object3D.setScale(.0008f);
        object3D.setModel(new Matrix4f().rotateY((float) Math.toRadians(90))
                .translate(0, -1.1f, 0));
    }

    @Override
    protected void changeCoords(float x, float y) {
        object3D.getModel().translate(1 / scale * (y - this.y), 0, 1 / scale * (x - this.x));
        Vector4f v = new Vector4f(width / 2 - 0.2f,0,0,1).mul(object3D.getModel());
        light.setPosition(new Vector3f(v.x, v.y, v.z));
        this.x = x;
        this.y = y;
    }

    @Override
    public void turn(boolean positive, int number) {
        this.angle += (positive ? 1 : -1) * turnDelta * number;
        object3D.getModel().rotateY((positive ? 1 : -1) * turnDelta * number);
        light.getDirection().rotateY((positive ? 1 : -1) * turnDelta * number);
    }
}
