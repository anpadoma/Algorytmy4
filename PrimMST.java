/******************************************************************************
 *  Kompilacja:  javac PrimMST.java
 *  Wykonanie:    java PrimMST V E
 *  Zale�no�ci: EdgeWeightedGraph.java Edge.java Queue.java IndexMinPQ.java
 *                UF.java
 *
 *  Algorytm Prima do obliczania minimalnego lasu rozpinaj�cego.
 *
 ******************************************************************************/

public class PrimMST {
    private Edge[] edgeTo;        // edgeTo[v] = najkr�tsza kraw�d� z wierzcho�ka drzewa do wierzcho�ka spoza drzewa
    private double[] distTo;      // distTo[v] = waga najkr�tszej kraw�dzi
    private boolean[] marked;     // marked[v] = true, je�li v znajduje si� w drzewie (false w przeciwnym razie)
    private IndexMinPQ<Double> pq;

    public PrimMST(EdgeWeightedGraph G) {
        edgeTo = new Edge[G.V()];
        distTo = new double[G.V()];
        marked = new boolean[G.V()];
        pq = new IndexMinPQ<Double>(G.V());
        for (int v = 0; v < G.V(); v++) distTo[v] = Double.POSITIVE_INFINITY;

        for (int v = 0; v < G.V(); v++)      // Nale�y uruchomi� w ka�dym wierzcho�ku, aby znale��
            if (!marked[v]) prim(G, v);      // minimalny las rozpinaj�cy

        // Sprawdzanie warunk�w optymalno�ci
        assert check(G);
    }

    // Uruchamianie algorytmu Prima dla grafu G pocz�wszy od wierzcho�ka s
    private void prim(EdgeWeightedGraph G, int s) {
        distTo[s] = 0.0;
        pq.insert(s, distTo[s]);
        while (!pq.isEmpty()) {
            int v = pq.delMin();
            scan(G, v);
        }
    }

    // Sprawdzanie wierzcho�ka v
    private void scan(EdgeWeightedGraph G, int v) {
        marked[v] = true;
        for (Edge e : G.adj(v)) {
            int w = e.other(v);
            if (marked[w]) continue;         // Kraw�d� v-w jest ju� oznaczona
            if (e.weight() < distTo[w]) {
                distTo[w] = e.weight();
                edgeTo[w] = e;
                if (pq.contains(w)) pq.change(w, distTo[w]);
                else                pq.insert(w, distTo[w]);
            }
        }
    }

    // Zwracanie iteratora po kraw�dziach drzewa MST
    public Iterable<Edge> edges() {
        Bag<Edge> mst = new Bag<Edge>();
        for (int v = 0; v < edgeTo.length; v++) {
            Edge e = edgeTo[v];
            if (e != null) {
                mst.add(e);
            }
        }
        return mst;
    }


    // Zwracanie wagi drzewa MST
    public double weight() {
        double weight = 0.0;
        for (Edge e : edges())
            weight += e.weight();
        return weight;
    }


    // Sprawdzanie warunk�w optymalno�ci (zajmuje czas proporcjonalnie do E V lg* V)
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

            // Wszystkie kraw�dzie drzewa MST opr�cz e
            uf = new UF(G.V());
            for (Edge f : edges()) {
                int x = f.either(), y = f.other(x);
                if (f != e) uf.union(x, y);
            }

            // Sprawdzanie, czy e to kraw�d� o minimalnej wadze w przekroju
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

        if (G.V() <= 10) StdOut.println(G);

        // Wyznaczanie drzewa MST i wy�wietlanie go
        PrimMST mst = new PrimMST(G);
        StdOut.println("Laczna waga = " + mst.weight());
        for (Edge e : mst.edges())
            StdOut.println(e);
    }

}
