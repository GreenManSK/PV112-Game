package net.greenmanov.muni.fi.pv112.kashima.game;

import javax.sound.sampled.*;
import java.io.BufferedInputStream;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Used for playing game sounds
 *
 * @author Lukáš Kurčík <lukas.kurcik@gmail.com>
 */
final public class SoundBoard {

    private static final Logger LOGGER = Logger.getLogger(SoundBoard.class.getName());

    public static final String SHOOT_SOUND = "sound/368731__leszek-szary__shoot-4.wav";
    public static final String BONUS_SOUND = "sound/173858__jivatma07__j1bonus-mono.wav";
    public static final String EXPLOSION_SOUND = "sound/435415__v-ktor__explosion10.wav";

    private SoundBoard() {
    }

    public static void play(String path) {
        try (
                InputStream stream = ClassLoader.getSystemResourceAsStream(path);
                InputStream bufferedIn = new BufferedInputStream(stream);
                AudioInputStream audioStream = AudioSystem.getAudioInputStream(bufferedIn)
        ) {
            AudioFormat format = audioStream.getFormat();
            DataLine.Info info = new DataLine.Info(Clip.class, format);
            Clip clip = (Clip) AudioSystem.getLine(info);
            clip.open(audioStream);
            clip.start();
        } catch (Exception e) {
            LOGGER.log(Level.WARNING, "Couldn't play sound", e);
        }
    }
}
