/*************************************************************************
 *  Kompilacja:  javac MaxPQ.java
 *  Wykonanie:    java MaxPQ < input.txt
 *  
 *  Generyczna implementacja kolejki priorytetowej z obs³ug¹ maksimum, oparta na kopcu binarnym.
 *  Mo¿na wykorzystaæ mechanizm porównywania zamiast porz¹dku naturalnego,
 *  jednak generyczny typ Key musi byæ zgodny z interfejsem Comparable.
 *
 *  % java MaxPQ < tinyPQ.txt 
 *  Q X P (liczba elementow w kolejce: 6)
 *
 *  U¿ywamy tablicy rozpoczynaj¹cej siê od 1, co upraszcza obliczenia rodziców i dzieci.
 *
 *************************************************************************/

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Comparator;

/**
 *  Klasa <tt>MaxPQ</tt> reprezentuje kolejkê priorytetow¹ generycznych kluczy.
 *  Obs³uguje standardowe operacje <em>wstaw</em> i <em>usuñ maksymalny</em>,
 *  a tak¿e metody do sprawdzania maksymalnego klucza,
 *  sprawdzania, czy kolejka jest pusta, i przechodzenia po 
 *  kluczach.
 *  <p>
 *  Operacje <em>wstaw</em> i <em>usuñ maksymalny</em> dzia³aj¹ w 
 *  czasie logarytmicznym (po amortyzacji).
 *  <p>
 *  Ta implementacja jest oparta na kopcu binarnym.
 *  <p>
 *  Dodatkow¹ dokumentacjê znajdziesz w <a href="http://algs4.cs.princeton.edu/34pq">podrozdziale 3.4</a> ksi¹¿ki
 *  <i>Algorytmy, wydanie czwarte</i> Roberta Sedgewicka i Kevina Wayne'a.
 */

public class MaxPQ<Key> implements Iterable<Key> {
    private Key[] pq;                    // Obejmuje elementy o indeksach od 1 do N.
    private int N;                       // Liczba elementów w kolejce priorytetowej.
    private Comparator<Key> comparator;  // Opcjonalny mechanizm porównywania

   /**
     * Tworzy pust¹ kolejkê priorytetow¹ o danej pocz¹tkowej pojemnoœci.
     */
    public MaxPQ(int capacity) {
        pq = (Key[]) new Object[capacity + 1];
        N = 0;
    }

   /**
     * Tworzy pust¹ kolejkê priorytetow¹.
     */
    public MaxPQ() { this(1); }

   /**
     * Tworzy pust¹ kolejkê priorytetow¹ o podanej pocz¹tkowej pojemnoœci,
     * u¿ywaj¹c mechanizmu porównywania.
     */
    public MaxPQ(int initCapacity, Comparator<Key> comparator) {
        this.comparator = comparator;
        pq = (Key[]) new Object[initCapacity + 1];
        N = 0;
    }

   /**
     * Tworzy pust¹ kolejkê priorytetow¹ za pomoc¹ mechanizmu porównywania.
     */
    public MaxPQ(Comparator<Key> comparator) { this(1, comparator); }

   /**
     * Czy kolejka jest pusta?
     */
    public boolean isEmpty() {
        return N == 0;
    }

   /**
     * Zwraca liczbê elementów w kolejce
     */
    public int size() {
        return N;
    }

   /**
     * Zwraca najwiêkszy klucz z kolejki priorytetowej.
     * Zg³asza wyj¹tek, jeœli kolejka jest pusta
     */
    public Key max() {
        if (isEmpty()) throw new RuntimeException("Brak elementow w kolejce priorytetowej");
        return pq[1];
    }

    // Funkcja pomocnicza do podwajania rozmiaru tablicy
    private void resize(int capacity) {
        assert capacity > N;
        Key[] temp = (Key[]) new Object[capacity];
        for (int i = 1; i <= N; i++) temp[i] = pq[i];
        pq = temp;
    }


   /**
     * Dodaje nowy klucz do kolejki priorytetowej
     */
    public void insert(Key x) {

        // Podwajanie rozmiaru tablicy, kiedy to potrzebne
        if (N >= pq.length - 1) resize(2 * pq.length);

        // Dodawanie x i przenoszenie go w celu zachowania niezmiennika kopca
        pq[++N] = x;
        swim(N);
        assert isMaxHeap();
    }

   /**
     * Usuwanie i zwracanie najmniejszego klucza z kolejki priorytetowej.
     * Zg³aszanie wyj¹tku, jeœli kolejka jest pusta.
     */
    public Key delMax() {
        if (N == 0) throw new RuntimeException("Brak elementow w kolejce priorytetowej");
        Key max = pq[1];
        exch(1, N--);
        sink(1);
        pq[N+1] = null;     // Unikanie wyciekania pamiêci i wspomaganie procesu przywracania pamiêci
        if ((N > 0) && (N == (pq.length - 1) / 4)) resize(pq.length / 2);
        assert isMaxHeap();
        return max;
    }


   /***********************************************************************
    * Funkcje pomocnicze do przywracania niezmiennika kopca
    **********************************************************************/

    private void swim(int k) {
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
    * Funkcje pomocnicze do porównywania i przestawiania
    **********************************************************************/
    private boolean less(int i, int j) {
        if (comparator == null) {
            return ((Comparable<Key>) pq[i]).compareTo(pq[j]) < 0;
        }
        else {
            return comparator.compare(pq[i], pq[j]) < 0;
        }
    }

    private void exch(int i, int j) {
        Key swap = pq[i];
        pq[i] = pq[j];
        pq[j] = swap;
    }

    // Czy pq[1..N] jest minimalnym kopcem?
    private boolean isMaxHeap() {
        return isMaxHeap(1);
    }

    // Czy poddrzewo pq[1..N] o korzeniu w k jest minimalnym kopcem?
    private boolean isMaxHeap(int k) {
        if (k > N) return true;
        int left = 2*k, right = 2*k + 1;
        if (left  <= N && less(k, left))  return false;
        if (right <= N && less(k, right)) return false;
        return isMaxHeap(left) && isMaxHeap(right);
    }


   /***********************************************************************
    * Iterator
    **********************************************************************/

   /**
     * Zwraca iterator przechodz¹cy po wszystkich kluczach kolejki priorytetowej
     * w porz¹dku malej¹cym.
     * <p>
     * W iteratorze nie zaimplementowano opcjonalnej operacji <tt>remove()</tt>.
     */
    public Iterator<Key> iterator() { return new HeapIterator(); }

    private class HeapIterator implements Iterator<Key> {

        // Tworzenie nowej kolejki priorytetowej
        private MaxPQ<Key> copy;

        // Dodawanie wszystkich elementów do kopii kopca wymaga czasu liniowego,
        //  poniewa¿ elementy s¹ ju¿ uporz¹dkowane w kopcu, dlatego nie trzeba przenosiæ kluczy
        public HeapIterator() {
            if (comparator == null) copy = new MaxPQ<Key>(size());
            else                    copy = new MaxPQ<Key>(size(), comparator);
            for (int i = 1; i <= N; i++)
                copy.insert(pq[i]);
        }

        public boolean hasNext()  { return !copy.isEmpty();                     }
        public void remove()      { throw new UnsupportedOperationException();  }

        public Key next() {
            if (!hasNext()) throw new NoSuchElementException();
            return copy.delMax();
        }
    }

   /**
     * Klient testowy
     */
    public static void main(String[] args) {
        MaxPQ<String> pq = new MaxPQ<String>();
        while (!StdIn.isEmpty()) {
            String item = StdIn.readString();
            if (!item.equals("-")) pq.insert(item);
            else if (!pq.isEmpty()) StdOut.print(pq.delMax() + " ");
        }
        StdOut.println("(liczba elementow w kolejce: " + pq.size() + ")");
    }

}
