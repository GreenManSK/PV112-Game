package net.greenmanov.muni.fi.pv112.kashima.materials;

import com.jogamp.opengl.GL4;
import net.greenmanov.muni.fi.pv112.kashima.opengl.program.Program;
import org.joml.Vector3f;

/**
 * Material used for lighting of objects
 *
 * @author Lukáš Kurčík <lukas.kurcik@gmail.com>
 */
public class Material {


    public static final String AMBIENT_UNIFOMR_NAME = "material.ambient";
    public static final String DIFFUSE_UNIFOMR_NAME = "material.diffuse";
    public static final String SPECULAR_UNIFOMR_NAME = "material.specular";
    public static final String SHININESS_UNIFOMR_NAME = "material.shininess";

    private final Vector3f ambient;
    private final Vector3f diffuse;
    private final Vector3f specular;
    private final float shininess;

    public Material(Vector3f ambient, Vector3f diffuse, Vector3f specular, float shininess) {
        this.ambient = ambient;
        this.diffuse = diffuse;
        this.specular = specular;
        this.shininess = shininess;
    }

    /**
     * Bind material uniforms to program
     */
    public void bind(GL4 gl, Program program) {
        program.setUniform(gl, AMBIENT_UNIFOMR_NAME, ambient);
        program.setUniform(gl, DIFFUSE_UNIFOMR_NAME, diffuse);
        program.setUniform(gl, SPECULAR_UNIFOMR_NAME, specular);
        program.setUniform(gl, SHININESS_UNIFOMR_NAME, shininess);
    }

    public Vector3f getAmbient() {
        return ambient;
    }

    public Vector3f getDiffuse() {
        return diffuse;
    }

    public Vector3f getSpecular() {
        return specular;
    }

    public float getShininess() {
        return shininess;
    }
}
