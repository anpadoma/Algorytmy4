/*************************************************************************
 *  Kompilacja:  javac Graph.java        
 *  Wykonanie:    java Graph input.txt
 *  Zale�no�ci: Bag.java
 *
 *  Graf zaimplementowany za pomoc� tablicy zbior�w.
 *  Kraw�dzie r�wnoleg�e i p�tle zwrotne s� dozwolone.
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
 *  Klasa <tt>Graph</tt> reprezentuje nieskierowany graf wierzcho�k�w o nazwach
 *  od 0 do V-1.
 *  Obs�uguje nast�puj�ce operacje: dodawanie kraw�dzi do grafu,
 *  przechodzenie po wszystkich s�siadach wierzcho�ka.
 *  Kraw�dzie r�wnoleg�e i p�tle zwrotne s� dozwolone.
 *  <p>
 *  Dodatkow� dokumentacj� mo�na znale�� w <a href="http://algs4.cs.princeton.edu/51undirected">podrozdziale 5.1</a> ksi��ki
 *  <i>Algorytmy, wydanie czwarte</i> Roberta Sedgewicka i Kevina Wayne'a.
 */
public class Graph {
    private final int V;
    private int E;
    private Bag<Integer>[] adj;
    
   /**
     * Tworzy pusty graf o V wierzcho�kach.
     */
    public Graph(int V) {
        if (V < 0) throw new RuntimeException("Liczba wierzcho�k�w musi by� nieujemna");
        this.V = V;
        this.E = 0;
        adj = (Bag<Integer>[]) new Bag[V];
        for (int v = 0; v < V; v++) {
            adj[v] = new Bag<Integer>();
        }
    }

   /**
     * Tworzy losowy graf o V wierzcho�kach i E kraw�dziach.
     * Oczekiwany czas wykonania jest proporcjonalny do V + E.
     */
    public Graph(int V, int E) {
        this(V);
        if (E < 0) throw new RuntimeException("Liczba kraw�dzi musi by� nieujemna");
        for (int i = 0; i < E; i++) {
            int v = (int) (Math.random() * V);
            int w = (int) (Math.random() * V);
            addEdge(v, w);
        }
    }

   /**
     * Tworzy graf nieskierowany na podstawie strumienia wej�ciowego, u�ywaj�c odst�pu jako ogranicznika.
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
     * Zwraca liczb� wierzcho�k�w grafu.
     */
    public int V() { return V; }

   /**
     * Zwraca liczb� kraw�dzi grafu.
     */
    public int E() { return E; }


   /**
     * Dodaje do grafu kraw�d� v-w.
     */
    public void addEdge(int v, int w) {
        E++;
        adj[v].add(w);
        adj[w].add(v);
    }


   /**
     * Zwraca obiekt Iterable z list� s�siad�w wierzcho�ka v.
     */
    public Iterable<Integer> adj(int v) {
        return adj[v];
    }


   /**
     * Zwraca �a�cuch znak�w reprezentuj�cy graf.
     */
    public String toString() {
        StringBuilder s = new StringBuilder();
        String NEWLINE = System.getProperty("line.separator");
        s.append("wierzcho�ki: " + V + "; kraw�dzie: " + E + NEWLINE);
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
