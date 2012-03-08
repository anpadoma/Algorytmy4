/*************************************************************************
 *  Kompilacja:  javac SymbolGraph.java
 *  Wykonanie:    java SymbolGraph
 *  Zale�no�ci: ST.java Graph.java In.java
 *  
 *  %  java SymbolGraph routes.txt " "
 *  JFK
 *     MCO
 *     ATL
 *     ORD
 *  LAX
 *     PHX
 *     LAS
 *
 *  % java SymbolGraph movies.txt "/"
 *  Tin Men (1987)
 *     DeBoy, David
 *     Blumenfeld, Alan
 *     ...
 *     Geppi, Cindy
 *     Hershey, Barbara ...
 *  Bacon, Kevin
 *     Mystic River (2003)
 *     Friday the 13th (1980)
 *     Flatliners (1990)
 *     Few Good Men, A (1992)
 *     ...
 *
 *************************************************************************/

public class SymbolGraph {
    private ST<String, Integer> st;  // �a�cuch znak�w -> indeks
    private String[] keys;           // Indeks  -> �a�cuch znak�w
    private Graph G;

    public SymbolGraph(String filename, String delimiter) {
        st = new ST<String, Integer>();

        // W pierwszy przebiegu tworzony jest indeks przez wczytanie �a�cuch�w znak�w w celu
        // powi�zania r�nych �a�cuch�w z indeksem
        In in = new In(filename);
        while (in.hasNextLine()) {
            String[] a = in.readLine().split(delimiter);
            for (int i = 0; i < a.length; i++) {
                if (!st.contains(a[i]))
                    st.put(a[i], st.size());
            }
        }

        // Odwr�cony indeks do pobierania kluczy w postaci �a�cuch�w znak�w
        keys = new String[st.size()];
        for (String name : st.keys()) {
            keys[st.get(name)] = name;
        }

        // W drugim przebiegu tworzony jest graf przez po��czenie pierwszego 
        // wierzcho�ka z ka�dego wiersza z wszystkimi pozosta�ymi wierzcho�kami
        G = new Graph(st.size());
        in = new In(filename);
        while (in.hasNextLine()) {
            String[] a = in.readLine().split(delimiter);
            int v = st.get(a[0]);
            for (int i = 1; i < a.length; i++) {
                int w = st.get(a[i]);
                G.addEdge(v, w);
            }
        }
    }

    public boolean contains(String s) {
        return st.contains(s);
    }

    public int index(String s) {
        return st.get(s);
    }

    public String name(int v) {
        return keys[v];
    }

    public Graph G() {
        return G;
    }


    public static void main(String[] args) {
        String filename  = args[0];
        String delimiter = args[1];
        SymbolGraph sg = new SymbolGraph(filename, delimiter);
        Graph G = sg.G();
        while (!StdIn.isEmpty()) {
            String t = StdIn.readLine();
            for (int v : G.adj(sg.index(t))) {
                StdOut.println("   " + sg.name(v));
            }
        }
    }
}
