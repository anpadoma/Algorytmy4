/*************************************************************************
 *  Kompilacja:  javac AssignmentProblem.java
 *  Wykonanie:    java AssignmentProblem N
 *  Zale�no�ci: DijkstraSP.java DirectedEdge.java
 *
 *  Rozwi�zuje problem przypisania N na N w czasie N^3 log N za pomoc�
 *  algorytmu wykrywania najkr�tszej �cie�ki.
 *
 *  Uwaga: mo�na u�y� "g�stej" wersji algorytmu Dijsktry, aby uzyska�
 *  teoretycznie lepsz� wydajno�� r�wn� N^3, jednak w praktyce nie 
 *  przynosi to korzy�ci.
 *
 *  Zak�adamy, �e macierz koszt�w N na N jest nieujemna.
 *
 *
 *********************************************************************/

public class AssignmentProblem {
    private static final int UNMATCHED = -1;

    private int N;              // Liczba wierszy i kolumn.
    private double[][] weight;  // Macierz koszt�w N na N.
    private double[] px;        // px[i] = zmienna dla wiersza i.
    private double[] py;        // py[j] = zmienna dla kolumny j.
    private int[] xy;           // xy[i] = j oznacza, �e i-j pasuj� do siebie.
    private int[] yx;           // yx[j] = i oznacza, �e i-j pasuj� do siebie.

 
    public AssignmentProblem(double[][] weight) {
        N = weight.length;
        this.weight = new double[N][N];
        for (int i = 0; i < N; i++)
            for (int j = 0; j < N; j++)
                this.weight[i][j] = weight[i][j];

        // Zmienne.
        px = new double[N];
        py = new double[N];

        // Pocz�tkowo nie ma dopasowa�
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

    // Znajdowanie najkr�tszej �cie�ki powi�kszaj�cej i aktualizowanie danych
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

        // Wyznaczanie najkr�tszej �cie�ki z s do ka�dego innego wierzcho�ka
        DijkstraSP spt = new DijkstraSP(G, s);

        // Powi�kszanie wzd�u� kolejnych �cie�ek
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

    // ��czna waga idealnego dopasowania o minimalnej wadzie	
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

    // Sprawdzanie, czy zmienne s� dopuszczalnym rozwi�zaniem zadania dualnego
    private boolean isDualFeasible() {
        // Sprawdzanie, czy zmniejszenie kosztu we wszystkich kraw�dziach jest >= 0
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                if (reduced(i, j) < 0) {
                    System.out.println("Zmienne nie s� dopuszczalnym rozwi�zaniem zadania dualnego");
                    return false;
                }
            }
        }
        return true;
    }

    // Sprawdzanie, czy zmienne pierwotne i dualne s� uzupe�niaj�ce
    private boolean isComplementarySlack() {
        
        for (int i = 0; i < N; i++) {
            if ((xy[i] != UNMATCHED) && (reduced(i, xy[i]) != 0)) {
                System.out.println("Zmienne pierwotne i dualne nie s� uzupe�niaj�ce");
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

        // Sprawdzanie, czy xy[] i yx[] to odwrotno�ci.
        for (int j = 0; j < N; j++) {
            if (xy[yx[j]] != j) {
                System.out.println("xy[] i yx[] nie s� odwrotno�ciami");
                return false;
            }
        }
        for (int i = 0; i < N; i++) {
            if (yx[xy[i]] != i) {
                System.out.println("xy[] i yx[] nie s� odwrotno�ciami");
                return false;
            }
        }

        return true;
    }


    // Sprawdzanie warunk�w optymalno�ci.
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
