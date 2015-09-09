import java.io.File;
import java.io.IOException;
import java.util.Scanner;

import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.WeightedQuickUnionUF;

/**
 * The <tt>Percolation</tt> class represents a percolate data structure.
 * This implementation supports <em>percolates</em> operation to determine
 * whether a system percolates.
 * <p> 
 * The implementation uses an object <tt>WeightedQuickUnionUF</tt>  to store 
 * the sites. For a <tt>Percolation</tt> object of size N, a 
 * <tt>WeightedQuickUnionUF</tt> of size N^2 + 2 is created.
 * The N^2 corresponds to all the sites in the Percolation, and
 * the extra 2 represent 2 virtual sites, one top and one bottom  
 * Whenever a site is opened, the <tt>WeightedQuickUnionUF</tt> 
 * will connect it to all its open neighbors (use a 4-connectivity).
 * </p>
 * <p>
 * To implement the percolation efficiently, the <tt>WeightedQuickUnionUF</tt>
 * stores 2 extra virtual sites. The top site is always connected to the open
 * sites of the first row. The bottom site is always connected to the open sites
 * of the last row. To check whether a system percolates, we just check whether
 * the first site (virtual) is connected to the last site (virtual) using 
 * the <tt>WeightedQuickUnionUF</tt> method <em>connected</em>
 * 
 * </p>
 * @author Ali K Thabet
 **/

public class Percolation {
    private boolean[][] sites; // array of sites with dimensions size x size
    private int size;  // dimensions of site array
    private WeightedQuickUnionUF quickUnionObject, auxObject; // quick union implementation
    
    /**
     * Initializes a Percolation item with N x N sites.
     * @param N number of sites in each dimension
     * @throws java.lang.IllegalArgumentException if N <= 0
     */
    public Percolation(int N) {
        if (N <= 0) {
            throw new IllegalArgumentException("Site dimension " + N + " is not greater than 0.");
        }
        
        size = N;
        sites = new boolean[N][N];
        quickUnionObject = new WeightedQuickUnionUF((N * N) + 2);
        auxObject = new WeightedQuickUnionUF((N * N) + 2);
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                sites[i][j] = false;
            }
        }
    }
    
    /** 
     * validate if index (i, j) is within bounds.
     * @param i index of 1st dimension
     * @param j index of second dimension
     * @throws java.lang.IndexOutOfBoundsException unless 0 < i <= N and 0 < j <= N
     */
    private void validate(int i, int j) {
        if (i <= 0 || i > size) { // first index
            throw new IndexOutOfBoundsException("index " + i + " is not between 1 and " + size);
        }
        if (j <= 0 || j > size) { // second index
            throw new IndexOutOfBoundsException("index " + j + " is not between 1 and " + size);
        }
    }
    
    /**
     * Returns linear index of quick union array.
     * @param i index of 1st dimension
     * @param j index of second dimension
     * @return linear index on quick union array
     */
    private int qufIndex(int i, int j) {
        
        if (i == 0) {
            return 0;
        }
        if (i == size + 1) {
            return size*size + 1; 
        }

        return size * (i - 1) + j;
    }
    
    /**
     * Opens the site located at (i, j), 
     * and connects the site to all its open neighbors.
     * @param i index of 1st dimension
     * @param j index of second dimension
     * @throws java.lang.IndexOutOfBoundsException unless 0 < i <= N and 0 < j <= N
     */
    public void open(int i, int j) {
        validate(i, j);
        sites[i - 1][j - 1] = true;
        
        // Check all neighbors of site (i,j)
        // Start with left and right
        if (isOpen(i, Math.max(1, j - 1))) {
            quickUnionObject.union(qufIndex(i, Math.max(1, j - 1)), qufIndex(i, j));
            auxObject.union(qufIndex(i, Math.max(1, j - 1)), qufIndex(i, j));
        }
        if (isOpen(i, Math.min(size, j + 1))) {
            quickUnionObject.union(qufIndex(i, j), qufIndex(i, Math.min(size, j + 1)));
            auxObject.union(qufIndex(i, j), qufIndex(i, Math.min(size, j + 1)));
        }
        
        // For top and bottom, if point is at extremes, then connect with
        // virtual top or bottom site
        if (isOpen(Math.max(i - 1, 1), j)) {
            quickUnionObject.union(qufIndex(i - 1, j), qufIndex(i, j));
            auxObject.union(qufIndex(i - 1, j), qufIndex(i, j));
        }
        if (isOpen(Math.min(i + 1, size), j)) {
            quickUnionObject.union(qufIndex(i + 1, j), qufIndex(i, j));
            if (qufIndex(Math.min(i + 1, size), j) != size*size + 1) {
                auxObject.union(qufIndex(Math.min(i + 1, size), j), qufIndex(i, j));
            }
        }
    }
    
    /**
     * Checks if site (i, j) is open.
     * @param i index of 1st dimension
     * @param j index of second dimension
     * @return <tt>true</tt> is site (i, j) is open, and <tt>false</tt> otherwise
     * @throws java.lang.IndexOutOfBoundsException unless 0 < i <= N and 0 < j <= N
     */
    public boolean isOpen(int i, int j) {
        validate(i, j);
        return sites[i - 1][j - 1];
    }
    
    /**
     * Checks if site (i, j) is full.
     * @param i index of 1st dimension
     * @param j index of second dimension
     * @return <tt>true</tt> is site (i, j) is full, and <tt>false</tt> otherwise
     * @throws java.lang.IndexOutOfBoundsException unless 0 < i <= N and 0 < j <= N
     */
    public boolean isFull(int i, int j) {
        validate(i, j);
        return auxObject.connected(0, qufIndex(i, j)) && isOpen(i, j);
    }
    
    /**
     * Checks if system percolates by finding a connection between
     * the virtual top and bottom nodes.
     * @return <tt>true</tt> is system percolates, and <tt>false</tt> otherwise
     */
    public boolean percolates() {
        return quickUnionObject.connected(0, qufIndex(size, size) + 1);
    }
    
    /**
     * Main function for testing.
     * @param args 1 argument (N) for size of site array
     * @throws IOException 
     */
    public static void main(String[] args) throws IOException {
        int N = 10;
        
        final String PATH_NAME = "C:\\Users\\THABETAK\\Documents\\Personal\\IDrive-Sync"
                               + "\\Coursera - Algorithms, Part I\\Programming Assingments"
                               + "\\percolation-testing\\";
        
        final String FILE_NAME = "input8.txt";
        
        Scanner scanner = new Scanner(new File(PATH_NAME + FILE_NAME));
        N = scanner.nextInt();
        StdDraw.show(0);
        Percolation perc = new Percolation(N);
        PercolationVisualizer.draw(perc, N);
        StdDraw.show(0);
        while (scanner.hasNextInt())
        {
            perc.open(scanner.nextInt(), scanner.nextInt());
            PercolationVisualizer.draw(perc, N);
            StdDraw.show(0);
        }
        scanner.close();
    }
}