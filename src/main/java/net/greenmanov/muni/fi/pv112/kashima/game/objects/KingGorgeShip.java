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

    // TODO: Set to real values
    public final static float MAX_SPEED = 4f;
    public final static float MAX_ACCELERATION = 1f;
    public final static float ACCELERATION_DELTA = 0.25f;
    public final static float TURN_DELTA = 0.02f;
    public final static int WIDTH = 20, HEIGHT = 60;

    public KingGorgeShip(GameController gameController) {
        super(gameController);
    }

    @Override
    protected void setShipProperties() {
        width = WIDTH;
        height = HEIGHT;
        maxSpeed = MAX_SPEED;
        maxAcceleration = MAX_ACCELERATION;
        accelerationDelta = ACCELERATION_DELTA;
        turnDelta = TURN_DELTA;
    }

    @Override
    protected void setObject3d() {
        object3D = new Object3D(Models.KING_GORGE_SHIP);
        object3D.setTexture(Textures.KING_GEORGE_SHIP);
        object3D.setMaterial(Materials.SHIP);
        object3D.setModel(new Matrix4f().rotate((float) Math.toRadians(-90.0), 1, 0, 0));
    }
}
