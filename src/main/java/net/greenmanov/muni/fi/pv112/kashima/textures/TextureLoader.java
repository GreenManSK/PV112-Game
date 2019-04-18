package net.greenmanov.muni.fi.pv112.kashima.textures;

import com.google.common.io.Files;
import com.jogamp.opengl.util.texture.Texture;
import com.jogamp.opengl.util.texture.TextureIO;

import java.io.IOException;
import java.io.InputStream;

/**
 * Loads image textures
 *
 * @author Lukáš Kurčík <lukas.kurcik@gmail.com>
 */
final public class TextureLoader {

    private TextureLoader() {
    }

    /**
     * Loads texture from resources
     */
    public static Texture fromResource(String path) {
        try (InputStream stream = ClassLoader.getSystemResourceAsStream(path)) {
            return TextureIO.newTexture(stream, true, Files.getFileExtension(path));
        } catch (IOException e) {
            throw new IllegalStateException("Couldn't load texture from resources", e);
        }
    }
}
