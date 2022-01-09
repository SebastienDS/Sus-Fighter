package fr.uge.susfighter.object;

import javafx.scene.input.KeyCode;
import javafx.scene.shape.Rectangle;
import fr.uge.susfighter.object.Command.Key;

import java.util.Objects;

public class Player {

    private static final int ATTACK_DISTANCE = 100;
    private static final int ATTACK_SIZE = 80;
    private static final int ULTIMATE_MULTIPLICATOR = 10;


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
    private final Vec2 projectionVelocity = new Vec2(0, 0);

    private final Vec2 attackVelocity = new Vec2(10, 0);
    private final Rectangle attack = new Rectangle(0, 0, ATTACK_SIZE, ATTACK_SIZE);
    private boolean isAttacking = false;
    private final Vec2 ultimateVelocity = new Vec2(10, 0);
    private final Rectangle ultimate = new Rectangle(2000, 2000, ATTACK_SIZE * ULTIMATE_MULTIPLICATOR, ATTACK_SIZE * ULTIMATE_MULTIPLICATOR);
    private boolean hasAlreadyHit = false;
    private boolean hasAlreadyHitUltimate = true;
    private double attackRotation = 0;

    private int combo = 0;
    private long lastHit;


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
            attack.setX(0);
            attack.setY(0);
        }
        else if (key == command.get(Key.ULTIMATE_ATTACK) && statistic.isFullEnergy()) {
            hasAlreadyHitUltimate = false;
            var flip = isFlipped ? -1: 1;
            var center = getPlayerCenter(ultimate);

            ultimate.setX(center.getX() + body.getWidth() / 2 * flip);
            ultimate.setY(center.getY());

            ultimateVelocity.setX(Math.abs(ultimateVelocity.getX()) * flip);
            statistic.consumeEnergy();
        }
    }

    public void keyReleased(KeyCode key) {
        if ((key == command.get(Key.LEFT) || key == command.get(Key.RIGHT)) && key == command.get(xMovement)) {
            velocity.setX(0);
        }
    }

    public void update(Vec2 gravity, Rectangle bounds) {
        projectionVelocity.setY(convergeTo0(projectionVelocity.getY(), gravity.getY()));

        velocity.addX(gravity.getX());
        velocity.addY(gravity.getY());

        manageAttack();
        manageUltimate();
        managePosition(bounds);
        manageJump(bounds);
    }

    private void manageAttack() {
        if (isAttacking && attack.getX() > ATTACK_DISTANCE) {
            if(!hasAlreadyHit || combo == 2) combo = 0;
            else combo++;

            isAttacking = false;
            attackRotation = 0;
        }
        else if (isAttacking) {
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
        if (body.getY() == bounds.getY() + bounds.getHeight() - body.getHeight()) {
            canJump = true;
            jump = false;
            velocity.setY(0);
            projectionVelocity.setX(convergeTo0(projectionVelocity.getX(), 1));
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
        if (!isAttacking) return null;

        var flip = isFlipped ? -1: 1;
        var center = getPlayerCenter(attack);

        return new Rectangle(
                (int)(center.getX() + (attack.getX() + 65) * flip),
                (int)(center.getY() + attack.getY() + 15),
                attack.getWidth(),
                attack.getHeight()
        );
    }

    public void checkAttack(Player player2) {
        checkUltimate(player2);
        checkBasicAttack(player2);
    }

    private void checkUltimate(Player player2) {
        if (hasAlreadyHitUltimate) return;

        hasAlreadyHitUltimate = ultimate.intersects(player2.body.getBoundsInLocal());
        if (hasAlreadyHitUltimate) player2.statistic.loseHP(-statistic.damage() * ULTIMATE_MULTIPLICATOR);
    }

    private void checkBasicAttack(Player player2) {
        if (!isAttacking || hasAlreadyHit) return;

        var rect = getAttackHitBox();
        if (rect == null) throw new NullPointerException();
        hasAlreadyHit = rect.intersects(player2.body.getBoundsInLocal());

        if (hasAlreadyHit) {
            var flip = isFlipped ? -1: 1;

            player2.statistic.loseHP(-statistic.damage());
            statistic.gainEnergy();
            applyCombo(flip, player2);
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

    private void applyCombo(int flip, Player player2) {
        var timeBetweenLastHit = System.currentTimeMillis() - lastHit;
        lastHit = System.currentTimeMillis();
        if (timeBetweenLastHit < 1000 && combo >= 2) {
            player2.projectionVelocity.addX(15 * flip);
            player2.projectionVelocity.setY(-30);
            return;
        }
        if (timeBetweenLastHit > 1000) {
            combo = 0;
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
                attackRotation += 5;
                yield new Vec2(attackVelocity.getX(), attackVelocity.getX() * 0.5);
            }
            case 2 -> {
                attackRotation -= 5;
                yield new Vec2(attackVelocity.getX(), attackVelocity.getX() * -1.5);
            }
            default -> attackVelocity;
        };
    }

    public double getAttackRotate() {
        return attackRotation;
    }

    public double getAttackWidth() {
        return attack.getWidth();
    }

    public double getAttackHeight() {
        return attack.getHeight();
    }

    private Vec2 getPlayerCenter(Rectangle attack) {
        return new Vec2(body.getX() + body.getWidth() / 2 - attack.getWidth() / 2,
                body.getY() + body.getHeight() / 2 - attack.getHeight() / 2);
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
}
