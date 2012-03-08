/*************************************************************************
 *  Kompilacja:  javac ClosestPair.java
 *  Wykonanie:    java ClosestPair < input.txt
 *  Zale�no�ci: Point.java
 *  
 *  Na podstawie N punkt�w w przestrzeni nale�y program znajduje najbli�sz� par� w czasie N log N.
 *
 *  Uwaga: mo�na przyspieszy� program przez por�wnywanie kwadrat�w odleg�o�ci euklidesowych
 *  zamiast odleg�o�ci euklidesowych.
 *
 *************************************************************************/

import java.util.Arrays;

public class ClosestPair {

    // Najbli�sza para punkt�w i odleg�o�� euklidesowa mi�dzy nimi
    private Point2D best1, best2;
    private double bestDistance = Double.POSITIVE_INFINITY;

    public ClosestPair(Point2D[] points) {
        int N = points.length;
        if (N <= 1) return;

        // Sortowanie wed�ug wsp�rz�dnej x (r�wne punkty s� sortowane wed�ug wsp�rz�dnej y)
        Point2D[] pointsByX = new Point2D[N];
        for (int i = 0; i < N; i++) pointsByX[i] = points[i];
        Arrays.sort(pointsByX, Point2D.X_ORDER);

        // Wykrywanie punkt�w o tych samych wsp�rz�dnych
        for (int i = 0; i < N-1; i++) {
            if (pointsByX[i].equals(pointsByX[i+1])) {
                bestDistance = 0.0;
                best1 = pointsByX[i];
                best2 = pointsByX[i+1];
                return;
            }
        }

        // Sortowanie wed�ug wsp�rz�dnej y (jeszcze nieposortowanych punkt�w) 
        Point2D[] pointsByY = new Point2D[N];
        for (int i = 0; i < N; i++) pointsByY[i] = pointsByX[i];

        // Tablica pomocnicza
        Point2D[] aux = new Point2D[N];

        closest(pointsByX, pointsByY, aux, 0, N-1);
    }

    // Znajdowanie pary najbli�szych punkt�w w tablicy pointsByX[lo..hi]
    // Warunek wst�pny:  pointsByX[lo..hi] i pointsByY[lo..hi] obejmuj� ten sam ci�g punkt�w
    // Warunek wst�pny:  pointsByX[lo..hi] s� posortowane wed�ug wsp�rz�dnej x
    // Warunek ko�cowy: pointsByY[lo..hi] s� posortowane wed�ug wsp�rz�dnej y
    private double closest(Point2D[] pointsByX, Point2D[] pointsByY, Point2D[] aux, int lo, int hi) {
        if (hi <= lo) return Double.POSITIVE_INFINITY;

        int mid = lo + (hi - lo) / 2;
        Point2D median = pointsByX[mid];

        // Wyznaczanie pary najbli�szych punkt�w, kt�re oba znajduj� si� w lewej lub prawej podtablicy
        double delta1 = closest(pointsByX, pointsByY, aux, lo, mid);
        double delta2 = closest(pointsByX, pointsByY, aux, mid+1, hi);
        double delta = Math.min(delta1, delta2);

        // Ponowne scalanie, tak aby punkty pointsByY[lo..hi] by�y posortowane wed�ug wsp�rz�dnej y
        Merge.merge(pointsByY, aux, lo, mid, hi);

        // aux[0..M-1] = ci�g punkt�w oddalonych o mniej ni� delta i posortowanych wed�ug wsp�rz�dnej y
        int M = 0;
        for (int i = lo; i <= hi; i++) {
            if (Math.abs(pointsByY[i].x() - median.x()) < delta)
                aux[M++] = pointsByY[i];
        }

        // Por�wnywanie ka�dego punktu z s�siadami, kt�rych wsp�rz�dna y jest bli�ej ni� o delta
        for (int i = 0; i < M; i++) {
            // Mo�na udowodni�, �e p�tla wykonuje najwy�ej 7 powt�rze�
            for (int j = i+1; (j < M) && (aux[j].y() - aux[i].y() < delta); j++) {
                double distance = aux[i].distanceTo(aux[j]);
                if (distance < delta) {
                    delta = distance;
                    if (distance < bestDistance) {
                        bestDistance = delta;
                        best1 = aux[i];
                        best2 = aux[j];
                        // StdOut.println("Lepsza odleg�o�� = " + delta + " z " + best1 + " do " + best2);
                    }
                }
            }
        }
        return delta;
    }

    public Point2D either() { return best1; }
    public Point2D other()  { return best2; }

    public double distance() {
        return bestDistance;
    }


    public static void main(String[] args) {
        int N = StdIn.readInt();
        Point2D[] points = new Point2D[N];
        for (int i = 0; i < N; i++) {
            double x = StdIn.readDouble();
            double y = StdIn.readDouble();
            points[i] = new Point2D(x, y);
        }
        ClosestPair closest = new ClosestPair(points);
        System.out.println(closest.distance() + " z " + closest.either() + " do " + closest.other());
    }

}
