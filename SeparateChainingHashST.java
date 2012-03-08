/*************************************************************************
 *  Kompilacja:  javac SeparateChainingHashST.java
 *  Wykonanie:    java SeparateChainingHashST
 *
 *  Tablica symboli zaimplementowana za pomoc¹ tablicy z haszowaniem (metoda ³añcuchowa)
 * 
 *  % java SeparateChainingHashST
 *
 *************************************************************************/

public class SeparateChainingHashST<Key, Value> {

    // Najwiêksze liczby pierwsze <= 2^i dla i = 3 do 31.
    // S³u¿¹ do powiêkszania i zmniejszania.
    // private static final int[] PRIMES = {
    //    7, 13, 31, 61, 127, 251, 509, 1021, 2039, 4093, 8191, 16381,
    //    32749, 65521, 131071, 262139, 524287, 1048573, 2097143, 4194301,
    //    8388593, 16777213, 33554393, 67108859, 134217689, 268435399,
    //    536870909, 1073741789, 2147483647
    // };

    private int N;                                // Liczba par klucz wartoœæ
    private int M;                                // Rozmiar tablicy z haszowaniem
    private SequentialSearchST<Key, Value>[] st;  // Tablica tablic symboli


    // Tworzenie tablicy z haszowaniem (metoda ³añcuchowa)
    public SeparateChainingHashST() {
        this(997);
    } 

    // Tworzenie tablicy z haszowaniem o M listach (metoda ³añcuchowa)
    public SeparateChainingHashST(int M) {
        this.M = M;
        st = (SequentialSearchST<Key, Value>[]) new SequentialSearchST[M];
        for (int i = 0; i < M; i++)
            st[i] = new SequentialSearchST<Key, Value>();
    } 

    // Zmiana wielkoœci tablicy z haszowaniem; nale¿y ponownie rozdzieliæ
	// wszystkie klucze po danej liczbie ³añcuchów
    private void resize(int chains) {
        SeparateChainingHashST<Key, Value> temp = new SeparateChainingHashST<Key, Value>(chains);
        for (int i = 0; i < M; i++) {
            for (Key key : st[i].keys()) {
                temp.put(key, st[i].get(key));
            }
        }
        this.M  = temp.M;
        this.N  = temp.N;
        this.st = temp.st;
    }

    // Watoœæ skrótu pomiêdzy 0 a M-1
    private int hash(Key key) {
        return (key.hashCode() & 0x7fffffff) % M;
    } 

    // Zwracanie liczby par klucz-wartoœæ z tablicy symboli
    public int size() {
        return N;
    } 

    // Czy tablica symboli jest pusta?
    public boolean isEmpty() {
        return size() == 0;
    }

    // Czy klucz znajduje siê w tablicy symboli?
    public boolean contains(Key key) {
        return get(key) != null;
    } 

    // Zwracanie wartoœci powi¹zanej z kluczem (lub null, jeœli klucz nie istnieje)
    public Value get(Key key) {
        int i = hash(key);
        return st[i].get(key);
    } 

    // Wstawianie par klucz-wartoœæ do tablicy
    public void put(Key key, Value val) {
        if (val == null) { delete(key); return; }
        int i = hash(key);
        if (!st[i].contains(key)) N++;
        st[i].put(key, val);
    } 

    // Usuwanie klucza (i powi¹zanej wartoœci), jeœli klucz znajduje siê w tablicy
    public void delete(Key key) {
        int i = hash(key);
        if (st[i].contains(key)) N--;
        st[i].delete(key);
    } 

    // Zwracanie kluczy z tablicy symboli jako obiektu Iterable
    public Iterable<Key> keys() {
        Queue<Key> queue = new Queue<Key>();
        for (int i = 0; i < M; i++) {
            for (Key key : st[i].keys())
                queue.enqueue(key);
        }
        return queue;
    } 


   /***********************************************************************
    *  Klient do testów jednostkowych
    ***********************************************************************/
    public static void main(String[] args) { 
        SeparateChainingHashST<String, Integer> st = new SeparateChainingHashST<String, Integer>();
        for (int i = 0; !StdIn.isEmpty(); i++) {
            String key = StdIn.readString();
            st.put(key, i);
        }

        // Wyœwietlanie kluczy
        for (String s : st.keys()) 
            StdOut.println(s + " " + st.get(s)); 
    }

}
