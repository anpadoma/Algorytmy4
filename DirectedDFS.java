/*************************************************************************
 *  Kompilacja:  javac DirectedDFS.java
 *  Wykonanie:    java DirectedDFS V E
 *  Zale¿noœci: Digraph.java Bag.java In.java
 *  Plik z danymi:   http://www.cs.princeton.edu/algs4/42directed/tinyDG.txt
 *
 *  Okreœlenie osi¹galnoœci z pojedynczego Ÿród³a i wielu Ÿróde³ w digrafie
 *  za pomoc¹ wyszukiwania w g³¹b.
 *  Z³o¿onoœæ O(E + V).
 *
 *  % java DirectedDFS tinyDG.txt 1
 *  1
 *
 *  % java DirectedDFS tinyDG.txt 2
 *  0 1 2 3 4 5
 *
 *  % java DirectedDFS tinyDG.txt 1 2 6
 *  0 1 2 3 4 5 6 9 10 11 12 
 *
 *************************************************************************/

public class DirectedDFS {
    private boolean[] marked;  // marked[v] = true, jeœli v jest osi¹galny
                               // ze Ÿród³a (lub Ÿróde³)

    // Osi¹galnoœæ z jednego Ÿród³a
    public DirectedDFS(Digraph G, int s) {
        marked = new boolean[G.V()];
        dfs(G, s);
    }

    // Osi¹galnoœæ z wielu Ÿróde³
    public DirectedDFS(Digraph G, Iterable<Integer> sources) {
        marked = new boolean[G.V()];
        for (int v : sources)
            dfs(G, v);
    }

    private void dfs(Digraph G, int v) { 
        marked[v] = true;
        for (int w : G.adj(v)) {
            if (!marked[w]) dfs(G, w);
        }
    }

    // Czy istnieje œcie¿ka skierowana ze Ÿród³a (lub Ÿróde³) do v?
    public boolean marked(int v) {
        return marked[v];
    }

    // Klient testowy
    public static void main(String[] args) {

        // Wczytywanie digrafu z argumentu wiersza poleceñ
        In in = new In(args[0]);
        Digraph G = new Digraph(in);

        // Wczytywanie Ÿróde³ z wiersza poleceñ
        Bag<Integer> sources = new Bag<Integer>();
        for (int i = 1; i < args.length; i++) {
            int s = Integer.parseInt(args[i]);
            sources.add(s);
        }

        // Osi¹galnoœæ z wielu Ÿróde³
        DirectedDFS dfs = new DirectedDFS(G, sources);

        // Wyœwietlanie wierzcho³ków osi¹galnych ze Ÿróde³
        for (int v = 0; v < G.V(); v++) {
            if (dfs.marked(v)) StdOut.print(v + " ");
        }
        StdOut.println();
    }

}
