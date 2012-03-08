/*************************************************************************
 *  Kompilacja:  javac IndexMaxPQ.java
 *  Wykonanie:    java IndexMaxPQ
 *
 *  Implementacja indeksowanej kolejki priorytetowej oparta na kopcu binarnym.
 *
 *********************************************************************/

import java.util.Iterator;
import java.util.NoSuchElementException;

public class IndexMaxPQ<Key extends Comparable<Key>> implements Iterable<Integer> {
    private int N;           // Liczba elementów w kolejce priorytetowej
    private int[] pq;        // Kopiec binarny z indeksami od 1
    private int[] qp;        // Odwrotnoœæ pq - qp[pq[i]] = pq[qp[i]] = i
    private Key[] keys;      // keys[i] = priorytet elementu i

    public IndexMaxPQ(int NMAX) {
        keys = (Key[]) new Comparable[NMAX + 1];    
        pq   = new int[NMAX + 1];
        qp   = new int[NMAX + 1];                   
        for (int i = 0; i <= NMAX; i++) qp[i] = -1;
    }

    // Czy kolejka jest pusta?
    public boolean isEmpty() { return N == 0; }

    // Czy indeks k wystêpuje w kolejce priorytetowej?
    public boolean contains(int k) {
        return qp[k] != -1;
    }

    // Liczba elementów w kolejce priorytetowej
    public int size() {
        return N;
    }

    // £¹czenie klucza z indeksem k
    public void insert(int k, Key key) {
        if (contains(k)) throw new RuntimeException("Element znajduje siê ju¿ w kolejce");
        N++;
        qp[k] = N;
        pq[N] = k;
        keys[k] = key;
        swim(N);
    }

    // Zwracanie indeksu powi¹zanego z maksymalnym kluczem
    public int max() { 
        if (N == 0) throw new RuntimeException("Brak elementow w kolejce priorytetowej");
        return pq[1];        
    }

    // Zwracanie maksymalnego klucza
    public Key maxKey() { 
        if (N == 0) throw new RuntimeException("Brak elementow w kolejce priorytetowej");
        return keys[pq[1]];        
    }

    // Usuwanie maksymalnego klucza i zwracanie powi¹zanego indeksu
    public int delMax() { 
        if (N == 0) throw new RuntimeException("Brak elementow w kolejce priorytetowej");
        int min = pq[1];        
        exch(1, N--); 
        sink(1);
        qp[min] = -1;            // Usuwanie
        keys[pq[N+1]] = null;    // Pomaga w przywracaniu pamiêci
        pq[N+1] = -1;            // Niepotrzebne
        return min; 
    }

    // £¹czy klucz z indeksem k (zmienia klucz, jeœli indeks k ju¿ istnieje)
    public void put(int k, Key key) {
        if (!contains(k)) insert(k, key);
        else changeKey(k, key);
    }

    // Zwraca klucz powi¹zany z indeksem k
    public Key get(int k) {
        if (!contains(k)) throw new RuntimeException("Element nie wystepuje w kolejce");
        else return keys[pq[k]];
    }

    // Zmienia klucz powi¹zany z indeksem k
    public void changeKey(int k, Key key) {
        if (!contains(k)) throw new RuntimeException("Element nie wystepuje w kolejce");
        keys[k] = key;
        swim(qp[k]);
        sink(qp[k]);
    }

    // Zwiêksza klucz powi¹zany z indeksem k
    public void increaseKey(int k, Key key) {
        if (!contains(k)) throw new RuntimeException("Element nie wystepuje w kolejce");
        if (keys[k].compareTo(key) >= 0) throw new RuntimeException("Niedozwolone zwiekszanie");
        keys[k] = key;
        swim(qp[k]);
    }

    // Zmniejsza klucz powi¹zany z indeksem k
    public void decreaseKey(int k, Key key) {
        if (!contains(k)) throw new RuntimeException("Element nie wystepuje w kolejce");
        if (keys[k].compareTo(key) <= 0) throw new RuntimeException("Niedozwolone zmniejszanie");
        keys[k] = key;
        sink(qp[k]);
    }


   /**************************************************************
    * Ogólne funkcje pomocnicze
    **************************************************************/
    private boolean less(int i, int j) {
        return keys[pq[i]].compareTo(keys[pq[j]]) < 0;
    }

    private void exch(int i, int j) {
        int swap = pq[i]; pq[i] = pq[j]; pq[j] = swap;
        qp[pq[i]] = i; qp[pq[j]] = j;
    }


   /**************************************************************
    * Funkcje pomocnicze dla kopca
    **************************************************************/
    private void swim(int k)  {
        while (k > 1 && less(k/2, k)) {
            exch(k, k/2);
            k = k/2;
        }
    }

    private void sink(int k) {
        while (2*k <= N) {
            int j = 2*k;
            if (j < N && less(j, j+1)) j++;
            if (!less(k, j)) break;
            exch(k, j);
            k = j;
        }
    }


   /***********************************************************************
    * Iteratory
    **********************************************************************/

   /**
     * Zwraca iterator przechodz¹cy po wszystkich elementach
     * kolejki priorytetowej w porz¹dku malej¹cym.
     * <p>
     * W iteratorze nie zaimplementowano opcjonalnej operacji <tt>remove()</tt>.
     */
    public Iterator<Integer> iterator() { return new HeapIterator(); }

    private class HeapIterator implements Iterator<Integer> {
        // Tworzenie nowej kolejki priorytetowej
        private IndexMaxPQ<Key> copy;

        // Dodaje wszystkie elementy w celu skopiowania kopca. 
        // Czas roœnie liniowo, poniewa¿ elementy wystêpuj¹ w kolejnoœci zgodnej ze stert¹, dlatego nie trzeba przenosiæ kluczy
        public HeapIterator() {
            copy = new IndexMaxPQ<Key>(pq.length - 1);
            for (int i = 1; i <= N; i++)
                copy.put(pq[i], keys[pq[i]]);
        }

        public boolean hasNext()  { return !copy.isEmpty();                     }
        public void remove()      { throw new UnsupportedOperationException();  }

        public Integer next() {
            if (!hasNext()) throw new NoSuchElementException();
            return copy.delMax();
        }
    }


    public static void main(String[] args) {
        // Wstawianie grupy ³añcuchów znaków
        String[] strings = { "it", "was", "the", "best", "of", "times", "it", "was", "the", "worst" };

        IndexMaxPQ<String> pq = new IndexMaxPQ<String>(strings.length);
        for (int i = 0; i < strings.length; i++) {
            pq.put(i, strings[i]);
        }

        // Usuwanie i wyœwietlanie ka¿dego klucza
        while (!pq.isEmpty()) {
            int i = pq.delMax();
            System.out.println(i + " " + strings[i]);
        }
        System.out.println();

        // Ponowne wstawianie tych samych ³añcuchów znaków
        for (int i = 0; i < strings.length; i++) {
            pq.put(i, strings[i]);
        }

        // Wyœwietlanie ka¿dego klucza za pomoc¹ iteratora
        for (int i : pq) {
            System.out.println(i + " " + strings[i]);
        }
        while (!pq.isEmpty()) {
            pq.delMax();
        }

    }
}
