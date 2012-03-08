/*************************************************************************
 *  Kompilacja:  javac SequentialSearchST.java
 *  Wykonanie:    java SequentialSearchST
 *  
 *  Implementacja tablicy symboli z wyszukiwaniem sekwencyjnym
 *  w powi¹zanej liœcie nieuporz¹dkowanej par klucz-wartoœæ.
 *
 *  % more tinyST.txt
 *  S E A R C H E X A M P L E
 *
 *  % java SequentialSearchST
 *  java SequentialSearchST < tiny.txt 
 *  L 11
 *  P 10
 *  M 9
 *  X 7
 *  H 5
 *  C 4
 *  R 3
 *  A 8
 *  E 12
 *  S 0
 *
 *************************************************************************/

public class SequentialSearchST<Key, Value> {
    private int N;           // Liczba par klucz-wartoœæ
    private Node first;      // Lista powi¹zana par klucz-wartoœæ

    // Pomocniczy typ danych dla listy powi¹zanej
    private class Node {
        private Key key;
        private Value val;
        private Node next;

        public Node(Key key, Value val, Node next)  {
            this.key  = key;
            this.val  = val;
            this.next = next;
        }
    }

    // Zwracanie liczby par klucz-wartoœæ
    public int size() { return N; }

    // Czy tablica symboli jest pusta?
    public boolean isEmpty() { return size() == 0; }

    // Czy tablica symboli obejmuje dany klucz?
    public boolean contains(Key key) {
        return get(key) != null;
    }

    // Zwracanie wartoœci powi¹zanej z kluczem (lub null, jeœli klucz nie istnieje)
    public Value get(Key key) {
        for (Node x = first; x != null; x = x.next) {
            if (key.equals(x.key)) return x.val;
        }
        return null;
    }

    // Dodawanie pary klucz-wartoœæ; jeœli klucz ju¿ istnieje, dawna 
	// para klucz-wartoœæ jest zastêpowana
    public void put(Key key, Value val) {
        if (val == null) { delete(key); return; }
        for (Node x = first; x != null; x = x.next)
            if (key.equals(x.key)) { x.val = val; return; }
        first = new Node(key, val, first);
        N++;
    }

    // Usuwanie pary klucz-wartoœæ z danym kluczem (jeœli ten klucz wystêpuje w tablicy)
    public void delete(Key key) {
        first = delete(first, key);
    }

    // Usuwanie klucza z listy powi¹zanej zaczynaj¹cej siê od wêz³a x
    // Ostrze¿enie: stos wywo³añ jest zbyt du¿y dla d³ugich tablic
    private Node delete(Node x, Key key) {
        if (x == null) return null;
        if (key.equals(x.key)) { N--; return x.next; }
        x.next = delete(x.next, key);
        return x;
    }


    // Zwracanie wszystkich kluczy jako obiektu Iterable
    public Iterable<Key> keys()  {
        Queue<Key> queue = new Queue<Key>();
        for (Node x = first; x != null; x = x.next)
            queue.enqueue(x.key);
        return queue;
    }




   /***********************************************************************
    * Klient testowy
    **********************************************************************/
    public static void main(String[] args) {
        SequentialSearchST<String, Integer> st = new SequentialSearchST<String, Integer>();
        for (int i = 0; !StdIn.isEmpty(); i++) {
            String key = StdIn.readString();
            st.put(key, i);
        }
        for (String s : st.keys())
            StdOut.println(s + " " + st.get(s));
    }
}
