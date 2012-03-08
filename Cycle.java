/*************************************************************************
 *  Kompilacja:  javac Cycle.java
 *  Zale�no�ci: Graph.java Stack.java
 *
 *  Wykrywa cykle.
 *  Z�o�ono�� O(E + V).
 *
 *************************************************************************/

public class Cycle {
    private boolean[] marked;
    private int[] edgeTo;
    private Stack<Integer> cycle;

    public Cycle(Graph G) {
        if (hasSelfLoop(G)) return;
        if (hasParallelEdges(G)) return;
        marked = new boolean[G.V()];
        edgeTo = new int[G.V()];
        for (int v = 0; v < G.V(); v++)
            if (!marked[v])
                dfs(G, -1, v);
    }


    // Czy graf obejmuje p�tl� zwrotn�?
    // Efekt uboczny: inicjuje cykl za pomoc� p�tli zwrotnej
    private boolean hasSelfLoop(Graph G) {
        for (int v = 0; v < G.V(); v++) {
            for (int w : G.adj(v)) {
                if (v == w) {
                    cycle = new Stack<Integer>();
                    cycle.push(v);
                    cycle.push(v);
                    return true;
                }
            }
        }
        return false;
    }

    // Czy graf obejmuje dwie r�wnoleg�e kraw�dzie?
    // Efekt uboczny: inicjuje cykl za pomoc� dw�ch kraw�dzi r�wnoleg�ych
    private boolean hasParallelEdges(Graph G) {
        marked = new boolean[G.V()];

        for (int v = 0; v < G.V(); v++) {

            // Wyszukuje kraw�dzie r�wnoleg�e incydentne wzgl�dem v
            for (int w : G.adj(v)) {
                if (marked[w]) {
                    cycle = new Stack<Integer>();
                    cycle.push(v);
                    cycle.push(w);
                    cycle.push(v);
                    return true;
                }
                marked[w] = true;
            }

            // Zerowanie, tak aby marked[v] = false dla wszystkich v
            for (int w : G.adj(v)) {
                marked[w] = false;
            }
        }
        return false;
    }

    public boolean hasCycle()        { return cycle != null; }
    public Iterable<Integer> cycle() { return cycle;         }

    private void dfs(Graph G, int u, int v) {
        marked[v] = true;
        for (int w : G.adj(v)) {

            // Skr�cone przetwarzanie, je�li znaleziono cykl
            if (cycle != null) return;

            if (!marked[w]) {
                edgeTo[w] = v;
                dfs(G, v, w);
            }

            // Wykrywanie cyklu (jednak z pomini�ciem kraw�dzi prowadz�cej z powrotem do v)
            else if (w != u) {
                cycle = new Stack<Integer>();
                for (int x = v; x != w; x = edgeTo[x]) {
                    cycle.push(x);
                }
                cycle.push(w);
                cycle.push(v);
            }
        }
    }

    // Klient testowy
    public static void main(String[] args) {
        int V = Integer.parseInt(args[0]);
        int E = Integer.parseInt(args[1]);
        Graph G = new Graph(V, E);
        System.out.println(G);

        Cycle finder = new Cycle(G);
        if (finder.hasCycle()) {
            for (int v : finder.cycle()) {
                StdOut.print(v + " ");
            }
            StdOut.println();
        }
        else {
            StdOut.println("Graf jest acykliczny");
        }
    }


}

