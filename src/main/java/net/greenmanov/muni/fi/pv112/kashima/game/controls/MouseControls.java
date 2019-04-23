package net.greenmanov.muni.fi.pv112.kashima.game.controls;

import com.jogamp.newt.event.MouseEvent;
import com.jogamp.newt.event.MouseListener;
import com.jogamp.newt.opengl.GLWindow;
import net.greenmanov.muni.fi.pv112.kashima.opengl.camera.MovingCamera;

import java.awt.*;

/**
 * Enable mouse controls for the Game
 *
 * @author Lukáš Kurčík <lukas.kurcik@gmail.com>
 */
public class MouseControls implements MouseListener {

    private MovingCamera camera, saveCamera;

    private boolean firstMouse = true;
    private int lastX, lastY;
    private Robot robot;
    private GLWindow window;

    public MouseControls(MovingCamera camera, GLWindow window) throws AWTException {
        this.robot = new Robot();
        this.camera = camera;
        this.window = window;
    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseMoved(MouseEvent e) {
        int xpos = e.getX();
        int ypos = e.getY();
        if (firstMouse) {
            lastX = xpos;
            lastY = ypos;
            firstMouse = false;
        }

        lastX = xpos;
        lastY = ypos;
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        if (camera == null) {
            camera = saveCamera;
            return;
        }

        int xpos = e.getX();
        int ypos = e.getY();
        if (firstMouse) {
            lastX = xpos;
            lastY = ypos;
            firstMouse = false;
        }

        float xoffset = xpos - lastX;
        float yoffset = lastY - ypos; // reversed since y-coordinates go from bottom to top
        lastX = xpos;
        lastY = ypos;

        if (camera != null) {
            camera.moseMove(xoffset, yoffset, true);
            fixMousePosition(xpos, ypos);
        }
    }

    private void fixMousePosition(int x, int y) {
        if (x > 0 && x < window.getWidth() && y > 0 && y < window.getHeight())
            return;
        saveCamera = camera;
        camera = null;
        lastX = window.getWidth()/2;
        lastY = window.getHeight()/2;
        robot.mouseMove(window.getX() + window.getWidth()/2, window.getY() + window.getHeight()/2);
    }

    @Override
    public void mouseWheelMoved(MouseEvent e) {
        float diff = -e.getRotation()[1];
        if (camera.getPosition().y + diff > 1f)
            camera.getPosition().y += diff;
    }
}
