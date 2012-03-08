/*************************************************************************
 *  Kompilacja:  javac TrieST.java
 *  Wykonanie:    java TrieST < words.txt
 *  Zale�no�ci: StdIn.java
 *
 *  Tablica symboli na �a�cuchy znak�w ASCII, zaimplementowana za pomoc� 256-kierunkowego drzewa trie.
 *
 *  % java TrieST < shellsST.txt 
 *  by 4
 *  sea 6
 *  sells 1
 *  she 0
 *  shells 3
 *  shore 7
 *  the 5
 *
 *************************************************************************/

public class TrieST<Value> {
    private static final int R = 256;        // Rozszerzony zbi�r ASCII

    private Node root;

    private static class Node {
        private Object val;
        private Node[] next = new Node[R];
    }

   /****************************************************
    * Czy klucz znajduje si� w tablicy symboli?
    ****************************************************/
    public boolean contains(String key) {
        return get(key) != null;
    }

    public Value get(String key) {
        Node x = get(root, key, 0);
        if (x == null) return null;
        return (Value) x.val;
    }

    private Node get(Node x, String key, int d) {
        if (x == null) return null;
        if (d == key.length()) return x;
        char c = key.charAt(d);
        return get(x.next[c], key, d+1);
    }

   /****************************************************
    * Wstawianie pary klucz-warto�� do tablicy symboli.
    ****************************************************/
    public void put(String key, Value val) {
        root = put(root, key, val, 0);
    }

    private Node put(Node x, String key, Value val, int d) {
        if (x == null) x = new Node();
        if (d == key.length()) {
            x.val = val;
            return x;
        }
        char c = key.charAt(d);
        x.next[c] = put(x.next[c], key, val, d+1);
        return x;
    }

    // Znajdowanie klucza, kt�ry jest najd�u�szym przedrostkiem s
    public String longestPrefixOf(String query) {
        int length = longestPrefixOf(root, query, 0, 0);
        return query.substring(0, length);
    }

    // Znajdowanie w poddrzewie o korzeniu w x klucza, kt�ry jest najd�u�szym
    // przedrostkiem �a�cucha z zapytania i zaczyna si� od d-tego znaku
    private int longestPrefixOf(Node x, String query, int d, int length) {
        if (x == null) return length;
        if (x.val != null) length = d;
        if (d == query.length()) return length;
        char c = query.charAt(d);
        return longestPrefixOf(x.next[c], query, d+1, length);
    }


    public Iterable<String> keys() {
        return keysWithPrefix("");
    }

    public Iterable<String> keysWithPrefix(String prefix) {
        Queue<String> queue = new Queue<String>();
        Node x = get(root, prefix, 0);
        collect(x, prefix, queue);
        return queue;
    }

    private void collect(Node x, String key, Queue<String> queue) {
        if (x == null) return;
        if (x.val != null) queue.enqueue(key);
        for (int c = 0; c < R; c++)
            collect(x.next[c], key + (char) c, queue);
    }

    public Iterable<String> keysThatMatch(String pat) {
        Queue<String> q = new Queue<String>();
        collect(root, "", pat, q);
        return q;
    }
 
    public void collect(Node x, String prefix, String pat, Queue<String> q) {
        if (x == null) return;
        if (prefix.length() == pat.length() && x.val != null) q.enqueue(prefix);
        if (prefix.length() == pat.length()) return;
        char next = pat.charAt(prefix.length());
        for (int c = 0; c < R; c++)
            if (next == '.' || next == c)
                collect(x.next[c], prefix + (char) c, pat, q);
    }

    public void delete(String key) {
        root = delete(root, key, 0);
    }

    private Node delete(Node x, String key, int d) {
        if (x == null) return null;
        if (d == key.length()) x.val = null;
        else {
            char c = key.charAt(d);
            x.next[c] = delete(x.next[c], key, d+1);
        }
        if (x.val != null) return x;
        for (int c = 0; c < R; c++)
            if (x.next[c] != null)
                return x;
        return null;
    }


    // Klient testowy
    public static void main(String[] args) {

        // Tworzenie tablicy symboli na podstawie standardowego wej�cia
        TrieST<Integer> st = new TrieST<Integer>();
        for (int i = 0; !StdIn.isEmpty(); i++) {
            String key = StdIn.readString();
            st.put(key, i);
        }

        // Wy�wietlanie wynik�w
        for (String key : st.keys()) {
            StdOut.println(key + " " + st.get(key));
        }
    }
}