package object;

public class Coordinate {
    private double x;
    private double y;

    public Coordinate(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return (int)x;
    }

    public int getY() {
        return (int)y;
    }

    public void move(double dx, double dy) {
        x += dx;
        y += dy;
    }
}
