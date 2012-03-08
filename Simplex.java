/*************************************************************************
 *  Kompilacja:  javac Simplex.java
 *  Wykonanie:    java Simplex
 *
 *  Na podstawie macierzy A o wymiarach M na N, wektora b o d³ugoœci M i
 *  wektora c o d³ugoœci N rozwi¹zuje problem z obszaru programowania liniowego { max cx : Ax <= b, x >= 0 }.
 *  Zak³adamy, ¿e b >= 0, tak wiêc x = 0 jest podstawowym mo¿liwym rozwi¹zaniem.
 *
 *  Tworzy tablicê sympleksow¹ (M+1) na (N+M+1) z 
 *  praw¹ stron¹ równania w kolumnie M+N, funkcj¹ celu w wierszu M i
 *  zmiennymi pomocniczymi w kolumnach od M do M+N-1.
 *
 *************************************************************************/

public class Simplex {
    private static final double EPSILON = 1.0E-10;
    private double[][] a;   // Tablica
    private int M;          // Liczba ograniczeñ
    private int N;          // Liczba pierwotnych zmiennych

    private int[] basis;    // basis[i] = zmienna podstawowa odpowiadaj¹ca wierszowi i.
                            // Potrzebna tylko do wyœwietlenia rozwi¹zania (nie w ksi¹¿ce)

    // Tworzenie prostej tablicy sympleksowej
    public Simplex(double[][] A, double[] b, double[] c) {
        M = b.length;
        N = c.length;
        a = new double[M+1][N+M+1];
        for (int i = 0; i < M; i++)
            for (int j = 0; j < N; j++)
                a[i][j] = A[i][j];
        for (int i = 0; i < M; i++) a[i][N+i] = 1.0;
        for (int j = 0; j < N; j++) a[M][j]   = c[j];
        for (int i = 0; i < M; i++) a[i][M+N] = b[i];

        basis = new int[M];
        for (int i = 0; i < M; i++) basis[i] = N + i;

        solve();

        // Sprawdzanie warunków optymalnoœci
        assert check(A, b, c);
    }

    // Uruchamianie algorytmu sympleksowego pocz¹wszy od wyjœciowego mo¿liwego rozwi¹zania
    private void solve() {
        while (true) {

            // Znajdowanie kolumny wejœciowej q
            int q = bland();
            if (q == -1) break;  // Optymalne

            // Znajdowanie wiersza wyjœciowego p
            int p = minRatioRule(q);
            if (p == -1) throw new RuntimeException("Nie istnieje skonczone rozwiazanie");

            // Przestawianie
            pivot(p, q);

            // Aktualizowanie podstawowej zmiennej
            basis[p] = q;
        }
    }

    // Najni¿szy indeks niepodstawowej kolumny o dodatnim koszcie
    private int bland() {
        for (int j = 0; j < M + N; j++)
            if (a[M][j] > 0) return j;
        return -1;  // Optymalne
    }

   // Indeks niepodstawowej kolumny o najbardziej dodatnim koszcie
    private int dantzig() {
        int q = 0;
        for (int j = 1; j < M + N; j++)
            if (a[M][j] > a[M][q]) q = j;

        if (a[M][q] <= 0) return -1;  // Optymalne
        else return q;
    }

    // Znajdowanie wiersza p za pomoc¹ regu³y minimalnego wspó³czynnika (zwracanie -1, jeœli taki wiersz nie istnieje)
    private int minRatioRule(int q) {
        int p = -1;
        for (int i = 0; i < M; i++) {
            if (a[i][q] <= 0) continue;
            else if (p == -1) p = i;
            else if ((a[i][M+N] / a[i][q]) < (a[p][M+N] / a[p][q])) p = i;
        }
        return p;
    }

    // Przestawianie (p, q) na podstawie eliminacji Gaussa-Jordana
    private void pivot(int p, int q) {

        // Wszystko oprócz wiersza p i kolumny q
        for (int i = 0; i <= M; i++)
            for (int j = 0; j <= M + N; j++)
                if (i != p && j != q) a[i][j] -= a[p][j] * a[i][q] / a[p][q];

        // Zerowanie kolumny q
        for (int i = 0; i <= M; i++)
            if (i != p) a[i][q] = 0.0;

        // Skalowanie wiersza p
        for (int j = 0; j <= M + N; j++)
            if (j != q) a[p][j] /= a[p][q];
        a[p][q] = 1.0;
    }

    // Zwracanie optymalnej wartoœci funkcji celu
    public double value() {
        return -a[M][M+N];
    }

    // Zwracanie wektora rozwi¹zania prymalnego
    public double[] primal() {
        double[] x = new double[N];
        for (int i = 0; i < M; i++)
            if (basis[i] < N) x[basis[i]] = a[i][M+N];
        return x;
    }

    // Zwracanie wektora rozwi¹zania dualnego
    public double[] dual() {
        double[] y = new double[M];
        for (int i = 0; i < M; i++)
            y[i] = -a[M][N+i];
        return y;
    }


    // Czy rozwi¹zanie prymalne jest mo¿liwe?
    private boolean isPrimalFeasible(double[][] A, double[] b) {
       double[] x = primal();

       // Sprawdzanie, czy x >= 0
       for (int j = 0; j < x.length; j++) {
           if (x[j] < 0.0) {
               StdOut.println("x[" + j + "] = " + x[j] + " jest ujemna");
               return false;
           }
       }

       // Sprawdzanie, czy Ax <= b
       for (int i = 0; i < M; i++) {
           double sum = 0.0;
           for (int j = 0; j < N; j++) {
                sum += A[i][j] * x[j];
           }
           if (sum > b[i] + EPSILON) {
               StdOut.println("Rozwiazanie prymalne niemozliwe");
               StdOut.println("b[" + i + "] = " + b[i] + ", sum = " + sum);
               return false;
           }
       }
       return true;
    }

    // Czy rozwi¹zanie dualne jest mo¿liwe?
    private boolean isDualFeasible(double[][] A, double[] c) {
       double[] y = dual();

       // Sprawdzanie, czy y >= 0
       for (int i = 0; i < y.length; i++) {
           if (y[i] < 0.0) {
               StdOut.println("y[" + i + "] = " + y[i] + " jest ujemna");
               return false;
           }
       }

       // Sprawdzanie, czy yA >= c
       for (int j = 0; j < N; j++) {
           double sum = 0.0;
           for (int i = 0; i < M; i++) {
                sum += A[i][j] * y[i];
           }
           if (sum < c[j] - EPSILON) {
               StdOut.println("Rozwiazanie dualne niemozliwe");
               StdOut.println("c[" + j + "] = " + c[j] + ", sum = " + sum);
               return false;
           }
       }
       return true;
    }

    // Sprawdzanie, czy wartoœæ optymalna = cx = yb
    private boolean isOptimal(double[] b, double[] c) {
       double[] x = primal();
       double[] y = dual();
       double value = value();

       // Sprawdzanie, czy wartoœæ = cx = yb
       double value1 = 0.0;
       for (int j = 0; j < x.length; j++)
           value1 += c[j] * x[j];
       double value2 = 0.0;
       for (int i = 0; i < y.length; i++)
           value2 += y[i] * b[i];
       if (Math.abs(value - value1) > EPSILON || Math.abs(value - value2) > EPSILON) {
           StdOut.println("value = " + value + ", cx = " + value1 + ", yb = " + value2);
           return false;
       }

       return true;
    }

    private boolean check(double[][]A, double[] b, double[] c) {
       return isPrimalFeasible(A, b) && isDualFeasible(A, c) && isOptimal(b, c);
    }

    // Wyœwietlanie tablicy
    public void show() {
        System.out.println("M = " + M);
        System.out.println("N = " + N);
        for (int i = 0; i <= M; i++) {
            for (int j = 0; j <= M + N; j++) {
                System.out.printf("%7.2f ", a[i][j]);
            }
            System.out.println();
        }
        System.out.println("value = " + value());
        for (int i = 0; i < M; i++)
            if (basis[i] < N) System.out.println("x_" + basis[i] + " = " + a[i][M+N]);
        System.out.println();
    }

    public static void test(double[][] A, double[] b, double[] c) {
        Simplex lp = new Simplex(A, b, c);
        System.out.println("value = " + lp.value());
        double[] x = lp.primal();
        for (int i = 0; i < x.length; i++)
            System.out.println("x[" + i + "] = " + x[i]);
        double[] y = lp.dual();
        for (int j = 0; j < y.length; j++)
            System.out.println("y[" + j + "] = " + y[j]);
    }

    public static void test1() {
        double[][] A = {
            { -1,  1,  0 },
            {  1,  4,  0 },
            {  2,  1,  0 },
            {  3, -4,  0 },
            {  0,  0,  1 },
        };
        double[] c = { 1, 1, 1 };
        double[] b = { 5, 45, 27, 24, 4 };
        test(A, b, c);
    }


    // x0 = 12, x1 = 28, opt = 800
    public static void test2() {
        double[] c = {  13.0,  23.0 };
        double[] b = { 480.0, 160.0, 1190.0 };
        double[][] A = {
            {  5.0, 15.0 },
            {  4.0,  4.0 },
            { 35.0, 20.0 },
        };
        test(A, b, c);
    }

    // Bez ograniczeñ
    public static void test3() {
        double[] c = { 2.0, 3.0, -1.0, -12.0 };
        double[] b = {  3.0,   2.0 };
        double[][] A = {
            { -2.0, -9.0,  1.0,  9.0 },
            {  1.0,  1.0, -1.0, -2.0 },
        };
        test(A, b, c);
    }

    // Wersja "zdegenerowana" - przy wyborze najbardziej 
	// dodatniego wspó³czynnika funkcji celu wystêpuj¹ cykle
    public static void test4() {
        double[] c = { 10.0, -57.0, -9.0, -24.0 };
        double[] b = {  0.0,   0.0,  1.0 };
        double[][] A = {
            { 0.5, -5.5, -2.5, 9.0 },
            { 0.5, -1.5, -0.5, 1.0 },
            { 1.0,  0.0,  0.0, 0.0 },
        };
        test(A, b, c);
    }

    // Klient testowy
    public static void main(String[] args) {

        try                { test1();             }
        catch(Exception e) { e.printStackTrace(); }
        StdOut.println("--------------------------------");

        try                { test2();             }
        catch(Exception e) { e.printStackTrace(); }
        StdOut.println("--------------------------------");

        try                { test3();             }
        catch(Exception e) { e.printStackTrace(); }
        StdOut.println("--------------------------------");

        try                { test4();             }
        catch(Exception e) { e.printStackTrace(); }
        StdOut.println("--------------------------------");


        int M = Integer.parseInt(args[0]);
        int N = Integer.parseInt(args[1]);
        double[] c = new double[N];
        double[] b = new double[M];
        double[][] A = new double[M][N];
        for (int j = 0; j < N; j++)
            c[j] = StdRandom.uniform(1000);
        for (int i = 0; i < M; i++)
            b[i] = StdRandom.uniform(1000);
        for (int i = 0; i < M; i++)
            for (int j = 0; j < N; j++)
                A[i][j] = StdRandom.uniform(100);
        Simplex lp = new Simplex(A, b, c);
        StdOut.println(lp.value());
    }
}
