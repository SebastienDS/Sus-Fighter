package fr.uge.susfighter.object;

import javafx.scene.input.KeyCode;
import javafx.scene.shape.Rectangle;

import java.util.Objects;
import java.util.Random;

public class Bot implements Fighter {

    private enum Mode {
        ATTACK,
        DEFENSE
    }

    private final Player player;
    private final Player enemy;
    private final Random random;

    private long lastModeChange = System.currentTimeMillis();
    private long lastActionTime = System.currentTimeMillis();
    private long lastJumpTime = System.currentTimeMillis();
    private Mode mode = Mode.DEFENSE;


    public Bot(String name, Rectangle body, Rectangle hitBox, Element element, Statistic stat,
               Command command, int numPlayer, boolean isFlipped, String type, Player enemy) {
        this.player = new Player(name, body, hitBox, element, stat, command, numPlayer, isFlipped, type);
        this.enemy = Objects.requireNonNull(enemy);
        random = new Random();
    }

    @Override
    public void keyPressed(KeyCode code) {}

    @Override
    public void keyReleased(KeyCode code) {}

    @Override
    public void update(Vec2 gravity, Rectangle bounds) {
        manageMode();
        manageDeplacement();
        manageJump();
        manageBlock();
        manageAttack();

        player.update(gravity, bounds);
    }

    @Override
    public void interact(Fighter player2) {
        player.interact(player2);
    }

    @Override
    public boolean isDead() {
        return player.isDead();
    }

    @Override
    public Rectangle getBody() {
        return player.getBody();
    }

    @Override
    public Rectangle getHitBox() {
        return player.getHitBox();
    }

    @Override
    public Rectangle getAttack() {
        return player.getAttack();
    }

    @Override
    public Rectangle getUltimate() {
        return player.getUltimate();
    }

    @Override
    public Rectangle getBlockHitBox() {
        return player.getBlockHitBox();
    }

    @Override
    public Statistic getStatistic() {
        return player.getStatistic();
    }

    @Override
    public Element getElement() {
        return player.getElement();
    }

    @Override
    public void project(Vec2 velocity) {
        player.project(velocity);
    }

    @Override
    public double percentageHpLeft() {
        return player.percentageHpLeft();
    }

    @Override
    public double percentageEnergy() {
        return player.percentageEnergy();
    }

    @Override
    public int getNumPlayer() {
        return player.getNumPlayer();
    }

    @Override
    public boolean isFlipped() {
        return player.isFlipped();
    }

    @Override
    public String getName() {
        return player.getName();
    }

    @Override
    public boolean isAttacking() {
        return player.isAttacking();
    }

    @Override
    public boolean isBlocking() {
        return player.isBlocking();
    }

    @Override
    public String getType() {
        return player.getType();
    }

    private void manageMode() {
        var now = System.currentTimeMillis();
        var elapsed = now - lastModeChange;
        if (elapsed < 1000) return;

        lastModeChange = now;

        var botMode = random.nextDouble();
        mode = botMode <= 0.7 ? Mode.ATTACK : Mode.DEFENSE;
    }

    private void manageDeplacement() {
        var securityDistance = mode == Mode.DEFENSE ? 100 : 0;
        var hitBox = getHitBox();
        var enemyHitBox = enemy.getHitBox();

        var distance = enemyHitBox.getX() - hitBox.getX();
        var leftToFaceEnemy = distance <= 0;
        distance = Math.abs(distance);

        if (distance >= 200 + securityDistance && distance <= 225 + securityDistance) {
            moveTowardEnemy(leftToFaceEnemy);
            player.stopMoving();
        }
        else if (distance >= 225 + securityDistance) {
            moveTowardEnemy(leftToFaceEnemy);
        }
        else {
            moveAgainstEnemy(leftToFaceEnemy);
        }
    }

    private void moveTowardEnemy(boolean leftToFaceEnemy) {
        if (leftToFaceEnemy) player.moveLeft();
        else player.moveRight();
    }

    private void moveAgainstEnemy(boolean leftToFaceEnemy) {
        if (leftToFaceEnemy) player.moveRight();
        else player.moveLeft();
    }

    private void manageJump() {
        if (!player.canJump()) return;

        var now = System.currentTimeMillis();
        var elapsed = now - lastJumpTime;
        if (elapsed < 1000) return;
        lastJumpTime = now;

        var rand = random.nextBoolean();
        if (rand) player.jump();
    }

    private void manageAttack() {
        if (mode == Mode.ATTACK && player.isBlocking()) player.stopBlocking();
        if (player.isAttacking() || player.isBlocking() || mode != Mode.ATTACK) return;

        var now = System.currentTimeMillis();
        var elapsed = now - lastActionTime;
        if (elapsed < 250) return;
        lastActionTime = now;

        var rand = random.nextDouble();

        if (rand <= 0.7) {
            player.attack();

            if (player.canUltimate()) player.ultimateAttack();
        }
    }

    private void manageBlock() {
        if (mode != Mode.DEFENSE ||player.isAttacking()) return;

        var now = System.currentTimeMillis();
        var elapsed = now - lastActionTime;
        if (elapsed < 500) return;
        lastActionTime = now;

        if (player.isBlocking()) {
            player.stopBlocking();
            return;
        }

        var rand = random.nextBoolean();

        if (rand) {
            player.block();
        }
    }
}
