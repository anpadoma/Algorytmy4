/*************************************************************************
 *  Kompilacja:  javac CC.java
 *  Zale¿noœci: Graph.java 
 *
 *  Wyznacza spójne sk³adowe przez wyszukiwanie w g³¹b.
 *  Z³o¿onoœæ O(E + V).
 *
 *************************************************************************/

public class CC {
    private boolean[] marked;   // marked[v] = czy oznaczono wierzcho³ek v?
    private int[] id;           // id[v] = identyfikator spójnej sk³adowej obejmuj¹cej v
    private int[] size;         // size[v] = liczba wierzcho³ków w sk³adowej obejmuj¹cej v
    private int count;          // Liczba spójnych sk³adowych

    public CC(Graph G) {
        marked = new boolean[G.V()];
        id = new int[G.V()];
        size = new int[G.V()];
        for (int v = 0; v < G.V(); v++) {
            if (!marked[v]) {
                dfs(G, v);
                count++;
            }
        }
    }

    // Wyszukiwanie w g³¹b
    private void dfs(Graph G, int v) {
        marked[v] = true;
        id[v] = count;
        size[v]++;
        for (int w : G.adj(v)) {
            if (!marked[w]) {
                dfs(G, w);
            }
        }
    }

    // Identyfikator spójnej sk³adowej obejmuj¹cej v
    public int id(int v) {
        return id[v];
    }

    // Rozmiar spójnej sk³adowej obejmuj¹cej v
    public int size(int v) {
        return size[id[v]];
    }

    // Liczba spójnych sk³adowych
    public int count() {
        return count;
    }

    // Czy v i w znajduj¹ siê w tej samej spójnej sk³adowej?
    public boolean areConnected(int v, int w) {
        return id(v) == id(w);
    }

    // Klient testowy
    public static void main(String[] args) {
        int V = Integer.parseInt(args[0]);
        int E = Integer.parseInt(args[1]);
        Graph G = new Graph(V, E);
        StdOut.println(G);
        CC cc = new CC(G);

        StdOut.println("Liczba spojnych skladowych = " + cc.count());
        for (int v = 0; v < G.V(); v++) {
            StdOut.println(v + ": " + cc.id(v));
        }
    }


}
