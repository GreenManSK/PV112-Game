package net.greenmanov.muni.fi.pv112.kashima;

import com.jogamp.opengl.GL4;
import com.jogamp.opengl.GLProfile;
import com.jogamp.opengl.util.texture.Texture;
import com.jogamp.opengl.util.texture.awt.AWTTextureIO;
import net.greenmanov.muni.fi.pv112.kashima.game.GameController;
import net.greenmanov.muni.fi.pv112.kashima.game.Player;
import net.greenmanov.muni.fi.pv112.kashima.opengl.drawable.IDrawable;
import net.greenmanov.muni.fi.pv112.kashima.opengl.drawable.Mesh;
import net.greenmanov.muni.fi.pv112.kashima.opengl.drawable.Object3D;
import net.greenmanov.muni.fi.pv112.kashima.opengl.program.Program;
import org.joml.Matrix4f;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

/**
 * Class for rendering GUI into Object3D
 *
 * @author Lukáš Kurčík <lukas.kurcik@gmail.com>
 */
public class GUI implements IDrawable {

    public static final String SOCRE_TEXT = "Score: ";
    public static final String HP_TEXT = "HP: ";
    public static final String FUEL_TEXT = "Fuel: ";
    public static final String VELOCITY_TEXT = "Speed: ";
    public static final String ACCELERATION_TEXT = "Acc: ";
    public static final String ROCKETS_TEXT = "Rocks: ";
    public static final String PAUSED_TEXT = "[Press SPACE to continue]";

    public static final Font GUI_FONT = new Font("TimesRoman", Font.BOLD, 20);
    public static final Color GUI_TEXT_COLOR = Color.WHITE;
    public static final Color GUI_BG_COLOR = new Color(0,0,0,150);


    public static final Font GUI_FONT_PAUSED = new Font("TimesRoman", Font.BOLD, 20);
    public static final Color GUI_COLOR_PAUSED = new Color(255,0,0,150);

    public static final int SCREEN_PADDING = 10;

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
    private Graphics2D ig2;

    private GL4 gl;
    private int width;
    private int height;

    private GameController gameController;
    private Player player;
    private int score;

    private BufferedImage image;

    public GUI(int width, int height, GL4 gl) {
        this.width = width;
        this.height = height;
        this.gl = gl;
        createGuiObject();
        createGraphics();
    }

    private void createGraphics() {
        if (image == null) {
            image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
            ig2 = image.createGraphics();
        }

        ig2.setFont(GUI_FONT);
        FontMetrics fontMetrics = ig2.getFontMetrics();
        int stringHeight = fontMetrics.getAscent();


        String message = SOCRE_TEXT + score;
        Rectangle2D bounds = fontMetrics.getStringBounds(message, ig2);

        ig2.setPaint(GUI_BG_COLOR);
        ig2.fill(new Rectangle((int)bounds.getWidth() + SCREEN_PADDING * 2,2*SCREEN_PADDING + stringHeight));

        ig2.setPaint(GUI_TEXT_COLOR);
        ig2.drawString(message, SCREEN_PADDING, SCREEN_PADDING + stringHeight);

        if (player != null) {
            List<String> bottomText = buildBottomText();

            ig2.setPaint(GUI_BG_COLOR);
            int h = 2*SCREEN_PADDING + bottomText.size()*stringHeight;
            ig2.fill(new Rectangle(0, height - h,2*(int)bounds.getWidth() + SCREEN_PADDING * 2,h));

            ig2.setPaint(GUI_TEXT_COLOR);
            int i = 0;
            for (String line : bottomText) {
                ig2.drawString(line, SCREEN_PADDING, height - SCREEN_PADDING - i * stringHeight);
                i += 1;
            }
        }

        if (gameController != null && gameController.isPaused()) {
            ig2.setFont(GUI_FONT_PAUSED);
            ig2.setPaint(GUI_COLOR_PAUSED);
            fontMetrics = ig2.getFontMetrics();
            stringHeight = fontMetrics.getAscent();
            bounds = fontMetrics.getStringBounds(PAUSED_TEXT, ig2);
            ig2.drawString(PAUSED_TEXT, width / 2 - (int) bounds.getWidth() / 2, height / 2 - stringHeight / 2);
        }

        setTexture();
    }

    private List<String> buildBottomText() {
        ArrayList<String> lines = new ArrayList<>();

        lines.add(createNumberLine(ROCKETS_TEXT, player.getShip().getRockets(),  player.getShip().getMaxRocket()));
        lines.add(createNumberLine(ACCELERATION_TEXT, player.getShip().getAcceleration().length(), player.getShip().getMaxAcceleration()));
        lines.add(createNumberLine(VELOCITY_TEXT, player.getShip().getVelocity().length(), player.getShip().getMaxSpeed()));
        lines.add(createNumberLine(FUEL_TEXT, (int) player.getFuel(), Player.MAX_FUEL));
        lines.add(createNumberLine(HP_TEXT, player.getHp(), Player.MAX_HP));

        return lines;
    }

    private String createNumberLine(String text, float value, float max) {
        StringBuilder sb = new StringBuilder();
        sb.append(text);
        sb.append(Math.ceil(value * 100) / 100);
        sb.append("/");
        sb.append(max);
        return sb.toString();
    }

    private String createNumberLine(String text, int value, int max) {
        StringBuilder sb = new StringBuilder();
        sb.append(text);
        sb.append(value);
        sb.append("/");
        sb.append(max);
        return sb.toString();
    }

    private void setTexture() {
        if (guiObject.getTexture() != null) {
            guiObject.getTexture().destroy(gl);
        }
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
        mesh.build(gl);
        guiObject = new Object3D(mesh);
    }

    @Override
    public void draw(GL4 gl, Program program) {
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
//        this.width = widht;
//        this.height = height;
//        redraw();
    }

    /**
     * Redraw score based on changed values
     */
    public void redraw() {
        createGraphics();
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public GameController getGameController() {
        return gameController;
    }

    public void setGameController(GameController gameController) {
        this.gameController = gameController;
    }
}
