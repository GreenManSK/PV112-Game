package net.greenmanov.muni.fi.pv112.kashima.game.objects;

/**
 * Objects used by {@link net.greenmanov.muni.fi.pv112.kashima.game.GameController}
 *
 * @author Lukáš Kurčík <lukas.kurcik@gmail.com>
 */
public interface IGameObject {
    /**
     * Move object based on delta time
     *
     * @param deltaTime Delta time in seconds
     */
    void move(double deltaTime);

    /**
     * Do any game logic that object have based on delta time
     *
     * @param deltaTime Delta time in seconds
     */
    void logic(double deltaTime);
}
