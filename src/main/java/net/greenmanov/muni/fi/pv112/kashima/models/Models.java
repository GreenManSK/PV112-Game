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

    public static final Mesh KING_GORGE_SHIP = ObjLoader.fromResource("models/14083_WWII_ship - UK - King_George_V_Class_Battleship_v1_L1.obj");
    public static final Mesh CHENG_KUNG_FRIGATE = ObjLoader.fromResource("models/Cheng Kung class frigate ROCS FFG 1107.obj");
    public static final Mesh BATTLESHIP_C = ObjLoader.fromResource("models/10619_Battleship.obj");
    public static final Mesh BARREL = ObjLoader.fromResource("models/oildrum.obj");
    public static final Mesh ROCKET = ObjLoader.fromResource("models/AGM-114HellFire.obj");

    private Models() {}

    /**
     * Build all models in the class for easier work with them
     */
    public static void buildModels(GL4 gl) {
        TEAPOT.build(gl);
        CAT.build(gl);
        CAT2.build(gl);
        CUBE.build(gl);
        KING_GORGE_SHIP.build(gl);
        CHENG_KUNG_FRIGATE.build(gl);
        BATTLESHIP_C.build(gl);
        BARREL.build(gl);
        ROCKET.build(gl);
    }
}
