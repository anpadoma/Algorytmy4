/*************************************************************************
 *  Kompilacja:  javac Arbitrage.java
 *  Wykonanie:    java Arbitrage < input.txt
 *  Zale¿noœci: EdgeWeightedDigraph.java DirectedEdge.java
 *                BellmanFordSP.java
 *  Plik z danymi:    http://algs4.cs.princeton.edu/44sp/rates.txt
 *
 *  Wykrywanie mo¿liwoœci arbitra¿u.
 *
 *  % more rates.txt
 *  5
 *  USD 1      0.741  0.657  1.061  1.005
 *  EUR 1.349  1      0.888  1.433  1.366
 *  GBP 1.521  1.126  1      1.614  1.538
 *  CHF 0.942  0.698  0.619  1      0.953
 *  CAD 0.995  0.732  0.650  1.049  1
 *
 *  % java Arbitrage < rates.txt
 *  1000.00000 USD =  741.00000 EUR
 *   741.00000 EUR = 1012.20600 CAD
 *  1012.20600 CAD = 1007.14497 USD
 *
 *************************************************************************/

public class Arbitrage {

    public static void main(String[] args) {

        // V walut.
        int V = StdIn.readInt();
        String[] name = new String[V];

        // Tworzenie pe³nej sieci.
        EdgeWeightedDigraph G = new EdgeWeightedDigraph(V);
        for (int v = 0; v < V; v++) {
            name[v] = StdIn.readString();
            for (int w = 0; w < V; w++) {
                double rate = StdIn.readDouble();
                DirectedEdge e = new DirectedEdge(v, w, -Math.log(rate));
                G.addEdge(e);
            }
        }

        // Znajdowanie cyklu ujemnego.
        BellmanFordSP spt = new BellmanFordSP(G, 0);
        if (spt.hasNegativeCycle()) {
            double stake = 1000.0;
            for (DirectedEdge e : spt.negativeCycle()) {
                StdOut.printf("%10.5f %s ", stake, name[e.from()]);
                stake *= Math.exp(-e.weight());
                StdOut.printf("= %10.5f %s\n", stake, name[e.to()]);
            }
        }
        else {
            StdOut.println("Brak mo¿liwoœci arbitra¿u");
        }
    }

}
