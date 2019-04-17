package net.greenmanov.muni.fi.pv112.kashima.models;

import de.javagl.obj.Obj;
import de.javagl.obj.ObjData;
import de.javagl.obj.ObjReader;
import de.javagl.obj.ObjUtils;
import net.greenmanov.muni.fi.pv112.kashima.opengl.drawable.Mesh;

import java.io.IOException;
import java.io.InputStream;

/**
 * Loads .obj models and create {@link Mesh} from them
 *
 * @author Lukáš Kurčík <lukas.kurcik@gmail.com>
 */
final public class ObjLoader {

    public static final int VERTICES_POSITION = 0;
    public static final int NORMALS_POSITION = 1;
    public static final int TEXTURE_COORDS_POSITION = 2;

    private ObjLoader() {
    }

    /**
     * Loads data from resource on given path and creates {@link Mesh} object
     * Mesh must be build before use
     */
    public static Mesh fromResource(String path) throws IOException {
        try (InputStream stream = ClassLoader.getSystemResourceAsStream(path)) {
            Obj obj = ObjUtils.convertToRenderable(ObjReader.read(stream));
            Mesh mesh = new Mesh();

            float[] vertices = ObjData.getVerticesArray(obj);
            float[] normals = ObjData.getNormalsArray(obj);
            float[] textureCoords = ObjData.getTexCoordsArray(obj, 2);

            if (vertices.length != 0) {
                mesh.addVerticies(vertices, VERTICES_POSITION);
            }
            if (normals.length != 0) {
                mesh.addNormals(normals, NORMALS_POSITION);
            }
            if (textureCoords.length != 0) {
                mesh.addTextureCoords(textureCoords, TEXTURE_COORDS_POSITION);
            }

            return mesh;
        }
    }
}
