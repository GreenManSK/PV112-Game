package net.greenmanov.muni.fi.pv112.kashima.game;

import net.greenmanov.muni.fi.pv112.kashima.game.objects.AShip;
import net.greenmanov.muni.fi.pv112.kashima.game.objects.ICollisionObject;
import net.greenmanov.muni.fi.pv112.kashima.game.objects.IDrawableObject;
import net.greenmanov.muni.fi.pv112.kashima.game.objects.IGameObject;
import net.greenmanov.muni.fi.pv112.kashima.opengl.camera.SimpleCamera;
import net.greenmanov.muni.fi.pv112.kashima.opengl.drawable.Object3D;
import org.joml.Vector4f;

import java.awt.geom.Area;

/**
 * Class Player
 *
 * @author Lukáš Kurčík <lukas.kurcik@gmail.com>
 */
public class Player implements IGameObject, IDrawableObject, ICollisionObject {

    private GameController gameController;
    private AShip ship;
    private SimpleCamera camera;

    public Player(GameController gameController, AShip ship, SimpleCamera camera) {
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
        camera.getTarget().x = shipPos.x;
        camera.getTarget().y = shipPos.y;
        camera.getTarget().z = shipPos.z;

        camera.getPosition().x = shipPos.x;
        camera.getPosition().z = shipPos.z;
    }

    @Override
    public Area getCollisionArea() {
        return ship.getCollisionArea();
    }

    @Override
    public void onCollision(ICollisionObject object) {

    }

    public AShip getShip() {
        return ship;
    }
}
