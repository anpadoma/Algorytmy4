/*************************************************************************
 *  Kompilacja:  javac Queue.java
 *  Wykonanie:    java Queue < input.txt
 *
 *  Generyczna kolejka, zaimplementowana za pomoc� listy powi�zanej.
 *
 *  % java Queue < tobe.txt 
 *  to be or not to be (elementy w kolejce: 2)
 *
 *************************************************************************/

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 *  Klasa <tt>Queue</tt> reprezentuje kolejk� FIFO
 *  generycznych element�w.
 *  Obs�uguje standardowe operacje <em>enqueue</em> i <em>dequeue</em>,
 *  a tak�e metody do podgl�dania elementu z wierzcho�ka,
 *  sprawdzania, czy kolejka jest pusta, i poruszania si� po
 *  elementach w porz�dku FIFO.
 *  <p>
 *  Wszystkie operacje na kolejce (opr�cz iterowania) dzia�aj� w sta�ym czasie.
 *  <p>
 *  Dodatkow� dokumentacj� mo�na znale�� w <a href="http://algs4.cs.princeton.edu/13stacks">podrozdziale 1.3</a> ksi��ki
 *  <i>Algorytmy, wydanie czwarte</i> Roberta Sedgewicka i Kevina Wayne'a.
 */
public class Queue<Item> implements Iterable<Item> {
    private int N;         // Liczba element�w w kolejce
    private Node first;    // Pocz�tek kolejki
    private Node last;     // Koniec kolejki

    // Klasa pomocnicza do tworzenia listy powi�zanej
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
     * Zwraca liczb� element�w w kolejce
     */
    public int size() {
        return N;     
    }

   /**
     * Zwraca element ostatnio dodany do kolejki.
     * Zg�asza wyj�tek, je�li kolejka jest pusta
     */
    public Item peek() {
        if (isEmpty()) throw new RuntimeException("Brak element�w w kolejce");
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
     * Zg�asza wyj�tek, je�li kolejka jest pusta
     */
    public Item dequeue() {
        if (isEmpty()) throw new RuntimeException("Brak element�w w kolejce");
        Item item = first.item;
        first = first.next;
        N--;
        if (isEmpty()) last = null;   // W celu unikni�cia wyciekania pami�ci
        return item;
    }

   /**
     * Zwraca �a�cuch znak�w reprezentuj�cy kolejk�
     */
    public String toString() {
        StringBuilder s = new StringBuilder();
        for (Item item : this)
            s.append(item + " ");
        return s.toString();
    } 
 

   /**
     * Zwraca iterator, kt�ry przechodzi po elementach kolejki w porz�dku FIFO
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
