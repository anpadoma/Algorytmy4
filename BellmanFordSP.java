/*************************************************************************
 *  Kompilacja:  javac BellmanFordSP.java
 *  Wykonanie:    java BellmanFordSP V E
 *  Zale�no�ci: EdgeWeightedDigraph.java DirectedEdge.java Queue.java
 *                EdgeWeightedDirectedCycle.java
 *
 *  Algorytm Bellmana-Forda do wyznaczania najkr�tszej �cie�ki. Wyznacza w wa�onym digrafie G 
 *  najkr�tsz� �cie�k� z wierzcho�ka s lub znajduje cykl ujemny
 *  osi�galny z s.
 * 
 *  % java BellmanFordSP 100 500
 *
 *************************************************************************/

public class BellmanFordSP {
    private double[] distTo;               // distTo[v] = d�ugo�� najkr�tszej �cie�ki s->v.
    private DirectedEdge[] edgeTo;         // edgeTo[v] = ostatnia kraw�d� najkr�tszej �cie�ki s->v.
    private boolean[] onQueue;             // onQueue[v] = czy v znajduje si� w kolejce?
    private Queue<Integer> queue;          // Kolejka wierzcho�k�w do relaksacji.
    private int cost;                      // Liczba wywo�a� metody relax().
    private Iterable<DirectedEdge> cycle;  // Cykl ujemny (lub null, je�li taki cykl nie istnieje).

    public BellmanFordSP(EdgeWeightedDigraph G, int s) {
        distTo  = new double[G.V()];
        edgeTo  = new DirectedEdge[G.V()];
        onQueue = new boolean[G.V()];
        for (int v = 0; v < G.V(); v++)
            distTo[v] = Double.POSITIVE_INFINITY;
        distTo[s] = 0.0;

        // Algorytm Bellmana-Forda.
        queue = new Queue<Integer>();
        queue.enqueue(s);
        onQueue[s] = true;
        while (!queue.isEmpty() && !hasNegativeCycle()) {
            int v = queue.dequeue();
            onQueue[v] = false;
            relax(G, v);
        }

        assert check(G, s);
    }

    // Relaksacja wierzcho�ka v i umieszczanie innych punkt�w ko�cowych w kolejce, je�li wyst�pi�a zmiana.
    private void relax(EdgeWeightedDigraph G, int v) {
        for (DirectedEdge e : G.adj(v)) {
            int w = e.to();
            if (distTo[w] > distTo[v] + e.weight()) {
                distTo[w] = distTo[v] + e.weight();
                edgeTo[w] = e;
                if (!onQueue[w]) {
                    queue.enqueue(w);
                    onQueue[w] = true;
                }
            }
            if (cost++ % G.V() == 0)
                findNegativeCycle();
        }
    }


    // Czy istnieje cykl ujemny osi�galny z s?
    public boolean hasNegativeCycle() {
        return cycle != null;
    }

    // Zwracanie cyklu ujemnego (lub null, je�li taki cykl nie istnieje).
    public Iterable<DirectedEdge> negativeCycle() {
        return cycle;
    }
    
    private void findNegativeCycle() {
        int V = edgeTo.length;
        EdgeWeightedDigraph spt = new EdgeWeightedDigraph(V);
        for (int v = 0; v < V; v++)
            if (edgeTo[v] != null)
                spt.addEdge(edgeTo[v]);

        EdgeWeightedDirectedCycle finder = new EdgeWeightedDirectedCycle(spt);
        cycle = finder.cycle();
    }

    // Czy istnieje �cie�ka z s do v?
    public boolean hasPathTo(int v) {
        return distTo[v] < Double.POSITIVE_INFINITY;
    }


    // Zwraca d�ugo�� najkr�tszej �cie�ki z s do v.
    public double distTo(int v) {
        return distTo[v];
    }

    // Zwraca najkr�tsz� �cie�k� z s do v (lub null, je�li taka �cie�ka nie istnieje).
    public Iterable<DirectedEdge> pathTo(int v) {
        if (!hasPathTo(v)) return null;
        Stack<DirectedEdge> path = new Stack<DirectedEdge>();
        for (DirectedEdge e = edgeTo[v]; e != null; e = edgeTo[e.from()]) {
            path.push(e);
        }
        return path;
    }

    // Sprawdzanie warunk�w optymalno�ci. Albo:
    // (i) istnieje cykl ujemny osi�galny z s
    //     albo
    // (ii)  dla wszystkich kraw�dzi e = v->w:            distTo[w] <= distTo[v] + e.weight()
    // (ii') dla wszystkich kraw�dzi e = v->w z SPT: distTo[w] == distTo[v] + e.weight()
    private boolean check(EdgeWeightedDigraph G, int s) {

        // Istnieje cykl ujemny.
        if (hasNegativeCycle()) {
            double weight = 0.0;
            for (DirectedEdge e : negativeCycle()) {
                weight += e.weight();
            }
            if (weight >= 0.0) {
                System.err.println("Blad: waga cyklu ujemnego = " + weight);
                return false;
            }
        }

        // Nie istnieje cykl ujemny osi�galny ze �r�d�a.
        else {

            // Sprawdzanie, czy distTo[v] i edgeTo[v] s� sp�jne.
            if (distTo[s] != 0.0 || edgeTo[s] != null) {
                System.err.println("distanceTo[s] i edgeTo[s] sa niespojne");
                return false;
            }
            for (int v = 0; v < G.V(); v++) {
                if (v == s) continue;
                if (edgeTo[v] == null && distTo[v] != Double.POSITIVE_INFINITY) {
                    System.err.println("distTo[] i edgeTo[] sa niespojne");
                    return false;
                }
            }

            // Sprawdzanie, czy dla wszystkich kraw�dzi e = v->w spe�niona jest zale�no�� distTo[w] <= distTo[v] + e.weight().
            for (int v = 0; v < G.V(); v++) {
                for (DirectedEdge e : G.adj(v)) {
                    int w = e.to();
                    if (distTo[v] + e.weight() < distTo[w]) {
                        System.err.println("krawedz " + e + " nie przeszla relaksacji");
                        return false;
                    }
                }
            }

            // Sprawdzanie, czy dla wszystkich kraw�dzi e = v->w z SPT spe�niona jest zale�no�� distTo[w] == distTo[v] + e.weight().
            for (int w = 0; w < G.V(); w++) {
                if (edgeTo[w] == null) continue;
                DirectedEdge e = edgeTo[w];
                int v = e.from();
                if (w != e.to()) return false;
                if (distTo[v] + e.weight() != distTo[w]) {
                    System.err.println("krawedz " + e + " na najkrotszej sciezce nie jest najkrotsza");
                    return false;
                }
            }
        }

        StdOut.println("Spelnia warunki optymalnosci");
        StdOut.println();
        return true;
    }



    public static void main(String[] args) {

        // Graf losowy o V wierzcho�kach i E kraw�dziach; kraw�dzie r�wnoleg�e s� dozwolone
        int V = Integer.parseInt(args[0]);
        int E = Integer.parseInt(args[1]);
        EdgeWeightedDigraph G = new EdgeWeightedDigraph(V);
        for (int i = 0; i < E; i++) {
            int v = (int) (V * Math.random());
            int w = (int) (V * Math.random());
            double weight = Math.round(100 * (Math.random() - 0.15)) / 100.0;
            if (v == w) G.addEdge(new DirectedEdge(v, w, Math.abs(weight)));
            else        G.addEdge(new DirectedEdge(v, w, weight));
        }

        StdOut.println(G);

        // Uruchamianie algorytmu Bellmana-Forda z wierzcho�ka 0
        int s = 0;
        BellmanFordSP sp = new BellmanFordSP(G, s);

        // Wy�wietlanie cyklu ujemnego
        if (sp.hasNegativeCycle()) {
            StdOut.println("Cykl ujemny:");
            for (DirectedEdge e : sp.negativeCycle())
                StdOut.println(e);
        }

        // Wy�wietlanie najkr�tszych �cie�ek
        else {
            StdOut.println("Najkrotsze sciezki z " + s);
            StdOut.println("------------------------");
            for (int v = 0; v < G.V(); v++) {
                if (sp.hasPathTo(v)) {
                    StdOut.printf("%d do %d (%5.2f)  ", s, v, sp.distTo(v));
                    for (DirectedEdge e : sp.pathTo(v)) {
                        StdOut.print(e + "   ");
                    }
                    StdOut.println();
                }
                else {
                    StdOut.printf("%d do %d           brak sciezki\n", s, v);
                }
            }
        }

    }

}
