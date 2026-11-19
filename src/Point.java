import java.awt.Graphics;

public class Point {
    private final double x;
    private final double y;

    public Point(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double distanceTo(Point that) {
        double dx = this.x - that.x;
        double dy = this.y - that.y;
        return Math.sqrt(dx * dx + dy * dy);
    }

    public void draw(Graphics g) {
        // PHASE 1 (tsp5 / tsp1000): 5x5 pixel circles
        // PHASE 2 (usa13509 / mona-1000): Shrink 5, 5 to 2, 2 (or 1, 1) before running!
        g.fillOval((int) x - 2, (int) y - 2, 5, 5);
    }

    public void drawTo(Point that, Graphics g) {
        g.drawLine((int) this.x, (int) this.y, (int) that.x, (int) that.y);
    }

    @Override
    public String toString() {
        return "(" + x + ", " + y + ")";
    }
}