/*************************************************************************
 *  Kompilacja:  javac Point2D.java
 *  Wykonanie:    java Point2D N
 *  Zale�no�ci: StdOut.java
 *
 *  Niezmienny typ danych dla punkt�w w przestrzeni.
 *
 *  Klient testowy tworzy N losowych punkt�w w jednostce kwadratowej i
 *  oblicza minimaln� odleg�o�� mi�dzy dwoma z nich (przez atak si�owy).
 *
 *  % java Point2D 100
 *  Odleg�o�� minimalna = 0.006187844627988278
 *
 *************************************************************************/

import java.util.Arrays;
import java.util.Comparator;

public class Point2D implements Comparable<Point2D> {
    public static final Comparator<Point2D> X_ORDER = new ByXComparator();
    public static final Comparator<Point2D> Y_ORDER = new ByYComparator();

    public final Comparator<Point2D> DISTANCE_TO_ORDER = new DistanceToOrder();

    private final double x;    // Wsp�rz�dna x
    private final double y;    // Wsp�rz�dna y

    public Point2D(double x, double y) { this.x = x; this.y = y; }

    // Zwraca wsp�rz�dn� x punktu
    public double x() { return x; }

    // Zwraca wsp�rz�dn� y punktu
    public double y() { return y; }

    // Zwracanie promienia dla punktu wed�ug wsp�rz�dny biegunowych
    public double r() { return Math.sqrt(x*x + y*y); }

    // Zwracanie k�ta dla punktu wed�ug wsp�rz�dnych biegunowych
    // (mi�dzy -pi/2 a pi/2)
    public double theta() { return Math.atan2(y, x); }

    // Zwracanie odleg�o�ci euklidesowej mi�dzy danym punktem a punktem that
    public double distanceTo(Point2D that) {
       double dx = this.x - that.x;
       double dy = this.y - that.y;
       return Math.sqrt(dx*dx + dy*dy);
    }

    // Zwracanie kwadratu odleg�o�ci mi�dzy danym punktem a punktem that
    public double distanceSquaredTo(Point2D that) {
       double dx = this.x - that.x;
       double dy = this.y - that.y;
       return dx*dx + dy*dy;
    }

    // Por�wnywanie wed�ug wsp�rz�dnej y; r�wne elementy s� por�wnywane wed�ug wsp�rz�dnej x
    public int compareTo(Point2D that) {
        if (this.y < that.y) return -1;
        if (this.y > that.y) return +1;
        if (this.x < that.x) return -1;
        if (this.x > that.x) return +1;
        return 0;
    }

    // Por�wnywanie punkt�w wed�ug wsp�rz�dnej x; r�wne elementy por�wnywane s� wed�ug wsp�rz�dnej y
    private static class ByXComparator implements Comparator<Point2D> {
        public int compare(Point2D p, Point2D q) {
           if (p.x < q.x) return -1;
           if (p.x > q.x) return +1;
           if (p.y < q.y) return -1;
           if (p.y > q.y) return +1;
           return 0;
        }
    }

    // Por�wnywanie punkt�w wed�ug wsp�rz�dnej y; r�wne elementy por�wnywane s� wed�ug wsp�rz�dnej x
    private static class ByYComparator implements Comparator<Point2D> {
        public int compare(Point2D p, Point2D q) {
           if (p.y < q.y) return -1;
           if (p.y > q.y) return +1;
           if (p.x < q.x) return -1;
           if (p.x > q.x) return +1;
           return 0;
        }
    }
 
    // Por�wnywanie punkt�w wed�ug odleg�o�ci o danego punktu
    private class DistanceToOrder implements Comparator<Point2D> {
        public int compare(Point2D p, Point2D q) {
            double dist1 = distanceSquaredTo(p);
            double dist2 = distanceSquaredTo(q);
            if      (dist1 < dist2) return -1;
            else if (dist1 > dist2) return +1;
            else                    return  0;
        }
    }


    // Czy dany punkt jest r�wny y?
    public boolean equals(Object y) {
        if (y == this) return true;
        if (y == null) return false;
        if (y.getClass() != this.getClass()) return false;
        Point2D that = (Point2D) y;
        return this.x == that.x && this.y == that.y;
    }

    // Przekszta�canie na �a�cuch znak�w
    public String toString() {
        return "(" + x + ", " + y + ")";
    }

    // Wy�wietlanie za pomoc� StdDraw
    public void draw() {
        StdDraw.point(x, y);
    }

    // Wy�wietlanie linii z danego punktu p do q za pomoc� StdDraw
    public void drawTo(Point2D that) {
        StdDraw.line(this.x, this.y, that.x, that.y);
    }


    // Generowanie N losowych punkt�w w jednostce kwadratowej i wyznaczanie 
    // odleg�o�ci mi�dzy par� najbli�szych punkt�w
    public static void main(String[] args) {
        int N = Integer.parseInt(args[0]);

        // Generowanie N losowych punkt�w w jednostce kwadratowej
        Point2D[] points = new Point2D[N];
        for (int i = 0; i < N; i++) {
            double x = StdRandom.random();
            double y = StdRandom.random();
            points[i] = new Point2D(x, y);
        }

        // Wyznaczanie przez atak si�owy odleg�o�ci mi�dzy par� najbli�szych punkt�w 
        // (mo�na u�y� metody distanceSquaredTo() w celu poprawy wydajno�ci)
        double min = Double.POSITIVE_INFINITY;
        for (int i = 0; i < N; i++) {
            for (int j = i+1; j < N; j++) {
                double dist = points[i].distanceTo(points[j]);
                if (dist < min) min = dist;
            }
        }

        StdOut.println("Minimalna odleg�o�� = " + min);
    }
}
