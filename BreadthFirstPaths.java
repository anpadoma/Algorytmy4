/*************************************************************************
 *  Kompilacja:  javac BreadthFirstPaths.java
 *  Wykonanie:    java BreadthFirstPaths G s
 *  Zaleønoúci: Graph.java Queue.java Stack.java
 *
 *  Wyszukiwanie wszerz w grafie nieskierowanym.
 *  Z≥oøonoúÊ O(E + V).
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
 *  %  java BreadthFirstPaths tinyCG.txt 0
 *  0 do 0 (0):  0
 *  0 do 1 (1):  0-1
 *  0 do 2 (1):  0-2
 *  0 do 3 (2):  0-2-3
 *  0 do 4 (2):  0-2-4
 *  0 do 5 (1):  0-5
 *************************************************************************/

public class BreadthFirstPaths {
    private static final int INFINITY = Integer.MAX_VALUE;
    private boolean[] marked;  // marked[v] = czy istnieje úcieøka s-v
    private int[] edgeTo;      // edgeTo[v] = poprzednia krawÍdü najkrÛtszej úcieøki s-v
    private int[] distTo;      // distTo[v] = liczba krawÍdzi najkrÛtszej úcieøki s-v
    private final int s;       // èrÛd≥o.

    public BreadthFirstPaths(Graph G, int s) {
        marked = new boolean[G.V()];
        distTo = new int[G.V()];
        edgeTo = new int[G.V()];
        this.s = s;
        bfs(G, s);

        assert check(G);
    }

    private void bfs(Graph G, int s) {
        Queue<Integer> q = new Queue<Integer>();
        for (int v = 0; v < G.V(); v++) distTo[v] = INFINITY;
        distTo[s] = 0;
        marked[s] = true;
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

    public boolean hasPathTo(int v) {
        return marked[v];
    }

    public int distTo(int v) {
        return distTo[v];
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

    // Sprawdza warunki optymalnoúci.
    private boolean check(Graph G) {

        // Sprawdza, czy odleg≥oúÊ s = 0.
        if (distTo[s] != 0) {
            StdOut.println("Odleglosc ze zrodla " + s + " do niego samego = " + distTo[s]);
            return false;
        }

        // Sprawdza, czy dla kaødej krawÍdzi v-w dist[w] <= dist[v] + 1
        // (przy za≥oøeniu, øe v jest osiπgalny z s).
        for (int v = 0; v < G.V(); v++) {
            for (int w : G.adj(v)) {
                if (hasPathTo(v) != hasPathTo(w)) {
                    StdOut.println("krawedz " + v + "-" + w);
                    StdOut.println("hasPathTo(" + v + ") = " + hasPathTo(v));
                    StdOut.println("hasPathTo(" + w + ") = " + hasPathTo(w));
                    return false;
                }
                if (hasPathTo(v) && (distTo[w] > distTo[v] + 1)) {
                    StdOut.println("krawedz " + v + "-" + w);
                    StdOut.println("distTo[" + v + "] = " + distTo[v]);
                    StdOut.println("distTo[" + w + "] = " + distTo[w]);
                    return false;
                }
            }
        }

        // Sprawdza, czy v = edgeTo[w] spe≥nia zaleønoúÊ distTo[w] + distTo[v] + 1
        // (przy za≥oøeniu, øe v jest osiπgalny z s).
        for (int w = 0; w < G.V(); w++) {
            if (!hasPathTo(w) || w == s) continue;
            int v = edgeTo[w];
            if (distTo[w] != distTo[v] + 1) {
                StdOut.println("krawedz najkrotszej scieøki " + v + "-" + w);
                StdOut.println("distTo[" + v + "] = " + distTo[v]);
                StdOut.println("distTo[" + w + "] = " + distTo[w]);
                return false;
            }
        }

        return true;
    }


    // Klient testowy.
    public static void main(String[] args) {
        In in = new In(args[0]);
        Graph G = new Graph(in);
        // StdOut.println(G);

        int s = Integer.parseInt(args[1]);
        BreadthFirstPaths bfs = new BreadthFirstPaths(G, s);

        for (int v = 0; v < G.V(); v++) {
            if (bfs.hasPathTo(v)) {
                StdOut.printf("%d do %d (%d):  ", s, v, bfs.distTo(v));
                for (int x : bfs.pathTo(v)) {
                    if (x == s) StdOut.print(x);
                    else        StdOut.print("-" + x);
                }
                StdOut.println();
            }

            else {
                StdOut.printf("%d do %d (-):  niepolaczone\n", s, v);
            }

        }
    }
}
