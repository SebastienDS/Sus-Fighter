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
    private final int attackSpeed;

    public Statistic(int hp, int shield, int energyPerAttack, int maxEnergy, int damage, int damageUltimate,
                     int critical, int defense, int speed, int attackSpeed) {
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


    public double percentageHpLeft(){
        return (double)hp / maxHp;
    }

    public double percentageEnergy() {
        return (double)energy / maxEnergy;
    }

    public void loseHP(int hp) {
        if (hp >= 0) throw new IllegalArgumentException("Can't give HP :(");
        this.hp += hp;
    }

    public boolean isDead() {
        return hp <= 0;
    }

    public int damage() {
        return damage;
    }

    public int damageUltimate() {
        return damageUltimate;
    }

    public void gainEnergy() {
        energy = Math.min(energy + energyPerAttack, maxEnergy);
    }

    public boolean isFullEnergy() {
        return energy == maxEnergy;
    }

    public void consumeEnergy() {
        energy = 0;
    }

    public int getSpeed() {
        return speed;
    }
}
