/*************************************************************************
 *  Kompilacja:  javac LazyPrimMST.java
 *  Wykonanie:    java LazyPrimMST V E
 *  Zale�no�ci: EdgeWeightedGraph.java Edge.java Queue.java MinPQ.java
 *                UF.java
 *
 *  Algorytm Prima wyznaczaj�cy minimalny las rozpinaj�cy
 *
 *************************************************************************/

public class LazyPrimMST {
    private double weight;       // ��czna waga drzewa MST
    private Queue<Edge> mst;     // Kraw�dzie w drzewie MST
    private boolean[] marked;    // marked[v] = true, je�li v znajduje si� w drzewie
    private MinPQ<Edge> pq;      // Kraw�dzie, kt�rych jeden punkt ko�cowy znajduje si� w drzewie

    // Wyznacza minimalny las rozpinaj�cy dla drzewa G
    public LazyPrimMST(EdgeWeightedGraph G) {
        mst = new Queue<Edge>();
        pq = new MinPQ<Edge>();
        marked = new boolean[G.V()];
        for (int v = 0; v < G.V(); v++)     // Uruchamianie algorytmu Prima dla wszystkich wierzcho�k�w
            if (!marked[v]) prim(G, v);     // w celu uzyskania minimalnego lasu rozpinaj�cego

        // Sprawdzanie warunk�w optymalno�ci
        assert check(G);
    }

    // Uruchamianie algorytmu Prima
    private void prim(EdgeWeightedGraph G, int s) {
        scan(G, s);
        while (!pq.isEmpty()) {                        // Zatrzymywanie si�, kiedy drzewo MST obejmuje V-1 kraw�dzi
            Edge e = pq.delMin();                      // Najmniejsza kraw�d� w kolejce pq
            int v = e.either(), w = e.other(v);        // Dwa punkty ko�cowe
            assert marked[v] || marked[w];
            if (marked[v] && marked[w]) continue;      // Wersja leniwa (v i w s� ju� oznaczone)
            mst.enqueue(e);                            // Dodawanie e do drzewa MST
            weight += e.weight();
            if (!marked[v]) scan(G, v);               // v staje si� cz�ci� drzewa
            if (!marked[w]) scan(G, w);               // w staje si� cz�ci� drzewa
        }
    }

    // Dodawanie wszystkich kraw�dzi e incydentnych z v do kolejki pq, je�li drugi punkt ko�cowy
    // nie jest jeszcze oznaczony
    private void scan(EdgeWeightedGraph G, int v) {
        assert !marked[v];
        marked[v] = true;
        for (Edge e : G.adj(v))
            if (!marked[e.other(v)]) pq.insert(e);
    }
        
    // Zwracanie kraw�dzi drzewa MST jako obiektu Iterable
    public Iterable<Edge> edges() {
        return mst;
    }

    // Zwracanie wagi drzewa MST
    public double weight() {
        return weight;
    }

    // Sprawdzanie warunk�w optymalno�ci (zajmuje czas proporcjonalny do E V lg* V)
    private boolean check(EdgeWeightedGraph G) {

        // Sprawdzanie wagi
        double weight = 0.0;
        for (Edge e : edges()) {
            weight += e.weight();
        }
        double EPSILON = 1E-12;
        if (Math.abs(weight - weight()) > EPSILON) {
            System.err.printf("Waga krawedzi nie jest rowna wartosci metody weight(): %f i %f\n", weight, weight());
            return false;
        }

        // Sprawdzanie, czy graf jest acykliczny
        UF uf = new UF(G.V());
        for (Edge e : edges()) {
            int v = e.either(), w = e.other(v);
            if (uf.connected(v, w)) {
                System.err.println("Nie jest to las");
                return false;
            }
            uf.union(v, w);
        }

        // Sprawdzanie, czy las jest rozpinaj�cy
        for (Edge e : edges()) {
            int v = e.either(), w = e.other(v);
            if (!uf.connected(v, w)) {
                System.err.println("Nie jest to las rozpinajacy");
                return false;
            }
        }

        // Sprawdzanie, czy jest to minimalny las rozpinaj�cy (warunki optymalno�ci przekroju)
        for (Edge e : edges()) {
            int v = e.either(), w = e.other(v);

            // Dla wszystkich kraw�dzi drzewa MST opr�cz e
            uf = new UF(G.V());
            for (Edge f : mst) {
                int x = f.either(), y = f.other(x);
                if (f != e) uf.union(x, y);
            }

            // Sprawdzanie, czy e to kraw�d� o wadze minimalnej w przekroju
            for (Edge f : G.edges()) {
                int x = f.either(), y = f.other(x);
                if (!uf.connected(x, y)) {
                    if (f.weight() < e.weight()) {
                        System.err.println("Krawedz " + f + " narusza warunki optymalnosci przekroju");
                        return false;
                    }
                }
            }

        }

        return true;
    }
    
    
    public static void main(String[] args) {
        EdgeWeightedGraph G;

        if (args.length == 0) {
            // Wczytywanie grafu ze standardowego wej�cia
            G = new EdgeWeightedGraph(new In());
        }

        else if (args.length == 1) {
            // Wczytywanie grafu z pliku
            G = new EdgeWeightedGraph(new In(args[0]));
        }

        else {
            // Losowy graf o V wierzcho�kach i E kraw�dziach
            int V = Integer.parseInt(args[0]);
            int E = Integer.parseInt(args[1]);
            G = new EdgeWeightedGraph(V, E);
        }
        

        // Wy�wietlanie grafu
        if (G.V() <= 10) StdOut.println(G);

        // Wyznaczanie i wy�wietlanie drzewa MST
        LazyPrimMST mst = new LazyPrimMST(G);
        StdOut.println("Laczny koszt = " + mst.weight());
        for (Edge e : mst.edges())
            StdOut.println(e);
    }

}
