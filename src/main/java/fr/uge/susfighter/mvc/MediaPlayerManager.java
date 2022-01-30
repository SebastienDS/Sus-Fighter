package fr.uge.susfighter.mvc;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;


public class MediaPlayerManager {
    private static MediaPlayer mediaPlayer;

    public static void play(){
        mediaPlayer.play();
    }

    public static void setSound(Media media){
        if(mediaPlayer != null) mediaPlayer.dispose();
        mediaPlayer = new MediaPlayer(media);
        mediaPlayer.play();
    }

    public static void setRepeat(boolean b) {
        if(b) mediaPlayer.setOnEndOfMedia(() -> mediaPlayer.seek(Duration.ZERO));
        else mediaPlayer.setOnEndOfMedia(()-> mediaPlayer.pause());
    }
}
