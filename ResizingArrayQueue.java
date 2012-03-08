/*************************************************************************
 *  Kompilacja:  javac ResizingArrayQueue.java
 *  Wykonanie:    java ResizingArrayQueue < input.txt
 *  
 *  Implementacja kolejki oparta na tablicy o zmiennej wielko�ci.
 *
 *  % java ResizingArrayQueue < tobe.txt 
 *  to be or not to be (elementy w kolejce: 2)
 *
 *************************************************************************/

import java.util.Iterator;
import java.util.NoSuchElementException;

// Blokowanie ostrze�e� unchecked w Javie 1.5.0_6 i nowszych wersjach
@SuppressWarnings("unchecked")

public class ResizingArrayQueue<Item> implements Iterable<Item> {
    private Item[] q;            // Elementy kolejki
    private int N = 0;           // Liczba element�w w kolejce
    private int first = 0;       // Indeks pierwszego elementu kolejki
    private int last  = 0;       // Indeks nast�pnej wolnej pozycji

    // Potrzebne jest rzutowanie, poniewa� w Javie nie mo�na tworzy� generycznych tablic
    public ResizingArrayQueue() {
        q = (Item[]) new Object[2];
    }

    public boolean isEmpty() { return N == 0;    }
    public int size()        { return N;         }

    // Zmiana rozmiaru tablicy
    private void resize(int max) {
        assert max >= N;
        Item[] temp = (Item[]) new Object[max];
        for (int i = 0; i < N; i++) temp[i] = q[(first + i) % q.length];
        q = temp;
        first = 0;
        last  = N;
    }


    public void enqueue(Item item) {
        // Podwajanie rozmiaru tablicy (je�li to konieczne)
        if (N == q.length) resize(2*q.length);   // Podwajanie rozmiaru tablicy, je�li to konieczne
        q[last++] = item;                        // Dodawanie elementu
        if (last == q.length) last = 0;          // Zawijanie
        N++;
    }

    // Usuwanie najdawniej dodanego elementu
    public Item dequeue() {
        if (isEmpty()) throw new RuntimeException("Brak elementow w kolejce");
        Item item = q[first];
        q[first] = null;                            // Unikanie wyciekania pami�ci
        N--;
        first++;
        if (first == q.length) first = 0;           // Zawijanie
        // Zmniejszanie rozmiaru, je�li to konieczne
        if (N > 0 && N == q.length/4) resize(q.length/2); 
        return item;
    }

    public Iterator<Item> iterator() { return new QueueIterator(); }

    // Iterator; bez implementacji opcjonalnej metody remove()
    private class QueueIterator implements Iterator<Item> {
        private int i = 0;
        public boolean hasNext()  { return i < N;                               }
        public void remove()      { throw new UnsupportedOperationException();  }

        public Item next() {
            if (!hasNext()) throw new NoSuchElementException();
            Item item = q[i % q.length];
            i++;
            return item;
        }
    }

   /**
     * Klient testowy
     */
    public static void main(String[] args) {
        ResizingArrayQueue<String> q = new ResizingArrayQueue<String>();
        while (!StdIn.isEmpty()) {
            String item = StdIn.readString();
            if (!item.equals("-")) q.enqueue(item);
            else if (!q.isEmpty()) StdOut.print(q.dequeue() + " ");
        }
        StdOut.println("(elementy w kolejce: " + q.size() + ")");
    }

}
