package net.greenmanov.muni.fi.pv112.kashima.game.objects;

import net.greenmanov.muni.fi.pv112.kashima.game.GameController;
import net.greenmanov.muni.fi.pv112.kashima.game.Player;
import net.greenmanov.muni.fi.pv112.kashima.lights.SpotLight;
import net.greenmanov.muni.fi.pv112.kashima.opengl.drawable.Object3D;
import org.joml.Vector3f;
import org.joml.Vector4f;

import java.awt.geom.AffineTransform;
import java.awt.geom.Area;
import java.awt.geom.Rectangle2D;
import java.util.Random;

/**
 * Abstract ship object
 *
 * @author Lukáš Kurčík <lukas.kurcik@gmail.com>
 */
abstract public class AShip implements IGameObject, IDrawableObject, ICollisionObject {

    private static final Random RANDOM = new Random();
    private static final float LOGIC_SPEED = 0.5f;

    protected GameController gameController;

    protected Object3D object3D;
    protected SpotLight light;

    // Properties
    protected float width, height;
    protected float maxSpeed;
    protected float maxAcceleration;
    protected float accelerationDelta;
    protected float turnDelta;
    protected int scoreValue;
    protected int maxRocket;
    protected float rocketReloading;
    protected float rocketSpeed;
    protected float scale = 1;

    protected float x = 0, y = 0;
    protected float angle;
    protected float rockets;

    protected Vector3f velocity = new Vector3f();
    protected Vector3f acceleration = new Vector3f();

    protected boolean useAi = true;
    protected float time = 0;
    protected float lastRotation = 0;

    public AShip(float x, float y, GameController gameController) {
        this.gameController = gameController;
        setShipProperties();
        rockets = maxRocket;
        setObject3d();
        addLight();
        changeCoords(x,y);
    }

    abstract protected void setShipProperties();
    abstract protected void setObject3d();

    protected void addLight() {
        light = new SpotLight(
                new Vector3f(0, 0, 0),
                1f,
                0.09f,
                0.032f,
                new Vector3f(0.2f, 0.2f, 0.2f),
                new Vector3f( 0.5f, 0.5f, 0.5f),
                new Vector3f(1.0f, 1.0f, 1.0f),
                new Vector3f(1f, 0f, 0f),
                (float) Math.cos(Math.toRadians(12.5)),
                (float) Math.cos(Math.toRadians(17.5))
        );
        gameController.getLightContainer().addLight(light);
    }

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

    abstract protected void changeCoords(float x, float y);

    /**
     * Accelerate ship by value if possible
     */
    public void accelerate(boolean forward) {
        accelerate(forward, 1);
    }

    public void accelerate(boolean forward, int number) {
        Vector3f newAcceleration = new Vector3f(acceleration);
        newAcceleration.add(new Vector3f((forward ? 1 : -1) * accelerationDelta * number, 0, 0));
        if (newAcceleration.length() <= maxAcceleration) {
            acceleration = newAcceleration;
        }
    }

    public void stop() {
        acceleration = new Vector3f();
        velocity = new Vector3f();
    }

    /**
     * Turn ship by angle
     */
    public void turn(boolean positive) {
        turn(positive, 1);
    }


    /**
     * Turn ship number of times
     */
    public void turn(boolean positive, int number) {
        this.angle += (positive ? 1 : -1) * turnDelta * number;
        object3D.getModel().rotateZ((positive ? 1 : -1) * turnDelta * number);
        light.getDirection().rotateY((positive ? 1 : -1) * turnDelta * number);
    }

    @Override
    public Area getCollisionArea() {
        Vector4f coords = new Vector4f(0, 0, 0,1).mul(object3D.getModel());
        float x = coords.x;
        float y = coords.z;
        Area area = new Area(new Rectangle2D.Float(
                x - (width / 2),
                y - (height / 2),
                width, height));
        AffineTransform at = new AffineTransform();
        at.rotate(-angle, x, y);
        return area.createTransformedArea(at);
    }

    @Override
    public void onCollision(ICollisionObject object) {
        if (object instanceof Player || object instanceof Rocket) {
            gameController.addScore(scoreValue);
        }
        destroy();
    }

    protected void destroy() {
        gameController.getLightContainer().removeLight(light);
        gameController.removeObject(this);
    }

    @Override
    public void logic(float deltaTime) {
        rockets = Math.min(maxRocket, rockets + rocketReloading * deltaTime);
        if (useAi) {
            time += deltaTime;
            if ((time - lastRotation) > LOGIC_SPEED) {
                lastRotation = time;
                turn(RANDOM.nextBoolean());
            }
        }
    }

    /**
     * Shoot rocket if possible
     */
    public void shoot() {
        if (rockets >= 1) {
            rockets -= 1;
            Vector4f coords = new Vector4f(width/2 + 0.5f, 0, 0,1).mul(object3D.getModel());
            Rocket rocket = new Rocket(
                    new Vector3f(coords.x,0,coords.z),
                    angle,
                    rocketSpeed,
                    gameController);
            gameController.addObject(rocket);
        }
    }

    @Override
    public Object3D getObject3D() {
        return object3D;
    }

    public float getMaxSpeed() {
        return maxSpeed;
    }

    public float getMaxAcceleration() {
        return maxAcceleration;
    }

    public Vector3f getVelocity() {
        return velocity;
    }

    public Vector3f getAcceleration() {
        return acceleration;
    }

    public float getRockets() {
        return rockets;
    }

    public int getMaxRocket() {
        return maxRocket;
    }

    public SpotLight getLight() {
        return light;
    }

    public boolean isUseAi() {
        return useAi;
    }

    public void setUseAi(boolean useAi) {
        this.useAi = useAi;
    }
}
