/*************************************************************************
 *  Kompilacja:  javac Stack.java
 *  Wykonanie:    java Stack < input.txt
 *
 *  Generyczny stos zaimplementowany za pomoc� listy powi�zanej. Ka�dy element
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
 *  Klasa <tt>Stack</tt> reprezentuje stos LIFO generycznych element�w.
 *  Obs�uguje standardowe operacje <em>push</em> i <em>pop</em>, a tak�e metody
 *  do podgl�dania elementu z wierzcho�ka, sprawdzania, czy stos jest pusty, i przechodzenia
 *  po elementach w porz�dku LIFO.
 *  <p>
 *  Wszystkie operacje na stosie z wyj�tkiem iterowania zajmuj� sta�y czas.
 *  <p>
 *  Dodatkow� dokumentacj� mo�na znale�� w <a href="/algs4/13stacks">podrozdziale 1.3</a> ksi��ki
 *  <i>Algorytmy, wydanie czwarte</i> Roberta Sedgewicka i Kevina Wayne'a.
 */
public class Stack<Item> implements Iterable<Item> {
    private int N;          // Wielko�� stosu
    private Node first;     // Wierzcho�ek stosu

    // Pomocnicza klasa dla listy powi�zanej
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
     * Zwraca liczb� element�w na stosie
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
     * Zg�asza wyj�tek, je�li stos jest pusty.
     */
    public Item pop() {
        if (isEmpty()) throw new RuntimeException("Brak element�w na stosie");
        Item item = first.item;        // Zapisywanie zwracanego elementu
        first = first.next;            // Usuwanie pierwszego w�z�a
        N--;
        return item;                   // Zwracanie zapisanego elementu
    }


   /**
     * Zwraca element ostatnio dodany do stosu.
     * Zg�asza wyj�tek, je�li stos jest pusty.
     */
    public Item peek() {
        if (isEmpty()) throw new RuntimeException("Brak element�w na stosie");
        return first.item;
    }

   /**
     * Zwraca �a�cuch reprezentuj�cy stos.
     */
    public String toString() {
        StringBuilder s = new StringBuilder();
        for (Item item : this)
            s.append(item + " ");
        return s.toString();
    }
       

   /**
     * Zwraca iterator stosu przechodz�cy po elementach w porz�dku LIFO.
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

