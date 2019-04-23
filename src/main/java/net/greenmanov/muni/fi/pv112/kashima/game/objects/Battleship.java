package net.greenmanov.muni.fi.pv112.kashima.game.objects;

import net.greenmanov.muni.fi.pv112.kashima.game.GameController;
import net.greenmanov.muni.fi.pv112.kashima.materials.Materials;
import net.greenmanov.muni.fi.pv112.kashima.models.Models;
import net.greenmanov.muni.fi.pv112.kashima.opengl.drawable.Object3D;
import net.greenmanov.muni.fi.pv112.kashima.textures.Textures;
import org.joml.Matrix4f;

/**
 * Class Battleship
 *
 * @author Lukáš Kurčík <lukas.kurcik@gmail.com>
 */
public class Battleship extends AShip {

    public final static float WIDTH = 2.7f, HEIGHT = .55f;

    public final static float MAX_SPEED = 5f;
    public final static float MAX_ACCELERATION = 1.5f;
    public final static float ACCELERATION_DELTA = 0.25f;
    public final static float TURN_DELTA = 0.02f;
    public final static int SCORE = 500;

    public final static int MAX_ROCKETS = 1;
    public final static float ROCKET_RELOADING = 1/3f;
    public final static float ROCKET_SPEED = 15f;

    private final static float SCALE = 0.0001f;

    public Battleship(float x, float y, GameController gameController) {
        super(x, y, gameController);
        gameController.getLightContainer().removeLight(light);
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
        object3D = new Object3D(Models.BATTLESHIP_C);
        object3D.setTexture(Textures.BATTLESHIP_C);
        object3D.setMaterial(Materials.SHIP);
        object3D.setScale(scale);
        object3D.setModel(new Matrix4f());
        object3D.setModel(new Matrix4f().rotateX((float) Math.toRadians(-90.0)).rotateZ((float) Math.toRadians(-90.0)));
    }

    @Override
    protected void changeCoords(float x, float y) {
        object3D.getModel().translate(1 / scale * (y - this.y),1 / scale * (x - this.x),0);
        this.x = x;
        this.y = y;
    }
}
