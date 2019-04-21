package net.greenmanov.muni.fi.pv112.kashima.game.controls;

import com.jogamp.newt.event.KeyEvent;
import com.jogamp.newt.event.KeyListener;
import com.jogamp.newt.opengl.GLWindow;

/**
 * Enable keyboard controls for the Game
 *
 * @author Lukáš Kurčík <lukas.kurcik@gmail.com>
 */
public class KeyboardControls implements KeyListener {

    private final static short QUIT_KEY = KeyEvent.VK_ESCAPE;
    private final static short FULLSCREEN_KEY = KeyEvent.VK_F4;

    private final GLWindow window;

    public KeyboardControls(GLWindow window) {
        this.window = window;
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == QUIT_KEY) {
            new Thread(window::destroy).start();
        } else if (e.getKeyCode() == FULLSCREEN_KEY) {
            window.setFullscreen(!window.isFullscreen());
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }
}
