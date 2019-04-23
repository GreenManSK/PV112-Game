package net.greenmanov.muni.fi.pv112.kashima.game;

import net.greenmanov.muni.fi.pv112.kashima.game.objects.*;
import org.joml.Vector3f;

import java.util.Random;

/**
 * Generates objects for game
 *
 * @author Lukáš Kurčík <lukas.kurcik@gmail.com>
 */
final public class ObjectGenerator {

    private static final Random rand = new Random();

    private ObjectGenerator() {
    }

    public static Battleship randomBattleship(GameController controller) {
        Battleship ship = new Battleship(
                (rand.nextBoolean() ? 1 : -1) * 50 * rand.nextFloat(),
                (rand.nextBoolean() ? 1 : -1) * 50 * rand.nextFloat(),
                controller);
        ship.turn(rand.nextBoolean(), rand.nextInt(360));
        ship.accelerate(rand.nextBoolean(), rand.nextInt(5));
        return ship;
    }

    public static KingGorgeShip randomKingGorge(GameController controller) {
        KingGorgeShip ship = new KingGorgeShip(
                (rand.nextBoolean() ? 1 : -1) * 50 * rand.nextFloat(),
                (rand.nextBoolean() ? 1 : -1) * 50 * rand.nextFloat(),
                controller);
        ship.turn(rand.nextBoolean(), rand.nextInt(360));
        ship.accelerate(rand.nextBoolean(), rand.nextInt(5));
        return ship;
    }

    public static ChengKungFrigate randomChengKung(GameController controller) {
        ChengKungFrigate ship = new ChengKungFrigate(
                (rand.nextBoolean() ? 1 : -1) * 50 * rand.nextFloat(),
                (rand.nextBoolean() ? 1 : -1) * 50 * rand.nextFloat(),
                controller);
        ship.turn(rand.nextBoolean(), rand.nextInt(360));
        ship.accelerate(rand.nextBoolean(), rand.nextInt(5));
        return ship;
    }

    public static Barrel randomBarrel(GameController controller) {
        return new Barrel(
                new Vector3f((rand.nextBoolean() ? 1 : -1) * 50 * rand.nextFloat(),
                        0,
                        (rand.nextBoolean() ? 1 : -1) * 50 * rand.nextFloat()
                ), controller);
    }

    public static Wrench randomWrench(GameController controller) {
        return new Wrench(
                new Vector3f((rand.nextBoolean() ? 1 : -1) * 50 * rand.nextFloat(),
                        0,
                        (rand.nextBoolean() ? 1 : -1) * 50 * rand.nextFloat()
                ), controller);
    }

    public static IGameObject randomObject(GameController gameController) {
        int i = rand.nextInt(5);
        if (i == 0) {
            return randomBarrel(gameController);
        }
        if (i == 1) {
            return randomChengKung(gameController);
        }
        if (i == 2) {
            return randomWrench(gameController);
        }
        if (i == 3) {
            return randomKingGorge(gameController);
        }

        return randomBattleship(gameController);
    }
}
