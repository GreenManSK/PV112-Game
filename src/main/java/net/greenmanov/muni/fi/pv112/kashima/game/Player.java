package net.greenmanov.muni.fi.pv112.kashima.game;

import net.greenmanov.muni.fi.pv112.kashima.game.objects.*;
import net.greenmanov.muni.fi.pv112.kashima.opengl.camera.MovingCamera;
import net.greenmanov.muni.fi.pv112.kashima.opengl.drawable.Object3D;
import org.joml.Vector4f;

import java.awt.geom.Area;

/**
 * Class Player
 *
 * @author Lukáš Kurčík <lukas.kurcik@gmail.com>
 */
public class Player implements IGameObject, IDrawableObject, ICollisionObject {

    public static final int MAX_HP = 100;
    public static final int MAX_FUEL = 250;
    public static final int ROCKET_DMG = 25;
    public static final int FUEL_PER_SECOND = 5;

    private GameController gameController;
    private AShip ship;
    private MovingCamera camera;

    private int hp = MAX_HP;
    private float fuel = MAX_FUEL;

    public Player(GameController gameController, AShip ship, MovingCamera camera) {
        this.gameController = gameController;
        this.ship = ship;
        this.camera = camera;
    }

    @Override
    public Object3D getObject3D() {
        return ship.getObject3D();
    }

    @Override
    public void move(float deltaTime) {
        ship.move(deltaTime);
    }

    @Override
    public void logic(float deltaTime) {
        ship.logic(deltaTime);

        Vector4f shipPos =  new Vector4f().mul(getObject3D().getModel());
        camera.getPosition().x = shipPos.x;
        camera.getPosition().z = shipPos.z;

        fuel -= deltaTime * FUEL_PER_SECOND;

        //TODO: HP & Fuel check - LOST
        //TODO: Coords check - LOST
    }

    @Override
    public Area getCollisionArea() {
        return ship.getCollisionArea();
    }

    @Override
    public void onCollision(ICollisionObject object) {
        if (object instanceof AShip) {
            //TODO: Lost
        } else if (object instanceof Rocket) {
            hp -= ROCKET_DMG;
        } else if (object instanceof Barrel) {
            fuel = Math.min(MAX_FUEL, fuel + ((Barrel) object).getCapacity());
        } else if (object instanceof Wrench) {
            hp = Math.min(MAX_HP, hp + ((Wrench) object).getCapacity());
        }
    }

    public AShip getShip() {
        return ship;
    }

    public int getHp() {
        return hp;
    }

    public void setHp(int hp) {
        this.hp = hp;
    }

    public float getFuel() {
        return fuel;
    }

    public void setFuel(float fuel) {
        this.fuel = fuel;
    }
}
