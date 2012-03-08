/*************************************************************************
 *  Kompilacja:  javac Bag.java
 *  Wykonanie:    java Bag < input.txt
 *
 *  Og�lny wielozbi�r, zaimplementowany za pomoc� listy powi�zanej.
 *
 *************************************************************************/

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 *  Klasa <tt>Bag</tt> reprezentuje wielozbi�r generycznych 
 *  element�w. Obs�uguje wstawianie element�w i iterowanie 
 *  po nich w dowolnej kolejno�ci.
 *  <p>
 *  Operacje <em>add</em>, <em>isEmpty</em> i <em>size</em> dzia�aj�
 *  w sta�ym czasie. Iterowanie zajmuje czas proporcjonalnie do liczby element�w.
 *  <p>
 *  Dodatkow� dokumentacj� znajdziesz w <a href="http://algs4.cs.princeton.edu/13stacks">podrozdziale 1.3</a> ksi��ki
 *  <i>Algorytmy, wydanie czwarte</i> Roberta Sedgewicka i Kevina Wayne'a.
 */
public class Bag<Item> implements Iterable<Item> {
    private int N;         // Liczba element�w w wielozbiorze.
    private Node first;    // Pocz�tek wielozbioru.

    // Klasa pomocnicza z list� powi�zan�.
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
     * Czy wielozbi�r jest pusty?
     */
    public boolean isEmpty() {
        return first == null;
    }

   /**
     * Zwracanie liczby element�w w wielozbiorze.
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
     * Zwracanie iteratora, kt�ry przechodzi po elementach wielozbioru.
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
