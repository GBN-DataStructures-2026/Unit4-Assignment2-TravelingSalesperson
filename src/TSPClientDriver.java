import java.awt.Color;
import java.awt.Graphics;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class TSPClientDriver {

    private static class Dataset {
        int width;
        int height;
        List<Point> points = new ArrayList<>();
    }

    public static void main(String[] args) throws FileNotFoundException {
        System.out.println("=== PHASE 1A: Verifying tsp5.txt Order & Distance ===");
        if (!verifyTsp5("tsp5.txt")) {
            System.err.println("\n[STOPPED] tsp5.txt is NOT in the correct order.");
            System.err.println("Fix your Tour insertion order before advancing to tsp1000.txt.");
            return;
        }

        System.out.println("\n[PASSED] tsp5.txt verified! Advancing to tsp1000.txt...\n");

        System.out.println("=== PHASE 1B: Running tsp1000.txt ===");
        // In Phase 2, comment out "add" and "nearest" so only "smallest" runs:
        runAndDraw("tsp1000.txt", "add", "Points as given");
        runAndDraw("tsp1000.txt", "nearest", "Nearest neighbor heuristic");
        runAndDraw("tsp1000.txt", "smallest", "Smallest increase heuristic");

        /*
         * === PHASE 2: USA Map & Mona Lisa (1000 points) ===
         * Checklist before uncommenting:
         * 1. Shrink g.fillOval(..., 5, 5) to 2, 2 (or 1, 1) in Point.java.
         * 2. Comment out Phase 1B above (and keep "add" and "nearest" commented out).
         * 3. Ignore mona-20k/50k/100k; only run usa13509.txt and mona-1000.txt.
         *
         * runAndDraw("usa13509.txt", "smallest", "Smallest increase heuristic");
         * runAndDraw("mona-1000.txt", "smallest", "Smallest increase heuristic");
         */
    }

    private static boolean verifyTsp5(String filename) throws FileNotFoundException {
        Dataset data = loadDataset(filename);
        List<Point> pts = data.points;
        if (pts.size() != 5) {
            System.err.println("Expected 5 points in " + filename + ", found " + pts.size());
            return false;
        }

        // Exact expected point indices for tsp5.txt:
        // add:      [0, 1, 2, 3, 4] -> distance 741.58
        // nearest:  [0, 3, 2, 1, 4] -> distance 577.79
        // smallest: [0, 2, 4, 1, 3] -> distance 467.79
        int[] orderAdd      = {0, 1, 2, 3, 4};
        int[] orderNearest  = {0, 3, 2, 1, 4};
        int[] orderSmallest = {0, 2, 4, 1, 3};

        boolean ok1 = checkTourOrder("add", buildTour(pts, "add"), pts, orderAdd, 741.58);
        boolean ok2 = checkTourOrder("insertNearest", buildTour(pts, "nearest"), pts, orderNearest, 577.79);
        boolean ok3 = checkTourOrder("insertSmallest", buildTour(pts, "smallest"), pts, orderSmallest, 467.79);

        return ok1 && ok2 && ok3;
    }

    private static boolean checkTourOrder(
            String label, TourInterface tour, List<Point> originalPts, int[] expectedIdx, double expectedDist) {

        if (tour.size() != expectedIdx.length) {
            System.err.printf("  [FAIL] %-14s size mismatch: expected %d, got %d%n",
                    label, expectedIdx.length, tour.size());
            return false;
        }

        List<Point> actualPts = capturePrintedPoints(tour);
        if (actualPts.size() != expectedIdx.length) {
            System.err.printf("  [FAIL] %-14s print() output %d points, expected %d%n",
                    label, actualPts.size(), expectedIdx.length);
            return false;
        }

        boolean orderMatches = true;
        for (int i = 0; i < expectedIdx.length; i++) {
            Point exp = originalPts.get(expectedIdx[i]);
            Point act = actualPts.get(i);
            if (Math.abs(exp.getX() - act.getX()) > 1e-3 || Math.abs(exp.getY() - act.getY()) > 1e-3) {
                orderMatches = false;
                break;
            }
        }

        if (!orderMatches) {
            System.err.println("  [FAIL] " + label + " order mismatch!");
            System.err.print("    Expected: ");
            for (int i = 0; i < expectedIdx.length; i++) {
                System.err.print(originalPts.get(expectedIdx[i]) + (i < 4 ? " -> " : "\n"));
            }
            System.err.print("    Actual:   ");
            for (int i = 0; i < actualPts.size(); i++) {
                System.err.print(actualPts.get(i) + (i < actualPts.size() - 1 ? " -> " : "\n"));
            }
            return false;
        }

        double actualDist = tour.distance();
        System.out.printf("  [OK] %-14s | Order verified | Distance: %.2f (expected %.2f)%n",
                label, actualDist, expectedDist);
        return true;
    }

    private static List<Point> capturePrintedPoints(TourInterface tour) {
        PrintStream originalOut = System.out;
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        System.setOut(new PrintStream(buffer));
        try {
            tour.print();
        } finally {
            System.setOut(originalOut);
        }

        List<Point> parsed = new ArrayList<>();
        String[] lines = buffer.toString().trim().split("\\R+");
        for (String line : lines) {
            String cleaned = line.replace("(", "").replace(")", "").trim();
            if (cleaned.isEmpty()) continue;
            String[] parts = cleaned.split(",");
            if (parts.length == 2) {
                parsed.add(new Point(Double.parseDouble(parts[0].trim()), Double.parseDouble(parts[1].trim())));
            }
        }
        return parsed;
    }

    private static void runAndDraw(String filename, String mode, String label) throws FileNotFoundException {
        Dataset data = loadDataset(filename);
        long start = System.currentTimeMillis();
        TourInterface tour = buildTour(data.points, mode);
        double elapsedSec = (System.currentTimeMillis() - start) / 1000.0;

        DrawingPanel panel = new DrawingPanel(data.width, data.height);
        Graphics g = panel.getGraphics();
        g.setColor(Color.BLACK);
        tour.draw(g);

        g.setColor(Color.RED);
        g.drawString("Tour with " + tour.size() + " points", 10, 20);
        g.drawString(label, 10, 35);
        g.drawString(String.format("Tour distance: %.2f", tour.distance()), 10, 50);
        g.drawString(String.format("Elapsed time: %.2f seconds", elapsedSec), 10, 65);

        System.out.printf("%s [%-8s] | Size: %d | Distance: %.2f | Time: %.2fs%n",
                filename, mode, tour.size(), tour.distance(), elapsedSec);
    }

    private static TourInterface buildTour(List<Point> points, String mode) {
        TourInterface tour = new Tour();
        for (Point p : points) {
            switch (mode) {
                case "add":
                    tour.add(p);
                    break;
                case "nearest":
                    tour.insertNearest(p);
                    break;
                case "smallest":
                    tour.insertSmallest(p);
                    break;
                default:
                    throw new IllegalArgumentException("Unknown mode: " + mode);
            }
        }
        return tour;
    }

    private static Dataset loadDataset(String filename) throws FileNotFoundException {
        File file = new File("Data Files", filename);
        if (!file.exists()) {
            file = new File(filename);
        }

        Dataset data = new Dataset();
        try (Scanner sc = new Scanner(file)) {
            data.width = sc.nextInt();
            data.height = sc.nextInt();
            while (sc.hasNextDouble()) {
                double x = sc.nextDouble();
                double y = sc.nextDouble();
                data.points.add(new Point(x, y));
            }
        }
        return data;
    }
}