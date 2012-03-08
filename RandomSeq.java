/*************************************************************************
 *  Kompilacja:  javac RandomSeq.java
 *  Wykonanie:    java RandomSeq N lo hi
 *
 *  Wy�wietla N liczb z przedzia�u od lo do hi.
 *
 *  % java RandomSeq 5 100.0 200.0
 *  123.43
 *  153.13
 *  144.38
 *  155.18
 *  104.02
 *
 *************************************************************************/

public class RandomSeq { 
    public static void main(String[] args) {

        // Argumenty wiersza polece�
        int N = Integer.parseInt(args[0]);
        double lo = Double.parseDouble(args[1]);
        double hi = Double.parseDouble(args[2]);

        // Generuje i wy�wietla N liczb z przedzia�u od lo do hi
        for (int i = 0; i < N; i++) {
            double x = StdRandom.uniform(lo, hi);
            StdOut.printf("%.2f\n", x);
        }
    }
}
