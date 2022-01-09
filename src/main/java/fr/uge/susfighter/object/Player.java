package fr.uge.susfighter.object;

import javafx.scene.input.KeyCode;
import javafx.scene.shape.Rectangle;
import fr.uge.susfighter.object.Command.Key;

import java.util.Objects;

public class Player {

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
    private Key xMovement = Key.LEFT;

    private final Vec2 velocity = new Vec2(0, 0);

    private final Vec2 attackVelocity = new Vec2(15, 0);
    private final Vec2 attackPosition = new Vec2(0, 0);
    private boolean isAttacking = false;
    private boolean hasAlreadyHit = false;


    public Player(String name, Rectangle body, Element element, Command command, int numPlayer, boolean isFlipped) {
        this.name = Objects.requireNonNull(name);
        this.body = Objects.requireNonNull(body);
        this.element = Objects.requireNonNull(element);
        this.command = Objects.requireNonNull(command);
        statistic = new Statistic();
        this.isFlipped = isFlipped;
        this.numPlayer = numPlayer;
    }

    public void keyPressed(KeyCode key) {
        if (key == command.get(Key.LEFT)) {
            velocity.setX(-10);
            xMovement = Key.LEFT;
            isFlipped = true;
        }
        else if (key == command.get(Key.RIGHT)) {
            velocity.setX(10);
            xMovement = Key.RIGHT;
            isFlipped = false;
        }

        else if (key == command.get(Key.UP) && canJump) {
            velocity.setY(-25);
            jump = true;
            canJump = false;
        }
        else if (key == command.get(Key.DOWN) && jump) {
            if (velocity.getY() < 0) velocity.setY(0);
            else velocity.addY(50);
        }
        else if (key == command.get(Key.ATTACK) && !isAttacking) {
            isAttacking = true;
            hasAlreadyHit = false;
            attackPosition.setX(0);
        }
    }

    public void keyReleased(KeyCode key) {
        if ((key == command.get(Key.LEFT) || key == command.get(Key.RIGHT)) && key == command.get(xMovement)) {
            velocity.setX(0);
        }
    }

//    public void displayAttack(Display d, Images images) {
//        if (!isAttacking) return;
//
//        var g = d.getGraphics();
//
//        var fistKey = (isFlipped) ? ImageKey.valueOf("PLAYER_" + numPlayer + "_FIST_FLIPPED"):
//                ImageKey.valueOf("PLAYER_" + numPlayer + "_FIST");
//        var fistImage = images.get(fistKey);
//
//        var rect = getAttackHitBox();
//        assert rect != null;
//        g.drawImage(fistImage, rect.x, rect.y, null);
//    }

    public void update(Vec2 gravity, Rectangle bounds) {
        velocity.addX(gravity.getX());
        velocity.addY(gravity.getY());

        if (isAttacking && attackPosition.getX() > ATTACK_DISTANCE) {
            isAttacking = false;
        }
        else if (isAttacking) {
            attackPosition.addX(attackVelocity.getX());
            attackPosition.addY(attackVelocity.getY());
        }

        body.setX(body.getX() + velocity.getX());
        body.setY(body.getY() + velocity.getY());

        body.setX(Math.min(Math.max(bounds.getX(), body.getX()), bounds.getX() + bounds.getWidth() - body.getWidth()));
        body.setY(Math.min(Math.max(bounds.getY(), body.getY()), bounds.getY() + bounds.getHeight() - body.getHeight()));

        if (body.getY() == bounds.getY() + bounds.getHeight() - body.getHeight()) {
            canJump = true;
            jump = false;
        }
    }

    public boolean needFlip(Player player) {
        return body.getX() < player.body.getX() && isFlipped || player.body.getX() < body.getX() && player.isFlipped;
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

    private Rectangle getAttackHitBox() {
        if (!isAttacking) return  null;

        var flip = isFlipped ? -1: 1;
        var center = new Vec2(body.getX() + body.getWidth() / 2 - 80 / 2,
                body.getY() + body.getHeight() / 2 - 80 / 2);

        return new Rectangle(
                (int)(center.getX() + (attackPosition.getX() + 65) * flip),
                (int)(center.getY() + attackPosition.getY() + 15),
                80,
                80
        );
    }

    public void checkAttack(Player player2) {
        if (!isAttacking || hasAlreadyHit) return;
        var rect = getAttackHitBox();
        if (rect == null) throw new NullPointerException();
        hasAlreadyHit = rect.intersects(player2.body.getBoundsInLocal());
        if (hasAlreadyHit) {
            player2.statistic.loseHP(-statistic.damage());
        }
    }

    public boolean isDead() {
        return statistic.isDead();
    }

    public int getX() {
        return (int)body.getX();
    }

    public int getY() {
        return (int)body.getY();
    }

    public int getWidth() {
        return (int)body.getWidth();
    }

    public int getHeight() {
        return (int)body.getHeight();
    }

    public boolean isFlipped() {
        return isFlipped;
    }

    public String getName() {
        return name;
    }

    public boolean isAttacking() {
        return isAttacking;
    }

    public double getXFist() {
        return Objects.requireNonNull(getAttackHitBox()).getX();
    }

    public double getYFist() {
        return Objects.requireNonNull(getAttackHitBox()).getY();
    }
}
