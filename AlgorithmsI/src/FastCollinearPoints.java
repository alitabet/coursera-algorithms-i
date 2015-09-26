import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

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

        points = p;
        ArrayList<LineSegment> tempLineSegment = new ArrayList<>();
        N = 0;

        if (points == null) throw new NullPointerException("Input array is null");
        ArrayList<Point> tempList = new ArrayList<>();
        for (int i = 0; i < points.length; i++) {
            checkPoint(i);
            Point[] temp = new Point[points.length - 1]; // aux array
            int idx = 0;
            for (int j = 0; j < points.length; j++) {
                if (i == j) continue;;
                temp[idx++] = points[j];
            }
            Arrays.sort(temp, points[i].slopeOrder());
            int prev = 0, curr = 1;
            while (curr < temp.length) {
                if (points[i].slopeTo(temp[prev]) == points[i].slopeTo(temp[curr])) {
                    curr++;
                }
                else {
                    if (curr - prev > 2) {
                        Point[] aux = new Point[curr - prev + 1];
                        aux[0] = points[i];
                        int count = 1;
                        for (int k = prev; k < curr; k++) aux[count++] = temp[k];

                        Arrays.sort(aux);
                        boolean duplicate = false;
                        for (Point pp : tempList) {
                            if (aux[0].compareTo(pp) == 0) {
                                duplicate = true;
                                break;
                            }
                        }
                        if (!duplicate) {
                            LineSegment line = new LineSegment(aux[0], aux[aux.length - 1]);
                            tempList.add(aux[0]);
                            tempLineSegment.add(line);
                        }
                    }
                    prev = curr;
                    curr++;
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
        return lineSegments;
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
        String fileName = "collinear/input10.txt";
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
        FastCollinearPoints collinear = new FastCollinearPoints(points);
        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
            segment.draw();
        }
    }
}
