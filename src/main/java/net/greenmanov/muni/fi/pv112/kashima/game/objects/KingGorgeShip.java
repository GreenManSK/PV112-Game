package net.greenmanov.muni.fi.pv112.kashima.game.objects;

import net.greenmanov.muni.fi.pv112.kashima.game.GameController;
import net.greenmanov.muni.fi.pv112.kashima.materials.Materials;
import net.greenmanov.muni.fi.pv112.kashima.models.Models;
import net.greenmanov.muni.fi.pv112.kashima.opengl.drawable.Object3D;
import net.greenmanov.muni.fi.pv112.kashima.textures.Textures;
import org.joml.Matrix4f;

/**
 * Class KingGorgeShip
 *
 * @author Lukáš Kurčík <lukas.kurcik@gmail.com>
 */
public class KingGorgeShip extends AShip {

    public final static float MAX_SPEED = 5f;
    public final static float MAX_ACCELERATION = 1.5f;
    public final static float ACCELERATION_DELTA = 0.25f;
    public final static float TURN_DELTA = 0.02f;
    public final static float WIDTH = 2.6f, HEIGHT = 0.3f;
    public final static float Z_COORD = -1.05f;
    public final static int SCORE = 100;

    public final static int MAX_ROCKETS = 1;
    public final static float ROCKET_RELOADING = 1/5f;
    public final static float ROCKET_SPEED = 15f;

    public KingGorgeShip(float x, float y, GameController gameController) {
        super(x, y, gameController);
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
    }

    @Override
    protected void setObject3d() {
        object3D = new Object3D(Models.KING_GORGE_SHIP);
        object3D.setTexture(Textures.KING_GEORGE_SHIP);
        object3D.setMaterial(Materials.SHIP);
        object3D.setModel(new Matrix4f().rotate((float) Math.toRadians(-90.0), 1, 0, 0)
                .translate(0, 0, Z_COORD));
    }
}
