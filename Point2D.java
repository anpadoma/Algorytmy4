/*************************************************************************
 *  Kompilacja:  javac Point2D.java
 *  Wykonanie:    java Point2D N
 *  Zale¿noœci: StdOut.java
 *
 *  Niezmienny typ danych dla punktów w przestrzeni.
 *
 *  Klient testowy tworzy N losowych punktów w jednostce kwadratowej i
 *  oblicza minimaln¹ odleg³oœæ miêdzy dwoma z nich (przez atak si³owy).
 *
 *  % java Point2D 100
 *  Odleg³oœæ minimalna = 0.006187844627988278
 *
 *************************************************************************/

import java.util.Arrays;
import java.util.Comparator;

public class Point2D implements Comparable<Point2D> {
    public static final Comparator<Point2D> X_ORDER = new ByXComparator();
    public static final Comparator<Point2D> Y_ORDER = new ByYComparator();

    public final Comparator<Point2D> DISTANCE_TO_ORDER = new DistanceToOrder();

    private final double x;    // Wspó³rzêdna x
    private final double y;    // Wspó³rzêdna y

    public Point2D(double x, double y) { this.x = x; this.y = y; }

    // Zwraca wspó³rzêdn¹ x punktu
    public double x() { return x; }

    // Zwraca wspó³rzêdn¹ y punktu
    public double y() { return y; }

    // Zwracanie promienia dla punktu wed³ug wspó³rzêdny biegunowych
    public double r() { return Math.sqrt(x*x + y*y); }

    // Zwracanie k¹ta dla punktu wed³ug wspó³rzêdnych biegunowych
    // (miêdzy -pi/2 a pi/2)
    public double theta() { return Math.atan2(y, x); }

    // Zwracanie odleg³oœci euklidesowej miêdzy danym punktem a punktem that
    public double distanceTo(Point2D that) {
       double dx = this.x - that.x;
       double dy = this.y - that.y;
       return Math.sqrt(dx*dx + dy*dy);
    }

    // Zwracanie kwadratu odleg³oœci miêdzy danym punktem a punktem that
    public double distanceSquaredTo(Point2D that) {
       double dx = this.x - that.x;
       double dy = this.y - that.y;
       return dx*dx + dy*dy;
    }

    // Porównywanie wed³ug wspó³rzêdnej y; równe elementy s¹ porównywane wed³ug wspó³rzêdnej x
    public int compareTo(Point2D that) {
        if (this.y < that.y) return -1;
        if (this.y > that.y) return +1;
        if (this.x < that.x) return -1;
        if (this.x > that.x) return +1;
        return 0;
    }

    // Porównywanie punktów wed³ug wspó³rzêdnej x; równe elementy porównywane s¹ wed³ug wspó³rzêdnej y
    private static class ByXComparator implements Comparator<Point2D> {
        public int compare(Point2D p, Point2D q) {
           if (p.x < q.x) return -1;
           if (p.x > q.x) return +1;
           if (p.y < q.y) return -1;
           if (p.y > q.y) return +1;
           return 0;
        }
    }

    // Porównywanie punktów wed³ug wspó³rzêdnej y; równe elementy porównywane s¹ wed³ug wspó³rzêdnej x
    private static class ByYComparator implements Comparator<Point2D> {
        public int compare(Point2D p, Point2D q) {
           if (p.y < q.y) return -1;
           if (p.y > q.y) return +1;
           if (p.x < q.x) return -1;
           if (p.x > q.x) return +1;
           return 0;
        }
    }
 
    // Porównywanie punktów wed³ug odleg³oœci o danego punktu
    private class DistanceToOrder implements Comparator<Point2D> {
        public int compare(Point2D p, Point2D q) {
            double dist1 = distanceSquaredTo(p);
            double dist2 = distanceSquaredTo(q);
            if      (dist1 < dist2) return -1;
            else if (dist1 > dist2) return +1;
            else                    return  0;
        }
    }


    // Czy dany punkt jest równy y?
    public boolean equals(Object y) {
        if (y == this) return true;
        if (y == null) return false;
        if (y.getClass() != this.getClass()) return false;
        Point2D that = (Point2D) y;
        return this.x == that.x && this.y == that.y;
    }

    // Przekszta³canie na ³añcuch znaków
    public String toString() {
        return "(" + x + ", " + y + ")";
    }

    // Wyœwietlanie za pomoc¹ StdDraw
    public void draw() {
        StdDraw.point(x, y);
    }

    // Wyœwietlanie linii z danego punktu p do q za pomoc¹ StdDraw
    public void drawTo(Point2D that) {
        StdDraw.line(this.x, this.y, that.x, that.y);
    }


    // Generowanie N losowych punktów w jednostce kwadratowej i wyznaczanie 
    // odleg³oœci miêdzy par¹ najbli¿szych punktów
    public static void main(String[] args) {
        int N = Integer.parseInt(args[0]);

        // Generowanie N losowych punktów w jednostce kwadratowej
        Point2D[] points = new Point2D[N];
        for (int i = 0; i < N; i++) {
            double x = StdRandom.random();
            double y = StdRandom.random();
            points[i] = new Point2D(x, y);
        }

        // Wyznaczanie przez atak si³owy odleg³oœci miêdzy par¹ najbli¿szych punktów 
        // (mo¿na u¿yæ metody distanceSquaredTo() w celu poprawy wydajnoœci)
        double min = Double.POSITIVE_INFINITY;
        for (int i = 0; i < N; i++) {
            for (int j = i+1; j < N; j++) {
                double dist = points[i].distanceTo(points[j]);
                if (dist < min) min = dist;
            }
        }

        StdOut.println("Minimalna odleg³oœæ = " + min);
    }
}
