/*************************************************************************
 *  Kompilacja:  javac DirectedEdge.java
 *  Wykonanie:    java DirectedEdge
 *
 *  Niezmienne wa�one kraw�dzie skierowane.
 *
 *************************************************************************/

/**
 *  Klasa <tt>DirectedEdge</tt> reprezentuje kraw�d� wa�on� w grafie skierowanym.
 *  <p>
 *  Dodatkow� dokumentacj� znajdziesz w <a href="http://algs4.cs.princeton.edu/44sp">podrozdziale 4.4</a> ksi��ki
 *  <i>Algorytmy, wydanie czwarte</i> Roberta Sedgewicka i Kevina Wayne'a.
 */

public class DirectedEdge { 
    private final int v;
    private final int w;
    private final double weight;

   /**
     * Tworzy kraw�d� skierowan� z v do w o danej wadze.
     */
    public DirectedEdge(int v, int w, double weight) {
        this.v = v;
        this.w = w;
        this.weight = weight;
    }

   /**
     * Zwraca wierzcho�ek pocz�tkowy kraw�dzi.
     */
    public int from() {
        return v;
    }

   /**
     * Zwraca wierzcho�ek ko�cowy kraw�dzi.
     */
    public int to() {
        return w;
    }

   /**
     * Zwraca wag� kraw�dzi.
     */
    public double weight() { return weight; }

   /**
     * Zwraca �a�cuch znak�w reprezentuj�cy dan� kraw�d�.
     */
    public String toString() {
        return v + "->" + w + " " + String.format("%5.2f", weight);
    }

   /**
     * Klient testowy.
     */
    public static void main(String[] args) {
        DirectedEdge e = new DirectedEdge(12, 23, 3.14);
        StdOut.println(e);
    }
}
