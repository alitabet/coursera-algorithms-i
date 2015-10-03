import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;

import java.util.Arrays;

/**
 * Call <tt>Board</tt> implements an NxN board to
 * represent the K-puzzle problem, where K = NxN - 1.
 *
 * @author Ali K Thabet
 */
public class Board {

    private int N; // dimension of the board
    private final int[][] tiles; // NxN array of blocks
    private int iZero, jZero; // location of zero index
    /**
     * Initialize a block using the tiles
     * provide in array <em>params</em>
     *
     * @param blocks
     */
    public Board(int[][] blocks) {
        N = blocks[0].length;
        tiles = new int[N][N];
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                tiles[i][j] = blocks[i][j];
                checkTile(i, j);
                if (blocks[i][j] == 0) {
                    iZero = i;
                    jZero = j;
                }
            }
        }
    }

    // check if tile is null
    private void checkTile(int i, int j) {
        if (tiles[i][j] < 0 || tiles[i][j] > N*N - 1) {
            throw new IllegalArgumentException("Value at location ("
                    + i + ", " + j + ") = " + tiles[i][j]
                    + " is not in [0, " + N + "]");
        }
    }

    /**
     * Return the dimension of the block
     *
     * @return dimension of block
     */
    public int dimension() {
        return N;
    }

    /**
     * Return the Hamming distance, defined
     * in this case as the number of blocks
     * out of place
     *
     * @return number of blocks out of place
     */
    public int hamming() {
        int outOfPlace = 0;

        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                if (tiles[i][j] == 0) continue;

                if (tiles[i][j] != linearIndex(i, j)) outOfPlace++;
            }
        }

        return outOfPlace;
    }

    /**
     * Return the Manhattan distance, defined
     * in this case as the sum of Manhattan
     * distances between the blocks and the
     * goal
     *
     * @return sum of Manhattan distances
     */
    public int manhattan() {
        int manhattanDistance = 0;
        int[] curr;

        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                if (tiles[i][j] == 0) continue;

                curr = doubleIndex(tiles[i][j]);
                manhattanDistance +=
                        (Math.abs(curr[0] - i - 1) + Math.abs(curr[1] - j - 1));
            }
        }
        return manhattanDistance;
    }

    // convert index (i, j) to linear index
    private int linearIndex(int i, int j) {
        return i * N + j + 1;
    }

    //convert linear index into (i, j)
    private int[] doubleIndex(int i) {
        int[] doubleIndex = new int[2];
        doubleIndex[1] = (i - 1) % N + 1;
        doubleIndex[0] = ((i - 1)/ N) + 1;
        return doubleIndex;
    }

    /**
     * True if current board is the goal board
     *
     * @return true if goal board
     */
    public boolean isGoal() {
        return this.hamming() == 0;
    }

    private void swap(int[][] a, int i, int j, int k, int w) {
        int temp = a[i][j];
        a[i][j] = a[k][w];
        a[k][w] = temp;
    }
    /**
     * Returns a twin board, which is a board
     * that is obtained by exchanging any pair
     * of blocks
     *
     * @return twin board of current board
     */
    public Board twin() {
        int[][] aux = new int[N][N];

        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                aux[i][j] = tiles[i][j];
            }
        }

        int i = StdRandom.uniform(N);
        int j = StdRandom.uniform(N);

        while (aux[i][j] == 0) {
            i = StdRandom.uniform(N);
            j = StdRandom.uniform(N);
        }

        int k = StdRandom.uniform(N);
        int w = StdRandom.uniform(N);

        while (aux[k][w] == 0 || aux[k][w] == aux[i][j]) {
            k = StdRandom.uniform(N);
            w = StdRandom.uniform(N);
        }

        swap(aux, i, j, k, w);

        return new Board(aux);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Board board = (Board) o;

        if (N != board.N) return false;
        return Arrays.deepEquals(tiles, board.tiles);

    }

    /**
     * String representation of board, where N goes first
     * followed by a matrix look of board
     *
     * @return String representation of board
     */
    public String toString() {
        StringBuilder s = new StringBuilder();
        s.append(N + "\n");
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                s.append(String.format("%2d ", tiles[i][j]));
            }
            s.append("\n");
        }
        return s.toString();
    }

    /**
     * Returns an iterable object containing the
     * neighbors of the board. We use an object
     * of class {@link Queue} to store the neighbors
     * and return the {@link Queue}
     * @return
     */
    public Iterable<Board> neighbors() {
        Queue<Board> neighbors = new Queue<>();
        int[][] aux;

        if (iZero > 0) {
            swap(tiles, iZero, jZero, iZero - 1, jZero);
            neighbors.enqueue(new Board(tiles));
            swap(tiles, iZero, jZero, iZero - 1, jZero);
        }

        if (iZero < N - 1) {
            swap(tiles, iZero, jZero, iZero + 1, jZero);
            neighbors.enqueue(new Board(tiles));
            swap(tiles, iZero, jZero, iZero + 1, jZero);
        }

        if (jZero > 0) {
            swap(tiles, iZero, jZero, iZero, jZero - 1);
            neighbors.enqueue(new Board(tiles));
            swap(tiles, iZero, jZero, iZero, jZero - 1);
        }

        if (jZero < N - 1) {
            swap(tiles, iZero, jZero, iZero, jZero + 1);
            neighbors.enqueue(new Board(tiles));
            swap(tiles, iZero, jZero, iZero, jZero + 1);
        }

        return neighbors;
    }

    // unit tests
    public static void main(String[] args) {
        // create initial board from file
        String fileName = "8puzzle/puzzle3x3-12.txt";
        In in = new In(fileName);
        int N = in.readInt();
        int[][] blocks = new int[N][N];
        for (int i = 0; i < N; i++)
            for (int j = 0; j < N; j++)
                blocks[i][j] = in.readInt();
        Board initial = new Board(blocks);

        StdOut.println(initial.toString());
        StdOut.println(initial.twin().toString());
        Iterable<Board> iterable = initial.neighbors();
        for (Board b  : iterable) {
            StdOut.println(b.toString());
        }
        StdOut.println(initial.toString());
        StdOut.println("Hamming distance = " + initial.hamming());
        StdOut.println("Manhattan distance = " + initial.manhattan());
        if (initial.isGoal()) StdOut.println("Goal");
        else StdOut.println("Not goal.");
    }
}
