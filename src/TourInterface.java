import java.awt.Graphics;

public interface TourInterface {
    void print();
    void draw(Graphics g);
    double distance();
    int size();
    void add(Point p);
    void insertNearest(Point p);
    void insertSmallest(Point p);
}