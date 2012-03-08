/*************************************************************************
 *  Kompilacja:  javac GrahamaScan.java
 *  Wykonanie:    java GrahamScan < input.txt
 * 
 *  Tworzy punkty ze standardowego wejœcia i wyznacza otoczkê wypuk³¹
 *  za pomoc¹ algorytmu Grahama.
 *
 *************************************************************************/

import java.util.Arrays;

public class GrahamScan {
    private Stack<Point> hull = new Stack<Point>();

    public GrahamScan(Point[] pts) {

        // Kopiowanie zabezpieczaj¹ce
        int N = pts.length;
        Point[] points = new Point[N];
        for (int i = 0; i < N; i++)
            points[i] = pts[i];

        // Wstêpne przetwarzanie, tak aby element points[0] mia³ najmniejsz¹ wspó³rzêdn¹ y; 
		// równe elementy s¹ porównywane wed³ug wspó³rzêdnej x.
        // Element points[0] jest skrajnym punktem otoczki wypuk³ej
        // (problem mo¿na ³atwo rozwi¹zaæ w czasie liniowym)
        Arrays.sort(points);

        // Sortowanie wed³ug k¹ta biegunowego z wzglêdem punktu bazowego points[0];
        // równe elementy s¹ porównywane wed³ug odleg³oœci od punktu points[0]
        Arrays.sort(points, 1, N, points[0].BY_CCW);

        hull.push(points[0]);       // p[0] to pierwszy punkt skrajny

        // Znajdowanie indeksu k1 pierwszgo punktu ró¿nego od points[0]
        int k1;
        for (k1 = 1; k1 < N; k1++)
            if (!points[0].equals(points[k1])) break;
        if (k1 == N) return;        // Wszystkie punkty s¹ równe

        // Znajdowanie indeksu k2 pierwszego punktu, który nie jest wspó³liniowy wobec punktów points[0] i points[k1]
        int k2;
        for (k2 = k1 + 1; k2 < N; k2++)
            if (Point.ccw(points[0], points[k1], points[k2]) != 0) break;
        hull.push(points[k2-1]);    // points[k2-1] to drugi skrajny punkt

        // Algorytm Grahama; zauwa¿, ¿e points[N-1] to skrajny punkt ró¿ny od punktu points[0]
        for (int i = k2; i < N; i++) {
            Point top = hull.pop();
            while (Point.ccw(top, hull.peek(), points[i]) <= 0) {
                top = hull.pop();
            }
            hull.push(top);
            hull.push(points[i]);
        }
    }

    // Zwracanie obiektu Iterable ze skrajnymi punktami otoczki wypuk³ej w porz¹dku przeciwnym do ruchu wskazówek zegara
    public Iterable<Point> hull() {
        // Odwracanie kolejnoœci i odrzucanie ostatniego punktu (powtarza siê dwukrotnie)
        Stack<Point> s = new Stack<Point>();
        for (Point p : hull) s.push(p);
        return s;
    }

    // Klient testowy
    public static void main(String[] args) {
        int N = StdIn.readInt();
        Point[] points = new Point[N];
        for (int i = 0; i < N; i++) {
            int x = StdIn.readInt();
            int y = StdIn.readInt();
            points[i] = new Point(x, y);
        }
        GrahamScan graham = new GrahamScan(points);
        for (Point p : graham.hull())
            StdOut.println(p);
    }

}