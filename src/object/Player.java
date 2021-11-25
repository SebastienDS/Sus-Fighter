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
    private boolean jump;
    private final int width;
    private final int height;

    private double dx = 0;
    private double dy = 0;

    public Player(String name, Coordinate coordinate, Element element, Command command, Path path, boolean isFlipped) throws IOException {
        this.name = Objects.requireNonNull(name);
        this.coordinate = Objects.requireNonNull(coordinate);
        this.element = Objects.requireNonNull(element);
        this.command = Objects.requireNonNull(command);
        statistic = new Statistic();
        this.isFlipped = isFlipped;
        images = new ImagePlayer(path);
        width = images.get(ImageKey.IDLE).getWidth(null);
        height = images.get(ImageKey.IDLE).getHeight(null);
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
        else if (key == command.get(KeyCode.UP) && !jump) {
            dy = -50;
            jump = true;
            System.out.println(jump);
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

    public void update(boolean needFlip, double floorHeight) {
        dy += 1;
        coordinate.move(dx, dy, 0, Display.display().getWidth() - width, 0, floorHeight - height);
        if(needFlip) {
            isFlipped = !isFlipped;
        }
        if(jump && (int) (floorHeight - height) <= coordinate.getY()){
            jump = !jump;
        }
    }

    public boolean needFlip(Player player){
        return coordinate.getX() < player.coordinate.getX() && isFlipped || player.coordinate.getX() < coordinate.getX() && player.isFlipped;
    }
}
