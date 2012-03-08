/*************************************************************************
 *  Kompilacja:  javac GaussianElimination.java
 *  Wykonanie:    java GaussianElimination
 * 
 *  Eliminacja Gaussa ze zmian¹ kolejnoœci wierszy.
 *
 *  % java GaussianElimination
 *  -1.0
 *  2.0
 *  2.0
 *
 *************************************************************************/

public class GaussianElimination {
    private static final double EPSILON = 1e-10;

    // Eliminacja Gaussa ze zmian¹ kolejnoœci wierszy
    public static double[] lsolve(double[][] A, double[] b) {
        int N  = b.length;

        for (int p = 0; p < N; p++) {

            // Znajdowanie wiersza podstawowego i zmiana kolejnoœci
            int max = p;
            for (int i = p + 1; i < N; i++) {
                if (Math.abs(A[i][p]) > Math.abs(A[max][p])) {
                    max = i;
                }
            }
            double[] temp = A[p]; A[p] = A[max]; A[max] = temp;
            double   t    = b[p]; b[p] = b[max]; b[max] = t;

            // Osobliwa lub prawie osobliwa
            if (Math.abs(A[p][p]) <= EPSILON) {
                throw new RuntimeException("Macierz jest osobliwa lub prawie osobliwa");
            }

            // Wybieranie elementu podstawowego na podstawie A i b
            for (int i = p + 1; i < N; i++) {
                double alpha = A[i][p] / A[p][p];
                b[i] -= alpha * b[p];
                for (int j = p; j < N; j++) {
                    A[i][j] -= alpha * A[p][j];
                }
            }
        }

        // Podstawianie wstecz
        double[] x = new double[N];
        for (int i = N - 1; i >= 0; i--) {
            double sum = 0.0;
            for (int j = i + 1; j < N; j++) {
                sum += A[i][j] * x[j];
            }
            x[i] = (b[i] - sum) / A[i][i];
        }
        return x;
    }


    // Przyk³adowy klient
    public static void main(String[] args) {
        int N = 3;
        double[][] A = { { 0, 1,  1 },
                         { 2, 4, -2 },
                         { 0, 3, 15 }
                       };
        double[] b = { 4, 2, 36 };
        double[] x = lsolve(A, b);


        // Wyœwietlanie wyników
        for (int i = 0; i < N; i++) {
            System.out.println(x[i]);
        }

    }

}
