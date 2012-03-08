/*************************************************************************
 *  Kompilacja:  javac EdgeWeightedDigraph.java
 *  Wykonanie:    java EdgeWeightedDigraph V E
 *  Zale¿noœci: Bag.java DirectedEdge.java
 *
 *  Digraf wa¿ony, zaimplementowany za pomoc¹ list s¹siedztwa.
 *
 *************************************************************************/

/**
 *  Klasa <tt>EdgeWeightedDigraph</tt> reprezentuje graf skierowany z wierzcho³kami
 *  o nazwach od 0 do V-1, gdzie ka¿da krawêdŸ ma wagê w postaci liczby rzeczywistej.
 *  Obs³uguje nastêpuj¹ce operacje: dodawanie krawêdzi do grafu,
 *  przechodzenie po wszystkich krawêdziach wychodz¹cych z wierzcho³ka.
 *  Krawêdzie równoleg³e i pêtle zwrotne s¹ dozwolone.
 *  <p>
 *  Dodatkow¹ dokumentacjê znajdziesz w <a href="http://algs4.cs.princeton.edu/44sp">podrozdziale 4.4</a> ksi¹¿ki
 *  <i>Algorytmy, wydanie czwarte</i> Roberta Sedgewicka i Kevina Wayne'a.
 */



public class EdgeWeightedDigraph {
    private final int V;
    private int E;
    private Bag<DirectedEdge>[] adj;
    
    /**
     * Tworzy pusty digraf wa¿ony o V wierzcho³kach.
     */
    public EdgeWeightedDigraph(int V) {
        if (V < 0) throw new RuntimeException("Liczba wierzcho³ków musi byæ nieujemna");
        this.V = V;
        this.E = 0;
        adj = (Bag<DirectedEdge>[]) new Bag[V];
        for (int v = 0; v < V; v++)
            adj[v] = new Bag<DirectedEdge>();
    }

   /**
     * Tworzy digraf wa¿ony o V wierzcho³kach i E krawêdziach.
     */
    public EdgeWeightedDigraph(int V, int E) {
        this(V);
        if (E < 0) throw new RuntimeException("Liczba wierzcho³ków musi byæ nieujemna");
        for (int i = 0; i < E; i++) {
            int v = (int) (Math.random() * V);
            int w = (int) (Math.random() * V);
            double weight = Math.round(100 * Math.random()) / 100.0;
            DirectedEdge e = new DirectedEdge(v, w, weight);
            addEdge(e);
        }
    }

    /**
     * Tworzy digraf wa¿ony na podstawie strumienia wejœciowego.
     */
    public EdgeWeightedDigraph(In in) {
        this(in.readInt());
        int E = in.readInt();
        for (int i = 0; i < E; i++) {
            int v = in.readInt();
            int w = in.readInt();
            double weight = in.readDouble();
            addEdge(new DirectedEdge(v, w, weight));
        }
    }


   /**
     * Zwraca liczbê wierzcho³ków w digrafie.
     */
    public int V() {
        return V;
    }

   /**
     * Zwraca liczbê krawêdzi w digrafie.
     */
    public int E() {
        return E;
    }


   /**
     * Dodaje krawêdŸ e do digrafu.
     */
    public void addEdge(DirectedEdge e) {
        int v = e.from();
        adj[v].add(e);
        E++;
    }


   /**
     * Zwraca wychodz¹ce z wierzcho³ka v krawêdzie jako obiekt Iterable.
     * Aby przejœæ po krawêdziach wychodz¹cych z wierzcho³ka v, u¿yj zapisu foreach:
     * <tt>for (DirectedEdge e : graph.adj(v))</tt>.
     */
    public Iterable<DirectedEdge> adj(int v) {
        return adj[v];
    }

   /**
     * Zwraca wszystkie krawêdzie grafu jako obiekt Iterable.
     * Aby przejœæ po krawêdziach, u¿yj zapisu foreach:
     * <tt>for (DirectedEdge e : graph.edges())</tt>.
     */
    public Iterable<DirectedEdge> edges() {
        Bag<DirectedEdge> list = new Bag<DirectedEdge>();
        for (int v = 0; v < V; v++) {
            for (DirectedEdge e : adj(v)) {
                list.add(e);
            }
        }
        return list;
    } 

   /**
     * Zwraca liczbê krawêdzi wychodz¹cych z v.
     */
    public int outdegree(int v) {
        return adj[v].size();
    }



   /**
     * Zwraca reprezentacjê ³añcucha znaków dla grafu.
     */
    public String toString() {
        String NEWLINE = System.getProperty("line.separator");
        StringBuilder s = new StringBuilder();
        s.append(V + " " + E + NEWLINE);
        for (int v = 0; v < V; v++) {
            s.append(v + ": ");
            for (DirectedEdge e : adj[v]) {
                s.append(e + "  ");
            }
            s.append(NEWLINE);
        }
        return s.toString();
    }

    /**
     * Klient testowy.
     */
    public static void main(String[] args) {
        int V = Integer.parseInt(args[0]);
        int E = Integer.parseInt(args[1]);
        EdgeWeightedDigraph G = new EdgeWeightedDigraph(V, E);
        StdOut.println(G);
    }

}
