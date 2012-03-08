/*************************************************************************
 *  Kompilacja:  javac DepthFirstDirectedPaths.java
 *  Wykonanie:    java DepthFirstDirectedPaths G s
 *  Zale¿noœci: Digraph.java Stack.java
 *
 *  Okreœlanie przez wyszukiwanie w g³¹b
 *  osi¹galnoœci z danego wierzcho³ka w digrafie.
 *  Z³o¿onoœæ O(E + V).
 *
 *  % tinyDG.txt 3
 *  3 do 0:  3-5-4-2-0
 *  3 do 1:  3-5-4-2-0-1
 *  3 do 2:  3-5-4-2
 *  3 do 3:  3
 *  3 do 4:  3-5-4
 *  3 do 5:  3-5
 *  3 do 6:  brak polaczenia
 *  3 do 7:  brak polaczenia
 *  3 do 8:  brak polaczenia
 *  3 do 9:  brak polaczenia
 *  3 do 10:  brak polaczenia
 *  3 do 11:  brak polaczenia
 *  3 do 12:  brak polaczenia
 *
 *************************************************************************/

public class DepthFirstDirectedPaths {
    private boolean[] marked;  // marked[v] = true, jeœli v jest osi¹galny z s
    private int[] edgeTo;      // edgeTo[v] = ostatnia krawêdŸ na œcie¿ce z s do v
    private final int s;       // Wierzcho³ek Ÿród³owy

    public DepthFirstDirectedPaths(Digraph G, int s) {
        marked = new boolean[G.V()];
        edgeTo = new int[G.V()];
        this.s = s;
        dfs(G, s);
    }

    private void dfs(Digraph G, int v) { 
        marked[v] = true;
        for (int w : G.adj(v)) {
            if (!marked[w]) {
                edgeTo[w] = v;
                dfs(G, w);
            }
        }
    }

    // Czy istnieje œcie¿ka skierowana z s do v?
    public boolean hasPathTo(int v) {
        return marked[v];
    }

    // Zwracanie œcie¿ki z s do v (lub null, jeœli taka œcie¿ka nie istnieje)
    public Iterable<Integer> pathTo(int v) {
        if (!hasPathTo(v)) return null;
        Stack<Integer> path = new Stack<Integer>();
        for (int x = v; x != s; x = edgeTo[x])
            path.push(x);
        path.push(s);
        return path;
    }


  public static void main(String[] args) {
        In in = new In(args[0]);
        Digraph G = new Digraph(in);
        // StdOut.println(G);

        int s = Integer.parseInt(args[1]);
        DepthFirstDirectedPaths dfs = new DepthFirstDirectedPaths(G, s);

        for (int v = 0; v < G.V(); v++) {
            if (dfs.hasPathTo(v)) {
                StdOut.printf("%d do %d:  ", s, v);
                for (int x : dfs.pathTo(v)) {
                    if (x == s) StdOut.print(x);
                    else        StdOut.print("-" + x);
                }
                StdOut.println();
            }

            else {
                StdOut.printf("%d do %d:  brak polaczenia\n", s, v);
            }

        }
    }

}
