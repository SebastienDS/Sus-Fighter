import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Objects;

public class Game implements Runnable {

    private final static int FPS = 60;

    private final Display display;

    private Thread thread;
    private boolean running = false;
    private final Rectangle rect;
    private int dx = 0;
    private int dy = 0;

    public Game(String title, int width, int height) {
        Objects.requireNonNull(title);
        display = new Display(title, width, height);

        display.addKeyListener(new KeyAdapter());
        rect = new Rectangle((width + 50) / 2, (height + 50) / 2, 50, 50);
    }

    private void update() {
        rect.translate(dx, dy);
    }

    private void render(Graphics g) {
        var w = display.getWidth();
        var h = display.getHeight();

        g.clearRect(0, 0, w, h);

        g.setColor(Color.BLACK);
        g.drawRect(rect.x, rect.y, rect.width, rect.height);

        System.out.println(rect);
    }

    private class KeyAdapter implements KeyListener {
        @Override
        public void keyTyped(KeyEvent e) {
        }

        @Override
        public void keyPressed(KeyEvent e) {
            var key = e.getKeyCode();

            switch (key) {
                case KeyEvent.VK_LEFT -> dx = -5;
                case KeyEvent.VK_RIGHT -> dx = 5;
                case KeyEvent.VK_UP -> dy = -5;
                case KeyEvent.VK_DOWN -> dy = 5;
            }
        }

        @Override
        public void keyReleased(KeyEvent e) {
            var key = e.getKeyCode();

            switch (key) {
                case KeyEvent.VK_LEFT, KeyEvent.VK_RIGHT -> dx = 0;
                case KeyEvent.VK_UP, KeyEvent.VK_DOWN -> dy = 0;
            }
        }
    }

    @Override
    public void run() {
        double tickDuration = 1000000000 / FPS;
        double delta = 0;
        long now;
        long lastTime = System.nanoTime();

        while (running) {
            now = System.nanoTime();
            delta += (now - lastTime) / tickDuration;
            lastTime = now;

            if (delta >= 1) {
                update();

                var g = display.getGraphics();
                render(g);
                g.dispose();

                display.render();

                delta--;
            }
        }

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