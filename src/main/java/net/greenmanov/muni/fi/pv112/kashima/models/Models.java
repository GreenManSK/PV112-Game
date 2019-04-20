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
    public static final Mesh CAT = ObjLoader.fromResource("models/cat.obj");
    public static final Mesh CAT2 = ObjLoader.fromResource("models/12221_Cat_v1_l3.obj");
    public static final Mesh CUBE = ObjLoader.fromResource("models/cube.obj");

    private Models() {}

    /**
     * Build all models in the class for easier work with them
     */
    public static void buildModels(GL4 gl) {
        TEAPOT.build(gl);
        CAT.build(gl);
        CAT2.build(gl);
        CUBE.build(gl);
    }
}
