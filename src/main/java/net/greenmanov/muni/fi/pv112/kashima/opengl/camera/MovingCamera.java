package net.greenmanov.muni.fi.pv112.kashima.opengl.camera;

import org.joml.Matrix4f;
import org.joml.Vector3f;

/**
 * Camera that is able to move using keyboard and mouse
 * Base on https://learnopengl.com/code_viewer_gh.php?code=includes/learnopengl/camera.h
 *
 * @author Lukáš Kurčík <lukas.kurcik@gmail.com>
 */
public class MovingCamera implements ICamera {

    private final static float YAW = -90.0f;
    private final static float PITCH = 0.0f;
    private final static float SPEED = 2.5f;
    private final static float SENSITIVITY = 0.1f;
    private final static float ZOOM = 45.0f;

    // Camera Attributes
    private Vector3f position;
    private Vector3f front;
    private Vector3f up;
    private Vector3f right;
    private Vector3f worldUp;

    // Euler Angles
    private float yaw;
    private float pitch;

    // Camera options
    private float movementSpeed;
    private float mouseSensitivity;
    private float zoom;

    // Listeners
    private ZoomChangeListener zoomChangeListener;

    public MovingCamera() {
        this(new Vector3f(0.0f, 0.0f, 0.0f), new Vector3f(0.0f, 1.0f, 0.0f), YAW, PITCH);
    }

    public MovingCamera(Vector3f position, Vector3f worldUp, float yaw, float pitch) {
        this.position = position;
        this.worldUp = worldUp;
        this.yaw = yaw;
        this.pitch = pitch;

        this.front = new Vector3f(0.0f, 0.0f, -1.0f);
        this.movementSpeed = SPEED;
        this.mouseSensitivity = SENSITIVITY;
        this.zoom = ZOOM;
        updateCameraVectors();
    }

    @Override
    public Matrix4f getView() {
        return new Matrix4f().lookAt(position, new Vector3f(position).add(front), up);
    }

    public void keyboardMove(MovementDirection direction, float deltaTime) {
        float velocity = movementSpeed * deltaTime;
        switch (direction) {
            case FORWARD:
                position.add(new Vector3f(front).mul(velocity));
                break;
            case BACKWARD:
                position.sub(new Vector3f(front).mul(velocity));
                break;
            case LEFT:
                position.sub(new Vector3f(right).mul(velocity));
                break;
            default:
                position.add(new Vector3f(right).mul(velocity));
        }
    }

    public void moseMove(float xoffset, float yoffset, boolean constrainPitch) {
        xoffset *= mouseSensitivity;
        yoffset *= mouseSensitivity;

        yaw += xoffset;
        pitch += yoffset;

        // Make sure that when pitch is out of bounds, screen doesn't get flipped
        if (constrainPitch) {
            if (pitch > 89.0f)
                pitch = 89.0f;
            if (pitch < -89.0f)
                pitch = -89.0f;
        }

        // Update Front, Right and Up Vectors using the updated Euler angles
        updateCameraVectors();
    }

    public void scroll(float yoffset) {
        if (zoom >= 1.0f && zoom <= 45.0f)
            zoom -= yoffset;
        if (zoom <= 1.0f)
            zoom = 1.0f;
        if (zoom >= 45.0f)
            zoom = 45.0f;
        if (zoomChangeListener != null) {
            zoomChangeListener.readZoom(zoom);
        }
    }

    private void updateCameraVectors() {
        Vector3f newFront = new Vector3f();
        newFront.x = (float) (Math.cos(Math.toRadians(yaw)) * Math.cos(Math.toRadians(pitch)));
        newFront.y = (float) Math.sin(Math.toRadians(pitch));
        newFront.z = (float) (Math.sin(Math.toRadians(yaw)) * Math.cos(Math.toRadians(pitch)));
        newFront = newFront.normalize();

        Vector3f newRight = new Vector3f(newFront).cross(worldUp).normalize();
        Vector3f newUp = new Vector3f(newRight).cross(newFront).normalize();

        front = newFront;
        right = newRight;
        up = newUp;
    }

    public ZoomChangeListener getZoomChangeListener() {
        return zoomChangeListener;
    }

    public void setZoomChangeListener(ZoomChangeListener zoomChangeListener) {
        this.zoomChangeListener = zoomChangeListener;
    }

    public float getMovementSpeed() {
        return movementSpeed;
    }

    public void setMovementSpeed(float movementSpeed) {
        this.movementSpeed = movementSpeed;
    }

    public float getMouseSensitivity() {
        return mouseSensitivity;
    }

    public void setMouseSensitivity(float mouseSensitivity) {
        this.mouseSensitivity = mouseSensitivity;
    }

    @Override
    public Vector3f getPosition() {
        return position;
    }

    @FunctionalInterface
    public interface ZoomChangeListener {
        void readZoom(float zoom);
    }
}
