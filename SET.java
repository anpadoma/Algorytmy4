/*************************************************************************
 *  Kompilacja:  javac SET.java
 *  Wykonanie:    java SET
 *  
 *  Implementacja oparta na bibliotece TreeSet Javy.
 *  Powt�rzenia s� niedozwolone.
 *
 *  % java SET
 *  128.112.136.11
 *  208.216.181.15
 *  null
 *
 *  Uwagi
 *  -------
 *   - wed�ug metody equals() dwa pusty zbiory s� r�wne,
 *     nawet je�li s� sparametryzowane za pomoc� r�nych typ�w generycznych.
 *     Jest to sp�jne z dzia�aniem metody equals() w
 *     kolekcjach z Javy.
 *
 *************************************************************************/

import java.util.TreeSet;
import java.util.Iterator;
import java.util.SortedSet;



/**
 *  Klasa <tt>SET</tt> reprezentuje uporz�dkowany zbi�r. Zak�adamy, �e 
 *  elementy s� zgodne z interfejsem <tt>Comparable</tt>.
 *  Udost�pnia standardowe metody <em>add</em>, <em>contains</em> i <em>remove</em>.
 *  Obejmuje te� metody dla uporz�dkowanych danych do znajdowania <em>minimum</em>,
 *  <em>maksimum</em>, <em>pod�ogi</em> i <em>sufitu</em>.
 *  <p>
 *  W tej implementacji u�yto zbalansowanego drzewa wyszukiwa� binarnych.
 *  Metody <em>add</em>, <em>contains</em>, <em>remove</em>, <em>minimum</em>,
 *  <em>maximum</em>, <em>ceiling</em> i <em>floor</em> dzia�aj� w 
 *  czasie logarytmicznym.
 *  <p>
 *  Dodatkow� dokumentacj� mo�na znale�� w <a href="/algs4/45applications">podrozdziale 4.5</a> ksi��ki
 *  <i>Algorytmy, wydanie czwarte</i> Roberta Sedgewicka i Kevina Wayne'a.
 */

public class SET<Key extends Comparable<Key>> implements Iterable<Key> {
    private TreeSet<Key> set;

    /**
     * Tworzy pusty zbi�r.
     */
    public SET() {
        set = new TreeSet<Key>();
    }

    /**
     * Czy zbi�r jest pusty?
     */
    public boolean isEmpty() {
        return set.isEmpty();
    }
 
    /**
     * Dodawanie klucza do zbioru.
     */
    public void add(Key key) {
        set.add(key);
    }

    /**
     * Czy zbi�r obejmuje dany klucz?
     */
    public boolean contains(Key key) {
        return set.contains(key);
    }

    /**
     * Usuwa dany klucz ze zbioru.
     */
    public void remove(Key key) {
        set.remove(key);
    }

    /**
     * Zwraca liczb� kluczy w zbiorze.
     */
    public int size() {
        return set.size();
    }

    /**
     * Zwraca iterator dla zbioru.
     */
    public Iterator<Key> iterator() {
        return set.iterator();
    }

    /**
     * Zwraca klucz ze zbioru maj�cy maksymaln� warto��.
     */
    public Key max() {
        return set.last();
    }

    /**
     * Zwraca klucz ze zbioru maj�cy minimaln� warto��.
     */
    public Key min() {
        return set.first();
    }

    /**
     * Zwraca najmniejszy klucz z danego zbioru o warto�ci wi�kszej lub r�wnej k.
     */
    public Key ceil(Key k) {
        SortedSet<Key> tail = set.tailSet(k);
        if (tail.isEmpty()) return null;
        else return tail.first();
    }

    /**
     * Zwraca najwi�kszy klucze z danego zbioru o warto�ci mniejszej lub r�wnej k.
     */
    public Key floor(Key k) {
        if (set.contains(k)) return k;
 
        SortedSet<Key> head = set.headSet(k);
        if (head.isEmpty()) return null;
        else return head.last();
    }

    /**
     * Zwraca sum� danego zbioru i zbioru that.
     */
    public SET<Key> union(SET<Key> that) {
        SET<Key> c = new SET<Key>();
        for (Key x : this) { c.add(x); }
        for (Key x : that) { c.add(x); }
        return c;
    }

    /**
     * Zwraca cz�� wsp�ln� danego zbioru i zbioru that.
     */
    public SET<Key> intersects(SET<Key> that) {
        SET<Key> c = new SET<Key>();
        if (this.size() < that.size()) {
            for (Key x : this) {
                if (that.contains(x)) c.add(x);
            }
        }
        else {
            for (Key x : that) {
                if (this.contains(x)) c.add(x);
            }
        }
        return c;
    }

    /**
     * Czy dany zbi�r jest r�wny zbiorowi that?
     */
    public boolean equals(Object y) {
        if (y == this) return true;
        if (y == null) return false;
        if (y.getClass() != this.getClass()) return false;
        SET<Key> that = (SET<Key>) y;
        if (this.size() != that.size()) return false;
        try {
            for (Key k : this)
                if (!that.contains(k)) return false;
        }
        catch (ClassCastException exception) {
            return false;
        }
        return true;
    }

   /***********************************************************************
    * Procedura testowa
    **********************************************************************/
    public static void main(String[] args) {
        SET<String> set = new SET<String>();


        // Wstawianie kilku kluczy
        set.add("www.cs.princeton.edu");
        set.add("www.cs.princeton.edu");    // Nadpisanie dawnej warto�ci
        set.add("www.princeton.edu");
        set.add("www.math.princeton.edu");
        set.add("www.yale.edu");
        set.add("www.amazon.com");
        set.add("www.simpsons.com");
        set.add("www.stanford.edu");
        set.add("www.google.com");
        set.add("www.ibm.com");
        set.add("www.apple.com");
        set.add("www.slashdot.com");
        set.add("www.whitehouse.gov");
        set.add("www.espn.com");
        set.add("www.snopes.com");
        set.add("www.movies.com");
        set.add("www.cnn.com");
        set.add("www.iitb.ac.in");


        System.out.println(set.contains("www.cs.princeton.edu"));
        System.out.println(!set.contains("www.harvardsucks.com"));
        System.out.println(set.contains("www.simpsons.com"));
        System.out.println();

        System.out.println("ceil(www.simpsonr.com) = " + set.ceil("www.simpsonr.com"));
        System.out.println("ceil(www.simpsons.com) = " + set.ceil("www.simpsons.com"));
        System.out.println("ceil(www.simpsont.com) = " + set.ceil("www.simpsont.com"));
        System.out.println("floor(www.simpsonr.com) = " + set.floor("www.simpsonr.com"));
        System.out.println("floor(www.simpsons.com) = " + set.floor("www.simpsons.com"));
        System.out.println("floor(www.simpsont.com) = " + set.floor("www.simpsont.com"));
        System.out.println();


        // Wy�wietlanie wszystkich kluczy w porz�dku leksykograficznym
        for (String s : set) {
            System.out.println(s);
        }

    }

}
