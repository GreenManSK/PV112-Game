package net.greenmanov.muni.fi.pv112.kashima.game.objects;

import net.greenmanov.muni.fi.pv112.kashima.game.GameController;
import net.greenmanov.muni.fi.pv112.kashima.opengl.drawable.Object3D;
import org.joml.Vector3f;

/**
 * Abstract ship object
 *
 * @author Lukáš Kurčík <lukas.kurcik@gmail.com>
 */
abstract public class AShip implements IGameObject, IDrawableObject {

    protected GameController gameController;

    protected Object3D object3D;

    // Properties
    protected float width, height;
    protected float maxSpeed;
    protected float maxAcceleration;
    protected float accelerationDelta;
    protected float turnDelta;

    protected float x = 0, y = 0;
    protected float angle;

    protected Vector3f velocity = new Vector3f();
    protected Vector3f acceleration = new Vector3f();

    public AShip(GameController gameController) {
        this.gameController = gameController;
        setShipProperties();
        setObject3d();
    }

    abstract protected void setShipProperties();
    abstract protected void setObject3d();

    @Override
    public void move(float deltaTime) {
        updateVelocity(deltaTime);
        changeCoords(x + velocity.x * deltaTime, y + velocity.y * deltaTime);
    }

    protected void updateVelocity(float deltaTime) {
        if (acceleration.length() == 0)
            return;
        Vector3f newVelocity = new Vector3f(velocity);
        newVelocity.add(new Vector3f(acceleration).mul(deltaTime));
        if (newVelocity.length() < maxSpeed) {
            velocity = newVelocity;
        }
    }

    protected void changeCoords(float x, float y) {
        object3D.getModel().translate(x - this.x, y - this.y, 0);
        this.x = x;
        this.y = y;
    }

    /**
     * Accelerate ship by value if possible
     */
    public void accelerate(boolean forward) {
        Vector3f newAcceleration = new Vector3f(acceleration);
        newAcceleration.add(new Vector3f((forward ? 1 : -1) * accelerationDelta, 0, 0));
        if (newAcceleration.length() <= maxAcceleration) {
            acceleration = newAcceleration;
        }
    }

    /**
     * Turn ship by angle
     */
    public void turn(boolean positive) {
        this.angle += (positive ? 1 : -1) * turnDelta;
        object3D.getModel().rotateZ((positive ? 1 : -1) * turnDelta);
    }

    @Override
    public void logic(float deltaTime) {

    }

    @Override
    public Object3D getObject3D() {
        return object3D;
    }
}
