package net.greenmanov.muni.fi.pv112.kashima.textures;

import com.jogamp.opengl.GL4;
import com.jogamp.opengl.util.texture.Texture;

/**
 * Class Textures
 *
 * @author Lukáš Kurčík <lukas.kurcik@gmail.com>
 */
final public class Textures {

    public static final Texture LUKY = TextureLoader.fromResource("textures/luky.jpg");

    private static final Texture[] textures = {LUKY};


    private Textures() {}

    /**
     * Sets default texture settings for all textures
     */
    public static void setDefaultSettings(GL4 gl) {
        for (Texture texture : textures) {
            gl.glGenerateTextureMipmap(texture.getTextureObject());
            texture.setTexParameteri(gl, GL4.GL_TEXTURE_MIN_FILTER, GL4.GL_LINEAR_MIPMAP_LINEAR);
            texture.setTexParameteri(gl, GL4.GL_TEXTURE_MAG_FILTER, GL4.GL_LINEAR);
            texture.setTexParameteri(gl, GL4.GL_TEXTURE_WRAP_S, GL4.GL_MIRRORED_REPEAT);
            texture.setTexParameteri(gl, GL4.GL_TEXTURE_WRAP_T, GL4.GL_MIRRORED_REPEAT);
        }
    }

    /**
     * Dispose of all textures
     */
    public static void disposeTextures(GL4 gl) {
        for (Texture texture : textures) {
            texture.destroy(gl);
        }
    }
}
