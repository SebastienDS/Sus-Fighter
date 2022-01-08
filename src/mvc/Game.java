package mvc;



import object.*;
import object.Command.KeyCode;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Objects;
import java.util.List;
import java.util.Optional;

public class Game implements Runnable {

    private final static int FPS = 60;

    private final Display display;
    private final Images images;
    private int menu = 2;

    private Thread thread;
    private boolean running = false;

    private Duel duel;

    public Game(String title, int width, int height) throws IOException {
        Objects.requireNonNull(title);
        display = new Display(title, width, height);

        var command1 = new Command();
        command1.addKeyCode(KeyCode.LEFT, KeyEvent.VK_Q)
                .addKeyCode(KeyCode.RIGHT, KeyEvent.VK_D)
                .addKeyCode(KeyCode.UP, KeyEvent.VK_Z)
                .addKeyCode(KeyCode.DOWN, KeyEvent.VK_S)
                .addKeyCode(KeyCode.ATTACK, KeyEvent.VK_T);

        var command2 = new Command();
        command2.addKeyCode(KeyCode.LEFT, KeyEvent.VK_LEFT)
                .addKeyCode(KeyCode.RIGHT, KeyEvent.VK_RIGHT)
                .addKeyCode(KeyCode.UP, KeyEvent.VK_UP)
                .addKeyCode(KeyCode.DOWN, KeyEvent.VK_DOWN)
                .addKeyCode(KeyCode.ATTACK, KeyEvent.VK_NUMPAD1);

        var map = new Field(Element.WATER, new ArrayList<>(), 0.95f * display.getHeight());

        var players = List.of(
            new Player("Player 1", new Rectangle(150, 150, 150, 300),
                    Element.WATER, command1, 1, false),
            new Player("Player 2",  new Rectangle(600, 150, 150, 300),
                    Element.FIRE, command2, 2, true)
        );

        players.forEach(display::addKeyListener);
        images = new Images();
        duel = new Duel(players, map, Optional.empty(), images, 90, Path.of("resources", "images", "map", "UwU.jpg"));
    }

    private boolean update() {
        return duel.update();
    }

    private void render(Graphics g, Images images) {
        var w = display.getWidth();
        var h = display.getHeight();

        g.clearRect(0, 0, w, h);

        duel.display(display, images);
    }

    @Override
    public void run() {
        double tickDuration = 1000000000.0 / (double)FPS;
        double delta = 0;
        long now;
        long lastTime = System.nanoTime();

        var g = display.getGraphics();
        var dead = false;

        while (running && !dead) {
            now = System.nanoTime();
            delta += (now - lastTime) / tickDuration;
            lastTime = now;
            if (delta >= 1) {
                dead = update();
                show_menu(g);

                delta--;
            }
        }

        System.out.println("A player is dead");

        display.dispose();
        stop();
    }

    private void show_menu(Graphics g) {
        if (menu == 0) {

        }
        else if (menu == 1) {

        }
        else if (menu == 2) {
            render(g, images);
            display.render();
        }
    }

    public synchronized void start() {
        if (running) return;
        running = true;

        thread = new Thread(this);
        thread.start();
    }

    public synchronized void stop() {
        if (!running) return;
        running = false;

        thread.interrupt();
        // TODO: properly stop the thread ? A simple join does not seem to work as desired
    }
}