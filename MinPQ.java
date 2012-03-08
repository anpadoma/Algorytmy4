/*************************************************************************
 *  Kompilacja:  javac MinPQ.java
 *  Wykonanie:    java MinPQ < input.txt
 *  
 *  Generyczna kolejka priorytetowa oparta na kopcu binarnym.
 *  Mo�na u�y� mechanizmu por�wnywania zamiast porz�dku naturalnego.
 *
 *  % java MinPQ < tinyPQ.txt
 *  E A E (liczba element�w w kolejce: 6)
 *
 *  Korzystamy z tablicy zaczynaj�cej si� od 1, co upraszcza wyznaczanie dzieci i rodzic�w.
 *
 *************************************************************************/

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Comparator;

/**
 *  Klasa <tt>MinPQ</tt> reprezentuje kolejk� priorytetow� z generycznymi kluczami.
 *  Obs�uguje standardowe operacje <em>wstaw</em> i <em>usu� minimalny</em>,
 *  a tak�e metody do sprawdzania maksymalnego klucza,
 *  sprawdzania, czy kolejka priorytetowa jest pusta, i przechodzenia po
 *  kluczach.
 *  <p>
 *  Operacje <em>wstaw</em> i <em>usu� minimalny</em> dzia�aj� w 
 *  czasie logarytmicznym (po amortyzacji).
 *  <p>
 *  Ta implementacja jest oparta na kopcu binarnym.
 *  <p>
 *  Dodatkow� dokumentacj� znajdziesz w <a href="http://algs4.cs.princeton.edu/34pq">podrozdziale 3.4</a> ksi��ki
 *  <i>Algorytmy, wydanie czwarte</i> Roberta Sedgewicka i Kevina Wayne'a.
 */
public class MinPQ<Key> implements Iterable<Key> {
    private Key[] pq;                    // Obejmuje elementy o indeksach od 1 do N.
    private int N;                       // Liczba element�w w kolejce priorytetowej.
    private Comparator<Key> comparator;  // Opcjonalny mechanizm por�wnywania

   /**
     * Tworzy pust� kolejk� priorytetow� o danej pocz�tkowej pojemno�ci.
     */
    public MinPQ(int initCapacity) {
        pq = (Key[]) new Object[initCapacity + 1];
        N = 0;
    }

   /**
     * Tworzy pust� kolejk� priorytetow�.
     */
    public MinPQ() { this(1); }

   /**
     * Tworzy pust� kolejk� priorytetow� o podanej pocz�tkowej pojemno�ci,
     * u�ywaj�c mechanizmu por�wnywania.
     */
    public MinPQ(int initCapacity, Comparator<Key> comparator) {
        this.comparator = comparator;
        pq = (Key[]) new Object[initCapacity + 1];
        N = 0;
    }

   /**
     * Tworzy pust� kolejk� priorytetow� za pomoc� mechanizmu por�wnywania.
     */
    public MinPQ(Comparator<Key> comparator) { this(1, comparator); }

   /**
     * Czy kolejka jest pusta?
     */
    public boolean isEmpty() {
        return N == 0;
    }

   /**
     * Zwraca liczb� element�w w kolejce
     */
    public int size() {
        return N;
    }

   /**
     * Zwraca najmniejszy klucz z kolejki priorytetowej.
     * Zg�asza wyj�tek, je�li kolejka jest pusta
     */
    public Key min() {
        if (isEmpty()) throw new RuntimeException("Brak element�w w kolejce priorytetowej");
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
        if (N == pq.length - 1) resize(2 * pq.length);

        // Dodawanie x i przenoszenie go w celu zachowania niezmiennika kopca
        pq[++N] = x;
        swim(N);
        assert isMinHeap();
    }

   /**
     * Usuwanie i zwracanie najmniejszego klucza z kolejki priorytetowej.
     * Zg�aszanie wyj�tku, je�li kolejka jest pusta.
     */
    public Key delMin() {
        if (N == 0) throw new RuntimeException("Brak element�w w kolejce priorytetowej");
        exch(1, N);
        Key min = pq[N--];
        sink(1);
        pq[N+1] = null;         // Unikanie wyciekania pami�ci i wspomaganie procesu przywracania pami�ci
        if ((N > 0) && (N == (pq.length - 1) / 4)) resize(pq.length  / 2);
        assert isMinHeap();
        return min;
    }


   /***********************************************************************
    * Funkcje pomocnicze do przywracania niezmiennika kopca
    **********************************************************************/

    private void swim(int k) {
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
    * Funkcje pomocnicze do por�wnywania i przestawiania
    **********************************************************************/
    private boolean greater(int i, int j) {
        if (comparator == null) {
            return ((Comparable<Key>) pq[i]).compareTo(pq[j]) > 0;
        }
        else {
            return comparator.compare(pq[i], pq[j]) > 0;
        }
    }

    private void exch(int i, int j) {
        Key swap = pq[i];
        pq[i] = pq[j];
        pq[j] = swap;
    }

    // Czy pq[1..N] jest minimalnym kopcem?
    private boolean isMinHeap() {
        return isMinHeap(1);
    }

    // Czy poddrzewo pq[1..N] o korzeniu w k jest minimalnym kopcem?
    private boolean isMinHeap(int k) {
        if (k > N) return true;
        int left = 2*k, right = 2*k + 1;
        if (left  <= N && greater(k, left))  return false;
        if (right <= N && greater(k, right)) return false;
        return isMinHeap(left) && isMinHeap(right);
    }


   /***********************************************************************
    * Iteratory
    **********************************************************************/

   /**
     * Zwraca iterator przechodz�cy po wszystkich kluczach kolejki priorytetowej
     * w porz�dku rosn�cym.
     * <p>
     * W iteratorze nie zaimplementowano opcjonalnej operacji <tt>remove()</tt>.
     */
    public Iterator<Key> iterator() { return new HeapIterator(); }

    private class HeapIterator implements Iterator<Key> {
        // Tworzenie nowej kolejki priorytetowej
        private MinPQ<Key> copy;

        // Dodawanie wszystkich element�w do kopii kopca wymaga czasu liniowego,
        //  poniewa� elementy s� ju� uporz�dkowane w kopcu, dlatego nie trzeba przenosi� kluczy
        public HeapIterator() {
            if (comparator == null) copy = new MinPQ<Key>(size());
            else                    copy = new MinPQ<Key>(size(), comparator);
            for (int i = 1; i <= N; i++)
                copy.insert(pq[i]);
        }

        public boolean hasNext()  { return !copy.isEmpty();                     }
        public void remove()      { throw new UnsupportedOperationException();  }

        public Key next() {
            if (!hasNext()) throw new NoSuchElementException();
            return copy.delMin();
        }
    }

   /**
     * Klient testowy
     */
    public static void main(String[] args) {
        MinPQ<String> pq = new MinPQ<String>();
        while (!StdIn.isEmpty()) {
            String item = StdIn.readString();
            if (!item.equals("-")) pq.insert(item);
            else if (!pq.isEmpty()) StdOut.print(pq.delMin() + " ");
        }
        StdOut.println("(liczba element�w w kolejce: " + pq.size() + ")");
    }

}
