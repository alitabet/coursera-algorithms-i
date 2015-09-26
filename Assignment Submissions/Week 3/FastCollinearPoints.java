import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.Stopwatch;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * The <tt>FastCollinearPoints</tt> class implements
 * an algorithm to find line segments of at least
 * 4 collinear points. The fast implementation uses
 * sorting to optimize the search.
 * <p>
 * The implementation uses stores an array of points in
 * <em>points</em> and finds all line segments containing
 * 4 collinear points. The line segments are stored in
 * <em>lineSegments</em>
 * </p>
 *
 * @author Ali K Thabet
 **/
public class FastCollinearPoints {

    private Point[] points; // array of all points
    private LineSegment[] lineSegments; // array of line segments with 4 collinear points
    private int N; // number of line segments with 4 collinear points

    /**
     * Fast collinear search method
     * using sorting
     *
     * @param p array of input points
     */
    public FastCollinearPoints(Point[] p) {
        points = p.clone();
        ArrayList<LineSegment> tempLineSegment = new ArrayList<>();
        N = 0;

        if (points == null) throw new NullPointerException("Input array is null");

        Point[] aux;
        Arrays.sort(points);
        for (int i = 0; i < points.length; i++) {
            checkPoint(i);

            aux = points.clone();

            Arrays.sort(aux, points[i].slopeOrder());

            int prev = aux.length - 1, curr = prev - 1;
            double prevSlope = points[i].slopeTo(aux[prev]);
            double currSlope;
            while (curr >= 0) {
                currSlope = points[i].slopeTo(aux[curr]);
                if (points[i].slopeTo(aux[curr + 1]) == Double.NEGATIVE_INFINITY) throw new IllegalArgumentException();

                if (prevSlope == currSlope) {
                    curr--;
                }
                else {
                    if (prev - curr > 2) {

                        if (points[i].compareTo(aux[curr + 1]) < 0) {
                            tempLineSegment.add(new LineSegment(points[i], aux[prev]));
                        }
                    }
                    prev = curr;
                    prevSlope = currSlope;
                    curr--;
                }
            }
        }

        N = tempLineSegment.size();
        lineSegments = new LineSegment[N];
        for (int i = 0; i < N; i++) {
            lineSegments[i] = tempLineSegment.get(i);
        }
        tempLineSegment.clear();
    }

    private void printSlopes(Point[] a, Point p) {
        StdOut.println("---------------------------------------------------");
        StdOut.println("Current point: " + p);
        for (int i = 0; i < a.length; i++) {
            StdOut.print(p.slopeTo(a[i]) + " ");
        }
        StdOut.print("\n");
        for (int i = 0; i < a.length; i++) {
            StdOut.print(a[i]);
        }
        StdOut.print("\n");
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
        String fileName = "collinear/input8.txt";
        In in = new In(fileName);
        int N = in.readInt();
        Point[] points = new Point[N];
        for (int i = 0; i < N; i++) {
            int x = in.readInt();
            int y = in.readInt();
            points[i] = new Point(x, y);
        }

        FastCollinearPoints collinear = new FastCollinearPoints(points);
        StdOut.println(collinear.numberOfSegments());
        StdOut.println(collinear.numberOfSegments());
        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
        }

        points[0] = new Point(20000, 0);

        StdOut.println(collinear.numberOfSegments());
        StdOut.println(collinear.numberOfSegments());
        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
        }

        LineSegment[] lineSegments = collinear.segments();
        for (LineSegment segment : lineSegments) {
            StdOut.println(segment);
        }

        lineSegments[0] = new LineSegment(new Point(100, 100), new Point(200, 200));
        for (LineSegment segment : lineSegments) {
            StdOut.println(segment);
        }

        LineSegment[] lineSegments2 = collinear.segments();
        for (LineSegment segment : lineSegments2) {
            StdOut.println(segment);
        }
//        // draw the points
//        StdDraw.show(0);
//        StdDraw.setXscale(0, 32768);
//        StdDraw.setYscale(0, 32768);
//        for (Point p : points) {
//            p.draw();
//        }
//        StdDraw.show();
//
//        // print and draw the line segments
//        FastCollinearPoints collinear = new FastCollinearPoints(points);
//        for (LineSegment segment : collinear.segments()) {
//            StdOut.println(segment);
//            segment.draw();
//        }
    }
}
