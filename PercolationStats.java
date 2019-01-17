/* *****************************************************************************
 *  Name:GAB
 *  Date:1.13.2019
 *  Description:This class runs Monte Carlo simulation on the Percolation Class
 **************************************************************************** */

import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

public class PercolationStats {

    private static final double CONFIDENCE_95 = 1.96;
    private final int times;
    private final double[] percolates;

    public PercolationStats(int n,
                            int trials)    // perform trials independent experiments on an n-by-n grid
    {
        if (n <= 0 || trials <= 0)
            throw new java.lang.IllegalArgumentException("parameters out of bounds");

        times = trials;
        percolates = new double[trials];
        int perccount = 0;
        int v, k;

        for (int i = 0; i < trials; i++) {
            Percolation once = new Percolation(n);

            while (!once.percolates()) {
                v = StdRandom.uniform(n) + 1;
                k = StdRandom.uniform(n) + 1;
                if (!once.isOpen(v, k)) {
                    perccount++;
                    once.open(v, k);
                }
            }
            percolates[i] = (double) perccount / (n * n);
        }

    }

    public double mean()                          // sample mean of percolation threshold
    {
        return StdStats.mean(percolates);
    }

    public double stddev()                        // sample standard deviation of percolation threshold
    {
        if (times == 1) return Double.NaN;
        return StdStats.stddev(percolates);
    }

    public double confidenceLo()                  // low  endpoint of 95% confidence interval
    {
        return mean() - CONFIDENCE_95 * stddev() / Math.sqrt(times);
    }

    public double confidenceHi()                  // high endpoint of 95% confidence interval
    {
        return mean() + CONFIDENCE_95 * stddev() / Math.sqrt(times);
    }

    public static void main(String[] args) {
        int nsize = Integer.parseInt(args[0]);
        int ttimes = Integer.parseInt(args[1]);
        PercolationStats stats = new PercolationStats(nsize, ttimes);
        StdOut.printf("mean = %f\n", stats.mean());
        StdOut.printf("stddev = %f\n", stats.stddev());
        StdOut.printf("95%% confidence interval = %f, %f\n", stats.confidenceLo(),
                      stats.confidenceHi());

    }
}
