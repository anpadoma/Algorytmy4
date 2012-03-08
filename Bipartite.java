/*************************************************************************
 *  Kompilacja:  javac Bipartite.java
 *  Zale¿noœci: Graph.java 
 *
 *  Na podstawie grafu znajduje (i) podzia³ dwudzielny lub (ii) cykl o nieparzystej d³ugoœci.
 *  Dzia³a w czasie O(E + V).
 *
 *
 *************************************************************************/

public class Bipartite {
    private boolean isBipartite;   // Czy graf jest dwudzielny?
    private boolean[] color;       // color[v] okreœla wierzcho³ki z jednej strony podzia³u dwudzielnego.
    private boolean[] marked;      // marked[v] = true, jeœli v odwiedzono w algorytmie DFS.
    private int[] edgeTo;          // edgeTo[v] = ostatnia krawêdŸ na œcie¿ce do v.
    private Stack<Integer> cycle;  // Cykl o nieparzystej d³ugoœci.

    public Bipartite(Graph G) {
        isBipartite = true;
        color  = new boolean[G.V()];
        marked = new boolean[G.V()];
        edgeTo = new int[G.V()];

        for (int v = 0; v < G.V(); v++) {
            if (!marked[v]) {
//                color[v] = false;
                dfs(G, v);
            }
        }
        assert check(G);
    }

    private void dfs(Graph G, int v) { 
        marked[v] = true;
        for (int w : G.adj(v)) {

            // Jeœli znaleziono cykl o nieparzystej d³ugoœci, powstanie pêtla.
            if (cycle != null) return;

            // Znaleziono niepokolorowany wierzcho³ek, dlatego nale¿y rekurencyjnie wywo³aæ metodê.
            if (!marked[w]) {
                edgeTo[w] = v;
                color[w] = !color[v];
                dfs(G, w);
            } 

            // Jeœli v-w tworzy cykl o nieparzystej d³ugoœci, metoda goznajduje.
            else if (color[w] == color[v]) {
                isBipartite = false;
                cycle = new Stack<Integer>();
                cycle.push(w);  // Nie jest potrzebne, chyba ¿e chcesz dwukrotnie 
                                // dodaæ wierzcho³wek pocz¹tkowy.
                for (int x = v; x != w; x = edgeTo[x]) {
                    cycle.push(x);
                }
                cycle.push(w);
            }
        }
    }

    boolean isBipartite()            { return isBipartite; }
    boolean color(int v)             { return color[v];    }
    public Iterable<Integer> cycle() { return cycle;       }

    private boolean check(Graph G) {
        // Graf jest dwudzielny.
        if (isBipartite) {
            for (int v = 0; v < G.V(); v++) {
                for (int w : G.adj(v)) {
                    if (color[v] == color[w]) {
                        System.err.printf("Krawedz %d-%d z %d i %d maja ten sam kolor\n", v, w, v, w);
                        return false;
                    }
                }
            }
        }

        // Graf obejmuje cykl o nieparzystej d³ugoœci.
        else {
            // Sprawdzanie cyklu.
            int first = -1, last = -1;
            for (int v : cycle()) {
                if (first == -1) first = v;
                last = v;
            }
            if (first != last) {
                System.err.printf("Cykl zaczyna sie od %d, a konczy %d\n", first, last);
                return false;
            }
        }

        return true;
    }

    public static void main(String[] args) {
        // Tworzy losowy graf dwudzielny o V wierzcho³kach i E krawêdziach, a nastêpnie dodaje F krawêdzie losowe.
        int V = Integer.parseInt(args[0]);
        int E = Integer.parseInt(args[1]);
        int F = Integer.parseInt(args[2]);

        Graph G = new Graph(V);
        int[] vertices = new int[V];
        for (int i = 0; i < V; i++) vertices[i] = i;
        StdRandom.shuffle(vertices);
        for (int i = 0; i < E; i++) {
            int v = StdRandom.uniform(V/2);
            int w = StdRandom.uniform(V/2);
            G.addEdge(vertices[v], vertices[V/2 + w]);
        }

        // Dodawanie F dodatkowych krawêdzi.
        for (int i = 0; i < F; i++) {
            int v = (int) (Math.random() * V);
            int w = (int) (Math.random() * V);
            G.addEdge(v, w);
        }

        StdOut.println(G);


        Bipartite b = new Bipartite(G);
        if (b.isBipartite()) {
            StdOut.println("Graf jest dwudzielny");
            for (int v = 0; v < G.V(); v++) {
                StdOut.println(v + ": " + b.color(v));
            }
        }
        else {
            StdOut.print("Graf obejmuje cykl o nieparzystej dlugosci: ");
            for (int x : b.cycle()) {
                StdOut.print(x + " ");
            }
            StdOut.println();
        }
    }


}
