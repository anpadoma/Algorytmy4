/*************************************************************************
 * Kompilacja:  javac KruskalMST.java
 * Wykonanie:  java KruskalMST V E
 * Zale�no�ci: EdgeWeightedGraph.java Edge.java Queue.java UF.java
 *
 * Algorytm Kruskala do wyznaczania minimalnego lasu rozpinaj�cego.
 *
 * % java KruskalMST < graph8.txt
 *
 *************************************************************************/

public class KruskalMST {
    private double weight;  // Waga drzewa MST
    private Queue<Edge> mst = new Queue<Edge>();  // Kraw�dzie w drzewie MST

    // Algorytm Kruskala
    public KruskalMST(EdgeWeightedGraph G) {
        // Wydajniejsze ni� budowanie kopca przez przekazanie tablicy kraw�dzi
        MinPQ<Edge> pq = new MinPQ<Edge>();
        for (Edge e : G.edges()) {
            pq.insert(e);
        }

        // Uruchamianie algorytmu zach�annego
        UF uf = new UF(G.V());
        while (!pq.isEmpty() && mst.size() < G.V() - 1) {
            Edge e = pq.delMin();
            int v = e.either();
            int w = e.other(v);
            if (!uf.connected(v, w)) { // v-w nie tworzy cyklu
                uf.union(v, w);  // Scalanie sk�adowych v i w
                mst.enqueue(e);  // Dodawanie kraw�dzi e do drzewa MST
                weight += e.weight();
            }
        }

        // Sprawdzanie warunk�w optymalno�ci
        assert check(G);
    }

    // Kraw�dzie w minimalnym lasie rozpinaj�cym jako obiekt Iterable
    public Iterable<Edge> edges() {
        return mst;
    }

    // Waga minimalnego lasu rozpinaj�cego
    public double weight() {
        return weight;
    }
    
    // Sprawdzanie warunk�w optymalno�ci (dzia�a w czasie proporcjonalnym do E V lg* V)
    private boolean check(EdgeWeightedGraph G) {

        // Sprawdzanie ��cznej wagi
        double total = 0.0;
        for (Edge e : edges()) {
            total += e.weight();
        }
        double EPSILON = 1E-12;
        if (Math.abs(total - weight()) > EPSILON) {
            System.err.printf("Waga krawedzi nie jest rowna wartosci metody weight(): %f i %f\n", total, weight());
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

        // Sprawdzanie, czy jest to minimalny lar rozpinaj�cy (warunki optymalno�ci przekroju)
        for (Edge e : edges()) {
            int v = e.either(), w = e.other(v);
            
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
        else {
            // Graf losowy o V wierzcho�kach i E kraw�dziach
            int V = Integer.parseInt(args[0]);
            int E = Integer.parseInt(args[1]);
            G = new EdgeWeightedGraph(V, E);
        }

        // Wy�wietlanie grafu G
        if (G.V() <= 10) StdOut.println(G);

        // Wyznaczanie i wy�wietlanie drzewa MST
        KruskalMST mst = new KruskalMST(G);
        StdOut.println("Laczna waga = " + mst.weight());
        for (Edge e : mst.edges())
            StdOut.println(e);
    }

}

