package mvc;

import object.*;
import object.Event;

import java.awt.*;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import object.Images.ImageKey;


public class Duel implements Displayable {

    private final List<Player> players;
    private final Field map;
    private Optional<Event> currentEvent;
    private final List<WeaponCase> weapons;
    private long startTime;

    public Duel(List<Player> players, Field map, Optional<Event> currentEvent, Images images, Path mapPath) throws IOException {
        this.players = List.copyOf(Objects.requireNonNull(players));
        this.map = map;
        this.currentEvent = Objects.requireNonNull(currentEvent);
        weapons = new ArrayList<>();
        startTime = System.currentTimeMillis();
        images.loadImagePlayer(Path.of("resources", "images", "character", "purple"), "PLAYER_1",
                players.get(0).getWidth(), players.get(0).getHeight(), false);
        images.loadImagePlayer(Path.of("resources", "images", "character", "red"), "PLAYER_2",
                players.get(1).getWidth(), players.get(1).getHeight(), true);
        images.loadImage(mapPath, ImageKey.FIELD, Display.display().getWidth(), Display.display().getHeight(), false);
    }

    public long time() {
        return System.currentTimeMillis() - startTime;
    }

    public void update() {
        var player1 = players.get(0);
        var player2 = players.get(1);
        var needFlip = player1.needFlip(player2);

        player1.update(needFlip, map.gravity(), map.bounds());
        player2.update(needFlip, map.gravity(), map.bounds());
    }

    private  void drawImageWithOutline(Image image, int x, int y, int length, int height, int outline, Color color, Graphics g){
        g.setColor(color);
        g.fillRect(x - outline, y - outline, length + outline * 2, height + outline * 2);
        g.drawImage(image, x, y,Color.BLACK, null);
    }

    private void drawBar(int x, int y, int x_value, int width, int widthMax, int height, int outline,
                         Color outlineColor, Color barColor, Graphics g){
        g.setColor(outlineColor);
        g.fillRect(x - outline, y - outline, widthMax + outline * 2, height + outline * 2);
        g.setColor(Color.BLACK);
        g.fillRect(x, y, widthMax, height);
        g.setColor(barColor);
        g.fillRect(x_value, y, width, height);
    }

    private void displayHp(Player player, int sens, Display d){
        var g = d.getGraphics();
        int outline = 5;
        int length = d.getWidth() / 2 - Images.WIDTH_HEAD;
        int x = Images.WIDTH_HEAD + ((length - outline) * sens) + outline;
        int width = (int) ((length - outline) * player.percentageHpLeft());
        int x_value = length - outline - width;
        drawBar(x, outline, x_value * sens + x, width, length - outline,
                Images.HEIGHT_HEAD / 2 - outline, outline, Color.ORANGE, Color.RED, g);

    }

    private void displayEnergy(Player player, int sens, Display d) {
        var g = d.getGraphics();
        int outline = 5;
        int length = d.getWidth() / 2 - Images.WIDTH_HEAD;
        int length2 = d.getWidth() / 4 - Images.WIDTH_HEAD;
        int x = Images.WIDTH_HEAD + ((length * 2 - length2)  * sens) + outline;
        int width = (int) ((length2 - outline) * player.percentageEnergy());
        int x_value = length2 - 2 * outline - width ;
        drawBar(x, Images.HEIGHT_HEAD / 2 + outline * 4,  sens * x_value + x, width,
                length2 - 2 * outline, Images.HEIGHT_HEAD / 4, outline, Color.ORANGE, Color.BLUE, g);
    }

    private void display_bar(List<Player> players, Display d, Images images){
        Graphics g = d.getGraphics();
        for (int i = 0; i < players.size(); i++) {
            var str = "PLAYER_" + players.get(i).getNumPlayer() + "_HEAD";
            drawImageWithOutline(images.get(ImageKey.valueOf(str)), i * (d.getWidth() - Images.WIDTH_HEAD), 0,
                    Images.WIDTH_HEAD, Images.HEIGHT_HEAD, 5, Color.ORANGE, g);
            displayHp(players.get(i), i, d);
            displayEnergy(players.get(i), i, d);
        }
    }

    @Override
    public void display(Display d, Images images) {
        map.display(d, images);
        players.forEach(p -> p.display(d, images));
        display_bar(players, d, images);
    }
}