package net.greenmanov.muni.fi.pv112.kashima;

import com.jogamp.opengl.GL4;
import com.jogamp.opengl.GLProfile;
import com.jogamp.opengl.util.texture.Texture;
import com.jogamp.opengl.util.texture.awt.AWTTextureIO;
import net.greenmanov.muni.fi.pv112.kashima.opengl.drawable.IDrawable;
import net.greenmanov.muni.fi.pv112.kashima.opengl.drawable.Mesh;
import net.greenmanov.muni.fi.pv112.kashima.opengl.drawable.Object3D;
import net.greenmanov.muni.fi.pv112.kashima.opengl.program.Program;
import net.greenmanov.muni.fi.pv112.kashima.textures.Textures;
import org.joml.Matrix4f;

import javax.imageio.ImageIO;
import javax.xml.soap.Text;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Random;

/**
 * Class for rendering GUI into Object3D
 *
 * @author Lukáš Kurčík <lukas.kurcik@gmail.com>
 */
public class GUI implements IDrawable {

    public static final int VERTICES_POSITION = 0;
    public static final int TEXTURE_COORDS_POSITION = 1;
    private static final float[] SQUARE_VERTICES = new float[]{
            -1, -1, 0,
            1, -1, 0,
            1, 1, 0,
            -1, 1, 0
    };
    private static final int[] SQUARE_INDICES = new int[]{0, 1, 2, 0, 3, 2};
    private static final float[] TEXTURE_COORDS = new float[]{
            0, 1,
            1, 1,
            1, 0,
            0, 0,
    };

    private Object3D guiObject;
    Graphics2D ig2;

    private GL4 gl;
    private int width;
    private int height;

    private BufferedImage image;

    public GUI(int width, int height, GL4 gl) {
        this.width = width;
        this.height = height;
        this.gl = gl;
        createGuiObject();
        createGraphics();
    }

    private void createGraphics() {
        image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        ig2 = image.createGraphics();

        Font font = new Font("TimesRoman", Font.BOLD, 20);
        ig2.setFont(font);
        String message = "Kashima";
        FontMetrics fontMetrics = ig2.getFontMetrics();
        int stringHeight = fontMetrics.getAscent();
        ig2.setPaint(Color.WHITE);
        ig2.drawString(message, 10, 10 + stringHeight);

        setTexture();
        try {
            ImageIO.write(image, "PNG", new File("C:\\Users\\lukas\\OneDrive\\Downloads\\3d project\\test.PNG"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void setTexture() {
        Texture texture = AWTTextureIO.newTexture(GLProfile.get(GLProfile.GL4), image, true);
        gl.glGenerateTextureMipmap(texture.getTextureObject());
        texture.setTexParameteri(gl, GL4.GL_TEXTURE_MIN_FILTER, GL4.GL_LINEAR_MIPMAP_LINEAR);
        texture.setTexParameteri(gl, GL4.GL_TEXTURE_MAG_FILTER, GL4.GL_LINEAR);
        texture.setTexParameteri(gl, GL4.GL_TEXTURE_WRAP_S, GL4.GL_MIRRORED_REPEAT);
        texture.setTexParameteri(gl, GL4.GL_TEXTURE_WRAP_T, GL4.GL_MIRRORED_REPEAT);
        guiObject.setTexture(texture);
    }

    private void createGuiObject() {
        Mesh mesh = new Mesh();
        mesh.addVerticies(SQUARE_VERTICES, VERTICES_POSITION);
        mesh.addIndices(SQUARE_INDICES);
        mesh.addTextureCoords(TEXTURE_COORDS, TEXTURE_COORDS_POSITION);
        guiObject = new Object3D(mesh);
    }

    @Override
    public void draw(GL4 gl, Program program) {
        if (!guiObject.getMesh().isBuild()) {
            guiObject.getMesh().build(gl);
        }
        guiObject.draw(gl, program);
    }

    @Override
    public Matrix4f getModel() {
        return new Matrix4f();
    }

    @Override
    public void dispose(GL4 gl) {
        guiObject.dispose(gl);
    }

    public void reshape(int widht, int height) {
        this.width = widht;
        this.height = height;
        createGraphics();
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }
}
