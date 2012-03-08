/*************************************************************************
 *  Kompilacja:  javac Digraph.java
 *  Wykonanie:    java Digraph V E
 *  Zale¿noœci: Bag.java
 *
 *  Graf zaimplementowany za pomoc¹ tablicy list.
 *  Krawêdzie równoleg³e i pêtle zwrotne s¹ dozwolone
 *  
 *************************************************************************/



/**
 *  Klasa <tt>Digraph</tt> reprezentuje graf skierowany wierzcho³ków o nazwach
 *  od 0 do V-1.
 *  Udostêpnia nastêpuj¹ce operacje - dodawanie krawêdzi do grafu,
 *  przechodzenie iterate over all of the neighbors incident to a vertex.
 *  Parallel edges and self-loops are permitted.
 *  <p>
 *  For additional documentation,
 *  see <a href="http://algs4.cs.princeton.edu/52directed">Section 5.2</a> of
 *  <i>Algorithms, 4th Edition</i> by Robert Sedgewick and Kevin Wayne.
 */

public class Digraph {
    private final int V;
    private int E;
    private Bag<Integer>[] adj;
    
   /**
     * Create an empty digraph with V vertices.
     */
    public Digraph(int V) {
        if (V < 0) throw new RuntimeException("Number of vertices must be nonnegative");
        this.V = V;
        this.E = 0;
        adj = (Bag<Integer>[]) new Bag[V];
        for (int v = 0; v < V; v++) {
            adj[v] = new Bag<Integer>();
        }
    }

   /**
     * Create a random digraph with V vertices and E edges.
     */
    public Digraph(int V, int E) {
        this(V);
        if (E < 0) throw new RuntimeException("Number of edges must be nonnegative");
        for (int i = 0; i < E; i++) {
            int v = (int) (Math.random() * V);
            int w = (int) (Math.random() * V);
            addEdge(v, w);
        }
    }

   /**
     * Create a digraph from input stream.
     */  
    public Digraph(In in) {
        this(in.readInt()); 
        int E = in.readInt();
        for (int i = 0; i < E; i++) {
            int v = in.readInt();
            int w = in.readInt();
            addEdge(v, w); 
        }
    }
        


   /**
     * Return the number of vertices in the digraph.
     */
    public int V() {
        return V;
    }

   /**
     * Return the number of edges in the digraph.
     */
    public int E() {
        return E;
    }

   /**
     * Add the directed edge v-w to the digraph.
     */
    public void addEdge(int v, int w) {
        adj[v].add(w);
        E++;
    }

   /**
     * Return the list of neighbors of vertex v as in Iterable.
     */
    public Iterable<Integer> adj(int v) {
        return adj[v];
    }

   /**
     * Return the reverse of the digraph.
     */
    public Digraph reverse() {
        Digraph R = new Digraph(V);
        for (int v = 0; v < V; v++) {
            for (int w : adj(v)) {
                R.addEdge(w, v);
            }
        }
        return R;
    }

   /**
     * Return a string representation of the digraph.
     */
    public String toString() {
        StringBuilder s = new StringBuilder();
        String NEWLINE = System.getProperty("line.separator");
        s.append(V + " " + E + NEWLINE);
        for (int v = 0; v < V; v++) {
            s.append(v + ": ");
            for (int w : adj[v]) {
                s.append(w + " ");
            }
            s.append(NEWLINE);
        }
        return s.toString();
    }


   /**
     * A test client.
     */
    public static void main(String[] args) {
        int V = Integer.parseInt(args[0]);
        int E = Integer.parseInt(args[1]);
        Digraph G = new Digraph(V, E);
        StdOut.println("G");
        StdOut.println("------------");
        StdOut.println(G);
        StdOut.println();
        StdOut.println("Reverse of G");
        StdOut.println("------------");
        StdOut.println(G.reverse());
    }

}
