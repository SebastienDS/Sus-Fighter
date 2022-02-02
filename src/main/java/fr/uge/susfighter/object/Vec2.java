package fr.uge.susfighter.object;

public class Vec2 {
    private double x;
    private double y;

    public Vec2(double x, double y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public String toString() {
        return "Vec2{" +
                "x=" + x +
                ", y=" + y +
                '}';
    }

    /**
     * Set the X value
     * @param x value
     */
    public void setX(double x) {
        this.x = x;
    }

    /**
     * Add the X value
     * @param x value
     */
    public void addX(double x) {
        this.x += x;
    }

    /**
     * Get the X value
     * @return x value
     */
    public double getX() {
        return x;
    }

    /**
     * Set the Y value
     * @param y value
     */
    public void setY(double y) {
        this.y = y;
    }

    /**
     * Add the Y value
     * @param y value
     */
    public void addY(double y) {
        this.y += y;
    }

    /**
     * Get the Y value
     * @return y value
     */
    public double getY() {
        return y;
    }
}
