import java.awt.Graphics;

public class Tour implements TourInterface {

    // Private inner node class for the circular singly-linked list
    private class ListNode {
        private Point data;
        private ListNode next;

        public ListNode(Point data) {
            this(data, null);
        }

        public ListNode(Point data, ListNode next) {
            this.data = data;
            this.next = next;
        }
    }

    private ListNode front;

    /**
     * Constructs an empty tour.
     */
    public Tour() {
        front = null;
    }

    /**
     * Returns the number of points in this tour.
     */
    @Override
    public int size() {
        // TODO: Count and return the number of nodes in the circular list
        return 0;
    }

    /**
     * Prints each point in the tour to System.out, one per line.
     */
    @Override
    public void print() {
        // TODO: Traverse from front and print each node's Point on its own line
    }

    /**
     * Draws all points and connecting edges (including the closing edge back to front).
     */
    @Override
    public void draw(Graphics g) {
        // TODO: Traverse the circular list and draw each point and edge to curr.next
        // Hint: You can call curr.data.draw(g) and curr.data.drawTo(curr.next.data, g)
    }

    /**
     * Returns the total Euclidean length of the tour (including the closing edge).
     */
    @Override
    public double distance() {
        // TODO: Sum distances around the circular list, including last -> front
        return -1.0;
    }

    /**
     * Appends p to the end of the tour (immediately before front).
     */
    @Override
    public void add(Point p) {
        // TODO: Handle empty tour (front == null) and append p at the end of the cycle
    }

    /**
     * Inserts p immediately after the closest existing point in the tour.
     * Break ties by keeping the first closest point encountered from front.
     */
    @Override
    public void insertNearest(Point p) {
        // TODO: Implement nearest-neighbor insertion
    }

    /**
     * Inserts p where it causes the smallest net increase in tour distance.
     * Break ties by keeping the first position encountered from front.
     */
    @Override
    public void insertSmallest(Point p) {
        // TODO: Implement O(1)-per-edge smallest-increase insertion
    }
}