package object;

import mvc.Display;

import java.awt.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Objects;
import java.util.stream.Collectors;

public class ImagePlayer {

    public enum ImageKey{
        IDLE,
        IDLE_FLIPPED
    }

    private final HashMap<ImageKey, Image> images;

    public ImagePlayer(Path directory) throws IOException {
        Objects.requireNonNull(directory);
        images = new HashMap<>();
        for (var path : Files.list(directory).collect(Collectors.toList())) {
            var image = ImageManager.resize(ImageManager.loadImage(path), 150, 300);
            var name = path.getFileName().toString().replaceFirst("[.][^.]+$", "");
            images.put(ImageKey.valueOf(name), image);
            images.put(ImageKey.valueOf(name + "_FLIPPED"), ImageManager.flip(image));
        }
    }


    public Image get(ImageKey imageKey) {
        return images.get(imageKey);
    }
}
