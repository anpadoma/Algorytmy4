/*************************************************************************
 *  Kompilacja:  javac ST.java
 *  Wykonanie:    java ST
 *  
 *  Implementacja posortowanej tablicy symboli oparta na klasie java.util.TreeMap.
 *  Powtórzenia s¹ niedozwolone.
 *
 *  % java ST
 *
 *************************************************************************/

import java.util.TreeMap;
import java.util.SortedMap;


/**
 *  Klasa reprezentuje uporz¹dkowan¹ tablicê symboli. Zak³adamy, ¿e elementy
 *  s¹ zgodne z interfejsem <tt>Comparable</tt>.
 *  Udostêpnia standardowe metody <em>put</em>, <em>get</em>, <em>contains</em>
 *  i <em>remove</em>.
 *  Obejmuje te¿ metody dla danych uporz¹dkowanych do wyszukiwania <em>minimum</em>,
 *  <em>maksimum</em>, <em>pod³ogi</em> i <em>sufitu</em>.
 *  <p>
 *  W klasie zastosowano konwencjê, zgodnie z którymi wartoœci nie mog¹ byæ równe null. Ustawienie
 *  powi¹zanej z kluczem wartoœci na null jest równoznaczne z usuniêciem klucza.
 *  <p>
 *  W tej implementacji wykorzystano zbalansowane drzewo wyszukiwañ binarnych.
 *  Metody <em>add</em>, <em>contains</em>, <em>remove</em>, <em>minimum</em>,
 *  <em>maximum</em>, <em>ceiling</em> i <em>floor</em> dzia³aj¹
 *  w czasie logarytmicznym.
 *  <p>
 *  Dodatkow¹ dokumentacjê mo¿na znaleŸæ w <a href="http://algs4.cs.princeton.edu/35applications">podrozdziale 4.5</a> ksi¹¿ki
 *  <i>Algorytmy, wydanie czwarte</i> Roberta Sedgewicka i Kevina Wayne'a.
 */
public class ST<Key extends Comparable<Key>, Value> {
    private TreeMap<Key, Value> st;

    /**
     * Tworzenie pustej tablicy symboli.
     */
    public ST() {
        st = new TreeMap<Key, Value>();
    }

    /**
     * Umieszczanie pary klucz-wartoœæ w tablicy symboli. Jeœli wartoœæ to null, 
     * klucz jest usuwany z tablicy.
     */
    public void put(Key key, Value val) {
        if (val == null) st.remove(key);
        else             st.put(key, val);
    }

    /**
     * Zwraca wartoœæ powi¹zan¹ z danym kluczem (lub null, jeœli klucz nie wystêpuje w tablicy).
     */
    public Value get(Key key) {
        return st.get(key);
    }

    /**
     * Usuwa klucz (i powi¹zan¹ wartoœæ) z tablicy.
     * Zwraca wartoœæ powi¹zan¹ z danym kluczem (lub null, jeœli klucz nie wystêpuje w tablicy)
     */
    public Value delete(Key key) {
        return st.remove(key);
    }

    /**
     * Czy klucz wystêpuje w tablicy?
     */
    public boolean contains(Key key) {
        return st.containsKey(key);
    }

    /**
     * Ile kluczy wystêpuje w tablicy?
     */
    public int size() {
        return st.size();
    }

    /**
     * Zwraca obiekt <tt>Iterable</tt> z kluczami z tablicy.
     * Aby przejœæ po wszystkich kluczach tablicy symboli <tt>st</tt>, nale¿y u¿yæ
     * notacji foreach: <tt>for (Key key : st.keys())</tt>.
     */ 
    public Iterable<Key> keys() {
        return st.keySet();
    }

    /**
     * Zwraca najmniejszy klucz z tablicy.
     */ 
    public Key min() {
        return st.firstKey();
    }

    /**
     * Zwraca najwiêkszy klucz z tablicy.
     */ 
    public Key max() {
        return st.lastKey();
    }


    /**
     * Zwraca najmniejszy klucz z tablicy wiêkszy lub równy k.
     */ 
    public Key ceil(Key k) {
        SortedMap<Key, Value> tail = st.tailMap(k);
        if (tail.isEmpty()) return null;
        else return tail.firstKey();
    }

    /**
     * Zwraca najwiêkszy kluczy tablicy mniejszy lub równy k.
     */ 
    public Key floor(Key k) {
        if (st.containsKey(k)) return k;

        SortedMap<Key, Value> head = st.headMap(k);
        if (head.isEmpty()) return null;
        else return head.lastKey();
    }

   /***********************************************************************
    * Procedura testowa
    **********************************************************************/
    public static void main(String[] args) {
        ST<String, String> st = new ST<String, String>();

       // Wstawianie par klucz-wartoœæ
        st.put("www.cs.princeton.edu",   "128.112.136.11");
        st.put("www.cs.princeton.edu",   "128.112.136.35");    // Nadpisuje dawn¹ wartoœæ
        st.put("www.princeton.edu",      "128.112.130.211");
        st.put("www.math.princeton.edu", "128.112.18.11");
        st.put("www.yale.edu",           "130.132.51.8");
        st.put("www.amazon.com",         "207.171.163.90");
        st.put("www.simpsons.com",       "209.123.16.34");
        st.put("www.stanford.edu",       "171.67.16.120");
        st.put("www.google.com",         "64.233.161.99");
        st.put("www.ibm.com",            "129.42.16.99");
        st.put("www.apple.com",          "17.254.0.91");
        st.put("www.slashdot.com",       "66.35.250.150");
        st.put("www.whitehouse.gov",     "204.153.49.136");
        st.put("www.espn.com",           "199.181.132.250");
        st.put("www.snopes.com",         "66.165.133.65");
        st.put("www.movies.com",         "199.181.132.250");
        st.put("www.cnn.com",            "64.236.16.20");
        st.put("www.iitb.ac.in",         "202.68.145.210");


        System.out.println(st.get("www.cs.princeton.edu"));
        System.out.println(st.get("www.harvardsucks.com"));
        System.out.println(st.get("www.simpsons.com"));
        System.out.println();

        System.out.println("ceil(www.simpsonr.com) = " + st.ceil("www.simpsonr.com"));
        System.out.println("ceil(www.simpsons.com) = " + st.ceil("www.simpsons.com"));
        System.out.println("ceil(www.simpsont.com) = " + st.ceil("www.simpsont.com"));
        System.out.println("floor(www.simpsonr.com) = " + st.floor("www.simpsonr.com"));
        System.out.println("floor(www.simpsons.com) = " + st.floor("www.simpsons.com"));
        System.out.println("floor(www.simpsont.com) = " + st.floor("www.simpsont.com"));

        System.out.println();

        System.out.println("Klucz minimalny : " + st.min());
        System.out.println("Klucz maksymalny: " + st.max());
        System.out.println("Wielkoœæ        : " + st.size());
        System.out.println();

        // Wyœwietla wszystkie pary klucz-wartoœæ w kolejnoœci leksykograficznej
        for (String s : st.keys())
            System.out.println(s + " " + st.get(s));
    }

}
