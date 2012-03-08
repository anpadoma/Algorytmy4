/*************************************************************************
 *  Kompilacja:  javac IndexMinPQ.java
 *  Wykonanie:    java IndexMinPQ
 *
 *  Implementacja indeksowanej kolejki priorytetowej oparta na kopcu binarnym.
 *
 *********************************************************************/

import java.util.Iterator;
import java.util.NoSuchElementException;

public class IndexMinPQ<Key extends Comparable<Key>> implements Iterable<Integer> {
    private int N;           // Liczba element�w w kolejce priorytetowej 
    private int[] pq;        // Kopiec binarny z indeksami od 1
    private int[] qp;        // Odwrotno�� pq - qp[pq[i]] = pq[qp[i]] = i
    private Key[] keys;      // keys[i] = priorytet elementu i

    public IndexMinPQ(int NMAX) {
        keys = (Key[]) new Comparable[NMAX + 1];    
        pq   = new int[NMAX + 1];
        qp   = new int[NMAX + 1];                   
        for (int i = 0; i <= NMAX; i++) qp[i] = -1;
    }

    // Czy kolejka jest pusta?
    public boolean isEmpty() { return N == 0; }

    // Czy indeks k wyst�puje w kolejce priorytetowej
    public boolean contains(int k) {
        return qp[k] != -1;
    }

    // Liczba kluczy w kolejce priorytetowej
    public int size() {
        return N;
    }

    // ��czenie klucza z indeksem k
    public void insert(int k, Key key) {
        if (contains(k)) throw new RuntimeException("Element znajduje si� ju� w kolejce");
        N++;
        qp[k] = N;
        pq[N] = k;
        keys[k] = key;
        swim(N);
    }

    // Zwracanie indeksu powi�zanego z minimalnym kluczem
    public int min() { 
        if (N == 0) throw new RuntimeException("Brak element�w w kolejce priorytetowej");
        return pq[1];        
    }

    // Zwracanie minimalnego klucza
    public Key minKey() { 
        if (N == 0) throw new RuntimeException("Brak element�w w kolejce priorytetowej");
        return keys[pq[1]];        
    }

    // Usuwanie minimalnego klucza i zwracanie powi�zanego indeksu
    public int delMin() { 
        if (N == 0) throw new RuntimeException("Brak element�w w kolejce priorytetowej");
        int min = pq[1];        
        exch(1, N--); 
        sink(1);
        qp[min] = -1;            // Usuwanie
        keys[pq[N+1]] = null;    // Pomaga w przywracaniu pami�ci
        pq[N+1] = -1;            // Niepotrzebne
        return min; 
    }

/*
    // Zmienia klucz powi�zany z indeksem k (lub wstawia dane, je�li indeks k nie wyst�puje w kolejce)
    public void put(int k, Key key) {
        if (!contains(k)) insert(k, key);
        else changeKey(k, key);
    }

    // Zwraca klucz powi�zany z indeksem k
    public Key get(int k) {
        if (!contains(k)) throw new RuntimeException("Element nie wyst�puje w kolejce");
        else return keys[pq[k]];
    }
*/

    // Zmienia klucz powi�zany z indeksem k
    public void change(int k, Key key) {
        if (!contains(k)) throw new RuntimeException("Element nie wyst�puje w kolejce");
        keys[k] = key;
        swim(qp[k]);
        sink(qp[k]);
    }

    // Zmniejsza klucz powi�zany z indeksem k
    public void decrease(int k, Key key) {
        if (!contains(k)) throw new RuntimeException("Element nie wyst�puje w kolejce");
        if (keys[k].compareTo(key) <= 0) throw new RuntimeException("Niedozwolone zmniejszanie");
        keys[k] = key;
        swim(qp[k]);
    }

    // Zwi�ksza klucz powi�zany z indeksem k
    public void increase(int k, Key key) {
        if (!contains(k)) throw new RuntimeException("Element nie wyst�puje w kolejce");
        if (keys[k].compareTo(key) >= 0) throw new RuntimeException("Niedozwolone zwi�kszanie");
        keys[k] = key;
        sink(qp[k]);
    }


   /**************************************************************
    * Og�lne funkcje pomocnicze
    **************************************************************/
    private boolean greater(int i, int j) {
        return keys[pq[i]].compareTo(keys[pq[j]]) > 0;
    }

    private void exch(int i, int j) {
        int swap = pq[i]; pq[i] = pq[j]; pq[j] = swap;
        qp[pq[i]] = i; qp[pq[j]] = j;
    }


   /**************************************************************
    * Funkcje pomocnicze dla kopca
    **************************************************************/
    private void swim(int k)  {
        while (k > 1 && greater(k/2, k)) {
            exch(k, k/2);
            k = k/2;
        }
    }

    private void sink(int k) {
        while (2*k <= N) {
            int j = 2*k;
            if (j < N && greater(j, j+1)) j++;
            if (!greater(k, j)) break;
            exch(k, j);
            k = j;
        }
    }


   /***********************************************************************
    * Iteratory
    **********************************************************************/

   /**
     * Zwraca iterator przechodz�cy po wszystkich elementach
     * kolejki priorytetowej w porz�dku rosn�cym.
     * <p>
     * W iteratorze nie zaimplementowano opcjonalnej operacji <tt>remove()</tt>.
     */
    public Iterator<Integer> iterator() { return new HeapIterator(); }

    private class HeapIterator implements Iterator<Integer> {
        // Tworzenie nowej kolejki priorytetowej
        private IndexMinPQ<Key> copy;

        // Dodaje wszystkie elementy w celu skopiowania kopca. 
        // Czas ro�nie liniowo, poniewa� elementy wyst�puj� w kolejno�ci zgodnej ze stert�, dlatego nie trzeba przenosi� kluczy
        public HeapIterator() {
            copy = new IndexMinPQ<Key>(pq.length - 1);
            for (int i = 1; i <= N; i++)
                copy.insert(pq[i], keys[pq[i]]);
        }

        public boolean hasNext()  { return !copy.isEmpty();                     }
        public void remove()      { throw new UnsupportedOperationException();  }

        public Integer next() {
            if (!hasNext()) throw new NoSuchElementException();
            return copy.delMin();
        }
    }


    public static void main(String[] args) {
        // Wstawianie grupy �a�cuch�w znak�w
        String[] strings = { "it", "was", "the", "best", "of", "times", "it", "was", "the", "worst" };

        IndexMinPQ<String> pq = new IndexMinPQ<String>(strings.length);
        for (int i = 0; i < strings.length; i++) {
            pq.insert(i, strings[i]);
        }

        // Usuwanie i wy�wietlanie ka�dego klucza
        while (!pq.isEmpty()) {
            int i = pq.delMin();
            System.out.println(i + " " + strings[i]);
        }
        System.out.println();

        // Ponowne wstawianie tych samych �a�cuch�w znak�w
        for (int i = 0; i < strings.length; i++) {
            pq.insert(i, strings[i]);
        }

        // Wy�wietlanie ka�dego klucza za pomoc� iteratora
        for (int i : pq) {
            System.out.println(i + " " + strings[i]);
        }
        while (!pq.isEmpty()) {
            pq.delMin();
        }

    }
}
