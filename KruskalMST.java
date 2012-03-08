/*************************************************************************
 * Kompilacja:  javac KruskalMST.java
 * Wykonanie:  java KruskalMST V E
 * Zale¿noœci: EdgeWeightedGraph.java Edge.java Queue.java UF.java
 *
 * Algorytm Kruskala do wyznaczania minimalnego lasu rozpinaj¹cego.
 *
 * % java KruskalMST < graph8.txt
 *
 *************************************************************************/

public class KruskalMST {
    private double weight;  // Waga drzewa MST
    private Queue<Edge> mst = new Queue<Edge>();  // Krawêdzie w drzewie MST

    // Algorytm Kruskala
    public KruskalMST(EdgeWeightedGraph G) {
        // Wydajniejsze ni¿ budowanie kopca przez przekazanie tablicy krawêdzi
        MinPQ<Edge> pq = new MinPQ<Edge>();
        for (Edge e : G.edges()) {
            pq.insert(e);
        }

        // Uruchamianie algorytmu zach³annego
        UF uf = new UF(G.V());
        while (!pq.isEmpty() && mst.size() < G.V() - 1) {
            Edge e = pq.delMin();
            int v = e.either();
            int w = e.other(v);
            if (!uf.connected(v, w)) { // v-w nie tworzy cyklu
                uf.union(v, w);  // Scalanie sk³adowych v i w
                mst.enqueue(e);  // Dodawanie krawêdzi e do drzewa MST
                weight += e.weight();
            }
        }

        // Sprawdzanie warunków optymalnoœci
        assert check(G);
    }

    // Krawêdzie w minimalnym lasie rozpinaj¹cym jako obiekt Iterable
    public Iterable<Edge> edges() {
        return mst;
    }

    // Waga minimalnego lasu rozpinaj¹cego
    public double weight() {
        return weight;
    }
    
    // Sprawdzanie warunków optymalnoœci (dzia³a w czasie proporcjonalnym do E V lg* V)
    private boolean check(EdgeWeightedGraph G) {

        // Sprawdzanie ³¹cznej wagi
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

        // Sprawdzanie, czy las jest rozpinaj¹cy
        for (Edge e : edges()) {
            int v = e.either(), w = e.other(v);
            if (!uf.connected(v, w)) {
                System.err.println("Nie jest to las rozpinajacy");
                return false;
            }
        }

        // Sprawdzanie, czy jest to minimalny lar rozpinaj¹cy (warunki optymalnoœci przekroju)
        for (Edge e : edges()) {
            int v = e.either(), w = e.other(v);
            
            uf = new UF(G.V());
            for (Edge f : mst) {
                int x = f.either(), y = f.other(x);
                if (f != e) uf.union(x, y);
            }
            
            // Sprawdzanie, czy e to krawêdŸ o wadze minimalnej w przekroju
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
            // Wczytywanie grafu ze standardowego wejœcia
            G = new EdgeWeightedGraph(new In());
        }
        else {
            // Graf losowy o V wierzcho³kach i E krawêdziach
            int V = Integer.parseInt(args[0]);
            int E = Integer.parseInt(args[1]);
            G = new EdgeWeightedGraph(V, E);
        }

        // Wyœwietlanie grafu G
        if (G.V() <= 10) StdOut.println(G);

        // Wyznaczanie i wyœwietlanie drzewa MST
        KruskalMST mst = new KruskalMST(G);
        StdOut.println("Laczna waga = " + mst.weight());
        for (Edge e : mst.edges())
            StdOut.println(e);
    }

}

