package net.greenmanov.muni.fi.pv112.kashima.models;

import com.jogamp.opengl.GL4;
import net.greenmanov.muni.fi.pv112.kashima.opengl.drawable.Mesh;

/**
 * Class with models used by the game
 *
 * @author Lukáš Kurčík <lukas.kurcik@gmail.com>
 */
final public class Models {
    public static final Mesh TEAPOT = ObjLoader.fromResource("models/teapot.obj");

    private Models() {}

    /**
     * Build all models in the class for easier work with them
     */
    public static void buildModels(GL4 gl) {
        TEAPOT.build(gl);
    }
}
