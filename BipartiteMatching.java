/*************************************************************************
 *  Kompilacja:  javac BipartiteMatching.java
 *  Wykonanie:    java BipartiteMatching V E
 *  Zale¿noœci: FordFulkerson.java FlowNetwork.java FlowEdge.java 
 *
 *  Znajduje skojarzenie maksymalne w grafie dwudzielnym. Rozwi¹zanie oparte na
 *  redukcji do problemu przep³ywu maksymalnego.
 *
 *********************************************************************/

public class BipartiteMatching {

    public static void main(String[] args) {

        // Wczytuje sieæ dwudzieln¹ o 2N wierzcho³kach i E krawêdziach.
        // Zak³adamy, ¿e wierzcho³ki po jednej stronie podzia³u 
        // maj¹ nazwy od 0 do N-1, a po drugiej stronie - od N do 2N-1.
        int N = Integer.parseInt(args[0]);
        int E = Integer.parseInt(args[1]);
        int s = 2*N, t = 2*N + 1;
        FlowNetwork G = new FlowNetwork(2*N + 2);
        for (int i = 0; i < E; i++) {
            int v = StdRandom.uniform(N);
            int w = StdRandom.uniform(N) + N;
            G.addEdge(new FlowEdge(v, w, Double.POSITIVE_INFINITY));
            StdOut.println(v + "-" + w);
        }
        for (int i = 0; i < N; i++) {
            G.addEdge(new FlowEdge(s,     i, 1.0));
            G.addEdge(new FlowEdge(i + N, t, 1.0));
        }


        // Wyznaczanie przep³ywu maksymalnego i przekroju minimalnego.
        FordFulkerson maxflow = new FordFulkerson(G, s, t);
        StdOut.println();
        StdOut.println("Wartosc skojarzenia maksymalnego = " + (int) maxflow.value());
        for (int v = 0; v < N; v++) {
            for (FlowEdge e : G.adj(v)) {
                if (e.from() == v && e.flow() > 0)
                    StdOut.println(e.from() + "-" + e.to());
            }
        }
    }

}
