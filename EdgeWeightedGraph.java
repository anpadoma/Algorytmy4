/*************************************************************************
 *  Kompilacja:  javac EdgeWeightedGraph.java
 *  Wykonanie:    java EdgeWeightedGraph V E
 *  Zale�no�ci: Bag.java Edge.java
 *
 *  Nieskierowany graf wa�ony zaimplementowany za pomoc� list s�siedztwa.
 *  Kraw�dzie r�wnoleg�e i p�tle zwrotne s� dozwolone
 *
 *************************************************************************/

/**
 *  Klasa <tt>EdgeWeightedGraph</tt> reprezentuje nieskierowany graf wierzcho�k�w o nazwach
 *  od 0 do V-1, gdzie ka�da kraw�d� ma wag� o warto�ci rzeczywistej.
 *  Obs�uguje nast�puj�ce operacje: dodawanie kraw�dzi do grafu,
 *  przechodzenie po wszystkich s�siadach wierzcho�ka.
 *  Kraw�dzie r�wnoleg�e i p�tle zwrotne s� dozwolone.
 *  <p>
 *  Dodatkow� dokumentacj� mo�na znale�� w <a href="http://algs4.cs.princeton.edu/43mst">podrozdziale 4.3</a> ksi��ki
 *  <i>Algorytmy, wydanie czwarte</i> Roberta Sedgewicka i Kevina Wayne'a.
 */


public class EdgeWeightedGraph {
    private final int V;
    private int E;
    private Bag<Edge>[] adj;
    
   /**
     * Tworzenie pustego grafu wa�onego o V wierzcho�kach.
     */
    public EdgeWeightedGraph(int V) {
        if (V < 0) throw new RuntimeException("Liczba wierzcholkow musi byc nieujemna");
        this.V = V;
        this.E = 0;
        adj = (Bag<Edge>[]) new Bag[V];
        for (int v = 0; v < V; v++) adj[v] = new Bag<Edge>();
    }

   /**
     * Tworzenie losowego grafu wa�onego o V wierzcho�kach i E kraw�dziach.
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
     * Tworzenie grafu wa�onego na podstawie strumienia wej�ciowego.
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
     * Zwraca liczb� wierzcho�k�w w grafie.
     */
    public int V() {
        return V;
    }

   /**
     * Zwracanie liczby kraw�dzi w grafie.
     */
    public int E() {
        return E;
    }


   /**
     * Dodawanie kraw�dzi e do grafu.
     */
    public void addEdge(Edge e) {
        int v = e.either();
        int w = e.other(v);
        adj[v].add(e);
        adj[w].add(e);
        E++;
    }


   /**
     * Zwracanie kraw�dzi incydentnych do wierzcho�ka v jako obiektu Iterable.
     * Aby przej�� po kraw�dziach incydentnych do wierzcho�ka v nale�y u�y� zapisu foreach:
     * <tt>for (Edge e : graph.adj(v))</tt>.
     */
    public Iterable<Edge> adj(int v) {
        return adj[v];
    }

   /**
     * Zwracanie wszystkich kraw�dzi grafu jako obiektu Iterable.
     * Aby przej�� po kraw�dziach, nale�y u�y� zapisu foreach:
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
                // Dodawanie tylko jednej kopii ka�dej p�tli zwrotnej
                else if (e.other(v) == v) {
                    if (selfLoops % 2 == 0) list.add(e);
                    selfLoops++;
                }
            }
        }
        return list;
    }



   /**
     * Zwracanie �a�cucha znak�w reprezentuj�cego dany graf
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
            // Wczytywanie grafu ze standardowego wej�cia
            G = new EdgeWeightedGraph(new In());
        }
        else {
            // Losowy graf o V wierzcho�kach i E kraw�dziach z dozwolonymi kraw�dziami r�wnoleg�ymi
            int V = Integer.parseInt(args[0]);
            int E = Integer.parseInt(args[1]);
            G = new EdgeWeightedGraph(V, E);
        }

        StdOut.println(G);
    }

}
