/*************************************************************************
 *  Kompilacja:  javac AssignmentProblem.java
 *  Wykonanie:    java AssignmentProblem N
 *  Zale¿noœci: DijkstraSP.java DirectedEdge.java
 *
 *  Rozwi¹zuje problem przypisania N na N w czasie N^3 log N za pomoc¹
 *  algorytmu wykrywania najkrótszej œcie¿ki.
 *
 *  Uwaga: mo¿na u¿yæ "gêstej" wersji algorytmu Dijsktry, aby uzyskaæ
 *  teoretycznie lepsz¹ wydajnoœæ równ¹ N^3, jednak w praktyce nie 
 *  przynosi to korzyœci.
 *
 *  Zak³adamy, ¿e macierz kosztów N na N jest nieujemna.
 *
 *
 *********************************************************************/

public class AssignmentProblem {
    private static final int UNMATCHED = -1;

    private int N;              // Liczba wierszy i kolumn.
    private double[][] weight;  // Macierz kosztów N na N.
    private double[] px;        // px[i] = zmienna dla wiersza i.
    private double[] py;        // py[j] = zmienna dla kolumny j.
    private int[] xy;           // xy[i] = j oznacza, ¿e i-j pasuj¹ do siebie.
    private int[] yx;           // yx[j] = i oznacza, ¿e i-j pasuj¹ do siebie.

 
    public AssignmentProblem(double[][] weight) {
        N = weight.length;
        this.weight = new double[N][N];
        for (int i = 0; i < N; i++)
            for (int j = 0; j < N; j++)
                this.weight[i][j] = weight[i][j];

        // Zmienne.
        px = new double[N];
        py = new double[N];

        // Pocz¹tkowo nie ma dopasowañ
        xy = new int[N];
        yx = new int[N];
        for (int i = 0; i < N; i++) xy[i] = UNMATCHED;
        for (int j = 0; j < N; j++) yx[j] = UNMATCHED;

        for (int k = 0; k < N; k++) {
            System.out.println(k);
            assert isDualFeasible();
            assert isComplementarySlack();
            augment();
        }
        assert check();
    }

    // Znajdowanie najkrótszej œcie¿ki powiêkszaj¹cej i aktualizowanie danych
    private void augment() {

        // Tworzenie grafu rezydualnego
        EdgeWeightedDigraph G = new EdgeWeightedDigraph(2*N+2);
        int s = 2*N, t = 2*N+1;
        for (int i = 0; i < N; i++) {
            if (xy[i] == UNMATCHED) G.addEdge(new DirectedEdge(s, i, 0.0));
        }
        for (int j = 0; j < N; j++) {
            if (yx[j] == UNMATCHED) G.addEdge(new DirectedEdge(N+j, t, py[j]));
        }
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                if (xy[i] == j) G.addEdge(new DirectedEdge(N+j, i, 0.0));
                else            G.addEdge(new DirectedEdge(i, N+j, reduced(i, j)));
            }
        }

        // Wyznaczanie najkrótszej œcie¿ki z s do ka¿dego innego wierzcho³ka
        DijkstraSP spt = new DijkstraSP(G, s);

        // Powiêkszanie wzd³u¿ kolejnych œcie¿ek
        for (DirectedEdge e : spt.pathTo(t)) {
            int i = e.from(), j = e.to() - N;
            if (i < N) {
                xy[i] = j;
                yx[j] = i;
            }
        }

        // Aktualizowanie zmiennych dualnych
        for (int i = 0; i < N; i++) px[i] += spt.distTo(i);
        for (int j = 0; j < N; j++) py[j] += spt.distTo(N+j);
    }

    // Zmniejszony koszt dla i-j
    private double reduced(int i, int j) {
        return weight[i][j] + px[i] - py[j];
    }

    // £¹czna waga idealnego dopasowania o minimalnej wadzie	
    public double weight() {
        double total = 0.0;
        for (int i = 0; i < N; i++) {
            if (xy[i] != UNMATCHED)
                total += weight[i][xy[i]];
        }
        return total;
    }

    public int sol(int i) {
        return xy[i];
    }

    // Sprawdzanie, czy zmienne s¹ dopuszczalnym rozwi¹zaniem zadania dualnego
    private boolean isDualFeasible() {
        // Sprawdzanie, czy zmniejszenie kosztu we wszystkich krawêdziach jest >= 0
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                if (reduced(i, j) < 0) {
                    System.out.println("Zmienne nie s¹ dopuszczalnym rozwi¹zaniem zadania dualnego");
                    return false;
                }
            }
        }
        return true;
    }

    // Sprawdzanie, czy zmienne pierwotne i dualne s¹ uzupe³niaj¹ce
    private boolean isComplementarySlack() {
        
        for (int i = 0; i < N; i++) {
            if ((xy[i] != UNMATCHED) && (reduced(i, xy[i]) != 0)) {
                System.out.println("Zmienne pierwotne i dualne nie s¹ uzupe³niaj¹ce");
                return false;
            }
        }
        return true;
    }

    // Sprawdzanie, czy dla zmiennych podstawowych dopasowanie jest idealne
    private boolean isPerfectMatching() {

        // Sprawdzanie, czy xy[] jest idealnym dopasowaniem.
        boolean[] perm = new boolean[N];
        for (int i = 0; i < N; i++) {
            if (perm[xy[i]]) {
                System.out.println("Nie jest to idealne dopasowanie");
                return false;
            }
            perm[xy[i]] = true;
        }

        // Sprawdzanie, czy xy[] i yx[] to odwrotnoœci.
        for (int j = 0; j < N; j++) {
            if (xy[yx[j]] != j) {
                System.out.println("xy[] i yx[] nie s¹ odwrotnoœciami");
                return false;
            }
        }
        for (int i = 0; i < N; i++) {
            if (yx[xy[i]] != i) {
                System.out.println("xy[] i yx[] nie s¹ odwrotnoœciami");
                return false;
            }
        }

        return true;
    }


    // Sprawdzanie warunków optymalnoœci.
    private boolean check() {
        return isPerfectMatching() && isDualFeasible() && isComplementarySlack();
    }

    public static void main(String[] args) {

        int N = Integer.parseInt(args[0]);
        double[][] weight = new double[N][N];
        for (int i = 0; i < N; i++)
            for (int j = 0; j < N; j++)
                weight[i][j] = Math.random();

        AssignmentProblem assignment = new AssignmentProblem(weight);
        StdOut.println("waga = " + assignment.weight());
        for (int i = 0; i < N; i++)
            StdOut.println(i + "-" + assignment.sol(i));
    }

}
