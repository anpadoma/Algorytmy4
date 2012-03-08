/*************************************************************************
 *  Kompilacja:  javac BreadthFirstDirectedPaths.java
 *  Wykonanie:    java BreadthFirstDirectedPaths V E
 *  Zaleønoúci: Digraph.java Queue.java Stack.java
 *
 *  Przeszukiwanie digrafu wszerz.
 *  Dzia≥a w czasie O(E + V).
 *
 *  % java BreadthFirstDirectedPaths tinyDG.txt 3
 *  3 do 0 (2):  3->2->0
 *  3 do 1 (3):  3->2->0->1
 *  3 do 2 (1):  3->2
 *  3 do 3 (0):  3
 *  3 do 4 (2):  3->5->4
 *  3 do 5 (1):  3->5
 *  3 do 6 (-):  niepolaczone
 *  3 do 7 (-):  niepolaczone
 *  3 do 8 (-):  niepolaczone
 *  3 do 9 (-):  niepolaczone
 *  3 do 10 (-):  niepolaczone
 *  3 do 11 (-):  niepolaczone
 *  3 do 12 (-):  niepolaczone
 *
 *************************************************************************/

public class BreadthFirstDirectedPaths {
    private static final int INFINITY = Integer.MAX_VALUE;
    private boolean[] marked;  // marked[v] = czy istnieje úcieøka s->v ?
    private int[] edgeTo;      // edgeTo[v] = ostatnia krawÍdü najkrÛtszej úcieøki s->v.
    private int[] distTo;      // distTo[v] = d≥ugoúÊ najkrÛtszej úcieøki s->v.
    private final int s;       // èrÛd≥o.

    public BreadthFirstDirectedPaths(Digraph G, int s) {
        marked = new boolean[G.V()];
        distTo = new int[G.V()];
        edgeTo = new int[G.V()];
        for (int v = 0; v < G.V(); v++) distTo[v] = INFINITY;
        this.s = s;
        bfs(G, s);
    }

    private void bfs(Digraph G, int s) {
        Queue<Integer> q = new Queue<Integer>();
        marked[s] = true;
        distTo[s] = 0;
        q.enqueue(s);
        while (!q.isEmpty()) {
            int v = q.dequeue();
            for (int w : G.adj(v)) {
                if (!marked[w]) {
                    edgeTo[w] = v;
                    distTo[w] = distTo[v] + 1;
                    marked[w] = true;
                    q.enqueue(w);
                }
            }
        }
    }

    // D≥ugoúÊ najkrÛtszej úcieøki z s do v.
    public int distTo(int v) {
        return distTo[v];
    }

    // Czy istnieje úcieøka skierowana z s do v?
    public boolean hasPathTo(int v) {
        return marked[v];
    }

    // Zwraca najkrÛtszπ úcieøkÍ z s do v (lub null, jeúli taka úcieøka nie istnieje).
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
        BreadthFirstDirectedPaths bfs = new BreadthFirstDirectedPaths(G, s);

        for (int v = 0; v < G.V(); v++) {
            if (bfs.hasPathTo(v)) {
                StdOut.printf("%d do %d (%d):  ", s, v, bfs.distTo(v));
                for (int x : bfs.pathTo(v)) {
                    if (x == s) StdOut.print(x);
                    else        StdOut.print("->" + x);
                }
                StdOut.println();
            }

            else {
                StdOut.printf("%d do %d (-):  niepolaczone\n", s, v);
            }

        }
    }


}
