/*************************************************************************
 *  Kompilacja:  javac AcyclicSP.java
 *  Wykonanie:    java AcyclicSP V E
 *  Zale�no�ci: EdgeWeightedDigraph.java DirectedEdge.java Topological.java
 *  Pliki z danymi:   http://algs4.cs.princeton.edu/44sp/tinyEWDAG.txt
 *
 *  Wyznacza najkr�tsze �cie�ki w acyklicznym digrafie wa�onym.
 *
 *  Uwaga: przed uruchomieniem warto sprawdzi�, czy graf jest grafem DAG.
 *
 *  % java AcyclicSP tinyEWDAG.txt 5
 *  5 do 0 (0.73)  5->4  0,35   4->0  0,38   
 *  5 do 1 (0.32)  5->1  0,32   
 *  5 do 2 (0.62)  5->7  0,28   7->2  0,34   
 *  5 do 3 (0.61)  5->1  0,32   1->3  0,29   
 *  5 do 4 (0.35)  5->4  0,35   
 *  5 do 5 (0.00)  
 *  5 do 6 (1.13)  5->1  0,32   1->3  0,29   3->6  0,52   
 *  5 do 7 (0.28)  5->7  0,28   
 *
 *************************************************************************/

public class AcyclicSP {
    private double[] distTo;         // distTo[v] = d�ugo�� najkr�tszej �cie�ki s->v.
    private DirectedEdge[] edgeTo;   // edgeTo[v] = ostatnia kraw�d� najkr�tszej �cie�ki s->v

    public AcyclicSP(EdgeWeightedDigraph G, int s) {
        distTo = new double[G.V()];
        edgeTo = new DirectedEdge[G.V()];
        for (int v = 0; v < G.V(); v++)
            distTo[v] = Double.POSITIVE_INFINITY;
        distTo[s] = 0.0;

        // Przechodzi po wierzcho�kach w porz�dku topologicznym.
        Topological topological = new Topological(G);
        for (int v : topological.order()) {
            for (DirectedEdge e : G.adj(v))
                relax(e);
        }
    }

    // Relaksacja kraw�dzi e.
    private void relax(DirectedEdge e) {
        int v = e.from(), w = e.to();
        if (distTo[w] > distTo[v] + e.weight()) {
            distTo[w] = distTo[v] + e.weight();
            edgeTo[w] = e;
        }       
    }

    // Zwraca d�ugo�� najkr�tszej �cie�ki z s do v (lub niesko�czono��, je�li taka �cie�ka nie istnieje).
    public double distTo(int v) {
        return distTo[v];
    }

    // Czy istnieje �cie�ka z s do v?
    public boolean hasPathTo(int v) {
        return distTo[v] < Double.POSITIVE_INFINITY;
    }

    // Zwraca najkr�tsz� �cie�k� z s do v (null, je�li taka �cie�ka nie istnieje).
    public Iterable<DirectedEdge> pathTo(int v) {
        if (!hasPathTo(v)) return null;
        Stack<DirectedEdge> path = new Stack<DirectedEdge>();
        for (DirectedEdge e = edgeTo[v]; e != null; e = edgeTo[e.from()]) {
            path.push(e);
        }
        return path;
    }



    public static void main(String[] args) {
        In in = new In(args[0]);
        int s = Integer.parseInt(args[1]);
        EdgeWeightedDigraph G = new EdgeWeightedDigraph(in);

        // Znajduje najkr�tsz� �cie�k� z s do ka�dego innego wierzcho�ka grafu DAG.
        AcyclicSP sp = new AcyclicSP(G, s);
        for (int v = 0; v < G.V(); v++) {
            if (sp.hasPathTo(v)) {
                StdOut.printf("%d do %d (%.2f)  ", s, v, sp.distTo(v));
                for (DirectedEdge e : sp.pathTo(v)) {
                    StdOut.print(e + "   ");
                }
                StdOut.println();
            }
            else {
                StdOut.printf("%d do %d         brak �cie�ki\n", s, v);
            }
        }
    }
}
