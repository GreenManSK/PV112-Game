package net.greenmanov.muni.fi.pv112.kashima.game.enviroment;

import com.jogamp.opengl.GL4;
import net.greenmanov.muni.fi.pv112.kashima.materials.Materials;
import net.greenmanov.muni.fi.pv112.kashima.models.ObjLoader;
import net.greenmanov.muni.fi.pv112.kashima.opengl.drawable.IDrawable;
import net.greenmanov.muni.fi.pv112.kashima.opengl.drawable.Mesh;
import net.greenmanov.muni.fi.pv112.kashima.opengl.drawable.Object3D;
import net.greenmanov.muni.fi.pv112.kashima.opengl.program.Program;
import net.greenmanov.muni.fi.pv112.kashima.textures.Textures;
import org.joml.Matrix4f;


/**
 * Class for rendering GUI into Object3D
 *
 * @author Lukáš Kurčík <lukas.kurcik@gmail.com>
 */
public class WaterPlane implements IDrawable {

    public static final float WATER_LEVEL = -1f;
    public static final float SIZE = 100f;

    private static final float[] SQUARE_VERTICES = new float[]{
            -SIZE, -SIZE, 0,
            SIZE, -SIZE, 0,
            SIZE, SIZE, 0,
            -SIZE, SIZE, 0
    };
    private static final float[] SQUARE_NORMALS = new float[]{
            0,0,1,
            0,0,1,
            0,0,1,
            0,0,1
    };
    private static final int[] SQUARE_INDICES = new int[]{0, 1, 2, 0, 3, 2};

    private GL4 gl;
    private Object3D object;

    public WaterPlane(GL4 gl) {
        this.gl = gl;
        createObject3D();
    }

    private void createObject3D() {
        Mesh mesh = new Mesh();
        mesh.addVerticies(SQUARE_VERTICES, ObjLoader.VERTICES_POSITION);
        mesh.addIndices(SQUARE_INDICES);
        mesh.addNormals(SQUARE_NORMALS, ObjLoader.NORMALS_POSITION);
        mesh.build(gl);
        object = new Object3D(mesh);
        object.setTexture(Textures.WATER);
        object.setMaterial(Materials.WATER);
    }

    @Override
    public void draw(GL4 gl, Program program) {
        object.draw(gl, program);
    }

    @Override
    public Matrix4f getModel() {
        return new Matrix4f().rotate((float) -Math.toRadians(90), 1, 0, 0).translate(0, 0, WATER_LEVEL);
    }

    @Override
    public void dispose(GL4 gl) {
        object.dispose(gl);
    }
}
