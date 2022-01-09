package fr.uge.susfighter.object;


public class Statistic {

    private static final int ENERGY_PER_ATTACK = 5;

    private int maxHp;
    private int hp;
    private int shield;
    private int maxEnergy;
    private int energy;
    private int damage;
    private int critical;
    private int defense;

    public Statistic(int hp, int shield, int energy, int damage, int critical, int defense) {
        this.maxHp = hp;
        this.hp = hp;
        this.shield = shield;
        this.maxEnergy = 100;
        this.energy = energy;
        this.damage = damage;
        this.critical = critical;
        this.defense = defense;
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

    public void gainEnergy() {
        energy = Math.min(energy + ENERGY_PER_ATTACK, maxEnergy);
    }

    public boolean isFullEnergy() {
        return energy == maxEnergy;
    }

    public void consumeEnergy() {
        energy = 0;
    }
}
