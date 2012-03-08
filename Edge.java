/*************************************************************************
 *  Kopilacja:  javac Edge.java
 *  Wykonanie:    java Edge
 *
 *  Typ danych dla niezmiennych krawêdzi wa¿onych.
 *
 *************************************************************************/

/**
 *  Klasa <tt>Edge</tt> reprezetuje krawêdŸ wa¿on¹ nieskierowanego grafu.
 *  <p>
 *  Dodatkow¹ dokumentacjê mo¿na znaleŸæ w <a href="/algs4/43mst">podrozdziale 4.3</a> ksi¹¿ki
 *  <i>Algorytmy, wydanie czwarte</i> Roberta Sedgewicka i Kevina Wayne'a.
 */
public class Edge implements Comparable<Edge> { 

    private final int v;
    private final int w;
    private final double weight;

   /**
     * Tworzenie krawêdzi o danej wadze miêdzy v a w.
     */
    public Edge(int v, int w, double weight) {
        this.v = v;
        this.w = w;
        this.weight = weight;
    }

   /**
     * Zwracanie wagi krawêdzi.
     */
    public double weight() {
        return weight;
    }

   /**
     * Zwracanie jednego z punktów koñcowych krawêdzi.
     */
    public int either() {
        return v;
    }

   /**
     * Zwracanie punktu koñcowego krawêdzi innego ni¿ podany wierzcho³ek
     * (chyba ¿e krawêdŸ tworzy pêtlê zwrotn¹).
     */
    public int other(int vertex) {
        if      (vertex == v) return w;
        else if (vertex == w) return v;
        else throw new RuntimeException("Niedozwolony punkt koncowy");
    }

   /**
     * Porównywanie krawêdzi na podstawie wag.
     */
    public int compareTo(Edge that) {
        if      (this.weight() < that.weight()) return -1;
        else if (this.weight() > that.weight()) return +1;
        else                                    return  0;
    }

   /**
     * Zwracanie ³añcucha znaków reprezentuj¹cego krawêdŸ.
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
