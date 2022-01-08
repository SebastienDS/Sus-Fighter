package fr.uge.susfighter.mvc;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class ImageManager {

    public enum ImageKey {
        PLAYER_1_IDLE,
        PLAYER_1_FIST,
        PLAYER_1_HEAD,
        PLAYER_2_IDLE,
        PLAYER_2_FIST,
        PLAYER_2_HEAD,

        FIELD;
    }

    private static final Map<ImageKey, Image> images = new HashMap<>();

    public static void loadImage(ImageKey key, String name) {
        URL imageURL = ImageManager.class.getResource(name);
        assert imageURL != null;
        Image image = new Image(imageURL.toExternalForm());

        images.put(key, image);
    }

    public static void loadImagePlayer(String name, int numPlayer) {
        var path = "images/character/" + name + "/";
        var player = "PLAYER_" + numPlayer;

        ImageManager.loadImage(ImageKey.valueOf(player + "_IDLE"), path + "IDLE.png");
        ImageManager.loadImage(ImageKey.valueOf(player + "_FIST"), path + "FIST.png");
        ImageManager.loadImage(ImageKey.valueOf(player + "_HEAD"), path + "HEAD.png");
    }

    public static Image getImage(ImageKey key) {
        return images.get(key);
    }
}
