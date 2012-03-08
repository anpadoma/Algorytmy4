/*************************************************************************
 *  Kompilacja:  javac CC.java
 *  Zale�no�ci: Graph.java 
 *
 *  Wyznacza sp�jne sk�adowe przez wyszukiwanie w g��b.
 *  Z�o�ono�� O(E + V).
 *
 *************************************************************************/

public class CC {
    private boolean[] marked;   // marked[v] = czy oznaczono wierzcho�ek v?
    private int[] id;           // id[v] = identyfikator sp�jnej sk�adowej obejmuj�cej v
    private int[] size;         // size[v] = liczba wierzcho�k�w w sk�adowej obejmuj�cej v
    private int count;          // Liczba sp�jnych sk�adowych

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

    // Wyszukiwanie w g��b
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

    // Identyfikator sp�jnej sk�adowej obejmuj�cej v
    public int id(int v) {
        return id[v];
    }

    // Rozmiar sp�jnej sk�adowej obejmuj�cej v
    public int size(int v) {
        return size[id[v]];
    }

    // Liczba sp�jnych sk�adowych
    public int count() {
        return count;
    }

    // Czy v i w znajduj� si� w tej samej sp�jnej sk�adowej?
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
