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
    public static final Texture KING_GEORGE_SHIP = TextureLoader.fromResource("textures/King_George_V.jpg");
    public static final Texture CHENG_KUNG_FRIGATE = TextureLoader.fromResource("textures/Cheng Kung class frigate ROCS FFG 1107.jpg");
    public static final Texture BATTLESHIP_C = TextureLoader.fromResource("textures/BattleshipC.jpg");
    public static final Texture BARREL = TextureLoader.fromResource("textures/oildrum_col.jpg");
    public static final Texture ROCKET = TextureLoader.fromResource("textures/HellFire.png");
    public static final Texture WATER = TextureLoader.fromResource("textures/water.jpg");
    public static final Texture WRENCH = TextureLoader.fromResource("textures/wrench.jpg");

    private static final Texture[] textures = {
            LUKY, KING_GEORGE_SHIP, CHENG_KUNG_FRIGATE, BATTLESHIP_C, BARREL, ROCKET, WATER, WRENCH
    };


    private Textures() {
    }

    /**
     * Sets default texture settings for all textures
     */
    public static void setDefaultSettings(GL4 gl) {
        for (Texture texture : textures) {
            gl.glGenerateTextureMipmap(texture.getTextureObject());
            texture.setTexParameteri(gl, GL4.GL_TEXTURE_MIN_FILTER, GL4.GL_LINEAR_MIPMAP_LINEAR);
            texture.setTexParameteri(gl, GL4.GL_TEXTURE_MAG_FILTER, GL4.GL_LINEAR);
            texture.setTexParameteri(gl, GL4.GL_TEXTURE_WRAP_S, GL4.GL_REPEAT);
            texture.setTexParameteri(gl, GL4.GL_TEXTURE_WRAP_T, GL4.GL_REPEAT);
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
