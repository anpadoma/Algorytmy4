/*************************************************************************
 *  Kompilacja:  javac Queue.java
 *  Wykonanie:    java Queue < input.txt
 *
 *  Generyczna kolejka, zaimplementowana za pomoc¹ listy powi¹zanej.
 *
 *  % java Queue < tobe.txt 
 *  to be or not to be (elementy w kolejce: 2)
 *
 *************************************************************************/

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 *  Klasa <tt>Queue</tt> reprezentuje kolejkê FIFO
 *  generycznych elementów.
 *  Obs³uguje standardowe operacje <em>enqueue</em> i <em>dequeue</em>,
 *  a tak¿e metody do podgl¹dania elementu z wierzcho³ka,
 *  sprawdzania, czy kolejka jest pusta, i poruszania siê po
 *  elementach w porz¹dku FIFO.
 *  <p>
 *  Wszystkie operacje na kolejce (oprócz iterowania) dzia³aj¹ w sta³ym czasie.
 *  <p>
 *  Dodatkow¹ dokumentacjê mo¿na znaleŸæ w <a href="http://algs4.cs.princeton.edu/13stacks">podrozdziale 1.3</a> ksi¹¿ki
 *  <i>Algorytmy, wydanie czwarte</i> Roberta Sedgewicka i Kevina Wayne'a.
 */
public class Queue<Item> implements Iterable<Item> {
    private int N;         // Liczba elementów w kolejce
    private Node first;    // Pocz¹tek kolejki
    private Node last;     // Koniec kolejki

    // Klasa pomocnicza do tworzenia listy powi¹zanej
    private class Node {
        private Item item;
        private Node next;
    }

   /**
     * Tworzenie pustej kolejki
     */
    public Queue() {
        first = null;
        last  = null;
    }

   /**
     * Czy kolejka jest pusta?
     */
    public boolean isEmpty() {
        return first == null;
    }

   /**
     * Zwraca liczbê elementów w kolejce
     */
    public int size() {
        return N;     
    }

   /**
     * Zwraca element ostatnio dodany do kolejki.
     * Zg³asza wyj¹tek, jeœli kolejka jest pusta
     */
    public Item peek() {
        if (isEmpty()) throw new RuntimeException("Brak elementów w kolejce");
        return first.item;
    }

   /**
     * Dodaje element do kolejki
     */
    public void enqueue(Item item) {
        Node x = new Node();
        x.item = item;
        if (isEmpty()) { first = x;     last = x; }
        else           { last.next = x; last = x; }
        N++;
    }

   /**
     * Usuta i zwraca ostatnio dodany element kolejki.
     * Zg³asza wyj¹tek, jeœli kolejka jest pusta
     */
    public Item dequeue() {
        if (isEmpty()) throw new RuntimeException("Brak elementów w kolejce");
        Item item = first.item;
        first = first.next;
        N--;
        if (isEmpty()) last = null;   // W celu unikniêcia wyciekania pamiêci
        return item;
    }

   /**
     * Zwraca ³añcuch znaków reprezentuj¹cy kolejkê
     */
    public String toString() {
        StringBuilder s = new StringBuilder();
        for (Item item : this)
            s.append(item + " ");
        return s.toString();
    } 
 

   /**
     * Zwraca iterator, który przechodzi po elementach kolejki w porz¹dku FIFO
     */
    public Iterator<Item> iterator()  {
        return new FIFOIterator();  
    }

    // Iterator (bez implementacji opcjonalnej metody remove())
    private class FIFOIterator implements Iterator<Item> {
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
        Queue<String> q = new Queue<String>();
        while (!StdIn.isEmpty()) {
            String item = StdIn.readString();
            if (!item.equals("-")) q.enqueue(item);
            else if (!q.isEmpty()) StdOut.print(q.dequeue() + " ");
        }
        StdOut.println("(elementy w kolejce: " + q.size() + ")");
    }
}
