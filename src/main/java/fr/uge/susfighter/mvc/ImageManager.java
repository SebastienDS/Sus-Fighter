package fr.uge.susfighter.mvc;

import fr.uge.susfighter.object.Player;
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

    public static void loadImage(ImageKey key, String name, int width, int height) {
        var imageURL = ImageManager.class.getResource(name);
        assert imageURL != null;
        var image = new Image(imageURL.toExternalForm(), width, height, false, false);

        images.put(key, image);
    }

    public static void loadImage(ImageKey key, String name) {
        var imageURL = ImageManager.class.getResource(name);
        assert imageURL != null;
        var image = new Image(imageURL.toExternalForm());

        images.put(key, image);
    }

    public static void loadImagePlayer(Player p) {
        var path = "images/character/" + p.getName() + "/";
        var player = "PLAYER_" + p.getNumPlayer();

        ImageManager.loadImage(ImageKey.valueOf(player + "_IDLE"), path + "IDLE.png", p.getWidth(), p.getHeight());
        ImageManager.loadImage(ImageKey.valueOf(player + "_FIST"), path + "FIST.png");
        ImageManager.loadImage(ImageKey.valueOf(player + "_HEAD"), path + "HEAD.png", 75, 75);
    }

    public static Image getImage(ImageKey key) {
        return images.get(key);
    }
}
