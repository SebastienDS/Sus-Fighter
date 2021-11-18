package object;

import mvc.Display;

import java.awt.*;
import java.io.IOException;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Objects;

public class ImagePlayer {

    public enum ImageKey{
        IDLE,
        IDLE_FLIPPED
    }

    private final HashMap<ImageKey, Image> images;

    public ImagePlayer(Path directory) throws IOException {
        images = new HashMap<>();
        var image = ImageManager.resize(ImageManager.loadImage(Objects.requireNonNull(directory)), 150, 300);
        images.put(ImageKey.IDLE, image);
        images.put(ImageKey.IDLE_FLIPPED, ImageManager.flip(image));
    }


    public Image get(ImageKey imageKey) {
        return images.get(imageKey);
    }
}
