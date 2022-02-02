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
                int numPlayer, boolean isFlipped, String type, Player enemy, int maxJumpCount) {
        this.player = new Player(name, body, hitBox, element, stat, new Command(), numPlayer, isFlipped, type, maxJumpCount);
        this.enemy = Objects.requireNonNull(enemy);
        random = new Random();
    }

    public Bot(Bot bot, Player enemy, int x, int y) {
        this.player = new Player(bot.player, x , y, true, 2);
        this.enemy = enemy;
        random = new Random();
    }

    /**
     * Start the action of a key pressed
     * @param code keyCode
     */
    @Override
    public void keyPressed(KeyCode code) {}

    /**
     * End the action of a key pressed
     * @param code keyCode
     */
    @Override
    public void keyReleased(KeyCode code) {}

    /**
     * Update player informations
     * @param gravity gravity of the map that must be applied to the player
     * @param bounds bounds of the map to restrict the player to a specific area
     */
    @Override
    public void update(Vec2 gravity, Rectangle bounds) {
        manageMode();
        manageDeplacement();
        manageJump();
        manageBlock();
        manageAttack();

        player.update(gravity, bounds);
    }

    /**
     * Apply the interaction between the player and another player
     * @param player2 other player
     */
    @Override
    public void interact(Fighter player2) {
        player.interact(player2);
    }

    /**
     * Check if the player is dead
     * @return true if the player is dead
     */
    @Override
    public boolean isDead() {
        return player.isDead();
    }

    /**
     * Get the body of the player
     * @return body
     */
    @Override
    public Rectangle getBody() {
        return player.getBody();
    }

    /**
     * Get the HitBox of the player
     * @return hitBox
     */
    @Override
    public Rectangle getHitBox() {
        return player.getHitBox();
    }

    /**
     * Get the Hitbox for the attacking fist
     * @return Rectangle
     */
    @Override
    public Rectangle getAttack() {
        return player.getAttack();
    }

    /**
     * Get the HitBox for the ultimate attack
     * @return Rectangle
     */
    @Override
    public Rectangle getUltimate() {
        return player.getUltimate();
    }

    /**
     * Get the HitBox for the blocking fist
     * @return Rectangle
     */
    @Override
    public Rectangle getBlockHitBox() {
        return player.getBlockHitBox();
    }

    /**
     * Get the statistic of the player
     * @return statistic
     */
    @Override
    public Statistic getStatistic() {
        return player.getStatistic();
    }

    /**
     * Get the element of the player
     * @return Element
     */
    @Override
    public Element getElement() {
        return player.getElement();
    }

    /**
     * Project the player with a given force
     * @param velocity projection force
     */
    @Override
    public void project(Vec2 velocity) {
        player.project(velocity);
    }

    /**
     * Get the percentage HP left
     * @return percentage HP left
     */
    @Override
    public double percentageHpLeft() {
        return player.percentageHpLeft();
    }

    /**
     * Get the percentage Energy left
     * @return percentage Energy left
     */
    @Override
    public double percentageEnergy() {
        return player.percentageEnergy();
    }

    /**
     * Get the number of the player
     * @return number of the player
     */
    @Override
    public int getNumPlayer() {
        return player.getNumPlayer();
    }

    /**
     * Check if the player is flipped
     * @return true if the player is turned toward the left
     */
    @Override
    public boolean isFlipped() {
        return player.isFlipped();
    }

    /**
     * Get the name of the player
     * @return name
     */
    @Override
    public String getName() {
        return player.getName();
    }

    /**
     * Check if the player is Attacking
     * @return true if the player is attacking
     */
    @Override
    public boolean isAttacking() {
        return player.isAttacking();
    }

    /**
     * Check if the player is Blocking
     * @return true if the player is blocking
     */
    @Override
    public boolean isBlocking() {
        return player.isBlocking();
    }

    /**
     * Get the type of the player
     * @return type
     */
    @Override
    public String getType() {
        return player.getType();
    }

    /**
     * Make the choice of the action between ATTACK or DEFENSE
     */
    private void manageMode() {
        var now = System.currentTimeMillis();
        var elapsed = now - lastModeChange;
        if (elapsed < 1000) return;

        lastModeChange = now;

        var botMode = random.nextDouble();
        mode = botMode <= 0.7 ? Mode.ATTACK : Mode.DEFENSE;
    }

    /**
     * Manage the bot deplacement to keep a distance between the bot and the player
     */
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

    /**
     * Move toward Enemy
     * @param leftToFaceEnemy true if the bot face the enemy
     */
    private void moveTowardEnemy(boolean leftToFaceEnemy) {
        if (leftToFaceEnemy) player.moveLeft();
        else player.moveRight();
    }

    /**
     * Move Against Enemy
     * @param leftToFaceEnemy true if the bot face the enemy
     */
    private void moveAgainstEnemy(boolean leftToFaceEnemy) {
        if (leftToFaceEnemy) player.moveRight();
        else player.moveLeft();
    }

    /**
     * Manage the jump
     */
    private void manageJump() {
        if (!player.canJump()) return;

        var now = System.currentTimeMillis();
        var elapsed = now - lastJumpTime;
        if (elapsed < 1000) return;
        lastJumpTime = now;

        var rand = random.nextBoolean();
        if (rand) player.jump();
    }

    /**
     * Manage the attack action
     */
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

    /**
     * Manage the defense action
     */
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
