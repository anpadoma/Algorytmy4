/*************************************************************************
 *  Kopilacja:  javac Edge.java
 *  Wykonanie:    java Edge
 *
 *  Typ danych dla niezmiennych kraw�dzi wa�onych.
 *
 *************************************************************************/

/**
 *  Klasa <tt>Edge</tt> reprezetuje kraw�d� wa�on� nieskierowanego grafu.
 *  <p>
 *  Dodatkow� dokumentacj� mo�na znale�� w <a href="/algs4/43mst">podrozdziale 4.3</a> ksi��ki
 *  <i>Algorytmy, wydanie czwarte</i> Roberta Sedgewicka i Kevina Wayne'a.
 */
public class Edge implements Comparable<Edge> { 

    private final int v;
    private final int w;
    private final double weight;

   /**
     * Tworzenie kraw�dzi o danej wadze mi�dzy v a w.
     */
    public Edge(int v, int w, double weight) {
        this.v = v;
        this.w = w;
        this.weight = weight;
    }

   /**
     * Zwracanie wagi kraw�dzi.
     */
    public double weight() {
        return weight;
    }

   /**
     * Zwracanie jednego z punkt�w ko�cowych kraw�dzi.
     */
    public int either() {
        return v;
    }

   /**
     * Zwracanie punktu ko�cowego kraw�dzi innego ni� podany wierzcho�ek
     * (chyba �e kraw�d� tworzy p�tl� zwrotn�).
     */
    public int other(int vertex) {
        if      (vertex == v) return w;
        else if (vertex == w) return v;
        else throw new RuntimeException("Niedozwolony punkt koncowy");
    }

   /**
     * Por�wnywanie kraw�dzi na podstawie wag.
     */
    public int compareTo(Edge that) {
        if      (this.weight() < that.weight()) return -1;
        else if (this.weight() > that.weight()) return +1;
        else                                    return  0;
    }

   /**
     * Zwracanie �a�cucha znak�w reprezentuj�cego kraw�d�.
     */
    public String toString() {
        return String.format("%d-%d %.2f", v, w, weight);
    }


   /**
     * Klient testowy.
     */
    public static void main(String[] args) {
        Edge e = new Edge(12, 23, 3.14);
        StdOut.println(e);
    }
}
