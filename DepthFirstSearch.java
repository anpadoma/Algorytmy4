/*************************************************************************
 *  Kompilacja:  javac DepthFirstSearch.java
 *  Wykonanie:    java DepthFirstSearch filename s
 *  Zale¿noœci: Graph.java
 *
 *  Wykonuje wyszukiwanie w g³¹b w grafie nieskierowanym.
 *  Z³o¿onoœæ O(E + V).
 *
 *  % java DepthFirstSearch tinyG.txt 0
 *  0 1 2 3 4 5 6 
 *  brak po³¹czenia
 *
 *  % java DepthFirstSearch tinyG.txt 9
 *  9 10 11 12 
 *  brak po³¹czenia
 *
 *************************************************************************/

public class DepthFirstSearch {
    private boolean[] marked;    // marked[v] = czy istnieje œcie¿ka s-v?
    private int count;           // Liczba wierzcho³ków po³¹czonych z s

    public DepthFirstSearch(Graph G, int s) {
        marked = new boolean[G.V()];
        dfs(G, s);
    }

    // Wyszukiwanie w g³¹b, pocz¹wszy od wierzcho³ka v
    private void dfs(Graph G, int v) {
        marked[v] = true;
        for (int w : G.adj(v)) {
            if (!marked[w]) {
                dfs(G, w);
            }
        }
    }

    // Czy istnieje œcie¿ka s-v?
    public boolean marked(int v) {
        return marked[v];
    }

    // Liczba wierzcho³ków po³¹czonych z s
    public int count() {
        return count;
    }

    // Klient testowy
    public static void main(String[] args) {
        In in = new In(args[0]);
        Graph G = new Graph(in);
        int s = Integer.parseInt(args[1]);
        DepthFirstSearch search = new DepthFirstSearch(G, s);
        for (int v = 0; v < G.V(); v++) {
            if (search.marked(v))
                StdOut.print(v + " ");
        }

        StdOut.println();
        if (search.count() != G.V()) StdOut.println("brak polaczenia");
        else                         StdOut.println("polaczone");
    }

}
