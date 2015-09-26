import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * The <tt>BruteCollinearPoints</tt> class implements
 * an algorithm to brute force find sets of 4 collinear
 * points given an array of points.
 * <p>
 * The implementation uses stores an array of points in
 * <em>points</em> and finds all line segments containing
 * 4 collinear points. The line segments are stored in
 * <em>lineSegments</em>
 * </p>
 *
 * @author Ali K Thabet
 **/
public class BruteCollinearPoints {

    private Point[] points; // array of all points
    private LineSegment[] lineSegments; // array of line segments with 4 collinear points
    private int N; // number of line segments with 4 collinear points

    /**
     * Brute force approach, search all
     * combinations of 4 points and store
     * those with 4 collinear points
     *
     * @param p array of input points
     */
    public BruteCollinearPoints(Point[] p) {

        points = p.clone();
        //lineSegments = new ArrayList<LineSegment>();
        ArrayList<LineSegment> tempLineSegment = new ArrayList<>();
        N = 0;
        if (points == null) throw new NullPointerException("Input array is null");

        for (int i = 0; i < points.length; i++) {
            checkPoint(i);
            for (int j = i + 1; j < points.length; j++) {
                checkPoint(j);
                checkPoints(i, j);
                for (int k = j + 1; k < points.length; k++) {
                    checkPoint(k);
                    if (points[i].slopeTo(points[j]) != points[i].slopeTo(points[k])) continue;
                    for (int w = k + 1; w < points.length; w++) {
                        checkPoint(w);
                        if (points[i].slopeTo(points[k]) == points[i].slopeTo(points[w])) {
                            Point[] temp = {points[i], points[j], points[k], points[w]};
                            Arrays.sort(temp, 0, temp.length);
                            tempLineSegment.add(new LineSegment(temp[0], temp[temp.length - 1]));
                        }
                    }
                }
            }
        }
        N = tempLineSegment.size();
        lineSegments = new LineSegment[N];
        for (int i = 0; i < N; i++) {
            lineSegments[i] = tempLineSegment.get(i);
        }
    }

    /**
     *
     * Return the number of line segments
     *
     * @return the number of line segments
     */
    public int numberOfSegments() {
        return N;
    }

    /**
     *
     * Return the array of line segments
     *
     * @return the array of line segments
     */
    public LineSegment[] segments() {
        return lineSegments.clone();
    }

    // check if two points are equal
    private void checkPoints(int i, int j) {
        if (points[i].compareTo(points[j]) == 0) {
            throw new IllegalArgumentException("Points at " + i + " and " + j + " are equal");
        }
    }

    // check if point is null
    private void checkPoint(int i) {
        if (points[i] == null) throw new NullPointerException("Null item at location " + i);
    }

    public static void main(String[] args) {

        // read the N points from a file
        String fileName = "collinear/input40.txt";
        In in = new In(fileName);
        int N = in.readInt();
        Point[] points = new Point[N];
        for (int i = 0; i < N; i++) {
            int x = in.readInt();
            int y = in.readInt();
            points[i] = new Point(x, y);
        }

        // draw the points
        StdDraw.show(0);
        StdDraw.setXscale(0, 32768);
        StdDraw.setYscale(0, 32768);
        for (Point p : points) {
            p.draw();
        }
        StdDraw.show();

        // print and draw the line segments
        BruteCollinearPoints collinear = new BruteCollinearPoints(points);
        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
            segment.draw();
        }
    }
}
