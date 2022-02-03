package fr.uge.susfighter.mvc;

import fr.uge.susfighter.object.Fighter;
import fr.uge.susfighter.object.Player;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.net.URI;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * This class manage all image loading of the game
 */
public class ImageManager {

    /**
     * Enum of all image needed during a duel
     */
    public enum ImageKey {
        /**
         * Player 1 idle mode
         */
        PLAYER_1_IDLE,
        /**
         * Player 1 fist
         */
        PLAYER_1_FIST,
        /**
         * Player 1 head for profile
         */
        PLAYER_1_HEAD,
        /**
         * Player 2 idle mode
         */
        PLAYER_2_IDLE,
        /**
         * Player 2 fist
         */
        PLAYER_2_FIST,
        /**
         * Player 2 head for profile
         */
        PLAYER_2_HEAD,
        /**
         * Map background
         */
        FIELD,
    }

    /**
     * Map saving all image needed for duel with enum of possibility as key and images as values
     */
    private static final Map<ImageKey, Image> images = new HashMap<>();

    /**
     * This method load an image with the name given and resize it with the width and height given. The image is saved in
     * the map with the key given after loading it.
     * @param key of the image
     * @param name path to the image
     * @param width of the image
     * @param height of the image
     */
    public static void loadImage(ImageKey key, String name, int width, int height) {
        Objects.requireNonNull(key);
        Objects.requireNonNull(name);
        var imageURL = ImageManager.class.getResource(name);
        assert imageURL != null;

        var image = new Image(imageURL.toExternalForm(), width, height, false, false);

        images.put(key, image);
    }

    /**
     * This method load the image of the given path and return it.
     * @param path
     * @return image loaded
     */
    public static Image loadImage(String path){
        Objects.requireNonNull(path);
        return new Image(path);
    }

    /**
     * This method load an image with the name given and save it in the map with the key given after loading it.
     * @param key of the image
     * @param name path of the image
     */
    public static void loadImage(ImageKey key, String name) {
        var imageURL = ImageManager.class.getResource(name);
        assert imageURL != null;
        var image = new Image(imageURL.toExternalForm());

        images.put(key, image);
    }

    /**
     * This method load all image of player given and save them in the map
     * @param p fighter who need his images loaded
     */
    public static void loadImagePlayer(Fighter p) {
        var path = "images/character/" + p.getType() + "/" + p.getName() + "/";
        var player = "PLAYER_" + p.getNumPlayer();
        ImageManager.loadImage(ImageKey.valueOf(player + "_IDLE"), path + "IDLE.png", (int)p.getBody().getWidth(), (int)p.getBody().getHeight());
        ImageManager.loadImage(ImageKey.valueOf(player + "_FIST"), path + "FIST.png");
        ImageManager.loadImage(ImageKey.valueOf(player + "_HEAD"), path + "HEAD.png", 75, 75);
    }

    /**
     * This method return the image saved in the map for the key given
     * @param key of the image
     * @return image of the key
     */
    public static Image getImage(ImageKey key) {
        return images.get(key);
    }
}
