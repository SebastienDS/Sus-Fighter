package object;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Objects;
import object.Command.KeyCode;

public class Player implements KeyListener {

    private final String name;
    private final Coordinate coordinate;
    private final Statistic statistic;
    private final Element element;
    private final Command command;

    private int dx = 0;
    private int dy = 0;

    public Player(String name, Coordinate coordinate, Element element, Command command) {
        this.name = Objects.requireNonNull(name);
        this.coordinate = Objects.requireNonNull(coordinate);
        this.element = Objects.requireNonNull(element);
        this.command = Objects.requireNonNull(command);
        statistic = new Statistic();
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

    public void update() {
        coordinate.move(dx, dy);
    }

    public void display(Graphics g) {
        g.setColor(Color.BLACK);
        g.drawRect(coordinate.getX(), coordinate.getY(), 50, 50);
    }
}
