import edu.princeton.cs.algs4.*;

/**
 * Created by alitabet on 10/12/15.
 */
public class KdTree {

    private static final boolean EVEN_NODE = true;

    private Node root; // root node of KDTree
    private int N = 0; // number of nodes in KDTree

    private static class Node {
        private Point2D p;      // the point
        private RectHV rect;    // the axis-aligned rectangle corresponding to this node
        private Node lb;        // the left/bottom subtree
        private Node rt;        // the right/top subtree

         Node(Point2D p, RectHV rect) {
            this.p = p;
            this.rect = rect;
        }
    }

    /**
     * Constructor for empty KDTree
     */
    public KdTree() {
        root = null;
        N = 0;
    }

    /**
     * Returns the size of the KDTree
     *
     * @return size of the KDTree
     */
    public int size() {
        return N;
    }

    /**
     * Returns true if KDTree is empty
     *
     * @return true if KDTree empty
     */
    public boolean isEmpty() {
        return N == 0;
    }

    // helper function to check if point is null
    private void checkPoint(Point2D p) {
        if (p == null) {
            throw new NullPointerException("Point provided is NULL");
        }
    }

    /**
     * Inserts point into KDTree
     *
     * @param p Point to insert
     */
    public void insert(Point2D p) {
        checkPoint(p);
        root = insert(root, p,
                0.0, 0.0, 1.0, 1.0, EVEN_NODE);
    }

    private Node insert(Node x, Point2D p, double xmin, double ymin, double xmax, double ymax, boolean isEven) {
        if (x == null) {
            N++;
            return new Node(p, new RectHV(xmin, ymin, xmax, ymax));
        }

        if (p.equals(x.p)) return x;

        int cmp;
        if (isEven) cmp = Point2D.X_ORDER.compare(p, x.p);
        else        cmp = Point2D.Y_ORDER.compare(p, x.p);

        if (cmp < 0) {
            if (isEven) xmax = x.p.x();
            else        ymax = x.p.y();
            x.lb = insert(x.lb, p, xmin, ymin, xmax, ymax, !isEven);
        }
        else        {
            if (isEven) xmin = x.p.x();
            else        ymin = x.p.y();
            x.rt = insert(x.rt, p, xmin, ymin, xmax, ymax, !isEven);
        }

        return x;
    }

    private Node insert(Node x, Point2D p, RectHV rect, boolean isEven) {
        if (x == null) {
            N++;
            return new Node(p, rect);
        }

        if (p.equals(x.p)) return x;

        int cmp;
        if (isEven) cmp = Point2D.X_ORDER.compare(p, x.p);
        else        cmp = Point2D.Y_ORDER.compare(p, x.p);

        double xmin = rect.xmin(), xmax = rect.xmax();
        double ymin = rect.ymin(), ymax = rect.ymax();
        if (cmp < 0) {
            if (isEven) xmax = x.p.x();
            else        ymax = x.p.y();
            x.lb = insert(x.lb, p, new RectHV(xmin, ymin, xmax, ymax), !isEven);
        }
        else        {
            if (isEven) xmin = x.p.x();
            else        ymin = x.p.y();
            x.rt = insert(x.rt, p, new RectHV(xmin, ymin, xmax, ymax), !isEven);
        }

        return x;
    }

    /**
     * Returns true is query point is in the KDTree
     *
     * @param p Query point
     * @return true if query point is in the KDTree
     */
    public boolean contains(Point2D p) {
        checkPoint(p);
        return contains(root, p, EVEN_NODE) != null;
    }

    private Point2D contains(Node x, Point2D p, boolean isEven) {
        if (x == null) return null;

        if (p.equals(x.p)) return x.p;

        int cmp;
        if (isEven) cmp = Point2D.X_ORDER.compare(p, x.p);
        else        cmp = Point2D.Y_ORDER.compare(p, x.p);

        if      (cmp < 0) return contains(x.lb, p, !isEven);
        else              return contains(x.rt, p, !isEven);
    }

    /**
     * Draw all the points in the Tree
     * and the dividing lines.
     */
    public void draw() {
        Queue<Node> queue = new Queue<>();
        Queue<Boolean> evenQueue = new Queue<>();
        queue.enqueue(root);
        evenQueue.enqueue(EVEN_NODE);

        while (!queue.isEmpty()) {
            Node x = queue.dequeue();
            boolean isEven = evenQueue.dequeue();

            if (x == null) continue;

            StdDraw.setPenColor(StdDraw.BLACK);
            StdDraw.setPenRadius(.01);
            x.p.draw();
            StdDraw.setPenRadius(.005);
            if (isEven) {
                StdDraw.setPenColor(StdDraw.RED);
                StdDraw.line(x.p.x(), x.rect.ymin(), x.p.x(), x.rect.ymax());
            } else {
                StdDraw.setPenColor(StdDraw.BLUE);
                StdDraw.line(x.rect.xmin(), x.p.y(), x.rect.xmax(), x.p.y());
            }
            queue.enqueue(x.lb); evenQueue.enqueue(!isEven);
            queue.enqueue(x.rt); evenQueue.enqueue(!isEven);
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
        range(root, rect, q);
        return q;
    }

    private void range(Node x, RectHV rect, Queue<Point2D> q) {
        if (x == null) return;
        if (!rect.intersects(x.rect)) return;

        if (rect.contains(x.p)) q.enqueue(x.p);

        range(x.lb, rect, q);
        range(x.rt, rect, q);
    }

    /**
     * Returns the point nearest to the query points
     *
     * @param p Query point
     * @return nearest point to p
     */
    public Point2D nearest(Point2D p) {
        if (isEmpty()) return null;
        return nearest(root, p, root.p, EVEN_NODE);
    }

    private Point2D nearest(Node x, Point2D p, Point2D closest, boolean isEven) {
        if (x == null) return closest;

        double dist = p.distanceSquaredTo(closest);

        if (x.rect.distanceSquaredTo(p) > dist) return closest;

        if (p.distanceSquaredTo(x.p) < dist) closest = x.p;

        int cmp;
        if (isEven) cmp = Point2D.X_ORDER.compare(p, x.p);
        else        cmp = Point2D.Y_ORDER.compare(p, x.p);

        if (cmp < 0) {
            closest = nearest(x.lb, p, closest, !isEven);
            closest = nearest(x.rt, p, closest, !isEven);
        } else {
            closest = nearest(x.rt, p, closest, !isEven);
            closest = nearest(x.lb, p, closest, !isEven);
        }

        return closest;
    }

    // unit test
    public static void main(String[] args) {
        String filename = "kdtree/input1M.txt";

        StdDraw.show(0);
        StdDraw.setPenRadius(.02);
        // initialize the data structure with point from standard input
        KdTree kdtree = new KdTree();
        PointSET brute = new PointSET();

        In in = new In(filename);
        Stopwatch stopwatch = new Stopwatch();
        while (!in.isEmpty()) {
            double x = in.readDouble();
            double y = in.readDouble();
            Point2D p = new Point2D(x, y);
            kdtree.insert(p);
        }
        StdOut.println("Inserting in KDTree took " + stopwatch.elapsedTime());

        in = new In(filename);
        stopwatch = new Stopwatch();
        while (!in.isEmpty()) {
            double x = in.readDouble();
            double y = in.readDouble();
            Point2D p = new Point2D(x, y);
            brute.insert(p);
        }
        StdOut.println("Inserting in PointSET took " + stopwatch.elapsedTime());

        Point2D query = new Point2D(0.1, 0.7);
        StdDraw.setPenRadius(.02);
        StdDraw.setPenColor(StdDraw.RED);
        query.draw();
        StdDraw.setPenColor(StdDraw.YELLOW);
        stopwatch = new Stopwatch();
        brute.nearest(query).draw();
        StdOut.println("Nearest in PointSET took " + stopwatch.elapsedTime());
        StdDraw.setPenColor(StdDraw.BLUE);
        stopwatch = new Stopwatch();
        kdtree.nearest(query).draw();
        StdOut.println("Nearest in KDTree took " + stopwatch.elapsedTime());
        StdDraw.show(50);

        KdTree test = new KdTree();
        test.nearest(query);
        test.contains(query);
    }


}
