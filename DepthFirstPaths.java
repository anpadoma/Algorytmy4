/*************************************************************************
 *  Kompilacja:  javac DepthFirstPaths.java
 *  Wykonanie:    java DepthFirstPaths G s
 *  Zale¿noœci: Graph.java Stack.java
 *
 *  Wykonuje wyszukiwanie w g³¹b w grafie nieskierowanym.
 *  Z³o¿onoœæ O(E + V).
 *
 *  %  java Graph tinyCG.txt
 *  6 8
 *  0: 2 1 5 
 *  1: 0 2 
 *  2: 0 1 3 4 
 *  3: 5 4 2 
 *  4: 3 2 
 *  5: 3 0 
 *
 *  % java DepthFirstPaths tinyCG.txt 0
 *  0 do 0:  0
 *  0 do 1:  0-2-1
 *  0 do 2:  0-2
 *  0 do 3:  0-2-3
 *  0 do 4:  0-2-3-4
 *  0 do 5:  0-2-3-5
 *
 *************************************************************************/

public class DepthFirstPaths {
    private boolean[] marked;    // marked[v] = czy istnieje œcie¿ka s-v?
    private int[] edgeTo;        // edgeTo[v] = ostatnia krawêdŸ œcie¿ki s-v
    private final int s;         // Wierzcho³ek Ÿród³owy

    public DepthFirstPaths(Graph G, int s) {
        this.s = s;
        edgeTo = new int[G.V()];
        marked = new boolean[G.V()];
        dfs(G, s);
    }

    // Wyszukiwanie w g³¹b, pocz¹wszy od wierzcho³ka v
    private void dfs(Graph G, int v) {
        marked[v] = true;
        for (int w : G.adj(v)) {
            if (!marked[w]) {
                edgeTo[w] = v;
                dfs(G, w);
            }
        }
    }

    // Czy istnieje œcie¿ka miêdzy s a v?
    public boolean hasPathTo(int v) {
        return marked[v];
    }

    // Zwracanie œcie¿ki miêdzy s a v (lub null, jeœli œcie¿ka nie istnieje)
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
        Graph G = new Graph(in);
        // StdOut.println(G);

        int s = Integer.parseInt(args[1]);
        DepthFirstPaths dfs = new DepthFirstPaths(G, s);

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
