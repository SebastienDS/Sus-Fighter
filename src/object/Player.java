package object;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Objects;

import mvc.Display;
import object.Command.KeyCode;
import object.ImagePlayer.ImageKey;
import org.jbox2d.common.Vec2;

public class Player implements KeyListener, Displayable {

    private final String name;
    private final Rectangle body;
    private final Statistic statistic;
    private final Element element;
    private final Command command;
    private final ImagePlayer images;
    private boolean isFlipped;

    private boolean jump = false;
    private boolean canJump = true;
    private KeyCode xMovement = KeyCode.LEFT;

    private final Vec2 velocity = new Vec2(0, 0);


    public Player(String name, Rectangle body, Element element, Command command, Path path, boolean isFlipped) throws IOException {
        this.name = Objects.requireNonNull(name);
        this.body = Objects.requireNonNull(body);
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
            velocity.x = -10;
            xMovement = KeyCode.LEFT;
            isFlipped = true;
        }
        else if (key == command.get(KeyCode.RIGHT)) {
            velocity.x = 10;
            xMovement = KeyCode.RIGHT;
            isFlipped = false;
        }

        else if (key == command.get(KeyCode.UP) && canJump) {
            velocity.y = -25;
            jump = true;
            canJump = false;
        }
        else if (key == command.get(KeyCode.DOWN) && jump) {
            if (velocity.y < 0) velocity.y = 0;
            else velocity.y += 20;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        var key = e.getKeyCode();

        if ((key == command.get(KeyCode.LEFT) || key == command.get(KeyCode.RIGHT)) && key == command.get(xMovement)) {
            velocity.x = 0;
        }
    }

    @Override
    public void display(Display d) {
        var g = d.getGraphics();
        g.setColor(Color.BLACK);
        var imageKey = (isFlipped) ? ImageKey.IDLE_FLIPPED : ImageKey.IDLE;
        var image = images.get(imageKey);

        g.drawImage(image, body.x, body.y, Color.BLACK, null);
    }

    private void manageFlip(boolean needFlip) {
        if (needFlip) {
            isFlipped = !isFlipped;
        }
    }

    public void update(boolean needFlip, Vec2 gravity, Rectangle bounds) {
        velocity.x += gravity.x;
        velocity.y += gravity.y;

        body.x += velocity.x;
        body.y += velocity.y;

        body.x = Math.min(Math.max(bounds.x, body.x), bounds.x + bounds.width - body.width);
        body.y = Math.min(Math.max(bounds.y, body.y), bounds.y + bounds.height - body.height);

        if (body.y == bounds.y + bounds.height - body.height) {
            canJump = true;
            jump = false;
        }
    }

    public boolean needFlip(Player player) {
        return body.x < player.body.x && isFlipped || player.body.x < body.x && player.isFlipped;
    }
}
