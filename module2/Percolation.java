import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.WeightedQuickUnionUF;

/**
 * Models an n-by-n percolation system using the Weighted Quick Union with
 * two virtual sites (top and bottom) to efficiently check percolation.
 */
public class Percolation {
    private final int n;                    // grid dimension
    private final boolean[][] grid;        // open/blocked sites
    private final WeightedQuickUnionUF uf; // union-find structure
    private int openSites;                 // number of open sites

    // creates n-by-n grid, with all sites initially blocked
    public Percolation(int n) {
        if (n <= 0) throw new IllegalArgumentException("n must be > 0");
        this.n = n;
        grid = new boolean[n][n];
        // +2 for virtual top (0) and virtual bottom (n*n+1)
        uf = new WeightedQuickUnionUF(n * n + 2);
        openSites = 0;
    }

    // opens the site (row, col) if it is not open already
    public void open(int row, int col) {
        validate(row, col);
        if (!isOpen(row, col)) {
            grid[row - 1][col - 1] = true;
            openSites++;
            int index = xyTo1D(row, col);

            // connect to virtual top or bottom
            if (row == 1) uf.union(0, index);
            if (row == n) uf.union(n * n + 1, index);

            // connect to any open neighbors
            connectAdjacent(row, col, index);
        }
    }

    // is the site (row, col) open?
    public boolean isOpen(int row, int col) {
        validate(row, col);
        return grid[row - 1][col - 1];
    }

    // is the site (row, col) full? (connected to top)
    public boolean isFull(int row, int col) {
        validate(row, col);
        return uf.connected(0, xyTo1D(row, col));
    }

    // returns the number of open sites
    public int numberOfOpenSites() {
        return openSites;
    }

    // does the system percolate?
    public boolean percolates() {
        return uf.connected(0, n * n + 1);
    }

    // converts (row, col) to 1D union-find index
    private int xyTo1D(int row, int col) {
        return (row - 1) * n + col;
    }

    // connect an open site at (row, col) to any open neighbor
    private void connectAdjacent(int row, int col, int index) {
        // up
        if (row > 1 && isOpen(row - 1, col))
            uf.union(index, xyTo1D(row - 1, col));
        // down
        if (row < n && isOpen(row + 1, col))
            uf.union(index, xyTo1D(row + 1, col));
        // left
        if (col > 1 && isOpen(row, col - 1))
            uf.union(index, xyTo1D(row, col - 1));
        // right
        if (col < n && isOpen(row, col + 1))
            uf.union(index, xyTo1D(row, col + 1));
    }

    // validate that (row, col) is within bounds
    private void validate(int row, int col) {
        if (row < 1 || row > n || col < 1 || col > n)
            throw new IllegalArgumentException(
                "row or column index out of bounds: (" + row + ", " + col + ")");
    }

    // test client (optional)
    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("Usage: java Percolation <grid size>");
            return;
        }
        int n = Integer.parseInt(args[0]);
        Percolation percolation = new Percolation(n);
        while (!percolation.percolates()) {
            int row = StdRandom.uniform(1, n + 1);
            int col = StdRandom.uniform(1, n + 1);
            percolation.open(row, col);
        }
        System.out.println("Percolates after opening "
            + percolation.numberOfOpenSites() + " sites.");
    }
}