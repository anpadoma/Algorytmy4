/*************************************************************************
 *  Kompilacja:  javac DijkstraSP.java
 *  Wykonanie:    java DijkstraSP input.txt s
 *  Zale¿noœci: EdgeWeightedDigraph.java IndexMinPQ.java Stack.java DirectedEdge.java
 *  Pliki z danymi:   http://algs4.cs.princeton.edu/44sp/tinyEWD.txt
 *                http://algs4.cs.princeton.edu/44sp/mediumEWD.txt
 *                http://algs4.cs.princeton.edu/44sp/largeEWD.txt
 *
 *  Algorytm Dijkstry. Wyznacza drzewo najkrótszych œcie¿ek.
 *  Zak³adamy, ¿e wszystkie wagi s¹ nieujemne.
 *
 *  % java DijkstraSP tinyEWD.txt 0
 *  0 do 0 (0.00)  
 *  0 do 1 (1.05)  0->4  0.38   4->5  0.35   5->1  0.32   
 *  0 do 2 (0.26)  0->2  0.26   
 *  0 do 3 (0.99)  0->2  0.26   2->7  0.34   7->3  0.39   
 *  0 do 4 (0.38)  0->4  0.38   
 *  0 do 5 (0.73)  0->4  0.38   4->5  0.35   
 *  0 do 6 (1.51)  0->2  0.26   2->7  0.34   7->3  0.39   3->6  0.52   
 *  0 do 7 (0.60)  0->2  0.26   2->7  0.34   
 *
 *  % java DijkstraSP mediumEWD.txt 0
 *  0 do 0 (0.00)  
 *  0 do 1 (0.71)  0->44  0.06   44->93  0.07   ...  107->1  0.07   
 *  0 do 2 (0.65)  0->44  0.06   44->231  0.10  ...  42->2  0.11   
 *  0 do 3 (0.46)  0->97  0.08   97->248  0.09  ...  45->3  0.12   
 *  0 do 4 (0.42)  0->44  0.06   44->93  0.07   ...  77->4  0.11   
 *  ...
 *
 *************************************************************************/

public class DijkstraSP {
    private double[] distTo;          // distTo[v] = d³ugoœæ najkrótszej œcie¿ki s->v
    private DirectedEdge[] edgeTo;    // edgeTo[v] = ostatnia krawêdŸ najkrótszej œcie¿ki s->v
    private IndexMinPQ<Double> pq;    // Kolejka priorytetowa wierzcho³ków

    public DijkstraSP(EdgeWeightedDigraph G, int s) {
        distTo = new double[G.V()];
        edgeTo = new DirectedEdge[G.V()];
        for (int v = 0; v < G.V(); v++)
            distTo[v] = Double.POSITIVE_INFINITY;
        distTo[s] = 0.0;

        // Relaksacja wierzcho³ków wed³ug odleg³oœci od s
        pq = new IndexMinPQ<Double>(G.V());
        pq.insert(s, distTo[s]);
        while (!pq.isEmpty()) {
            int v = pq.delMin();
            for (DirectedEdge e : G.adj(v))
                relax(e);
        }

        // Sprawdzanie warunków optymalnoœci
        assert check(G, s);
    }

    // Relaksacja krawêdzi e i aktualizowanie pq przy zmianach
    private void relax(DirectedEdge e) {
        int v = e.from(), w = e.to();
        if (distTo[w] > distTo[v] + e.weight()) {
            distTo[w] = distTo[v] + e.weight();
            edgeTo[w] = e;
            if (pq.contains(w)) pq.change(w, distTo[w]);
            else                pq.insert(w, distTo[w]);
        }
    }

    // D³ugoœæ najkrótszej œcie¿ki z s do v
    public double distTo(int v) {
        return distTo[v];
    }

    // Czy istnieje œcie¿ka z s do v?
    public boolean hasPathTo(int v) {
        return distTo[v] < Double.POSITIVE_INFINITY;
    }

    // Najkrótsza œcie¿ka z s do v jako obiekt Iterable (zwraca null, jeœli taka œcie¿ka nie istnieje)
    public Iterable<DirectedEdge> pathTo(int v) {
        if (!hasPathTo(v)) return null;
        Stack<DirectedEdge> path = new Stack<DirectedEdge>();
        for (DirectedEdge e = edgeTo[v]; e != null; e = edgeTo[e.from()]) {
            path.push(e);
        }
        return path;
    }


    // Sprawdzanie warunków optymalnoœci:
    // (i) dla wszystkich krawêdzi e:            distTo[e.to()] <= distTo[e.from()] + e.weight()
    // (ii) dla wszystkich krawêdzi e z SPT: distTo[e.to()] == distTo[e.from()] + e.weight()
    private boolean check(EdgeWeightedDigraph G, int s) {

        // Sprawdzanie, czy wagi krawêdzi s¹ nieujemne
        for (DirectedEdge e : G.edges()) {
            if (e.weight() < 0) {
                System.err.println("Wykryto krawêdŸ o wadze ujemnej");
                return false;
            }
        }

        // Sprawdzanie, czy distTo[v] i edgeTo[v] s¹ spójne
        if (distTo[s] != 0.0 || edgeTo[s] != null) {
            System.err.println("distTo[s] i edgeTo[s] s¹ niespójne");
            return false;
        }
        for (int v = 0; v < G.V(); v++) {
            if (v == s) continue;
            if (edgeTo[v] == null && distTo[v] != Double.POSITIVE_INFINITY) {
                System.err.println("distTo[] i edgeTo[] s¹ niespójne");
                return false;
            }
        }

        // Sprawdzanie, czy dla wszystkich krawêdzi e = v->w spe³nia zale¿noœæ distTo[w] <= distTo[v] + e.weight()
        for (int v = 0; v < G.V(); v++) {
            for (DirectedEdge e : G.adj(v)) {
                int w = e.to();
                if (distTo[v] + e.weight() < distTo[w]) {
                    System.err.println("krawêdŸ " + e + " bez relaksacji");
                    return false;
                }
            }
        }

        // Sprawdzanie, czy dla wszystkich krawêdzi e = v->w z drzewa
		// SPT spe³niona jest zale¿noœæ distTo[w] == distTo[v] + e.weight()
        for (int w = 0; w < G.V(); w++) {
            if (edgeTo[w] == null) continue;
            DirectedEdge e = edgeTo[w];
            int v = e.from();
            if (w != e.to()) return false;
            if (distTo[v] + e.weight() != distTo[w]) {
                System.err.println("krawêdŸ " + e + " na najkrótszej œcie¿ce nie jest najkrótsza");
                return false;
            }
        }
        return true;
    }


    public static void main(String[] args) {
        In in = new In(args[0]);
        EdgeWeightedDigraph G = new EdgeWeightedDigraph(in);
        int s = Integer.parseInt(args[1]);

        // Wyznacza najkrótsze œcie¿ki
        DijkstraSP sp = new DijkstraSP(G, s);


        // Wyœwietla najkrótsz¹ œcie¿kê
        for (int t = 0; t < G.V(); t++) {
            if (sp.hasPathTo(t)) {
                StdOut.printf("%d do %d (%.2f)  ", s, t, sp.distTo(t));
                if (sp.hasPathTo(t)) {
                    for (DirectedEdge e : sp.pathTo(t)) {
                        StdOut.print(e + "   ");
                    }
                }
                StdOut.println();
            }
            else {
                StdOut.printf("%d do %d         brak œcie¿ki\n", s, t);
            }
        }
    }

}
