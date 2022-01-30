package fr.uge.susfighter.mvc;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;


public class MediaPlayerManager {

    private static MediaPlayer mediaPlayer;
    private static boolean isMuted;

    public static void setSound(Media media){
        setSound(media, true);
    }

    public static void setSound(Media media, boolean bool){
        if(mediaPlayer != null) mediaPlayer.dispose();
        mediaPlayer = new MediaPlayer(media);
        mediaPlayer.play();
        setRepeat(bool);
        if(isMuted) mediaPlayer.setVolume(0);
    }

    public static void setRepeat(boolean b) {
        if(b) mediaPlayer.setOnEndOfMedia(() -> mediaPlayer.seek(Duration.ZERO));
        else mediaPlayer.setOnEndOfMedia(()-> mediaPlayer.pause());
    }

    public static void setPlay(boolean b){
        if(b) mediaPlayer.play();
        else mediaPlayer.pause();
    }

    public static void swapVolume(){
        isMuted = !isMuted;
        if(isMuted) mediaPlayer.setVolume(0);
        else mediaPlayer.setVolume(1);
    }

    public static boolean isMuted() {
        return isMuted;
    }
}
