package net.greenmanov.muni.fi.pv112.kashima.materials;

import org.joml.Vector3f;

/**
 * Materials
 *
 * @author Lukáš Kurčík <lukas.kurcik@gmail.com>
 */
final public class Materials {
    public static final Material NONE = new Material(
            new Vector3f(0f, 0f, 0f),
            new Vector3f(0, 0, 0),
            new Vector3f(0, 0, 0),
            0f
    );
    public static final Material SILVER = new Material(
            new Vector3f(0.19225f, 0.19225f, 0.19225f),
            new Vector3f(0.50754f, 0.50754f, 0.50754f),
            new Vector3f(0.508273f, 0.508273f, 0.508273f),
            0.4f
    );
    public static final Material SHIP = new Material(
            new Vector3f(0, 0, 0),
            new Vector3f(0, 0, 0),
            new Vector3f(0.7f, 0.7f, 0.7f),
            5f
    );

    private Materials() {
    }
}
