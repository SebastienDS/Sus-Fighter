package fr.uge.susfighter.mvc;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;

/**
 * This class manage the sound of the game (music)
 */
public class MediaPlayerManager {

    /**
     * Media player to play music
     */
    private static MediaPlayer mediaPlayer;
    /**
     * True if sound of game is muted
     */
    private static boolean isMuted;

    /**
     * This method dispose of old sound if there is one and create a new media player with the new song and play it.
     * It also make it repeat itself
     * @param media sound
     */
    public static void setSound(Media media){
        setSound(media, true);
    }

    /**
     * This method dispose of old sound if there is one and create a new media player with the new song and play it.
     * It also make it repeat itself if bool. Volume is set to 0 if media is muted
     * @param media sound
     * @param bool true to repeat song indefinitely
     */
    public static void setSound(Media media, boolean bool){
        if(mediaPlayer != null) mediaPlayer.dispose();
        mediaPlayer = new MediaPlayer(media);
        mediaPlayer.play();
        setRepeat(bool);
        if(isMuted) mediaPlayer.setVolume(0);
    }

    /**
     * Make sound repeat itself at the end of the music if b
     * @param b true if need to repeat
     */
    public static void setRepeat(boolean b) {
        if(b) mediaPlayer.setOnEndOfMedia(() -> mediaPlayer.seek(Duration.ZERO));
        else mediaPlayer.setOnEndOfMedia(()-> mediaPlayer.pause());
    }

    /**
     * swap the volume of music to 1 or 0 and swap isMuted too
     */
    public static void swapVolume(){
        isMuted = !isMuted;
        if(isMuted) mediaPlayer.setVolume(0);
        else mediaPlayer.setVolume(1);
    }

    /**
     * This method return true if sound is muted
     * @return true if sound is muted
     */
    public static boolean isMuted() {
        return isMuted;
    }
}
