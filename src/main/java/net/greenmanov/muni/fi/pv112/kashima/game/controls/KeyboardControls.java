package net.greenmanov.muni.fi.pv112.kashima.game.controls;

import com.jogamp.newt.event.KeyEvent;
import com.jogamp.newt.event.KeyListener;
import com.jogamp.newt.opengl.GLWindow;
import net.greenmanov.muni.fi.pv112.kashima.game.Player;

/**
 * Enable keyboard controls for the Game
 *
 * @author Lukáš Kurčík <lukas.kurcik@gmail.com>
 */
public class KeyboardControls implements KeyListener {

    private final static short QUIT_KEY = KeyEvent.VK_ESCAPE;
    private final static short FULLSCREEN_KEY = KeyEvent.VK_F4;

    private final GLWindow window;
    private Player player;

    public KeyboardControls(GLWindow window, Player player) {
        this.window = window;
        this.player = player;
    }

    @Override
    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
            case QUIT_KEY:
                new Thread(window::destroy).start();
                break;
            case FULLSCREEN_KEY:
                window.setFullscreen(!window.isFullscreen());
                break;
            case KeyEvent.VK_A:
                player.getShip().turn(true);
                break;
            case KeyEvent.VK_D:
                player.getShip().turn(false);
                break;
            case KeyEvent.VK_W:
                player.getShip().accelerate(true);
                break;
            case KeyEvent.VK_S:
                player.getShip().accelerate(false);
                break;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }
}
