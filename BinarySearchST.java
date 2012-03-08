/*************************************************************************
 *  Kompilacja:  javac BinarySearchST.java
 *  Wykonanie:    java BinarySearchST
 *  
 *  Implementacja tablicy symboli oparta na wyszukiwaniu binarnym w uporz¹dkowanej tablicy.
 *
 *  % more tinyST.txt
 *  S E A R C H E X A M P L E
 *  
 *  % java BinarySearchST < tinyST.txt
 *  A 8
 *  C 4
 *  E 12
 *  H 5
 *  L 11
 *  M 9
 *  P 10
 *  R 3
 *  S 0
 *  X 7

 *
 *************************************************************************/


public class BinarySearchST<Key extends Comparable<Key>, Value> {
    private static final int INIT_CAPACITY = 2;
    private Key[] keys;
    private Value[] vals;
    private int N = 0;

    // Tworzy pust¹ tablicê symboli o domyœlnej pocz¹tkowej pojemnoœci.
    public BinarySearchST() { this(INIT_CAPACITY); }   

    // Tworzy pust¹ tablicê symboli o podanej pocz¹tkowej pojemnoœci.
    public BinarySearchST(int capacity) { 
        keys = (Key[]) new Comparable[capacity]; 
        vals = (Value[]) new Object[capacity]; 
    }   

    // Zmienia wielkoœæ tablic.
    private void resize(int capacity) {
        assert capacity >= N;
        Key[]   tempk = (Key[])   new Comparable[capacity];
        Value[] tempv = (Value[]) new Object[capacity];
        for (int i = 0; i < N; i++) {
            tempk[i] = keys[i];
            tempv[i] = vals[i];
        }
        vals = tempv;
        keys = tempk;
    }


    // Czy klucz znajduje siê w tablicy?
    public boolean contains(Key key) {
        return get(key) != null;
    }

    // Liczba par klucz-wartoœæ w tablicy.
    public int size() {
        return N;
    }

    // Czy tablica symboli jest pusta?
    public boolean isEmpty() {
        return size() == 0;
    }

    // Zwraca wartoœæ powi¹zan¹ z danym kluczem (lub null, jeœli klucz nie istnieje).
    public Value get(Key key) {
        if (isEmpty()) return null;
        int i = rank(key); 
        if (i < N && keys[i].compareTo(key) == 0) return vals[i];
        return null;
    } 

    // Zwraca liczbê kluczy w tablicy mniejszych od danego.
    public int rank(Key key) {
        int lo = 0, hi = N-1; 
        while (lo <= hi) { 
            int m = lo + (hi - lo) / 2; 
            int cmp = key.compareTo(keys[m]); 
            if      (cmp < 0) hi = m - 1; 
            else if (cmp > 0) lo = m + 1; 
            else return m; 
        } 
        return lo;
    } 


    // Szuka klucza i aktualizuje wartoœæ, jeœli go znajdzie, oraz powiêksza tablicê, je¿eli klucz jest nowy. 
    public void put(Key key, Value val)  {
        if (val == null) { delete(key); return; }

        int i = rank(key);

        // Klucz znajduje siê ju¿ w tablicy.
        if (i < N && keys[i].compareTo(key) == 0) {
            vals[i] = val;
            return;
        }

        // Wstawia now¹ parê klucz-wartoœæ.
        if (N == keys.length) resize(2*keys.length);

        for (int j = N; j > i; j--)  {
            keys[j] = keys[j-1];
            vals[j] = vals[j-1];
        }
        keys[i] = key;
        vals[i] = val;
        N++;

        assert check();
    } 


    // Usuwa parê klucz-wartoœæ, jeœli taka istnieje.
    public void delete(Key key)  {
        if (isEmpty()) return;

        // Wyznacza pozycjê.
        int i = rank(key);

        // Klucz nie znajduje siê w tablicy.
        if (i == N || keys[i].compareTo(key) != 0) {
            return;
        }

        for (int j = i; j < N-1; j++)  {
            keys[j] = keys[j+1];
            vals[j] = vals[j+1];
        }

        N--;
        keys[N] = null;  // Aby unikn¹æ wyciekania pamiêci.
        vals[N] = null;

        // Zmiana rozmiaru, jeœli tablica zape³niona w 1/4.
        if (N > 0 && N == keys.length/4) resize(keys.length/2);

        assert check();
    } 

    // Usuwanie minimalnego klucza i powi¹zanej wartoœci.
    public void deleteMin() {
        if (isEmpty()) throw new RuntimeException("B³¹d niedope³nienia tablicy symboli");
        delete(min());
    }

    // Usuwanie maksymalnego klucza i powi¹zanej wartoœci.
    public void deleteMax() {
        if (isEmpty()) throw new RuntimeException("B³¹d niedope³nienia tablicy symboli");
        delete(max());
    }


   /*****************************************************************************
    *  Metody dla uporz¹dkowanej tablicy symboli
    *****************************************************************************/
    public Key min() {
        if (isEmpty()) return null;
        return keys[0]; 
    }

    public Key max() {
        if (isEmpty()) return null;
        return keys[N-1];
    }

    public Key select(int k) {
        if (k < 0 || k >= N) return null;
        return keys[k];
    }

    public Key floor(Key key) {
        int i = rank(key);
        if (i < N && key.compareTo(keys[i]) == 0) return keys[i];
        if (i == 0) return null;
        else return keys[i-1];
    }

    public Key ceiling(Key key) {
        int i = rank(key);
        if (i == N) return null; 
        else return keys[i];
    }

    public int size(Key lo, Key hi) {
        if (lo.compareTo(hi) > 0) return 0;
        if (contains(hi)) return rank(hi) - rank(lo) + 1;
        else              return rank(hi) - rank(lo);
    }

    public Iterable<Key> keys() {
        return keys(min(), max());
    }

    public Iterable<Key> keys(Key lo, Key hi) {
        Queue<Key> queue = new Queue<Key>(); 
        if (lo == null && hi == null) return queue;
        if (lo == null) throw new RuntimeException("lo to null w metodzie keys()");
        if (hi == null) throw new RuntimeException("hi to null w metodzie keys()");
        if (lo.compareTo(hi) > 0) return queue;
        for (int i = rank(lo); i < rank(hi); i++) 
            queue.enqueue(keys[i]);
        if (contains(hi)) queue.enqueue(keys[rank(hi)]);
        return queue; 
    }


    private boolean check() {
        return isSorted() && rankCheck();
    }

    private boolean isSorted() {
        for (int i = 1; i < size(); i++)
            if (keys[i].compareTo(keys[i-1]) < 0) return false;
        return true;
    }

    private boolean rankCheck() {
        for (int i = 0; i < size(); i++)
            if (i != rank(select(i))) return false;
        for (int i = 0; i < size(); i++)
            if (keys[i].compareTo(select(rank(keys[i]))) != 0) return false;
        return true;
    }


   /*****************************************************************************
    *  Klient testowy.
    *****************************************************************************/
    public static void main(String[] args) { 
        BinarySearchST<String, Integer> st = new BinarySearchST<String, Integer>();
        for (int i = 0; !StdIn.isEmpty(); i++) {
            String key = StdIn.readString();
            st.put(key, i);
        }
        for (String s : st.keys())
            StdOut.println(s + " " + st.get(s));
    }
}
