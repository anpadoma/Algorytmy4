/*************************************************************************
 *  Kompilacja:  javac Interval2D.java
 *  Wykonanie:    java Interval2D
 *  
 *  Typ danych dla dwuwymiarowego przedzia³u.
 *
 *************************************************************************/

public class Interval2D {
    private final Interval1D x;
    private final Interval1D y;

    public Interval2D(Interval1D x, Interval1D y) {
        this.x = x;
        this.y = y;
    }

    // Czy dany przedzia³ pokrywa siê z przedzia³em that?
    public boolean intersects(Interval2D that) {
        if (!this.x.intersects(that.x)) return false;
        if (!this.y.intersects(that.y)) return false;
        return true;
    }

    // Czy dany przedzia³ obejmuje x?
    public boolean contains(Point2D p) {
        return x.contains(p.x())  && y.contains(p.y());
    }

    // Obszar danego przedzia³u
    public double area() {
        return x.length() * y.length();
    }
        
    public String toString() {
        return x + " x " + y;
    }

    // Klient testowy
    public static void main(String[] args) {
        Interval1D interval1 = new Interval1D(15.0, 33.0);
        Interval1D interval2 = new Interval1D(45.0, 60.0);
        Interval2D interval = new Interval2D(interval1, interval2);
        StdOut.println(interval);
    }
}
