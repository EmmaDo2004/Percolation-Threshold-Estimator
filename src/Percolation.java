import edu.princeton.cs.algs4.WeightedQuickUnionUF;


public class Percolation {
    private final boolean[][] grid;
    private int openSite;
    private final WeightedQuickUnionUF noBackwashUF;
    private final WeightedQuickUnionUF unionFind;
    private final int size;
    private final int virtualTop;
    private final int virtualBottom;


    public Percolation(int N) {
        if (N <= 0) {
            throw new IllegalArgumentException();
        }
        grid = new boolean[N][N];
        size = N;
        unionFind = new WeightedQuickUnionUF(N * N + 2);
        noBackwashUF = new WeightedQuickUnionUF(N * N + 1);
        virtualTop = N * N;
        virtualBottom = N * N + 1;
        openSite = 0;
    }

    public void open(int row, int col) {
        if (row < 0 || row >= size || col < 0 || col >= size) {
            throw new IndexOutOfBoundsException();
        }
        if (!grid[row][col]) {
            grid[row][col] = true;
            openSite++;
            int a = xyTo1D(row, col);

            if (row == 0) {
                unionFind.union(a, virtualTop);
                noBackwashUF.union(a, virtualTop);
            }

            if (row == size - 1) {
                unionFind.union(a, virtualBottom);
            }
            //helper function to create connection between blocks
            // Top neighbor
            connectIfOpen(row - 1, col, a);
            // Bottom neighbor
            connectIfOpen(row + 1, col, a);
            // Left neighbor
            connectIfOpen(row, col - 1, a);
            // Right neighbor
            connectIfOpen(row, col + 1, a);
        }

    }

    public boolean isOpen(int row, int col) {
        if (col >= 0 && row >= 0 && col < size && row < size) {
            return grid[row][col];
        }
        return false;
    }

    public boolean isFull(int row, int col) {
        if (row < 0 || row >= size || col < 0 || col >= size) {
            throw new IndexOutOfBoundsException();
        }
        if (!isOpen(row, col)) {
            return false;
        }
        int a = xyTo1D(row, col);
        return noBackwashUF.connected(a, virtualTop) && isOpen(row, col);
    }

    public int numberOfOpenSites() {
        return openSite;
    }

    public boolean percolates() {
        // use boolean to check if the top and botton are connected in the set
        return unionFind.connected(virtualTop, virtualBottom);
    }

    public int xyTo1D(int row, int col) {
        //row*size + column
        return row * size + col;
    }

    //create a helper method to check if the current connect with prev
    private void connectIfOpen(int row, int col, int a) {
        if (isOpen(row, col) && row >= 0 && row < size && col >= 0 && col < size) {
            int b = xyTo1D(row, col);
            unionFind.union(a, b);
            noBackwashUF.union(a, b);
        }
    }

}
