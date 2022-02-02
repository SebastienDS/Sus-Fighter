package fr.uge.susfighter.object;


public class Statistic {

    private final int maxHp;
    private int hp;
    private int shield;
    private int energy;
    private final int energyPerAttack;
    private final int maxEnergy;
    private int damage;
    private int damageUltimate;
    private final int critical;
    private final int defense;
    private final int speed;
    private final double attackSpeed;

    public Statistic(int hp, int shield, int energyPerAttack, int maxEnergy, int damage, int damageUltimate,
                     int critical, int defense, int speed, double attackSpeed) {
        this.maxHp = hp;
        this.hp = hp;
        this.shield = shield;
        this.energy = 0;
        this.energyPerAttack = energyPerAttack;
        this.maxEnergy = maxEnergy;
        this.damage = damage;
        this.damageUltimate = damageUltimate;
        this.critical = critical;
        this.defense = defense;
        this.speed = speed;
        this.attackSpeed = attackSpeed;
    }

    public Statistic(int hp, int shield, int energy, int damage, int critical, int defense) {
        this(hp, shield, energy, 100, damage, damage * 10, critical, defense, 10, 1);
    }

    public Statistic() {
        this(1000, 0, 0, 25, 0, 0);
    }

    /**
     * Get the percentage HP left of the player
     * @return percentage HP left
     */
    public double percentageHpLeft(){
        return (double)hp / maxHp;
    }

    /**
     * Get the percentage Energy of the player
     * @return percentage Energy
     */
    public double percentageEnergy() {
        return (double)energy / maxEnergy;
    }

    /**
     * Remove the amount hp of the player
     * @param hp hp
     */
    public void loseHP(int hp) {
        if (hp >= 0) throw new IllegalArgumentException("Can't give HP :(");
        this.hp += hp;
    }

    /**
     * Check if the player is dead
     * @return true if the player is dead
     */
    public boolean isDead() {
        return hp <= 0;
    }

    /**
     * Get the damage of the player
     * @return damage of the player
     */
    public int damage() {
        return damage;
    }

    /**
     * Get the ultimate damage of the player
     * @return ultimate damage
     */
    public int damageUltimate() {
        return damageUltimate;
    }

    /**
     * Gain Energy
     */
    public void gainEnergy() {
        energy = Math.min(energy + energyPerAttack, maxEnergy);
    }

    /**
     * Check if the energy is full
     * @return true if the energy is full
     */
    public boolean isFullEnergy() {
        return energy == maxEnergy;
    }

    /**
     * Consume all the energy
     */
    public void consumeEnergy() {
        energy = 0;
    }

    /**
     * Get the speed of the player
     * @return speed
     */
    public int speed() {
        return speed;
    }

    /**
     * Reset the player to the initial values
     */
    public void reset() {
        hp = maxHp;
        energy = 0;
    }
}
