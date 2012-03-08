/*************************************************************************
 *  Kompilacja:  javac EdgeWeightedDirectedCycle.java
 *  Wykonanie:    java EdgeWeightedDirectedCycle V E F
 *  Zale�no�ci: EdgeWeightedDigraph.java DirectedEdge Stack.java
 *
 *  Znajduje cykl skierowany w digrafie wa�onym
 *  Z�o�ono�� O(E + V).
 *
 *
 *************************************************************************/

public class EdgeWeightedDirectedCycle {
    private boolean[] marked;             // marked[v] = czy wierzcho�ek v jest oznaczony?
    private DirectedEdge[] edgeTo;        // edgeTo[v] = poprzednia kraw�d� na �cie�ce do v
    private boolean[] onStack;            // onStack[v] = czy wierzcho�ek znajduje si� na stosie?
    private Stack<DirectedEdge> cycle;    // Cykl skierowany (lub null, je�li taki cykl nie istnieje)

    public EdgeWeightedDirectedCycle(EdgeWeightedDigraph G) {
        marked  = new boolean[G.V()];
        onStack = new boolean[G.V()];
        edgeTo  = new DirectedEdge[G.V()];
        for (int v = 0; v < G.V(); v++)
            if (!marked[v]) dfs(G, v);

        // Sprawdzanie, czy digraf obejmuje cykl
        assert check(G);
    }


    // Sprawdzanie, czy algorytm wyznacza porz�dek topologiczny lub znajduje cykl skierowany
    private void dfs(EdgeWeightedDigraph G, int v) {
        onStack[v] = true;
        marked[v] = true;
        for (DirectedEdge e : G.adj(v)) {
            int w = e.to();

            // Ko�czenie pracy, je�li znaleziono cykl skierowany
            if (cycle != null) return;

            // Znaleziono wierzcho�ek, dlatego nale�y rekurencyjnie wywo�a� metod�
            else if (!marked[w]) {
                edgeTo[w] = e;
                dfs(G, w);
            }

            // Odtwarzanie cyklu skierowanego
            else if (onStack[w]) {
                cycle = new Stack<DirectedEdge>();
                while (e.from() != w) {
                    cycle.push(e);
                    e = edgeTo[e.from()];
                }
                cycle.push(e);
            }
        }

        onStack[v] = false;
    }

    public boolean hasCycle()             { return cycle != null;   }
    public Iterable<DirectedEdge> cycle() { return cycle;           }


    // Sprawdzanie, czy digraf jest acykliczny lub obejmuje cykl skierowany
    private boolean check(EdgeWeightedDigraph G) {

        // Digraf wa�ony jest cykliczny
        if (hasCycle()) {
            // Sprawdzanie cyklu
            DirectedEdge first = null, last = null;
            for (DirectedEdge e : cycle()) {
                if (first == null) first = e;
                if (last != null) {
                    if (last.to() != e.from()) {
                        System.err.printf("Kraw�dzie cyklu %s i %s nie s� incydentne\n", last, e);
                        return false;
                    }
                }
                last = e;
            }

            if (last.to() != first.from()) {
                System.err.printf("Kraw�dzie cyklu %s i %s nie s� incydentne\n", last, first);
                return false;
            }
        }


        return true;
    }

    public static void main(String[] args) {

        // Tworzenie losowego grafu DAG o V wierzcho�kach i E kraw�dziach oraz dodawanie F losowych kraw�dzi
        int V = Integer.parseInt(args[0]);
        int E = Integer.parseInt(args[1]);
        int F = Integer.parseInt(args[2]);
        EdgeWeightedDigraph G = new EdgeWeightedDigraph(V);
        int[] vertices = new int[V];
        for (int i = 0; i < V; i++) vertices[i] = i;
        StdRandom.shuffle(vertices);
        for (int i = 0; i < E; i++) {
            int v, w;
            do {
                v = StdRandom.uniform(V);
                w = StdRandom.uniform(V);
            } while (v >= w);
            double weight = Math.random();
            G.addEdge(new DirectedEdge(v, w, weight));
        }

        // Dodawanie F dodatkowych kraw�dzi
        for (int i = 0; i < F; i++) {
            int v = (int) (Math.random() * V);
            int w = (int) (Math.random() * V);
            double weight = Math.random();
            G.addEdge(new DirectedEdge(v, w, weight));
        }

        StdOut.println(G);

        // Znajdowanie cyklu skierowanego
        EdgeWeightedDirectedCycle finder = new EdgeWeightedDirectedCycle(G);
        if (finder.hasCycle()) {
            StdOut.print("Cykl: ");
            for (DirectedEdge e : finder.cycle()) {
                StdOut.print(e + " ");
            }
            StdOut.println();
        }

        // lub istnieje porz�dek topologiczny
        else {
            StdOut.println("Brak cyklu skierowanego");
        }
    }

}
