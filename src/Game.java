import javax.swing.JPanel;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class Game extends JPanel {

    private int x = 0;
    private int y = 0;
    private int size = 50;

    public Game(double width, double height) {
        setFocusable(true);
        addKeyListener(new TAdapter());

        this.x = (int)width / 2 - size / 2;
        this.y = (int)height / 2 - size / 2;
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        g.setColor(Color.PINK);
        g.fillRect(x, y, size, size);
    }

    private class TAdapter extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e) {

        }

        @Override
        public void keyReleased(KeyEvent e) {

        }
    }
}