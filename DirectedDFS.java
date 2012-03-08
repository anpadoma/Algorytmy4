/*************************************************************************
 *  Kompilacja:  javac DirectedDFS.java
 *  Wykonanie:    java DirectedDFS V E
 *  Zale�no�ci: Digraph.java Bag.java In.java
 *  Plik z danymi:   http://www.cs.princeton.edu/algs4/42directed/tinyDG.txt
 *
 *  Okre�lenie osi�galno�ci z pojedynczego �r�d�a i wielu �r�de� w digrafie
 *  za pomoc� wyszukiwania w g��b.
 *  Z�o�ono�� O(E + V).
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
    private boolean[] marked;  // marked[v] = true, je�li v jest osi�galny
                               // ze �r�d�a (lub �r�de�)

    // Osi�galno�� z jednego �r�d�a
    public DirectedDFS(Digraph G, int s) {
        marked = new boolean[G.V()];
        dfs(G, s);
    }

    // Osi�galno�� z wielu �r�de�
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

    // Czy istnieje �cie�ka skierowana ze �r�d�a (lub �r�de�) do v?
    public boolean marked(int v) {
        return marked[v];
    }

    // Klient testowy
    public static void main(String[] args) {

        // Wczytywanie digrafu z argumentu wiersza polece�
        In in = new In(args[0]);
        Digraph G = new Digraph(in);

        // Wczytywanie �r�de� z wiersza polece�
        Bag<Integer> sources = new Bag<Integer>();
        for (int i = 1; i < args.length; i++) {
            int s = Integer.parseInt(args[i]);
            sources.add(s);
        }

        // Osi�galno�� z wielu �r�de�
        DirectedDFS dfs = new DirectedDFS(G, sources);

        // Wy�wietlanie wierzcho�k�w osi�galnych ze �r�de�
        for (int v = 0; v < G.V(); v++) {
            if (dfs.marked(v)) StdOut.print(v + " ");
        }
        StdOut.println();
    }

}
