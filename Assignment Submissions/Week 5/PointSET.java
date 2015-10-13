import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.SET;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.Queue;

/**
 * Class <tt>PointSET</tt> implements a collection
 * of points using <tt>Point2D</tt> in the unit square.
 * Points are stored in a BST using the <tt>SET</tt>
 * data structure.
 * This implementation supports the <em>insert</em>,
 * <em>isEmpty</em>, <em>contains</em>, and <em>draw</em>
 * methods to manipulate and display points in the BST.
 * Additionally, there functions supports for <em>range</em>
 * search, where an iterable of points within a given rectangle
 * are returned, and <em>nearest</em>, where the nearest points
 * to input from the set is returned.
 *
 * @author Ali K Thabet
 */
public class PointSET {

    private SET<Point2D> set; // BST to store points

    /**
     * Constructor for empty PointSET
     */
    public PointSET() {
        set = new SET<>();
    }

    /**
     * Returns true if SET is empty
     *
     * @return true if set empty
     */
    public boolean isEmpty() {
        return set.isEmpty();
    }

    // helper function to check if point is null
    private void checkPoint(Point2D p) {
        if (p == null) {
            throw new NullPointerException("Point provided is NULL");
        }
    }

    /**
     * Returns the size of the point set
     *
     * @return size of the point set
     */
    public int size() {
        return set.size();
    }

    /**
     * Inserts point into SET
     *
     * @param p Point to insert
     */
    public void insert(Point2D p) {
        checkPoint(p);
        set.add(p);
    }

    /**
     * Returns true is query point is in the SET
     *
     * @param p Query point
     * @return true if query point is in the SET
     */
    public boolean contains(Point2D p) {
        checkPoint(p);
        return set.contains(p);
    }

    /**
     * Draw all the point sin the SET
     */
    public void draw() {
        StdDraw.setPenColor(StdDraw.BLACK);
        StdDraw.setPenRadius(.01);
        for (Point2D p : set) {
            p.draw();
        }
    }

    /**
     * Return an iterable object with all the points
     * inside the range rectangle
     *
     * @param rect Range rectangle
     * @return Iterable object with all points inside rect
     */
    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null) throw new
                NullPointerException("Rectangle provided is NULL");
        Queue<Point2D> q = new Queue<>();
        for (Point2D p : set) {
            if (rect.contains(p)) q.enqueue(p);
        }
        return q;
    }

    /**
     * Returns the point nearest to the query points
     *
     * @param p Query point
     * @return nearest point to p
     */
    public Point2D nearest(Point2D p) {
        checkPoint(p);
        Point2D minPoint = null;
        double minDist = 0.0; // = minPoint.distanceSquaredTo(p);
        for (Point2D pp : set) {
            if (minPoint == null) {
                minPoint = pp;
                minDist = minPoint.distanceSquaredTo(p);
                continue;
            }
            double dist = pp.distanceSquaredTo(p);
            if (dist < minDist) {
                minPoint = pp;
                minDist = dist;
            }
        }
        return minPoint;
    }

    // unit test
    public static void main(String[] args) {
        String filename = "kdtree/input10K.txt";
        In in = new In(filename);

        StdDraw.show(0);

        // initialize the data structure with point from standard input
        PointSET brute = new PointSET();
        while (!in.isEmpty()) {
            double x = in.readDouble();
            double y = in.readDouble();
            Point2D p = new Point2D(x, y);
            brute.insert(p);
        }

        // draw the points
        StdDraw.clear();
        StdDraw.setPenColor(StdDraw.BLACK);
        StdDraw.setPenRadius(.01);
        brute.draw();
    }
}
