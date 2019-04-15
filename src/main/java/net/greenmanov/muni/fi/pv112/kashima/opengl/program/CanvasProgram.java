package net.greenmanov.muni.fi.pv112.kashima.opengl.program;

import com.jogamp.opengl.GL3;
import net.greenmanov.muni.fi.pv112.kashima.opengl.drawable.IDrawable;

import java.util.ArrayList;
import java.util.List;

/**
 * Program for rendering on {@link net.greenmanov.muni.fi.pv112.kashima.opengl.MVPCanvas}
 *
 * @author Lukáš Kurčík <lukas.kurcik@gmail.com>
 */
public class CanvasProgram {

    private final Program program;
    private List<IDrawable> drawables;
    private BeforeDrawListener beforeDrawListener;

    public CanvasProgram(Program program) {
        this.program = program;
        drawables = new ArrayList<>();
    }

    /**
     * Draw all object in the program
     */
    public void display(GL3 gl) {
        gl.glUseProgram(program.getName());
        for (IDrawable obj : drawables) {
            if (beforeDrawListener != null) {
                beforeDrawListener.apply(program, obj);
            }
            obj.draw(gl);
        }
    }

    /**
     * Dispose program
     */
    public void dispose(GL3 gl) {
        program.dispose(gl);
    }

    public Program getProgram() {
        return program;
    }

    public List<IDrawable> getDrawables() {
        return drawables;
    }

    public void setDrawables(List<IDrawable> drawables) {
        this.drawables = drawables;
    }

    public BeforeDrawListener getBeforeDrawListener() {
        return beforeDrawListener;
    }

    /**
     * Listener that is called before every object draw function
     */
    public void setBeforeDrawListener(BeforeDrawListener beforeDrawListener) {
        this.beforeDrawListener = beforeDrawListener;
    }

    @FunctionalInterface
    public interface BeforeDrawListener {
        void apply(Program program, IDrawable drawable);
    }
}
