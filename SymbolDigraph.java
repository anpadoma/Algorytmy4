/*************************************************************************
 *  Kompilacja:  javac SymbolDigraph.java
 *  Wykonanie:    java SymbolDigraph
 *  Zale¿noœci: ST.java Digraph.java In.java
 *  
 *  %  java SymbolDigraph routes.txt " "
 *  JFK
 *     MCO
 *     ATL
 *     ORD
 *  ATL
 *     HOU
 *     MCO
 *  LAX
 *
 *************************************************************************/

public class SymbolDigraph {
    private ST<String, Integer> st;  // £añcuch znaków -> indeks
    private String[] keys;           // Indeks  -> ³añcuch znaków
    private Digraph G;

    public SymbolDigraph(String filename, String delimiter) {
        st = new ST<String, Integer>();

        // Pierwszy przebieg tworzy indeks przez wczytanie ³añcuchów znaków w celu
        // powi¹zania ró¿nych ³añcuchów z indeksami
        In in = new In(filename);
        while (in.hasNextLine()) {
            String[] a = in.readLine().split(delimiter);
            for (int i = 0; i < a.length; i++) {
                if (!st.contains(a[i]))
                    st.put(a[i], st.size());
            }
        }

        // Odwrócony indeks do pobierania kluczy w postaci ³añcuchów znaków 		
        keys = new String[st.size()];
        for (String name : st.keys()) {
            keys[st.get(name)] = name;
        }

        // W drugim przebiegu budowany jest digraf przez ³¹czenie pierwszego wierzcho³ka
        // z ka¿dego wiersza z pozosta³ymi wierzcho³kami z tego wiersza
        G = new Digraph(st.size());
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

    public Digraph G() {
        return G;
    }


    public static void main(String[] args) {
        String filename  = args[0];
        String delimiter = args[1];
        SymbolDigraph sg = new SymbolDigraph(filename, delimiter);
        Digraph G = sg.G();
        while (!StdIn.isEmpty()) {
            String t = StdIn.readLine();
            for (int v : G.adj(sg.index(t))) {
                StdOut.println("   " + sg.name(v));
            }
        }
    }
}
