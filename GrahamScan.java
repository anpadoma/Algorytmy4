/*************************************************************************
 *  Kompilacja:  javac GrahamaScan.java
 *  Wykonanie:    java GrahamScan < input.txt
 * 
 *  Tworzy punkty ze standardowego wej�cia i wyznacza otoczk� wypuk��
 *  za pomoc� algorytmu Grahama.
 *
 *************************************************************************/

import java.util.Arrays;

public class GrahamScan {
    private Stack<Point> hull = new Stack<Point>();

    public GrahamScan(Point[] pts) {

        // Kopiowanie zabezpieczaj�ce
        int N = pts.length;
        Point[] points = new Point[N];
        for (int i = 0; i < N; i++)
            points[i] = pts[i];

        // Wst�pne przetwarzanie, tak aby element points[0] mia� najmniejsz� wsp�rz�dn� y; 
		// r�wne elementy s� por�wnywane wed�ug wsp�rz�dnej x.
        // Element points[0] jest skrajnym punktem otoczki wypuk�ej
        // (problem mo�na �atwo rozwi�za� w czasie liniowym)
        Arrays.sort(points);

        // Sortowanie wed�ug k�ta biegunowego z wzgl�dem punktu bazowego points[0];
        // r�wne elementy s� por�wnywane wed�ug odleg�o�ci od punktu points[0]
        Arrays.sort(points, 1, N, points[0].BY_CCW);

        hull.push(points[0]);       // p[0] to pierwszy punkt skrajny

        // Znajdowanie indeksu k1 pierwszgo punktu r�nego od points[0]
        int k1;
        for (k1 = 1; k1 < N; k1++)
            if (!points[0].equals(points[k1])) break;
        if (k1 == N) return;        // Wszystkie punkty s� r�wne

        // Znajdowanie indeksu k2 pierwszego punktu, kt�ry nie jest wsp�liniowy wobec punkt�w points[0] i points[k1]
        int k2;
        for (k2 = k1 + 1; k2 < N; k2++)
            if (Point.ccw(points[0], points[k1], points[k2]) != 0) break;
        hull.push(points[k2-1]);    // points[k2-1] to drugi skrajny punkt

        // Algorytm Grahama; zauwa�, �e points[N-1] to skrajny punkt r�ny od punktu points[0]
        for (int i = k2; i < N; i++) {
            Point top = hull.pop();
            while (Point.ccw(top, hull.peek(), points[i]) <= 0) {
                top = hull.pop();
            }
            hull.push(top);
            hull.push(points[i]);
        }
    }

    // Zwracanie obiektu Iterable ze skrajnymi punktami otoczki wypuk�ej w porz�dku przeciwnym do ruchu wskaz�wek zegara
    public Iterable<Point> hull() {
        // Odwracanie kolejno�ci i odrzucanie ostatniego punktu (powtarza si� dwukrotnie)
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