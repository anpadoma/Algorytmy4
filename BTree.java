/*************************************************************************
 *  Kompilacja:  javac BTree.java
 *  Wykonanie:    java BTree
 *
 *  Drzewo zbalansowane.
 *
 *  Ograniczenia
 *  -----------
 *   -  zak�adamy, �e M jest parzystne i M >= 4
 *   -  czy u�y� tablicy dzieci czy listy (zastosowanie listy
 *      pomaga w rzutowaniu)
 *
 *************************************************************************/


public class BTree<Key extends Comparable<Key>, Value>  {
    private static final int M = 4;    // Maksymalna liczba dzieci na w�ze� drzewa zbalansowanego = M-1

    private Node root;             // Korze� drzewa zbalansowanego
    private int HT;                // Wysoko�� drzewa zbalansowanego
    private int N;                 // Liczba par klucz-warto�� w drzewie zbalansowanym

    // Pomocniczy typ danych dla w�z��w drzewa zbalansowanego
    private static class Node {
        private int m;                             // Liczba dzieci
        private Entry[] children = new Entry[M];   // Tablica dzieci
        private Node(int k) { m = k; }             // Tworzy w�ze� o k dzieciach
    }

    // W�z�y wewn�trzne: u�ywane tylko zmienne key i next
    // W�z�y zewn�trzne: u�ywane tylko zmienne key i value
    private static class Entry {
        Comparable key;
        Object value;
        Node next;     // Pole pomocnicze do przechodzenia po elementach tablicy
        Entry(Comparable key, Object value, Node next) {
            this.key   = key;
            this.value = value;
            this.next  = next;
        }
    }

    // Konstruktor
    public BTree() { root = new Node(0); }
 
    // Zwraca liczb� par klucz-warto�� drzewa zbalansowanego
    public int size() { return N; }

    // Zwraca wysoko�� drzewa zbalansowanego
    public int height() { return HT; }


    // Wyszukuje dany klucz i zwraca powi�zan� warto�� associated value (lub null, je�li klucz nie istnieje)
    public Value get(Key key) { return search(root, key, HT); }
    private Value search(Node x, Key key, int ht) {
        Entry[] children = x.children;

        // W�ze� zewn�trzny
        if (ht == 0) {
            for (int j = 0; j < x.m; j++) {
                if (eq(key, children[j].key)) return (Value) children[j].value;
            }
        }

        // W�ze� wewn�trzny
        else {
            for (int j = 0; j < x.m; j++) {
                if (j+1 == x.m || less(key, children[j+1].key))
                    return search(children[j].next, key, ht-1);
            }
        }
        return null;
    }


    // Wstawia par� klucz-warto��. 
    // Nale�y doda� kod do wykrywania powtarzaj�cych si� kluczy
    public void put(Key key, Value value) {
        Node u = insert(root, key, value, HT); 
        N++;
        if (u == null) return;

        // Trzeba podzieli� korze�
        Node t = new Node(2);
        t.children[0] = new Entry(root.children[0].key, null, root);
        t.children[1] = new Entry(u.children[0].key, null, u);
        root = t;
        HT++;
    }


    private Node insert(Node h, Key key, Value value, int ht) {
        int j;
        Entry t = new Entry(key, value, null);

        // W�ze� zewn�trzny
        if (ht == 0) {
            for (j = 0; j < h.m; j++) {
                if (less(key, h.children[j].key)) break;
            }
        }

        // W�ze� wewn�trzny
        else {
            for (j = 0; j < h.m; j++) {
                if ((j+1 == h.m) || less(key, h.children[j+1].key)) {
                    Node u = insert(h.children[j++].next, key, value, ht-1);
                    if (u == null) return null;
                    t.key = u.children[0].key;
                    t.next = u;
                    break;
                }
            }
        }

        for (int i = h.m; i > j; i--) h.children[i] = h.children[i-1];
        h.children[j] = t;
        h.m++;
        if (h.m < M) return null;
        else         return split(h);
    }

    // Podzia� w�z�a na po�ow�
    private Node split(Node h) {
        Node t = new Node(M/2);
        h.m = M/2;
        for (int j = 0; j < M/2; j++)
            t.children[j] = h.children[M/2+j]; 
        return t;    
    }

    // Na potrzeby debugowania
    public String toString() {
        return toString(root, HT, "") + "\n";
    }
    private String toString(Node h, int ht, String indent) {
        String s = "";
        Entry[] children = h.children;

        if (ht == 0) {
            for (int j = 0; j < h.m; j++) {
                s += indent + children[j].key + " " + children[j].value + "\n";
            }
        }
        else {
            for (int j = 0; j < h.m; j++) {
                if (j > 0) s += indent + "(" + children[j].key + ")\n";
                s += toString(children[j].next, ht-1, indent + "     ");
            }
        }
        return s;
    }


    // Funkcje do por�wna� - nale�y u�y� Comparable zamiast Key, aby unikn�� rzutowania
    private boolean less(Comparable k1, Comparable k2) {
        return k1.compareTo(k2) < 0;
    }

    private boolean eq(Comparable k1, Comparable k2) {
        return k1.compareTo(k2) == 0;
    }


   /*************************************************************************
    *  Klient testowy
    *************************************************************************/
    public static void main(String[] args) {
        BTree<String, String> st = new BTree<String, String>();

//      st.put("www.cs.princeton.edu", "128.112.136.12");
        st.put("www.cs.princeton.edu", "128.112.136.11");
        st.put("www.princeton.edu",    "128.112.128.15");
        st.put("www.yale.edu",         "130.132.143.21");
        st.put("www.simpsons.com",     "209.052.165.60");
        st.put("www.apple.com",        "17.112.152.32");
        st.put("www.amazon.com",       "207.171.182.16");
        st.put("www.ebay.com",         "66.135.192.87");
        st.put("www.cnn.com",          "64.236.16.20");
        st.put("www.google.com",       "216.239.41.99");
        st.put("www.nytimes.com",      "199.239.136.200");
        st.put("www.microsoft.com",    "207.126.99.140");
        st.put("www.dell.com",         "143.166.224.230");
        st.put("www.slashdot.org",     "66.35.250.151");
        st.put("www.espn.com",         "199.181.135.201");
        st.put("www.weather.com",      "63.111.66.11");
        st.put("www.yahoo.com",        "216.109.118.65");


        System.out.println("cs.princeton.edu:  " + st.get("www.cs.princeton.edu"));
        System.out.println("hardvardsucks.com: " + st.get("www.harvardsucks.com"));
        System.out.println("simpsons.com:      " + st.get("www.simpsons.com"));
        System.out.println("apple.com:         " + st.get("www.apple.com"));
        System.out.println("ebay.com:          " + st.get("www.ebay.com"));
        System.out.println("dell.com:          " + st.get("www.dell.com"));
        System.out.println();

        System.out.println("wielkosc:    " + st.size());
        System.out.println("wysokosc:    " + st.height());
        System.out.println(st);
        System.out.println();
    }

}
