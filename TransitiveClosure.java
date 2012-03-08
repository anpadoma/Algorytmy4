/*************************************************************************
 *  Kompilacja:  javac TransitiveClosure.java
 *  Wykonanie:    java TransitiveClosure V E
 *  Zale¿noœci: Digraph.java DepthFirstDirectedPaths.java
 *
 *  Wyznacza domkniêcie przechodnie dla digrafu i obs³uguje
 *  zapytania dotycz¹ce osi¹galnoœci.
 *
 *  Z³o¿onoœæ wstêpnego przetwarzania: O(V(E + V)).
 *  Czas obs³ugi zapytania: O(1).
 *  Wymagania pamiêciowe: O(V^2).
 *
 *  % java TransitiveClosure 10 20
 *  V = 10
 *  E = 20
 *  0: 9 2 7 5 
 *  1: 6 7 
 *  2: 
 *  3: 9 9 9 7 6 
 *  4: 7 
 *  5: 7 
 *  6: 7 1 
 *  7: 2 
 *  8: 7 6 0 
 *  9: 2 
 *  
 *  Domkniêcie przechodnie
 *  -----------------------------------
 *         0  1  2  3  4  5  6  7  8  9
 *    0:   x     x        x     x     x
 *    1:      x  x           x  x      
 *    2:         x                     
 *    3:      x  x  x        x  x     x
 *    4:         x     x        x      
 *    5:         x        x     x      
 *    6:      x  x           x  x      
 *    7:         x              x      
 *    8:   x  x  x        x  x  x  x  x
 *    9:         x                    x
 *
 *************************************************************************/

public class TransitiveClosure {
    private DirectedDFS[] tc;  // tc[v] = osi¹galny z v

    public TransitiveClosure(Digraph G) {
        tc = new DirectedDFS[G.V()];
        for (int v = 0; v < G.V(); v++)
            tc[v] = new DirectedDFS(G, v);
    }

    public boolean reachable(int v, int w) {
        return tc[v].marked(w);
    }

    // Klient testowy
    public static void main(String[] args) {
        int V = Integer.parseInt(args[0]);
        int E = Integer.parseInt(args[1]);
        Digraph G = new Digraph(V, E);
        StdOut.println(G);
        TransitiveClosure tc = new TransitiveClosure(G);

        // Wyœwietlanie nag³ówka
        StdOut.println("Domkniêcie przechodnie");
        StdOut.println("-----------------------------------");
        StdOut.print("     ");
        for (int v = 0; v < G.V(); v++)
            StdOut.printf("%3d", v);
        StdOut.println();

        // Wyœwietlanie domkniêcia przechodniego
        for (int v = 0; v < G.V(); v++) {
            StdOut.printf("%3d: ", v);
            for (int w = 0; w < G.V(); w++) {
                if (tc.reachable(v, w)) StdOut.printf("  x");
                else                    StdOut.printf("   ");
            }
            StdOut.println();
        }
    }

}
