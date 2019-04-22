package net.greenmanov.muni.fi.pv112.kashima.game.controls;

import com.jogamp.newt.event.KeyEvent;
import com.jogamp.newt.event.KeyListener;
import com.jogamp.newt.opengl.GLWindow;
import net.greenmanov.muni.fi.pv112.kashima.game.GameController;
import net.greenmanov.muni.fi.pv112.kashima.game.Player;

/**
 * Enable keyboard controls for the Game
 *
 * @author Lukáš Kurčík <lukas.kurcik@gmail.com>
 */
public class KeyboardControls implements KeyListener {

    private final static short QUIT_KEY = KeyEvent.VK_ESCAPE;
    private final static short FULLSCREEN_KEY = KeyEvent.VK_F4;
    private final static short PAUSE_BUTTON = KeyEvent.VK_SPACE;

    private final GLWindow window;
    private Player player;
    private GameController gameController;

    public KeyboardControls(GLWindow window, Player player, GameController gameController) {
        this.window = window;
        this.player = player;
        this.gameController = gameController;
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
            case PAUSE_BUTTON:
                gameController.togglePause();
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
