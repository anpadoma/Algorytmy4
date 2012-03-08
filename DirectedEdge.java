/*************************************************************************
 *  Kompilacja:  javac DirectedEdge.java
 *  Wykonanie:    java DirectedEdge
 *
 *  Niezmienne wa¿one krawêdzie skierowane.
 *
 *************************************************************************/

/**
 *  Klasa <tt>DirectedEdge</tt> reprezentuje krawêdŸ wa¿on¹ w grafie skierowanym.
 *  <p>
 *  Dodatkow¹ dokumentacjê znajdziesz w <a href="http://algs4.cs.princeton.edu/44sp">podrozdziale 4.4</a> ksi¹¿ki
 *  <i>Algorytmy, wydanie czwarte</i> Roberta Sedgewicka i Kevina Wayne'a.
 */

public class DirectedEdge { 
    private final int v;
    private final int w;
    private final double weight;

   /**
     * Tworzy krawêdŸ skierowan¹ z v do w o danej wadze.
     */
    public DirectedEdge(int v, int w, double weight) {
        this.v = v;
        this.w = w;
        this.weight = weight;
    }

   /**
     * Zwraca wierzcho³ek pocz¹tkowy krawêdzi.
     */
    public int from() {
        return v;
    }

   /**
     * Zwraca wierzcho³ek koñcowy krawêdzi.
     */
    public int to() {
        return w;
    }

   /**
     * Zwraca wagê krawêdzi.
     */
    public double weight() { return weight; }

   /**
     * Zwraca ³añcuch znaków reprezentuj¹cy dan¹ krawêdŸ.
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
