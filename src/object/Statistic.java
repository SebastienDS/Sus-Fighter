package object;

public class Statistic {

    private int hp;
    private int shield;
    private int energy;
    private int damage;
    private int critical;
    private int defense;

    public Statistic(int hp, int shield, int energy, int damage, int critical, int defense) {
        this.hp = hp;
        this.shield = shield;
        this.energy = energy;
        this.damage = damage;
        this.critical = critical;
        this.defense = defense;
    }

    public Statistic() {
        this(100, 0, 0, 0, 0, 0);
    }
}
