import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdOut;

/**
 * @author Ali K Thabet
 */
public class Solver {

    private int numberMoves;
    private MinPQ<SearchNode> pq;
    private MinPQ<SearchNode> twinPq;
    private Stack<Board> iterable;

    public Solver(Board initial) {
        checkBoard(initial);
        numberMoves = 0;
        pq = new MinPQ<>();
        twinPq = new MinPQ<>();

        SearchNode first = new SearchNode();
        first.previous = null;
        first.moves = 0;
        first.item = initial;
        first.manhattan = first.item.manhattan();
        pq.insert(first);

        SearchNode twinFirst = new SearchNode();
        twinFirst.item = first.item.twin();
        twinFirst.previous = null;
        twinFirst.moves = 0;
        twinFirst.manhattan = twinFirst.item.manhattan();
        twinPq.insert(twinFirst);

        while (true) {
            SearchNode prev = pq.delMin();
            SearchNode twinPrev = twinPq.delMin();

            if (prev.item.isGoal()) {
                numberMoves = prev.moves;
                iterable = new Stack<>();
                while (prev != null) {
                    iterable.push(prev.item);
                    prev = prev.previous;
                }
                break;
            }

            if (twinPrev.item.isGoal()) {
                numberMoves = -1;
                break;
            }

            for (Board b : prev.item.neighbors()) {
                if (b.equals(prev.item)) continue;

                SearchNode curr = new SearchNode();
                curr.item = b;
                curr.previous = prev;
                curr.moves = prev.moves + 1;
                curr.manhattan = curr.item.manhattan();
                pq.insert(curr);
            }

            for (Board b : twinPrev.item.neighbors()) {
                if (b.equals(twinPrev.item)) continue;

                SearchNode curr = new SearchNode();
                curr.item = b;
                curr.previous = twinPrev;
                curr.moves = twinPrev.moves + 1;
                curr.manhattan = curr.item.manhattan();
                twinPq.insert(curr);
            }
        }
    }

    /**
     * Check if board is null
     *
     * @param board
     * @throws NullPointerException if board is null
     */
    private void checkBoard(Board board) {
        if (board == null) throw new NullPointerException("Board is null");
    }

    /**
     * Return true if initial board can be solve
     *
     * @return true if initial board can be sovlved
     */
    public boolean isSolvable() {
        return numberMoves != -1;
    }

    /**
     * Return the minimum number of moves to solve
     * an initial board. If board is unsolvable,
     * then return -1
     *
     * @return min number of move to solve board (-1 if unsolvable)
     */
    public int moves() {
        return numberMoves;
    }

    /**
     * Return the sequence of boards to reached
     * the shortest solution. If board is
     * unsolvable return nul
     *
     * @return sequence of boards in a shortest solution; null if unsolvable
     */
    public Iterable<Board> solution() {
        return iterable;
    }

    private static class SearchNode implements Comparable<SearchNode> {
        private Board item;
        private SearchNode previous;
        private int moves;
        private int manhattan;

        @Override
        public int compareTo(SearchNode that) {
            int priorityManhattan = this.manhattan+ this.moves;
            int thatPriorityManhattan = that.manhattan + that.moves;
            if (priorityManhattan < thatPriorityManhattan) return -1;
            if (priorityManhattan > thatPriorityManhattan) return 1;
            if (this.manhattan < that.manhattan) return -1;
            if (this.manhattan > that.manhattan) return 1;
            return 0;
        }
    }

    // unit test
    public static void main(String[] args) {
        // create initial board from file
        //String fileName = "8puzzle/puzzle20.txt";
        In in = new In(args[0]);
        int N = in.readInt();
        int[][] blocks = new int[N][N];
        for (int i = 0; i < N; i++)
            for (int j = 0; j < N; j++)
                blocks[i][j] = in.readInt();
        Board initial = new Board(blocks);

        // solve the puzzle
        Solver solver = new Solver(initial);

        // print solution to standard output
        if (!solver.isSolvable())
            StdOut.println("No solution possible");
        else {
            StdOut.println("Minimum number of moves = " + solver.moves());
            for (Board board : solver.solution())
                StdOut.println(board);
        }
    }
}
