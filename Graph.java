/*************************************************************************
 *  Kompilacja:  javac Graph.java        
 *  Wykonanie:    java Graph input.txt
 *  Zale¿noœci: Bag.java
 *
 *  Graf zaimplementowany za pomoc¹ tablicy zbiorów.
 *  Krawêdzie równoleg³e i pêtle zwrotne s¹ dozwolone.
 *
 *  % java Graph tinyG.txt
 *  13 vertices, 13 edges 
 *  0: 6 2 1 5 
 *  1: 0 
 *  2: 0 
 *  3: 5 4 
 *  4: 5 6 3 
 *  5: 3 4 0 
 *  6: 0 4 
 *  7: 8 
 *  8: 7 
 *  9: 11 10 12 
 *  10: 9 
 *  11: 9 12 
 *  12: 11 9 
 *
 *  % java Graph mediumG.txt
 *  250 vertices, 1273 edges 
 *  0: 225 222 211 209 204 202 191 176 163 160 149 114 97 80 68 59 58 49 44 24 15 
 *  1: 220 203 200 194 189 164 150 130 107 72 
 *  2: 141 110 108 86 79 51 42 18 14 
 *  ...
 *  
 *************************************************************************/


/**
 *  Klasa <tt>Graph</tt> reprezentuje nieskierowany graf wierzcho³ków o nazwach
 *  od 0 do V-1.
 *  Obs³uguje nastêpuj¹ce operacje: dodawanie krawêdzi do grafu,
 *  przechodzenie po wszystkich s¹siadach wierzcho³ka.
 *  Krawêdzie równoleg³e i pêtle zwrotne s¹ dozwolone.
 *  <p>
 *  Dodatkow¹ dokumentacjê mo¿na znaleŸæ w <a href="http://algs4.cs.princeton.edu/51undirected">podrozdziale 5.1</a> ksi¹¿ki
 *  <i>Algorytmy, wydanie czwarte</i> Roberta Sedgewicka i Kevina Wayne'a.
 */
public class Graph {
    private final int V;
    private int E;
    private Bag<Integer>[] adj;
    
   /**
     * Tworzy pusty graf o V wierzcho³kach.
     */
    public Graph(int V) {
        if (V < 0) throw new RuntimeException("Liczba wierzcho³ków musi byæ nieujemna");
        this.V = V;
        this.E = 0;
        adj = (Bag<Integer>[]) new Bag[V];
        for (int v = 0; v < V; v++) {
            adj[v] = new Bag<Integer>();
        }
    }

   /**
     * Tworzy losowy graf o V wierzcho³kach i E krawêdziach.
     * Oczekiwany czas wykonania jest proporcjonalny do V + E.
     */
    public Graph(int V, int E) {
        this(V);
        if (E < 0) throw new RuntimeException("Liczba krawêdzi musi byæ nieujemna");
        for (int i = 0; i < E; i++) {
            int v = (int) (Math.random() * V);
            int w = (int) (Math.random() * V);
            addEdge(v, w);
        }
    }

   /**
     * Tworzy graf nieskierowany na podstawie strumienia wejœciowego, u¿ywaj¹c odstêpu jako ogranicznika.
     */
    public Graph(In in) {
        this(in.readInt());
        int E = in.readInt();
        String discard = in.readLine();
        while (!in.isEmpty()) {
            String line = in.readLine().trim();
            String[] list = line.split("\\s+");
            int v = Integer.parseInt(list[0]);
            for (int j = 1; j < list.length; j++) {
                int w = Integer.parseInt(list[j]);
                addEdge(v, w);
            }
        }
    }

   /**
     * Zwraca liczbê wierzcho³ków grafu.
     */
    public int V() { return V; }

   /**
     * Zwraca liczbê krawêdzi grafu.
     */
    public int E() { return E; }


   /**
     * Dodaje do grafu krawêdŸ v-w.
     */
    public void addEdge(int v, int w) {
        E++;
        adj[v].add(w);
        adj[w].add(v);
    }


   /**
     * Zwraca obiekt Iterable z list¹ s¹siadów wierzcho³ka v.
     */
    public Iterable<Integer> adj(int v) {
        return adj[v];
    }


   /**
     * Zwraca ³añcuch znaków reprezentuj¹cy graf.
     */
    public String toString() {
        StringBuilder s = new StringBuilder();
        String NEWLINE = System.getProperty("line.separator");
        s.append("wierzcho³ki: " + V + "; krawêdzie: " + E + NEWLINE);
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
     * Klient testowy
     */
    public static void main(String[] args) {
        In in = new In(args[0]);
        Graph G = new Graph(in);
        StdOut.println(G);
    }
}
