/*************************************************************************
 *  Kompilacja:  javac Bag.java
 *  Wykonanie:    java Bag < input.txt
 *
 *  Ogólny wielozbiór, zaimplementowany za pomoc¹ listy powi¹zanej.
 *
 *************************************************************************/

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 *  Klasa <tt>Bag</tt> reprezentuje wielozbiór generycznych 
 *  elementów. Obs³uguje wstawianie elementów i iterowanie 
 *  po nich w dowolnej kolejnoœci.
 *  <p>
 *  Operacje <em>add</em>, <em>isEmpty</em> i <em>size</em> dzia³aj¹
 *  w sta³ym czasie. Iterowanie zajmuje czas proporcjonalnie do liczby elementów.
 *  <p>
 *  Dodatkow¹ dokumentacjê znajdziesz w <a href="http://algs4.cs.princeton.edu/13stacks">podrozdziale 1.3</a> ksi¹¿ki
 *  <i>Algorytmy, wydanie czwarte</i> Roberta Sedgewicka i Kevina Wayne'a.
 */
public class Bag<Item> implements Iterable<Item> {
    private int N;         // Liczba elementów w wielozbiorze.
    private Node first;    // Pocz¹tek wielozbioru.

    // Klasa pomocnicza z list¹ powi¹zan¹.
    private class Node {
        private Item item;
        private Node next;
    }

   /**
     * Tworzenie pustego wielozbioru.
     */
    public Bag() {
        first = null;
        N = 0;
    }

   /**
     * Czy wielozbiór jest pusty?
     */
    public boolean isEmpty() {
        return first == null;
    }

   /**
     * Zwracanie liczby elementów w wielozbiorze.
     */
    public int size() {
        return N;
    }

   /**
     * Dodawanie elementu do wielozbioru.
     */
    public void add(Item item) {
        Node oldfirst = first;
        first = new Node();
        first.item = item;
        first.next = oldfirst;
        N++;
    }


   /**
     * Zwracanie iteratora, który przechodzi po elementach wielozbioru.
     */
    public Iterator<Item> iterator()  {
        return new ListIterator();  
    }

    // Iterator (nie ma implementacji opcjonalnej operacji remove()).
    private class ListIterator implements Iterator<Item> {
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

}
