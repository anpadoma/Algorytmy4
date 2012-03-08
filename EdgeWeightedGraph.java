/*************************************************************************
 *  Kompilacja:  javac EdgeWeightedGraph.java
 *  Wykonanie:    java EdgeWeightedGraph V E
 *  Zale¿noœci: Bag.java Edge.java
 *
 *  Nieskierowany graf wa¿ony zaimplementowany za pomoc¹ list s¹siedztwa.
 *  Krawêdzie równoleg³e i pêtle zwrotne s¹ dozwolone
 *
 *************************************************************************/

/**
 *  Klasa <tt>EdgeWeightedGraph</tt> reprezentuje nieskierowany graf wierzcho³ków o nazwach
 *  od 0 do V-1, gdzie ka¿da krawêdŸ ma wagê o wartoœci rzeczywistej.
 *  Obs³uguje nastêpuj¹ce operacje: dodawanie krawêdzi do grafu,
 *  przechodzenie po wszystkich s¹siadach wierzcho³ka.
 *  Krawêdzie równoleg³e i pêtle zwrotne s¹ dozwolone.
 *  <p>
 *  Dodatkow¹ dokumentacjê mo¿na znaleŸæ w <a href="http://algs4.cs.princeton.edu/43mst">podrozdziale 4.3</a> ksi¹¿ki
 *  <i>Algorytmy, wydanie czwarte</i> Roberta Sedgewicka i Kevina Wayne'a.
 */


public class EdgeWeightedGraph {
    private final int V;
    private int E;
    private Bag<Edge>[] adj;
    
   /**
     * Tworzenie pustego grafu wa¿onego o V wierzcho³kach.
     */
    public EdgeWeightedGraph(int V) {
        if (V < 0) throw new RuntimeException("Liczba wierzcholkow musi byc nieujemna");
        this.V = V;
        this.E = 0;
        adj = (Bag<Edge>[]) new Bag[V];
        for (int v = 0; v < V; v++) adj[v] = new Bag<Edge>();
    }

   /**
     * Tworzenie losowego grafu wa¿onego o V wierzcho³kach i E krawêdziach.
     * Oczekiwany czas wykonania jest proporcjonalny do V + E.
     */
    public EdgeWeightedGraph(int V, int E) {
        this(V);
        if (E < 0) throw new RuntimeException("Liczba krawedzi musi byc nieujemna");
        for (int i = 0; i < E; i++) {
            int v = (int) (Math.random() * V);
            int w = (int) (Math.random() * V);
            double weight = Math.round(100 * Math.random()) / 100.0;
            Edge e = new Edge(v, w, weight);
            addEdge(e);
        }
    }

   /**
     * Tworzenie grafu wa¿onego na podstawie strumienia wejœciowego.
     */
    public EdgeWeightedGraph(In in) {
        this(in.readInt());
        int E = in.readInt();
        for (int i = 0; i < E; i++) {
            int v = in.readInt();
            int w = in.readInt();
            double weight = in.readDouble();
            Edge e = new Edge(v, w, weight);
            addEdge(e);
        }
    }

   /**
     * Zwraca liczbê wierzcho³ków w grafie.
     */
    public int V() {
        return V;
    }

   /**
     * Zwracanie liczby krawêdzi w grafie.
     */
    public int E() {
        return E;
    }


   /**
     * Dodawanie krawêdzi e do grafu.
     */
    public void addEdge(Edge e) {
        int v = e.either();
        int w = e.other(v);
        adj[v].add(e);
        adj[w].add(e);
        E++;
    }


   /**
     * Zwracanie krawêdzi incydentnych do wierzcho³ka v jako obiektu Iterable.
     * Aby przejœæ po krawêdziach incydentnych do wierzcho³ka v nale¿y u¿yæ zapisu foreach:
     * <tt>for (Edge e : graph.adj(v))</tt>.
     */
    public Iterable<Edge> adj(int v) {
        return adj[v];
    }

   /**
     * Zwracanie wszystkich krawêdzi grafu jako obiektu Iterable.
     * Aby przejœæ po krawêdziach, nale¿y u¿yæ zapisu foreach:
     * <tt>for (Edge e : graph.edges())</tt>.
     */
    public Iterable<Edge> edges() {
        Bag<Edge> list = new Bag<Edge>();
        for (int v = 0; v < V; v++) {
            int selfLoops = 0;
            for (Edge e : adj(v)) {
                if (e.other(v) > v) {
                    list.add(e);
                }
                // Dodawanie tylko jednej kopii ka¿dej pêtli zwrotnej
                else if (e.other(v) == v) {
                    if (selfLoops % 2 == 0) list.add(e);
                    selfLoops++;
                }
            }
        }
        return list;
    }



   /**
     * Zwracanie ³añcucha znaków reprezentuj¹cego dany graf
     */
    public String toString() {
        String NEWLINE = System.getProperty("line.separator");
        StringBuilder s = new StringBuilder();
        s.append(V + " " + E + NEWLINE);
        for (int v = 0; v < V; v++) {
            s.append(v + ": ");
            for (Edge e : adj[v]) {
                s.append(e + "  ");
            }
            s.append(NEWLINE);
        }
        return s.toString();
    }

   /**
     * Klient testowy
     */
    public static void main(String[] args) {
        EdgeWeightedGraph G;

        if (args.length == 0) {
            // Wczytywanie grafu ze standardowego wejœcia
            G = new EdgeWeightedGraph(new In());
        }
        else {
            // Losowy graf o V wierzcho³kach i E krawêdziach z dozwolonymi krawêdziami równoleg³ymi
            int V = Integer.parseInt(args[0]);
            int E = Integer.parseInt(args[1]);
            G = new EdgeWeightedGraph(V, E);
        }

        StdOut.println(G);
    }

}
