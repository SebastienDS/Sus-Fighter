package object;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Objects;
import java.util.stream.Collectors;

public class Images {

    public enum ImageKey{
        PLAYER_1_IDLE,
        PLAYER_1_IDLE_FLIPPED,
        PLAYER_1_FIST,
        PLAYER_1_FIST_FLIPPED,
        PLAYER_1_HEAD,
        PLAYER_2_IDLE,
        PLAYER_2_IDLE_FLIPPED,
        PLAYER_2_FIST,
        PLAYER_2_FIST_FLIPPED,
        PLAYER_2_HEAD,

        FIELD;
    }

    public static final int WIDTH_HEAD = 100;
    public static final int HEIGHT_HEAD = 100;

    private final HashMap<ImageKey, Image> images;

    public Images() {
        images = new HashMap<>();
    }

    public Image get(ImageKey imageKey) {
        return images.get(imageKey);
    }

    private void loadHead(Path path, String player, boolean headFlipped) throws IOException {
        path = path.resolve(Path.of("head", "HEAD.png"));
        var name = path.getFileName().toString();
        name = name.substring(0, name.lastIndexOf('.'));
        loadImage(path, ImageKey.valueOf(player + "_" + name), WIDTH_HEAD, HEIGHT_HEAD,headFlipped);
    }

    public void loadImagePlayer(Path path, String player, int width, int height, boolean headFlipped) throws IOException {
        var paths = Files.walk(path, 1)
                .filter(Files::isRegularFile)
                .collect(Collectors.toList());
        for (var path_ : paths) {
            var name = path_.getFileName().toString();
            name = name.substring(0, name.lastIndexOf('.'));
            loadImage(path_, ImageKey.valueOf(player + "_" + name), width, height,false);
            loadImage(path_, ImageKey.valueOf(player + "_" + name + "_FLIPPED"), width, height, true);
        }
        loadHead(path, player, headFlipped);

    }

    private static BufferedImage loadImage(Path path) throws IOException {
        try( var inputStream = Files.newInputStream(path)){
            return ImageIO.read(inputStream);
        }
    }

    public void loadImage(Path path, ImageKey key, int width, int height, boolean flipped) throws IOException {
        images.compute(key, ((imageKey, image) -> {
            try {
                if(flipped){
                    return flip(resize(loadImage(path), width, height));
                }
                return resize(loadImage(path), width, height);
            } catch (IOException e) {
                e.printStackTrace();
                System.exit(1);
            }
            return null;
        }));
    }

    private static BufferedImage resize(BufferedImage image, int width, int height){
        Image tmp = image.getScaledInstance(width, height, Image.SCALE_SMOOTH);
        BufferedImage dimg = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);

        Graphics2D g2d = dimg.createGraphics();
        g2d.drawImage(tmp, 0, 0, null);
        g2d.dispose();

        return dimg;
    }

    private static BufferedImage flip(BufferedImage image) {
        // Flip the image horizontally
        var tx = AffineTransform.getScaleInstance(-1, 1);
        tx.translate(-image.getWidth(null), 0);
        var op = new AffineTransformOp(tx, AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
        image = op.filter(image, null);
        return image;
    }
}
