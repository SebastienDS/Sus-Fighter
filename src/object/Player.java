package object;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Objects;

import mvc.Display;
import object.Command.KeyCode;
import object.ImagePlayer.ImageKey;

public class Player implements KeyListener, Displayable {

    private final String name;
    private final Coordinate coordinate;
    private final Statistic statistic;
    private final Element element;
    private final Command command;
    private final ImagePlayer images;
    private boolean isFlipped;

    private int dx = 0;
    private int dy = 0;

    public Player(String name, Coordinate coordinate, Element element, Command command, Path path, boolean isFlipped) throws IOException {
        this.name = Objects.requireNonNull(name);
        this.coordinate = Objects.requireNonNull(coordinate);
        this.element = Objects.requireNonNull(element);
        this.command = Objects.requireNonNull(command);
        statistic = new Statistic();
        this.isFlipped = isFlipped;
        images = new ImagePlayer(path);
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
        var key = e.getKeyCode();

        if (key == command.get(KeyCode.LEFT)) {
            dx = -5;
        }
        else if (key == command.get(KeyCode.RIGHT)) {
            dx = 5;
        }
        else if (key == command.get(KeyCode.UP)) {
            dy = -5;
        }
        else if (key == command.get(KeyCode.DOWN)) {
            dy = 5;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        var key = e.getKeyCode();

        if (key == command.get(KeyCode.LEFT) || key == command.get(KeyCode.RIGHT)) {
            dx = 0;
        }
        else if (key == command.get(KeyCode.UP) || key == command.get(KeyCode.DOWN)) {
            dy = 0;
        }
    }

    @Override
    public void display(Display d) {
        var g = d.getGraphics();
        g.setColor(Color.BLACK);
        var imageKey = (isFlipped) ? ImageKey.IDLE_FLIPPED : ImageKey.IDLE;
        var image = images.get(imageKey);
        g.drawImage(image, coordinate.getX(), coordinate.getY(), null);
    }

    public void update(boolean needFlip) {
        coordinate.move(dx, dy);
        if(needFlip) {
            isFlipped = !isFlipped;
        }
    }

    public boolean needFlip(Player player){
        return coordinate.getX() < player.coordinate.getX() && isFlipped || player.coordinate.getX() < coordinate.getX() && player.isFlipped;
    }
}
