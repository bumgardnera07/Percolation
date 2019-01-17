/* *****************************************************************************
 *  Name:GAB
 *  Date: 1/13/19
 *  Description: This program will take a series of inputs opening up sites on
 *  matrix and tell us if the remaining system 'percolates' i.e. there is a
 *  connected path from the top row of the matrix to the bottom.
 *
 **************************************************************************** */

import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {
    private boolean[][] network;
    private final int size;
    private final WeightedQuickUnionUF connsites;
    private final WeightedQuickUnionUF noBackWash;
    private final int top;
    private final int bot;
    private int opencells;

    public Percolation(int n) {
        if (n <= 0) throw new java.lang.IllegalArgumentException("size must be greater than 0");

        network = new boolean[n][n];
        for (int i = 0; i < n; i++) {
            for (int k = 0; k < n; k++) {
                network[i][k] = false;
            }
        }
        connsites = new WeightedQuickUnionUF((n * n) + 2);
        noBackWash = new WeightedQuickUnionUF(n * n + 1);
        top = n * n;
        bot = n * n + 1;
        size = n;
        opencells = 0;
    }

    public void open(int row, int col) {
        int currentCell = xyTo1D(row, col);
        validIndices(row, col);
        if (!isOpen(row, col)) {
            opencells++;
            network[row - 1][col - 1] = true;
            if (row == 1) {
                connsites.union(currentCell, top);
                noBackWash.union(currentCell, top);
            }
            if (row == size)
                connsites.union(currentCell, bot);

            // union with above
            if (row != 1) {
                if (network[row - 2][col - 1]) {
                    connsites.union(currentCell, currentCell - size);
                    noBackWash.union(currentCell, currentCell - size);
                }
            }
            // below
            if (row != size) {
                if (network[row][col - 1]) {
                    connsites.union(currentCell, currentCell + size);
                    noBackWash.union(currentCell, currentCell + size);
                }
            }
            // left
            if (col != 1) {
                if (network[row - 1][col - 2]) {
                    connsites.union(currentCell, currentCell - 1);
                    noBackWash.union(currentCell, currentCell - 1);
                }
            }
            // right
            if (col != size) {
                if (network[row - 1][col]) {
                    connsites.union(currentCell, currentCell + 1);
                    noBackWash.union(currentCell, currentCell + 1);
                }
            }
        }

    }

    public boolean isOpen(int row, int col) {
        validIndices(row, col);
        if (network[row - 1][col - 1])
            return true;
        return false;
    }

    public boolean isFull(int row, int col) {
        validIndices(row, col);
        return (noBackWash.connected(top, xyTo1D(row, col)) && isOpen(row, col));
    }

    public int numberOfOpenSites() {
        return opencells;
    }

    public boolean percolates() {
        return connsites.connected(top, bot);
    }

    private void validIndices(int row, int col) {
        if (row <= 0 || row > size || col <= 0 || col > size)
            throw new java.lang.IllegalArgumentException("row or column out of bounds");
    }

    private int xyTo1D(int row, int col) {
        return ((row - 1) * size) + col - 1;
    }

}
