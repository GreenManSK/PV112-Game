package net.greenmanov.muni.fi.pv112.kashima.lights;

import org.joml.Vector3f;

import java.nio.FloatBuffer;

/**
 * Abstract light
 *
 * @author Lukáš Kurčík <lukas.kurcik@gmail.com>
 */
abstract public class ALight {
    protected Vector3f ambient;
    protected Vector3f diffuse;
    protected Vector3f specular;

    public Vector3f getAmbient() {
        return ambient;
    }

    public void setAmbient(Vector3f ambient) {
        this.ambient = ambient;
    }

    public Vector3f getDiffuse() {
        return diffuse;
    }

    public void setDiffuse(Vector3f diffuse) {
        this.diffuse = diffuse;
    }

    public Vector3f getSpecular() {
        return specular;
    }

    public void setSpecular(Vector3f specular) {
        this.specular = specular;
    }

    abstract public void toBuffer(FloatBuffer buffer);
}
