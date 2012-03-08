/*************************************************************************
 *  Kompilacja:  javac KosarajuSCC.java
 *  Zale�no�ci: Digraph.java TransitiveClosure.java StdOut.java
 *
 *  Obliczanie silnie sp�jnych sk�adowych digrafu za pomoc�
 *  algorytmu Kosaraju.
 *
 *  Z�o�ono�� O(E + V).
 *
 *
 *************************************************************************/

public class KosarajuSCC {
    private boolean[] marked;     // marked[v] = czy odwiedzono wierzcho�ek v?
    private int[] id;             // id[v] = identyfikator silnej sk�adowej obejmuj�cej v
    private int count;            // Liczba silnie sp�jnych sk�adowych

    public KosarajuSCC(Digraph G) {

        // Wyznacza odwrotny porz�dek postorder dla odwr�conego grafu
        DepthFirstOrder dfs = new DepthFirstOrder(G.reverse());

        // Uruchamia wyszukiwanie w g��b w grafie G; dzia�anie oparte 
		// jest na odwr�conym porz�dku postorder
        marked = new boolean[G.V()];
        id = new int[G.V()];
        for (int v : dfs.reversePostorder()) {
            if (!marked[v]) {
                dfs(G, v);
                count++;
            }
        }

        // Sprawdza, czy id[] obejmuje silne sk�adowe
        assert check(G);
    }

    // Uruchamianie wyszukiwania w g��b dla grafu G
    private void dfs(Digraph G, int v) { 
        marked[v] = true;
        id[v] = count;
        for (int w : G.adj(v)) {
            if (!marked[w]) dfs(G, w);
        }
    }

    // Zwracanie liczby silnie sp�jnych sk�adowych
    public int count() { return count; }

    // Czy v i w s� silnie sp�jne?
    public boolean stronglyConnected(int v, int w) {
        return id[v] == id[w];
    }

    // Identyfikator silnej sk�adowej obejmuj�cej v
    public int id(int v) {
        return id[v];
    }

    // Czy tablica id[] obejmuje silnie sp�jne sk�adowe?
    private boolean check(Digraph G) {
        TransitiveClosure tc = new TransitiveClosure(G);
        for (int v = 0; v < G.V(); v++) {
            for (int w = 0; w < G.V(); w++) {
                if (stronglyConnected(v, w) != (tc.reachable(v, w) && tc.reachable(w, v)))
                    return false;
            }
        }
        return true;
    }

    public static void main(String[] args) {
        int V = Integer.parseInt(args[0]);
        int E = Integer.parseInt(args[1]);
        Digraph G = new Digraph(V, E);
        StdOut.println(G);
        KosarajuSCC scc = new KosarajuSCC(G);
        StdOut.println("Silnie spojna skladowa = " + scc.count());
        for (int v = 0; v < G.V(); v++)
            StdOut.println(v + ": " + scc.id(v));
    }

}
