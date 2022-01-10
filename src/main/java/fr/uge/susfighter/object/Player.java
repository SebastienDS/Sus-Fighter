package fr.uge.susfighter.object;

import javafx.scene.input.KeyCode;
import javafx.scene.shape.Rectangle;
import fr.uge.susfighter.object.Command.Key;

import java.util.Objects;

public class Player {

    private enum Action {
        IDLE,
        ATTACK,
        BLOCK
    }

    private static final int ATTACK_DISTANCE = 100;
    private static final int ATTACK_SIZE = 80;
    private static final int ULTIMATE_MULTIPLICATOR = 10;

    private final String name;
    private final int numPlayer;
    private final Rectangle body;
    private final Rectangle hitBox;
    private final Statistic statistic;
    private final Element element;
    private final Command command;
    private boolean isFlipped;


    private final int maxJumpCount;
    private int jumpCount = 0;
    private boolean isGrounded = true;
    private Key xMovement = Key.LEFT;

    private Action action = Action.IDLE;

    private final Vec2 velocity = new Vec2(0, 0);
    private final Vec2 projectionVelocity = new Vec2(0, 0);

    private final Vec2 attackVelocity = new Vec2(10, 0);
    private final Rectangle attack = new Rectangle(0, 0, ATTACK_SIZE, ATTACK_SIZE);
    private final Vec2 ultimateVelocity = new Vec2(10, 0);
    private final Rectangle ultimate = new Rectangle(2000, 2000, ATTACK_SIZE * ULTIMATE_MULTIPLICATOR, ATTACK_SIZE * ULTIMATE_MULTIPLICATOR);
    private boolean hasAlreadyHit = false;
    private boolean hasAlreadyHitUltimate = true;
    private final String type;

    private int combo = 0;
    private long lastHit;


    public Player(String name, Rectangle body, Rectangle hitBox, Element element, Statistic stat,
                  Command command, int numPlayer, boolean isFlipped, String type) {
        this.name = Objects.requireNonNull(name);
        this.body = Objects.requireNonNull(body);
        this.hitBox = Objects.requireNonNull(hitBox);
        this.element = Objects.requireNonNull(element);
        this.command = Objects.requireNonNull(command);
        statistic = Objects.requireNonNull(stat);
        this.isFlipped = isFlipped;
        this.numPlayer = numPlayer;
        this.type = type;
        maxJumpCount = 2;
    }

    public void keyPressed(KeyCode key) {
        if (key == command.get(Key.LEFT)) {
            velocity.setX(-statistic.speed());
            xMovement = Key.LEFT;
            isFlipped = true;
        }
        else if (key == command.get(Key.RIGHT)) {
            velocity.setX(statistic.speed());
            xMovement = Key.RIGHT;
            isFlipped = false;
        }

        else if (key == command.get(Key.UP) && (isGrounded || jumpCount < maxJumpCount)) {
            velocity.setY(-25);
            jumpCount++;
            isGrounded = false;
        }
        else if (key == command.get(Key.DOWN) && jumpCount > 0) {
            if (velocity.getY() < 0) velocity.setY(0);
            else velocity.addY(50);
        }
        else if (key == command.get(Key.ATTACK) && action == Action.IDLE) {
            action = Action.ATTACK;
            hasAlreadyHit = false;
            attack.setX(0);
            attack.setY(0);
            var timeBetweenLastHit = System.currentTimeMillis() - lastHit;
            lastHit = System.currentTimeMillis();
            if (timeBetweenLastHit > 1000) {
                combo = 0;
            }
        }
        else if (key == command.get(Key.ULTIMATE_ATTACK) && statistic.isFullEnergy()) {
            hasAlreadyHitUltimate = false;
            var flip = isFlipped ? -1: 1;
            var center = getPlayerCenter(ultimate);

            ultimate.setX(center.getX() + hitBox.getWidth() / 2 * flip);
            ultimate.setY(center.getY());

            ultimateVelocity.setX(Math.abs(ultimateVelocity.getX()) * flip);
            statistic.consumeEnergy();
        }
        else if (key == command.get(Key.BLOCK) && action == Action.IDLE) {
            action = Action.BLOCK;
        }
    }

    public void keyReleased(KeyCode key) {
        if ((key == command.get(Key.LEFT) || key == command.get(Key.RIGHT)) && key == command.get(xMovement)) {
            velocity.setX(0);
        } else if (key == command.get(Key.BLOCK)) {
            action = Action.IDLE;
        }
    }

    public void update(Vec2 gravity, Rectangle bounds) {
        projectionVelocity.setY(convergeTo0(projectionVelocity.getY(), gravity.getY()));

        velocity.addX(gravity.getX());
        velocity.addY(gravity.getY());

        managePosition(bounds);
        manageJump(bounds);
        manageAttack();
        manageUltimate();
    }

    private void manageAttack() {
        if (action == Action.ATTACK && attack.getX() > ATTACK_DISTANCE) {
            if (!hasAlreadyHit || combo == 2) combo = 0;
            else combo++;

            action = Action.IDLE;
            attack.setRotate(0);
        }
        else if (action == Action.ATTACK) {
            var comboAttack = getAttack();
            attack.setX(attack.getX() + comboAttack.getX());
            attack.setY(attack.getY() + comboAttack.getY());
        }
    }

    private void manageUltimate() {
        ultimate.setX(ultimate.getX() + ultimateVelocity.getX());
    }

    private void managePosition(Rectangle bounds) {
        body.setX(body.getX() + velocity.getX() + projectionVelocity.getX());
        body.setY(body.getY() + velocity.getY() + projectionVelocity.getY());

        body.setX(Math.min(Math.max(bounds.getX(), body.getX()), bounds.getX() + bounds.getWidth() - body.getWidth()));
        body.setY(Math.min(Math.max(bounds.getY(), body.getY()), bounds.getY() + bounds.getHeight() - body.getHeight()));
    }

    private void manageJump(Rectangle bounds) {
        if (body.getY() + hitBox.getY() >= bounds.getY() + bounds.getHeight() - hitBox.getHeight()) {
            isGrounded = true;
            jumpCount = 0;
            velocity.setY(0);
            projectionVelocity.setX(convergeTo0(projectionVelocity.getX(), 1));
        }
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
        if (action != Action.ATTACK) return null;

        var flip = isFlipped ? -1: 1;
        var center = getPlayerCenter(attack);

        return new Rectangle(
                center.getX() + (attack.getX() + 65) * flip,
                center.getY() + attack.getY() + 15,
                attack.getWidth(),
                attack.getHeight()
        );
    }

    public Rectangle getBlockHitBox() {
        if (action != Action.BLOCK) return null;

        var flip = isFlipped ? -1: 1;
        var center = getPlayerCenter(attack);

        var rect = new Rectangle(
                center.getX() + (getAttackHeight() + 20) * flip,
                center.getY(),
                attack.getWidth(),
                attack.getHeight()
        );
        var fistRotation = isFlipped ? 180 : 0;
        rect.setRotate(-90 + fistRotation);
        return rect;
    }

    public void checkAttack(Player player2) {
        checkUltimate(player2);
        checkBasicAttack(player2);
    }

    private void checkUltimate(Player player2) {
        if (hasAlreadyHitUltimate) return;

        var box = new Rectangle(
                player2.body.getX() + player2.hitBox.getX(),
                player2.body.getY() + player2.hitBox.getY(),
                player2.hitBox.getWidth(),
                player2.hitBox.getHeight()
        );
        hasAlreadyHitUltimate = ultimate.intersects(box.getBoundsInLocal());
        if (hasAlreadyHitUltimate) player2.statistic.loseHP(-(int)(statistic.damageUltimate() * element.getElementMultiplicator(player2.element)));
    }

    private void checkBasicAttack(Player player2) {
        if (action != Action.ATTACK || hasAlreadyHit) return;

        var rect = getAttackHitBox();
        if (rect == null) throw new NullPointerException();

        var box = new Rectangle(
                player2.body.getX() + player2.hitBox.getX(),
                player2.body.getY() + player2.hitBox.getY(),
                player2.hitBox.getWidth(),
                player2.hitBox.getHeight()
        );
        hasAlreadyHit = rect.intersects(box.getBoundsInLocal());

        if (hasAlreadyHit) {
            var flip = isFlipped ? -1: 1;

            player2.statistic.loseHP(-(int)(statistic.damage() * element.getElementMultiplicator(player2.element)));
            statistic.gainEnergy();
            applyCombo(flip, player2);
        }
    }

    public boolean isDead() {
        return statistic.isDead();
    }

    public int getX() {
        return (int) body.getX();
    }

    public int getY() {
        return (int) body.getY();
    }

    public int getWidth() {
        return (int) body.getWidth();
    }

    public int getHeight() {
        return (int) body.getHeight();
    }

    public boolean isFlipped() {
        return isFlipped;
    }

    public String getName() {
        return name;
    }

    public boolean isAttacking() {
        return action == Action.ATTACK;
    }

    public boolean isBlocking() {
        return action == Action.BLOCK;
    }

    public double getXFist() {
        return Objects.requireNonNull(getAttackHitBox()).getX();
    }

    public double getYFist() {
        return Objects.requireNonNull(getAttackHitBox()).getY();
    }

    private void applyCombo(int flip, Player player2) {
        if (combo >= 2) {
            player2.projectionVelocity.addX(15 * flip);
            player2.projectionVelocity.setY(-30);
        }
    }

    private static double convergeTo0(double value, double decrementValue) {
        if (value == 0) return 0;

        var tmp = value + (value / Math.abs(value)) * (-decrementValue);
        return Math.abs(tmp) <= 1 ? 0 : tmp;
    }

    private Vec2 getAttack() {
        return switch (combo) {
            case 1 -> {
                attack.setRotate(attack.getRotate() + 5);
                yield new Vec2(attackVelocity.getX(), attackVelocity.getX() * 0.5);
            }
            case 2 -> {
                attack.setRotate(attack.getRotate() - 5);
                yield new Vec2(attackVelocity.getX(), attackVelocity.getX() * -1.5);
            }
            default -> attackVelocity;
        };
    }

    public double getAttackRotate() {
        return attack.getRotate();
    }

    public double getAttackWidth() {
        return attack.getWidth();
    }

    public double getAttackHeight() {
        return attack.getHeight();
    }

    public Vec2 getPlayerCenter(Rectangle attack) {
        return new Vec2(body.getX() + hitBox.getX() + hitBox.getWidth() / 2 - attack.getWidth() / 2,
                body.getY() + hitBox.getY() + hitBox.getHeight() / 2 - attack.getHeight() / 2);
    }

    public double getUltimateWidth() {
        return ultimate.getWidth();
    }

    public double getUltimateHeight() {
        return ultimate.getHeight();
    }

    public double getUltimateX() {
        return ultimate.getX();
    }

    public double getUltimateY() {
        return ultimate.getY();
    }

    public int getUltimateFlip() {
        assert ultimateVelocity.getX() != 0;
        return (int) (ultimateVelocity.getX() / Math.abs(ultimateVelocity.getX()));
    }


    public String getType() {
        return type;
    }
}
