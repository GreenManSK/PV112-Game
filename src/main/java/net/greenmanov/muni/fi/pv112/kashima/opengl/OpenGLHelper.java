package net.greenmanov.muni.fi.pv112.kashima.opengl;

import com.jogamp.opengl.GL;

import java.util.logging.Level;
import java.util.logging.Logger;

import static com.jogamp.opengl.GL.*;
import static com.jogamp.opengl.GL.GL_INVALID_FRAMEBUFFER_OPERATION;
import static com.jogamp.opengl.GL.GL_OUT_OF_MEMORY;

/**
 * Helper functions for working with OpenGL
 *
 * @author Lukáš Kurčík <lukas.kurcik@gmail.com>
 */
final public class OpenGLHelper {

    private OpenGLHelper() {}

    /**
     * Check GL for error and log any error that occurred
     */
    public  static void checkError(Logger logger, GL gl, String location) {
        int error = gl.glGetError();
        if (error != GL_NO_ERROR) {
            String errorString;
            switch (error) {
                case GL_INVALID_ENUM:
                    errorString = "GL_INVALID_ENUM";
                    break;
                case GL_INVALID_VALUE:
                    errorString = "GL_INVALID_VALUE";
                    break;
                case GL_INVALID_OPERATION:
                    errorString = "GL_INVALID_OPERATION";
                    break;
                case GL_INVALID_FRAMEBUFFER_OPERATION:
                    errorString = "GL_INVALID_FRAMEBUFFER_OPERATION";
                    break;
                case GL_OUT_OF_MEMORY:
                    errorString = "GL_OUT_OF_MEMORY";
                    break;
                default:
                    errorString = "UNKNOWN";
                    break;
            }
            logger.log(Level.SEVERE, "OpenGL Error(" + errorString + "): " + location);
        }
    }
}
