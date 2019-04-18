package net.greenmanov.muni.fi.pv112.kashima.opengl.drawable;

import com.jogamp.opengl.GL4;
import com.jogamp.opengl.util.texture.Texture;
import org.joml.Matrix4f;

/**
 * Drawable object
 *
 * @author Lukáš Kurčík <lukas.kurcik@gmail.com>
 */
public class Object3D implements IDrawable {

    private Mesh mesh;
    private Matrix4f model = new Matrix4f();
    private Texture texture;
    private float scale = 1.0f;

    public Object3D(Mesh mesh) {
        this.mesh = mesh;
    }

    @Override
    public void draw(GL4 gl) {
        if (texture != null) {
            texture.enable(gl);
            texture.bind(gl);
        }
        mesh.draw(gl);
    }

    @Override
    public void dispose(GL4 gl) {
        mesh.dispose(gl);
    }

    @Override
    public Matrix4f getModel() {
        return new Matrix4f(model).scale(scale);
    }

    public void setModel(Matrix4f model) {
        this.model = model;
    }

    public Mesh getMesh() {
        return mesh;
    }

    public void setMesh(Mesh mesh) {
        this.mesh = mesh;
    }

    public float getScale() {
        return scale;
    }

    public void setScale(float scale) {
        this.scale = scale;
    }

    public Texture getTexture() {
        return texture;
    }

    public void setTexture(Texture texture) {
        this.texture = texture;
    }
}
