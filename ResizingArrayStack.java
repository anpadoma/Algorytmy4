/*************************************************************************
 *  Kompilacja:  javac ResizingArrayStack.java
 *  Wykonanie:   java ResizingArrayStack < input.txt
 *  
 *  Implementacja stosu oparta na tablicy o zmiennym rozmiarze.
 *
 *  % more tobe.txt 
 *  to be or not to - be - - that - - - is
 *
 *  % java ResizingArrayStack < tobe.txt
 *  to be not that or be (2 left on stack)
 *
 *************************************************************************/

import java.util.Iterator;
import java.util.NoSuchElementException;

// Blokuje ostrze¿enia unchecked w Javie 1.5.0_6 i nowszych wersjach
@SuppressWarnings("unchecked")

public class ResizingArrayStack<Item> implements Iterable<Item> {
    private Item[] a;         // Tablica elementów
    private int N = 0;        // Liczba elementów na stosie

    // Tworzenie pustego stosu
    public ResizingArrayStack() {
        a = (Item[]) new Object[2];
    }

    public boolean isEmpty() { return N == 0; }
    public int size()        { return N;      }



    // Zmiana rozmiaru tablicy z elementami
    private void resize(int capacity) {
        assert capacity >= N;
        Item[] temp = (Item[]) new Object[capacity];
        for (int i = 0; i < N; i++)
            temp[i] = a[i];
        a = temp;
    }

    // Umieszczanie nowego elementu na stosie
    public void push(Item item) {
        if (N == a.length) resize(2*a.length);    // Podwajanie wielkoœci tablicy, jeœli to konieczne
        a[N++] = item;                            // Dodawanie elementu
    }

    // Usuwanie i zwracanie ostatnio dodanego elementu
    public Item pop() {
        if (isEmpty()) { throw new RuntimeException("Brak elementow na stosie"); }
        Item item = a[N-1];
        a[N-1] = null;                              // Unikanie wyciekania pamiêci
        N--;
        // Zmniejszanie wielkoœci tablicy, jeœli to konieczne
        if (N > 0 && N == a.length/4) resize(a.length/2);
        return item;
    }


    public Iterator<Item> iterator()  { return new LIFOIterator();  }

    // Iterator; bez implementacji opcjonalnej metody remove()
    private class LIFOIterator implements Iterator<Item> {
        private int i = N;
        public boolean hasNext()  { return i > 0;                               }
        public void remove()      { throw new UnsupportedOperationException();  }

        public Item next() {
            if (!hasNext()) throw new NoSuchElementException();
            return a[--i];
        }
    }



   /***********************************************************************
    * Metoda testowa
    **********************************************************************/
    public static void main(String[] args) {
        ResizingArrayStack<String> s = new ResizingArrayStack<String>();
        while (!StdIn.isEmpty()) {
            String item = StdIn.readString();
            if (!item.equals("-")) s.push(item);
            else if (!s.isEmpty()) StdOut.print(s.pop() + " ");
        }
        StdOut.println("(elementy na stosie: " + s.size() + ")");
    }
}
