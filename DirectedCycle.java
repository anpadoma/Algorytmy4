/*************************************************************************
 *  Kompilacja:  javac DirectedCycle.java
 *  Wykonanie:    java DirectedCycle < input.txt
 *  Zale�no�ci: Digraph.java Stack.java
 *  Pliki z danymi:   http://algs.cs.princeton.edu/42digraph/tinyDG.txt
 *                http://algs.cs.princeton.edu/42digraph/tinyDAG.txt
 *
 *  Wyszukuje cykl skierowany w digrafie.
 *  Z�o�ono�� O(E + V).
 *
 *  % java DirectedCycle tinyDG.txt 
 *  Cykl: 3 5 4 3 
 *
 *  %  java DirectedCycle tinyDAG.txt 
 *  Brak cyklu
 *
 *************************************************************************/

public class DirectedCycle {
    private boolean[] marked;        // marked[v] = czy oznaczono wierzcho�ek v?
    private int[] edgeTo;            // edgeTo[v] = poprzedni wierzcho�ek na �cie�ce do v
    private boolean[] onStack;       // onStack[v] = czy wierzcho�ek znajduje si� na stosie?
    private Stack<Integer> cycle;    // Cykl skierowany (lub null, je�li cykl nie istnieje)

    public DirectedCycle(Digraph G) {
        marked  = new boolean[G.V()];
        onStack = new boolean[G.V()];
        edgeTo  = new int[G.V()];
        for (int v = 0; v < G.V(); v++)
            if (!marked[v]) dfs(G, v);

        // Sprawdzanie, czy digraf obejmuje cykl
        assert check(G);
    }

    // Sprawdzanie, czy algorytm wyznacza porz�dek topologiczny lub znajduje cykl skierowany
    private void dfs(Digraph G, int v) {
        onStack[v] = true;
        marked[v] = true;
        for (int w : G.adj(v)) {

            // Skr�cenie p�tli, je�li znaleziono cykl skierowany
            if (cycle != null) return;

            // Znaleziono nowy wierzcho�ek, dlatego nale�y rekurencyjnie wywo�a� metod�
            else if (!marked[w]) {
                edgeTo[w] = v;
                dfs(G, w);
            }

            // Odtwarzanie cyklu skierowanego
            else if (onStack[w]) {
                cycle = new Stack<Integer>();
                for (int x = v; x != w; x = edgeTo[x]) {
                    cycle.push(x);
                }
                cycle.push(w);
                cycle.push(v);
            }
        }

        onStack[v] = false;
    }

    public boolean hasCycle()        { return cycle != null;   }
    public Iterable<Integer> cycle() { return cycle;           }


    // Sprawdzanie, czy digraf jest acykliczny lub obejmuje cykl skierowany
    private boolean check(Digraph G) {

        if (hasCycle()) {
            // Sprawdzanie cyklu
            int first = -1, last = -1;
            for (int v : cycle()) {
                if (first == -1) first = v;
                last = v;
            }
            if (first != last) {
                System.err.printf("Cykl rozpoczyna si� od %d, a ko�czy si� %d\n", first, last);
                return false;
            }
        }


        return true;
    }

    public static void main(String[] args) {
        In in = new In(args[0]);
        Digraph G = new Digraph(in);

        DirectedCycle finder = new DirectedCycle(G);
        if (finder.hasCycle()) {
            StdOut.print("Cykl: ");
            for (int v : finder.cycle()) {
                StdOut.print(v + " ");
            }
            StdOut.println();
        }

        else {
            StdOut.println("Brak cyklu");
        }
    }

}
