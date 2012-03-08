/*************************************************************************
 *  Kompilacja:  javac LinearProbingHashST.java
 *  Wykonanie:    java LinearProbingHashST
 *  
 *  Implementacja tablica symboli oparta na tablicy z haszowaniem z próbkowaniem liniowym.
 *
 *  % java LinearProbingHashST
 *  128.112.136.11
 *  208.216.181.15
 *  null
 *
 *
 *************************************************************************/


public class LinearProbingHashST<Key, Value> {
    private static final int INIT_CAPACITY = 4;

    private int N;           // Liczba par klucz-wartoœæ w tablicy symboli
    private int M;           // Rozmiar tablicy z haszowaniem z próbkowaniem liniowym
    private Key[] keys;      // Klucze
    private Value[] vals;    // Wartoœci


    // Tworzenie pustej tablicy z haszowaniem - 16 to rozmiar domyœlny
    public LinearProbingHashST() {
        this(INIT_CAPACITY);
    }

    // Tworzenie tablicy z haszowaniem z próbkowaniem liniowym o podanej pojemnoœci
    public LinearProbingHashST(int capacity) {
        M = capacity;
        keys = (Key[])   new Object[M];
        vals = (Value[]) new Object[M];
    }

    // Zwraca liczbê par klucz-wartoœæ w tablicy symboli
    public int size() {
        return N;
    }

    // Czy tablica symboli jest pusta?
    public boolean isEmpty() {
        return size() == 0;
    }

    // Czy para klucz-wartoœæ o danym kluczu istnieje w tablicy symboli?
    public boolean contains(Key key) {
        return get(key) != null;
    }

    // Funkcja haszuj¹ca dla kluczy - zwraca wartoœæ z przedzia³u od 0 do M-1
    private int hash(Key key) {
        return (key.hashCode() & 0x7fffffff) % M;
    }

    // Zmiana rozmiaru tablicy z haszowaniem do podanej pojemnoœci przez 
    // ponowne obliczenie skrótów dla wszystkich kluczy
    private void resize(int capacity) {
        LinearProbingHashST<Key, Value> temp = new LinearProbingHashST<Key, Value>(capacity);
        for (int i = 0; i < M; i++) {
            if (keys[i] != null) {
                temp.put(keys[i], vals[i]);
            }
        }
        keys = temp.keys;
        vals = temp.vals;
        M    = temp.M;
    }

    // Wstawianie pary klucz-wartoœæ do tablicy symboli
    public void put(Key key, Value val) {
        if (val == null) delete(key);

        // Podwajanie rozmiaru tablicy, jeœli jest pe³na w 50%
        if (N >= M/2) resize(2*M);

        int i;
        for (i = hash(key); keys[i] != null; i = (i + 1) % M) {
            if (keys[i].equals(key)) { vals[i] = val; return; }
        }
        keys[i] = key;
        vals[i] = val;
        N++;
    }

    // Zwracanie wartoœci powi¹zanej z danym kluczem (lub null, jeœli taka wartoœæ nie istnieje)
    public Value get(Key key) {
        for (int i = hash(key); keys[i] != null; i = (i + 1) % M) 
            if (keys[i].equals(key))
                return vals[i];
        return null;
    }

    // Usuwanie klucza (i powi¹zanej wartoœci) z tablicy symboli
    public void delete(Key key) {
        if (!contains(key)) return;

        // Znajdowanie pozycji klucza
        int i = hash(key);
        while (!key.equals(keys[i])) {
            i = (i + 1) % M;
        }

        // Usuwanie klucza i powi¹zanej wartoœci
        keys[i] = null;
        vals[i] = null;

        // Zmiana skrótów dla wszystkich kluczy z danej grupy
        i = (i + 1) % M;
        while (keys[i] != null) {
            // Usuwanie elementów keys[i] i vals[i] oraz ponowne wstawianie
            Key   keyToRehash = keys[i];
            Value valToRehash = vals[i];
            keys[i] = null;
            vals[i] = null;
            N--;  
            put(keyToRehash, valToRehash);
            i = (i + 1) % M;
        }

        N--;        

        // Zmniejszanie rozmiaru tablicy o po³owê, jeœli jest zape³niona w 12,5% lub mniej
        if (N > 0 && N <= M/8) resize(M/2);

        assert check();
    }

    // Zwracanie wszystkich kluczy jako obiektu Iterable
    public Iterable<Key> keys() {
        Queue<Key> queue = new Queue<Key>();
        for (int i = 0; i < M; i++)
            if (keys[i] != null) queue.enqueue(keys[i]);
        return queue;
    }

    // Sprawdzanie integralnoœci - nie nale¿y robiæ tego po ka¿dym wywo³aniu put(),
    // poniewa¿ integralnoœæ nie jest zachowywana w czasie dzia³ania delete()
    private boolean check() {

        // Sprawdzanie, czy tablica z haszowaniem jest zape³niona w najwy¿ej 50%
        if (M < 2*N) {
            System.err.println("Rozmiar tablicy z haszowaniem M = " + M + "; rozmiar tablicy N = " + N);
            return false;
        }

        // Sprawdzanie, czy za pomoc¹ get() mo¿na znaleŸæ ka¿dy klucz z tablicy
        for (int i = 0; i < M; i++) {
            if (keys[i] == null) continue;
            else if (get(keys[i]) != vals[i]) {
                System.err.println("get[" + keys[i] + "] = " + get(keys[i]) + "; vals[i] = " + vals[i]);
                return false;
            }
        }
        return true;
    }


/***********************************************************************
    *  Klient do testów jednostkowych
    ***********************************************************************/
    public static void main(String[] args) { 
        LinearProbingHashST<String, Integer> st = new LinearProbingHashST<String, Integer>();
        for (int i = 0; !StdIn.isEmpty(); i++) {
            String key = StdIn.readString();
            st.put(key, i);
        }

        // Wyœwietlanie kluczy
        for (String s : st.keys()) 
            StdOut.println(s + " " + st.get(s)); 
    }
}
