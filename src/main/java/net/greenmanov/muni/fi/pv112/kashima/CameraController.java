package net.greenmanov.muni.fi.pv112.kashima;

import com.jogamp.newt.event.KeyEvent;
import com.jogamp.newt.event.KeyListener;
import com.jogamp.newt.event.MouseEvent;
import com.jogamp.newt.event.MouseListener;
import com.jogamp.newt.opengl.GLWindow;
import net.greenmanov.muni.fi.pv112.kashima.opengl.camera.MovementDirection;
import net.greenmanov.muni.fi.pv112.kashima.opengl.camera.MovingCamera;

/**
 * Used for controlling camera movement based on mouse and keyboard input
 *
 * @author Lukáš Kurčík <lukas.kurcik@gmail.com>
 */
public class CameraController implements KeyListener, MouseListener {

    private static final int BORDER_LIMIT = 10;

    private final GLWindow window;

    private MovingCamera camera;
    private boolean firstMouse = true;
    private int lastX, lastY;
    private double deltaTime;

    public CameraController(GLWindow window) {
        this.window = window;

    }

    public MovingCamera getCamera() {
        return camera;
    }

    public void setCamera(MovingCamera camera) {
        this.camera = camera;
    }

    public double getDeltaTime() {
        return deltaTime;
    }

    public void setDeltaTime(double deltaTime) {
        this.deltaTime = deltaTime;
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
            new Thread(window::destroy).start();
        }else if (e.getKeyCode() == KeyEvent.VK_F4) {
            window.setFullscreen(!window.isFullscreen());
        }

        if (camera == null)
            return;
        if (e.getKeyCode() == KeyEvent.VK_W) {
            camera.keyboardMove(MovementDirection.FORWARD, (float) deltaTime);
        } else if (e.getKeyCode() == KeyEvent.VK_S) {
            camera.keyboardMove(MovementDirection.BACKWARD, (float) deltaTime);
        } else if (e.getKeyCode() == KeyEvent.VK_A) {
            camera.keyboardMove(MovementDirection.LEFT, (float) deltaTime);
        } else if (e.getKeyCode() == KeyEvent.VK_D) {
            camera.keyboardMove(MovementDirection.RIGHT, (float) deltaTime);
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {

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
        moveFromBorder();
    }

    /**
     * Moves mouse from window border if needed
     */
    private void moveFromBorder() {
        if (lastX <= BORDER_LIMIT || lastY <= BORDER_LIMIT || lastX + BORDER_LIMIT >= window.getWidth() || lastY + BORDER_LIMIT >= window.getHeight()) {
            MovingCamera help = camera;
            camera = null;
            lastX = window.getWidth() / 2;
            lastY = window.getHeight() / 2;
            window.warpPointer(lastX, lastY);
            camera = help;
        }
    }

    @Override
    public void mouseDragged(MouseEvent e) {

    }

    @Override
    public void mouseWheelMoved(MouseEvent e) {
        camera.scroll(e.getRotation()[1]);
    }
}
