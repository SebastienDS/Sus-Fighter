package fr.uge.susfighter.object;

import javafx.scene.input.KeyCode;
import javafx.scene.shape.Rectangle;
import fr.uge.susfighter.object.Command.Key;

import java.util.Objects;

public class Player implements Fighter {

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
                  Command command, int numPlayer, boolean isFlipped, String type, int maxJumpCount) {
        this.name = Objects.requireNonNull(name);
        this.body = Objects.requireNonNull(body);
        this.hitBox = Objects.requireNonNull(hitBox);
        this.element = Objects.requireNonNull(element);
        this.command = Objects.requireNonNull(command);
        statistic = Objects.requireNonNull(stat);
        this.isFlipped = isFlipped;
        this.numPlayer = numPlayer;
        this.type = type;
        this.maxJumpCount = maxJumpCount;
    }

    public Player(Player player, int x, int y, boolean isFlipped, int numPlayer){
        this.name = player.name;
        this.body = player.body;
        body.setX(x);
        body.setY(y);
        this.hitBox = player.hitBox;
        this.element = player.element;
        this.command = player.command;
        this.statistic = player.statistic;
        statistic.reset();
        this.numPlayer = numPlayer;
        this.isFlipped = isFlipped;
        this.type = player.type;
        maxJumpCount = player.maxJumpCount;
    }

    /**
     * Start the action of a key pressed
     * @param key key
     */
    @Override
    public void keyPressed(KeyCode key) {
        if (key == command.get(Key.LEFT)) moveLeft();
        else if (key == command.get(Key.RIGHT)) moveRight();
        else if (key == command.get(Key.UP) && canJump()) jump();
        else if (key == command.get(Key.DOWN) && jumpCount > 0) down();
        else if (key == command.get(Key.ATTACK) && action == Action.IDLE) attack();
        else if (key == command.get(Key.ULTIMATE_ATTACK) && canUltimate()) ultimateAttack();
        else if (key == command.get(Key.BLOCK) && action == Action.IDLE) block();
    }

    /**
     * End the action of a key pressed
     * @param key key
     */
    @Override
    public void keyReleased(KeyCode key) {
        if ((key == command.get(Key.LEFT) || key == command.get(Key.RIGHT)) && key == command.get(xMovement)) {
            stopMoving();
        } else if (key == command.get(Key.BLOCK) && action == Action.BLOCK) {
            action = Action.IDLE;
        }
    }

    /**
     * Update player informations
     * @param gravity gravity of the map that must be applied to the player
     * @param bounds bounds of the map to restrict the player to a specific area
     */
    @Override
    public void update(Vec2 gravity, Rectangle bounds) {
        projectionVelocity.setY(convergeTo0(projectionVelocity.getY(), gravity.getY()));

        velocity.addX(gravity.getX());
        velocity.addY(gravity.getY());

        managePosition(bounds);
        manageJump(bounds);
        manageAttack();
        manageUltimate();
    }

    /**
     * Apply the interaction between the player and another player
     * @param player2 other player
     */
    @Override
    public void interact(Fighter player2) {
        checkUltimate(player2);
        checkBasicAttack(player2);
    }

    /**
     * Check if the player is dead
     * @return true if the player is dead
     */
    @Override
    public boolean isDead() {
        return statistic.isDead();
    }

    /**
     * Get the body of the player
     * @return body
     */
    @Override
    public Rectangle getBody() {
        return body;
    }

    /**
     * Get the HitBox of the player
     * @return hitBox
     */
    @Override
    public Rectangle getHitBox() {
        var hitBoxX = isFlipped ? body.getWidth() - (hitBox.getWidth() + hitBox.getX()) : hitBox.getX();
        return new Rectangle(
                body.getX() + hitBoxX,
                body.getY() + hitBox.getY(),
                hitBox.getWidth(),
                hitBox.getHeight()
        );
    }

    /**
     * Get the HitBox for the blocking fist
     * @return Rectangle
     */
    @Override
    public Rectangle getBlockHitBox() {
        var flip = isFlipped ? -1: 1;
        var center = getPlayerCenter(attack);

        var rect = new Rectangle(
                center.getX() + attack.getHeight() * flip,
                center.getY(),
                attack.getWidth(),
                attack.getHeight()
        );
        var fistRotation = isFlipped ? 180 : 0;
        rect.setRotate(-90 + fistRotation);
        return rect;
    }

    /**
     * Get the Hitbox for the attacking fist
     * @return Rectangle
     */
    @Override
    public Rectangle getAttack() {
        return getAttackHitBox();
    }

    /**
     * Get the HitBox for the ultimate attack
     * @return Rectangle
     */
    @Override
    public Rectangle getUltimate() {
        return ultimate;
    }

    /**
     * Get the statistic of the player
     * @return statistic
     */
    @Override
    public Statistic getStatistic() {
        return statistic;
    }

    /**
     * Get the element of the player
     * @return Element
     */
    @Override
    public Element getElement() {
        return element;
    }

    /**
     * Project the player with a given force
     * @param velocity projection force
     */
    @Override
    public void project(Vec2 velocity) {
        projectionVelocity.addX(velocity.getX());
        projectionVelocity.setY(velocity.getY());
    }

    /**
     * Get the percentage HP left
     * @return percentage HP left
     */
    @Override
    public double percentageHpLeft() {
        return statistic.percentageHpLeft();
    }

    /**
     * Get the percentage Energy left
     * @return percentage Energy left
     */
    @Override
    public double percentageEnergy() {
        return statistic.percentageEnergy();
    }

    /**
     * Get the number of the player
     * @return number of the player
     */
    @Override
    public int getNumPlayer() {
        return numPlayer;
    }

    /**
     * Check if the player is flipped
     * @return true if the player is turned toward the left
     */
    @Override
    public boolean isFlipped() {
        return isFlipped;
    }

    /**
     * Get the name of the player
     * @return name
     */
    @Override
    public String getName() {
        return name;
    }

    /**
     * Check if the player is Attacking
     * @return true if the player is attacking
     */
    @Override
    public boolean isAttacking() {
        return action == Action.ATTACK;
    }

    /**
     * Check if the player is Blocking
     * @return true if the player is blocking
     */
    @Override
    public boolean isBlocking() {
        return action == Action.BLOCK;
    }

    /**
     * Get the type of the player
     * @return type
     */
    @Override
    public String getType() {
        return type;
    }

    /**
     * Check if the player can use his ultimate
     * @return true if the player can use his ultimate
     */
    public boolean canUltimate() {
        return statistic.isFullEnergy();
    }

    /**
     * Check if the player can jump
     * @return true if the player can jump
     */
    public boolean canJump() {
        return isGrounded || jumpCount < maxJumpCount;
    }

    /**
     * Move the player to the left
     */
    public void moveLeft() {
        velocity.setX(-statistic.speed());
        xMovement = Key.LEFT;
        isFlipped = true;
    }

    /**
     * Move the player to the right
     */
    public void moveRight() {
        velocity.setX(statistic.speed());
        xMovement = Key.RIGHT;
        isFlipped = false;
    }

    /**
     * Stop the player movement
     */
    public void stopMoving() {
        velocity.setX(0);
    }

    /**
     * Jump player
     */
    public void jump() {
        if (!canJump()) throw new IllegalStateException("Can't jump");

        velocity.setY(-25);
        jumpCount++;
        isGrounded = false;
    }

    /**
     * Move down the player faster
     */
    public void down() {
        if (jumpCount <= 0) throw new IllegalStateException("Can't move down");

        if (velocity.getY() < 0) velocity.setY(0);
        else velocity.addY(50);
    }

    /**
     * Apply the attack action
     */
    public void attack() {
        if (action != Action.IDLE) throw new IllegalStateException("Can't attack while another action is active");

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

    /**
     * Consume the player's energy and perform an ultimate attack
     */
    public void ultimateAttack() {
        if (!statistic.isFullEnergy()) throw new IllegalStateException("Can't ultimate, energy is not full");

        hasAlreadyHitUltimate = false;
        var flip = isFlipped ? -1: 1;
        var center = getPlayerCenter(ultimate);

        ultimate.setX(center.getX() + (ultimate.getWidth() / 2) * flip);
        ultimate.setY(center.getY());
        ultimate.setScaleX((int) (ultimateVelocity.getX() / Math.abs(ultimateVelocity.getX())));

        ultimateVelocity.setX(Math.abs(ultimateVelocity.getX()) * flip);
        statistic.consumeEnergy();
    }

    /**
     * Apply the blocking action
     */
    public void block() {
        if (action != Action.IDLE) throw new IllegalStateException("Can't block while another action is active");
        action = Action.BLOCK;
    }

    /**
     * Stop the blocking action
     */
    public void stopBlocking() {
        if (action != Action.BLOCK) throw new IllegalStateException("Can't stop blocking if player is not blocking");
        action = Action.IDLE;
    }

    /**
     * Update the attack
     */
    private void manageAttack() {
        if (action == Action.ATTACK && attack.getX() > ATTACK_DISTANCE) {
            if (!hasAlreadyHit || combo == 2) combo = 0;
            else combo++;

            action = Action.IDLE;
            attack.setRotate(0);
        }
        else if (action == Action.ATTACK) {
            var comboAttack = getComboAttack();
            attack.setX(attack.getX() + comboAttack.getX());
            attack.setY(attack.getY() + comboAttack.getY());
        }
    }

    /**
     * Update the ultimate
     */
    private void manageUltimate() {
        ultimate.setX(ultimate.getX() + ultimateVelocity.getX());
    }

    /**
     * Update the player position
     * @param bounds bounds of the resticted area where the player can move
     */
    private void managePosition(Rectangle bounds) {
        body.setX(body.getX() + velocity.getX() + projectionVelocity.getX());
        body.setY(body.getY() + velocity.getY() + projectionVelocity.getY());

        var hitBoxX = (isFlipped)? body.getWidth() - (hitBox.getWidth() + hitBox.getX()) : hitBox.getX();
        var lastPositionX = body.getX();
        body.setX(Math.min(Math.max(bounds.getX() - hitBoxX,
                body.getX()), bounds.getX() + bounds.getWidth() - hitBox.getWidth() - hitBoxX));
        body.setY(Math.min(Math.max(bounds.getY() - hitBox.getY(),
                body.getY()), bounds.getY() + bounds.getHeight() - hitBox.getHeight() - hitBox.getY()));

        if (body.getX() != lastPositionX) projectionVelocity.setX(0);
    }

    /**
     * Update the jump
     * @param bounds bounds of the restricted area where the player can move
     */
    private void manageJump(Rectangle bounds) {
        if (body.getY() + hitBox.getY() + hitBox.getHeight() >= bounds.getY() + bounds.getHeight()) {
            isGrounded = true;
            jumpCount = 0;
            velocity.setY(0);
            projectionVelocity.setX(convergeTo0(projectionVelocity.getX(), 0.6));
        }
        projectionVelocity.setX(convergeTo0(projectionVelocity.getX(), 0.3));
    }

    /**
     * Get the Attack HitBox
     * @return Rectangle
     */
    private Rectangle getAttackHitBox() {
        var flip = isFlipped ? -1: 1;
        var center = getPlayerCenter(attack);

        var box = new Rectangle(
                center.getX() + (attack.getWidth() / 2 + attack.getX()) * flip,
                center.getY() + attack.getY() + 15,
                attack.getWidth(),
                attack.getHeight()
        );
        box.setRotate(attack.getRotate());
        return box;
    }

    /**
     * Check if the ultimate hit the enemy and apply the damage
     * @param player2 enemy
     */
    private void checkUltimate(Fighter player2) {
        if (hasAlreadyHitUltimate) return;

        var box = player2.getHitBox();
        hasAlreadyHitUltimate = ultimate.intersects(box.getBoundsInLocal());
        if (hasAlreadyHitUltimate) player2.getStatistic().loseHP(-(int)(statistic.damageUltimate() * element.getElementMultiplicator(player2.getElement())));
    }

    /**
     * Check if the basic attack hit the enemy and apply the damage
     * @param player2 enemy
     */
    private void checkBasicAttack(Fighter player2) {
        if (action != Action.ATTACK || hasAlreadyHit) return;

        var rect = getAttackHitBox();

        if (player2.isBlocking()) {
            var block = player2.getBlockHitBox();
            hasAlreadyHit = rect.intersects(block.getBoundsInLocal());
            if (hasAlreadyHit) return;
        }

        var box = player2.getHitBox();
        hasAlreadyHit = rect.intersects(box.getBoundsInLocal());

        if (hasAlreadyHit) {
            var flip = isFlipped ? -1: 1;

            player2.getStatistic().loseHP(-(int)(statistic.damage() * element.getElementMultiplicator(player2.getElement())));
            statistic.gainEnergy();
            applyCombo(flip, player2);
        }
    }

    /**
     * Apply the combo projection to the enemy if it is the last part of the combo
     * @param flip flip
     * @param player2 enemy
     */
    private void applyCombo(int flip, Fighter player2) {
        if (combo >= 2) {
            player2.project(new Vec2(15 * flip, -30));
        }
    }

    /**
     * Converge the given value the 0 with a step of decrementValue
     * @param value value
     * @param decrementValue value
     * @return converged value
     */
    private static double convergeTo0(double value, double decrementValue) {
        if (value == 0) return 0;

        var tmp = value + (value / Math.abs(value)) * (-decrementValue);
        return Math.abs(tmp) <= 1 ? 0 : tmp;
    }

    /**
     * Get the specific attack of the combo
     * @return Attack
     */
    private Vec2 getComboAttack() {
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

    /**
     * Get the center of the player
     * @param attack attack
     * @return Player center
     */
    private Vec2 getPlayerCenter(Rectangle attack) {
        var box = getHitBox();
        return new Vec2(box.getX() + box.getWidth() / 2 - attack.getWidth() / 2,
                box.getY() + box.getHeight() / 2 - attack.getHeight() / 2);
    }
}
