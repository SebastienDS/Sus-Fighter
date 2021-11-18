package mvc;



import object.Command;
import object.Command.KeyCode;
import object.Coordinate;
import object.Element;
import object.Player;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.Objects;
import java.util.List;
import java.util.Optional;

public class Game implements Runnable {

    private final static int FPS = 60;

    private final Display display;

    private Thread thread;
    private boolean running = false;

    private Duel duel;

    public Game(String title, int width, int height) {
        Objects.requireNonNull(title);
        display = new Display(title, width, height);

        var command1 = new Command();
        command1.addKeyCode(KeyCode.LEFT, KeyEvent.VK_Q)
                .addKeyCode(KeyCode.RIGHT, KeyEvent.VK_D)
                .addKeyCode(KeyCode.UP, KeyEvent.VK_Z)
                .addKeyCode(KeyCode.DOWN, KeyEvent.VK_S);

        var command2 = new Command();
        command2.addKeyCode(KeyCode.LEFT, KeyEvent.VK_LEFT)
                .addKeyCode(KeyCode.RIGHT, KeyEvent.VK_RIGHT)
                .addKeyCode(KeyCode.UP, KeyEvent.VK_UP)
                .addKeyCode(KeyCode.DOWN, KeyEvent.VK_DOWN);

        var players = List.of(
            new Player("Player 1", new Coordinate(50, 50), Element.WATER, command1),
            new Player("Player 2", new Coordinate(200, 50), Element.FIRE, command2)
        );

        players.stream().forEach(p -> display.addKeyListener(p));

        duel = new Duel(players, null, Optional.empty());
    }

    private void update() {
        duel.update();
    }

    private void render(Graphics g) {
        var w = display.getWidth();
        var h = display.getHeight();

        g.clearRect(0, 0, w, h);

        duel.display(g);
    }

    @Override
    public void run() {
        double tickDuration = 1000000000 / FPS;
        double delta = 0;
        long now;
        long lastTime = System.nanoTime();

        var g = display.getGraphics();

        while (running) {
            now = System.nanoTime();
            delta += (now - lastTime) / tickDuration;
            lastTime = now;

            if (delta >= 1) {
                update();

                render(g);
                display.render();

                delta--;
            }
        }

        g.dispose();
        stop();
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

        try {
            thread.join();
        } catch(InterruptedException e) {
            e.printStackTrace();
        }
    }
}