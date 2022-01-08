package object;

public class Statistic {

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
        this(100, 0, 0, 10, 0, 0);
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
}
