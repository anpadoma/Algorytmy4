/*************************************************************************
 *  Kompilacja:  javac TST.java
 *  Wykonanie:    java TST < words.txt
 *  Zale¿noœci: StdIn.java
 *
 *  Tablica symboli z kluczami w postaci ³añcuchów znaków, zaimplementowana za pomoc¹ 
 *  drzewa TST.
 *
 *
 *  % java TST < shellsST.txt
 *  by 4
 *  sea 6
 *  sells 1
 *  she 0
 *  shells 3
 *  shore 7
 *  the 5

 *
 *  % java TST
 *  theory the now is the time for all good men

 *  Uwagi
 *  --------
 *    - nie mo¿na u¿yæ klucza, który jest pustym ³añcuchem znaków ""
 *
 *************************************************************************/

public class TST<Value> {
    private int N;       // Rozmiar
    private Node root;   // Korzeñ drzewa TST

    private class Node {
        private char c;                 // Znak
        private Node left, mid, right;  // Lewe, œrodkowe i prawe poddrzewo
        private Value val;              // Wartoœæ powi¹zana z ³añcuchem znaków
    }

    // Zwraca liczbê par klucz-wartoœæ
    public int size() {
        return N;
    }

   /**************************************************************
    * Czy klucz znajduje siê w tablicy symboli?
    **************************************************************/
    public boolean contains(String key) {
        return get(key) != null;
    }

    public Value get(String key) {
        if (key == null || key.length() == 0) throw new RuntimeException("Niedozwolony klucz");
        Node x = get(root, key, 0);
        if (x == null) return null;
        return (Value) x.val;
    }

    // Zwraca poddrzewo odpowiadaj¹ce danemu kluczowi
    private Node get(Node x, String key, int d) {
        if (key == null || key.length() == 0) throw new RuntimeException("Niedozwolony klucz");
        if (x == null) return null;
        char c = key.charAt(d);
        if      (c < x.c)              return get(x.left,  key, d);
        else if (c > x.c)              return get(x.right, key, d);
        else if (d < key.length() - 1) return get(x.mid,   key, d+1);
        else                           return x;
    }


   /**************************************************************
    * Wstawianie ³añcucha znaków s do tablicy symboli.
    **************************************************************/
    public void put(String s, Value val) {
        if (!contains(s)) N++;
        root = put(root, s, val, 0);
    }

    private Node put(Node x, String s, Value val, int d) {
        char c = s.charAt(d);
        if (x == null) {
            x = new Node();
            x.c = c;
        }
        if      (c < x.c)             x.left  = put(x.left,  s, val, d);
        else if (c > x.c)             x.right = put(x.right, s, val, d);
        else if (d < s.length() - 1)  x.mid   = put(x.mid,   s, val, d+1);
        else                          x.val   = val;
        return x;
    }


   /**************************************************************
    * Znajdowanie i zwracanie najd³u¿szego przedrostka s w drzewie TST
    **************************************************************/
    public String longestPrefixOf(String s) {
        if (s == null || s.length() == 0) return null;
        int length = 0;
        Node x = root;
        int i = 0;
        while (x != null && i < s.length()) {
            char c = s.charAt(i);
            if      (c < x.c) x = x.left;
            else if (c > x.c) x = x.right;
            else {
                i++;
                if (x.val != null) length = i;
                x = x.mid;
            }
        }
        return s.substring(0, length);
    }

    // Wszystkie klucze z tablicy symboli
    public Iterable<String> keys() {
        Queue<String> queue = new Queue<String>();
        collect(root, "", queue);
        return queue;
    }

    // Wszystkie klucze zaczynaj¹ce siê od danego przedrostka
    public Iterable<String> prefixMatch(String prefix) {
        Queue<String> queue = new Queue<String>();
        Node x = get(root, prefix, 0);
        if (x == null) return queue;
        if (x.val != null) queue.enqueue(prefix);
        collect(x.mid, prefix, queue);
        return queue;
    }

    // Wszystkie klucze o danym przedrostku z poddrzewa o korzeniu w x
    private void collect(Node x, String prefix, Queue<String> queue) {
        if (x == null) return;
        collect(x.left,  prefix,       queue);
        if (x.val != null) queue.enqueue(prefix + x.c);
        collect(x.mid,   prefix + x.c, queue);
        collect(x.right, prefix,       queue);
    }


    // Zwracanie wszystkich kluczy pasuj¹cych do wzorca z symbolami wieloznacznymi
    public Iterable<String> wildcardMatch(String pat) {
        Queue<String> queue = new Queue<String>();
        collect(root, "", 0, pat, queue);
        return queue;
    }
 
    public void collect(Node x, String prefix, int i, String pat, Queue<String> q) {
        if (x == null) return;
        char c = pat.charAt(i);
        if (c == '.' || c < x.c) collect(x.left, prefix, i, pat, q);
        if (c == '.' || c == x.c) {
            if (i == pat.length() - 1 && x.val != null) q.enqueue(prefix + x.c);
            if (i < pat.length() - 1) collect(x.mid, prefix + x.c, i+1, pat, q);
        }
        if (c == '.' || c > x.c) collect(x.right, prefix, i, pat, q);
    }



    // Klient testowy
    public static void main(String[] args) {
        // Tworzenie tablicy symboli na podstawie standardowego wejœcia
        TST<Integer> st = new TST<Integer>();
        for (int i = 0; !StdIn.isEmpty(); i++) {
            String key = StdIn.readString();
            st.put(key, i);
        }


        // Wyœwietlanie efektów
        for (String key : st.keys()) {
            StdOut.println(key + " " + st.get(key));
        }
    }
}
