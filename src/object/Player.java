package object;

import mvc.Display;
import object.Command.KeyCode;
import object.Images.ImageKey;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;
import java.util.Objects;

public class Player implements KeyListener, Displayable {

    private static final int ATTACK_DISTANCE = 100;

    private final String name;
    private final int numPlayer;
    private final Rectangle body;
    private final Statistic statistic;
    private final Element element;
    private final Command command;
    private boolean isFlipped;

    private boolean jump = false;
    private boolean canJump = true;
    private KeyCode xMovement = KeyCode.LEFT;

    private final Vec2 velocity = new Vec2(0, 0);

    private final Vec2 attackVelocity = new Vec2(15, 0);
    private final Vec2 attackPosition = new Vec2(0, 0);
    private boolean isAttacking = false;


    public Player(String name, Rectangle body, Element element, Command command, int numPlayer, boolean isFlipped) throws IOException {
        this.name = Objects.requireNonNull(name);
        this.body = Objects.requireNonNull(body);
        this.element = Objects.requireNonNull(element);
        this.command = Objects.requireNonNull(command);
        statistic = new Statistic();
        this.isFlipped = isFlipped;
        this.numPlayer = numPlayer;
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
        var key = e.getKeyCode();

        if (key == command.get(KeyCode.LEFT)) {
            velocity.setX(-10);
            xMovement = KeyCode.LEFT;
            isFlipped = true;
        }
        else if (key == command.get(KeyCode.RIGHT)) {
            velocity.setX(10);
            xMovement = KeyCode.RIGHT;
            isFlipped = false;
        }

        else if (key == command.get(KeyCode.UP) && canJump) {
            velocity.setY(-25);
            jump = true;
            canJump = false;
        }
        else if (key == command.get(KeyCode.DOWN) && jump) {
            if (velocity.getY() < 0) velocity.setY(0);
            else velocity.addY(20);
        }
        else if (key == command.get(KeyCode.ATTACK) && !isAttacking) {
            isAttacking = true;
            attackPosition.setX(0);
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        var key = e.getKeyCode();

        if ((key == command.get(KeyCode.LEFT) || key == command.get(KeyCode.RIGHT)) && key == command.get(xMovement)) {
            velocity.setX(0);
        }
    }

    @Override
    public void display(Display d, Images images) {
        var g = d.getGraphics();
        g.setColor(Color.BLACK);
        var imageKey = (isFlipped) ? ImageKey.valueOf("PLAYER_" + numPlayer + "_IDLE_FLIPPED"):
                ImageKey.valueOf("PLAYER_" + numPlayer + "_IDLE");
        var image = images.get(imageKey);
        g.drawImage(image, body.x, body.y, null);


    }
    public void displayAttack(Display d, Images images) {
        if (!isAttacking) return;

        var g = d.getGraphics();

        var fistKey = (isFlipped) ? ImageKey.valueOf("PLAYER_" + numPlayer + "_FIST_FLIPPED"):
                ImageKey.valueOf("PLAYER_" + numPlayer + "_FIST");
        var fistImage = images.get(fistKey);

        var flip = isFlipped ? -1: 1;
        var center = new Vec2(body.x + body.width / 2 - Images.WIDTH_FIST / 2,
                body.y + body.height / 2 - Images.HEIGHT_FIST / 2);
        g.drawImage(fistImage, (int)(center.getX() + (attackPosition.getX() + 65) * flip), (int)(center.getY() + attackPosition.getY() + 15), null);
    }


    private void manageFlip(boolean needFlip) {
        if (needFlip) {
            isFlipped = !isFlipped;
        }
    }

    public int getWidth(){
        return body.width;
    }

    public int getHeight(){
        return body.height;
    }

    public void update(boolean needFlip, Vec2 gravity, Rectangle bounds) {
        velocity.addX(gravity.getX());
        velocity.addY(gravity.getY());

        if (isAttacking && attackPosition.getX() > ATTACK_DISTANCE) {
            isAttacking = false;
        }
        else if (isAttacking) {
            attackPosition.addX(attackVelocity.getX());
            attackPosition.addY(attackVelocity.getY());
        }

        body.x += velocity.getX();
        body.y += velocity.getY();

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

    public int getNumPlayer() {
        return numPlayer;
    }

    public double percentageHpLeft() {
        return statistic.percentageHpLeft();
    }

    public double percentageEnergy() {
        return statistic.percentageEnergy();
    }

}
