/*************************************************************************
 *  Kompilacja:  javac Stack.java
 *  Wykonanie:    java Stack < input.txt
 *
 *  Generyczny stos zaimplementowany za pomoc¹ listy powi¹zanej. Ka¿dy element
 *  stosu jest typu Item.
 *  
 *  % more tobe.txt 
 *  to be or not to - be - - that - - - is
 *
 *  % java Stack < tobe.txt
 *  to be not that or be (elementy na stosie: 2)
 *
 *************************************************************************/

import java.util.Iterator;
import java.util.NoSuchElementException;


/**
 *  Klasa <tt>Stack</tt> reprezentuje stos LIFO generycznych elementów.
 *  Obs³uguje standardowe operacje <em>push</em> i <em>pop</em>, a tak¿e metody
 *  do podgl¹dania elementu z wierzcho³ka, sprawdzania, czy stos jest pusty, i przechodzenia
 *  po elementach w porz¹dku LIFO.
 *  <p>
 *  Wszystkie operacje na stosie z wyj¹tkiem iterowania zajmuj¹ sta³y czas.
 *  <p>
 *  Dodatkow¹ dokumentacjê mo¿na znaleŸæ w <a href="/algs4/13stacks">podrozdziale 1.3</a> ksi¹¿ki
 *  <i>Algorytmy, wydanie czwarte</i> Roberta Sedgewicka i Kevina Wayne'a.
 */
public class Stack<Item> implements Iterable<Item> {
    private int N;          // Wielkoœæ stosu
    private Node first;     // Wierzcho³ek stosu

    // Pomocnicza klasa dla listy powi¹zanej
    private class Node {
        private Item item;
        private Node next;
    }

   /**
     * Tworzenie pustego stosu
     */
    public Stack() {
        first = null;
        N = 0;
    }

   /**
     *Czy stos jest pusty?
     */
    public boolean isEmpty() {
        return first == null;
    }

   /**
     * Zwraca liczbê elementów na stosie
     */
    public int size() {
        return N;
    }

   /**
     * Dodaje element do stosu
     */
    public void push(Item item) {
        Node oldfirst = first;
        first = new Node();
        first.item = item;
        first.next = oldfirst;
        N++;
    }

   /**
     * Usuwa i zwraca element ostatnio dodany do stosu.
     * Zg³asza wyj¹tek, jeœli stos jest pusty.
     */
    public Item pop() {
        if (isEmpty()) throw new RuntimeException("Brak elementów na stosie");
        Item item = first.item;        // Zapisywanie zwracanego elementu
        first = first.next;            // Usuwanie pierwszego wêz³a
        N--;
        return item;                   // Zwracanie zapisanego elementu
    }


   /**
     * Zwraca element ostatnio dodany do stosu.
     * Zg³asza wyj¹tek, jeœli stos jest pusty.
     */
    public Item peek() {
        if (isEmpty()) throw new RuntimeException("Brak elementów na stosie");
        return first.item;
    }

   /**
     * Zwraca ³añcuch reprezentuj¹cy stos.
     */
    public String toString() {
        StringBuilder s = new StringBuilder();
        for (Item item : this)
            s.append(item + " ");
        return s.toString();
    }
       

   /**
     * Zwraca iterator stosu przechodz¹cy po elementach w porz¹dku LIFO.
     */
    public Iterator<Item> iterator()  { return new LIFOIterator();  }

    // Iterator (bez implementacji opcjonalej metody remove())
    private class LIFOIterator implements Iterator<Item> {
        private Node current = first;
        public boolean hasNext()  { return current != null;                     }
        public void remove()      { throw new UnsupportedOperationException();  }

        public Item next() {
            if (!hasNext()) throw new NoSuchElementException();
            Item item = current.item;
            current = current.next; 
            return item;
        }
    }


   /**
     * Klient testowy
     */
    public static void main(String[] args) {
        Stack<String> s = new Stack<String>();
        while (!StdIn.isEmpty()) {
            String item = StdIn.readString();
            if (!item.equals("-")) s.push(item);
            else if (!s.isEmpty()) StdOut.print(s.pop() + " ");
        }
        StdOut.println("(elementy na stosie: " + s.size() + ")");
    }
}

