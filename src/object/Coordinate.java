package object;

import mvc.Display;

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

    public void move(double dx, double dy, double minX, double maxX, double minY, double maxY) {
        x += dx;
        x = (((x < minX) ? minX : x) > maxX) ? maxX : x;
        y += dy;
        y = (y > maxY) ? maxY : y;
    }
}
