package net.greenmanov.muni.fi.pv112.kashima.game.controls;

import com.jogamp.newt.event.MouseEvent;
import com.jogamp.newt.event.MouseListener;
import net.greenmanov.muni.fi.pv112.kashima.opengl.camera.MovingCamera;

/**
 * Enable mouse controls for the Game
 *
 * @author Lukáš Kurčík <lukas.kurcik@gmail.com>
 */
public class MouseControls implements MouseListener {

    private MovingCamera camera;

    private boolean firstMouse = true;
    private int lastX, lastY;

    public MouseControls(MovingCamera camera) {
        this.camera = camera;
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
        if (camera == null)
            return;

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

        camera.moseMove(xoffset, yoffset, true);
    }

    @Override
    public void mouseWheelMoved(MouseEvent e) {
        float diff = -e.getRotation()[1];
        if (camera.getPosition().y + diff > 1f)
            camera.getPosition().y += diff;
    }
}
