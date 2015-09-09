
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

/**
 * The <tt>PercolationStats</tt> class represents a Monte Carlo simulation
 * of the Percolation object to find the percolation threshold. It computes
 * basic statistics using the methods <em>mean</em>, <em>stddev</em>
 * <em>confidenceLo</em>, and <em>confidenceHi</em>.
 *
 * @author Ali K Thabet
 */
public class PercolationStats {

    //private Percolation perc; // the Percolation object
    private int size; // dimensions of site array for Percolation
    private int nExp; // number of random experiments
    private double[] thresholds; // an array of size T to store Precolation thresholds

    /**
     * Initializes a PercolationStats item with a Percolation
     * of size N x N and performs T random experiments.
     * @param N dimension of sites grid
     * @param T number of times to repeat experiment
     * @throws java.lang.IllegalArgumentException if N <= 0 or T <= 0
     */
    public PercolationStats(int N, int T) {
        if (N <= 0) {
            throw new IllegalArgumentException("Site dimension " + N + " is not greater than 0.");
        }
        if (T <= 0) {
            throw new IllegalArgumentException("Number of experiments " + T + " is not greater than 0.");
        }
        size = N;
        nExp = T;
        thresholds = new double[T]; // threshold for each T simulation
        for (int i = 0; i < T; i++) {
            thresholds[i] = randomPercolation();
        }
    }

    /**
     * Randomly open sites until system percolates.
     * @return precolation threshold
     */
    private double randomPercolation() {
        Percolation perc = new Percolation(size); // initialize percolation with N x N sites
        int count = 0; // count number of open sites
        while (!perc.percolates()) {
            int i = StdRandom.uniform(1, size + 1);
            int j = StdRandom.uniform(1, size + 1);
            if (!perc.isOpen(i, j)) {
                perc.open(i, j);
                count++;
            }
        }
        return 1.0*count/(size * size);
    }

    /**
     * Computes the mean of all percolation thresholds.
     * @return sample mean of percolation threshold
     */
    public double mean() {
        return StdStats.mean(thresholds);
    }

    /**
     * Computes the standard deviation of all percolation thresholds.
     * @return sample standard deviation of percolation threshold
     */
    public double stddev() {
        return StdStats.stddev(thresholds);
    }

    /**
     * Computes the low endpoint of the 95% confidence interval.
     * @return low  endpoint of 95% confidence interval
     */
    public double confidenceLo() {
        return mean() - (1.96*stddev()/Math.sqrt(nExp));
    }

    /**
     * Computes the high endpoint of the 95% confidence interval.
     * @return high endpoint of 95% confidence interval
     */
    public double confidenceHi() {
        return mean() + (1.96*stddev()/Math.sqrt(nExp));
    }

    /**
     * Test client for PercolationStats.
     *
     * @param args 2 command line arguments N and T
     */
    public static void main(String[] args) {

        if (args.length != 2) {
            throw new IllegalArgumentException("Should be 2 input arguments, got " + args.length +" instead.");
        }

        int N = Integer.parseInt(args[0]);
        int T = Integer.parseInt(args[1]);

        PercolationStats psStats = new PercolationStats(N, T);
        StdOut.println("mean = " + psStats.mean());
        StdOut.println("stddev = " + psStats.stddev());
        StdOut.println("95% confidence interval = " + psStats.confidenceLo() + ", " + psStats.confidenceHi());
    }
}
