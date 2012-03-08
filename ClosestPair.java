/*************************************************************************
 *  Kompilacja:  javac ClosestPair.java
 *  Wykonanie:    java ClosestPair < input.txt
 *  Zale¿noœci: Point.java
 *  
 *  Na podstawie N punktów w przestrzeni nale¿y program znajduje najbli¿sz¹ parê w czasie N log N.
 *
 *  Uwaga: mo¿na przyspieszyæ program przez porównywanie kwadratów odleg³oœci euklidesowych
 *  zamiast odleg³oœci euklidesowych.
 *
 *************************************************************************/

import java.util.Arrays;

public class ClosestPair {

    // Najbli¿sza para punktów i odleg³oœæ euklidesowa miêdzy nimi
    private Point2D best1, best2;
    private double bestDistance = Double.POSITIVE_INFINITY;

    public ClosestPair(Point2D[] points) {
        int N = points.length;
        if (N <= 1) return;

        // Sortowanie wed³ug wspó³rzêdnej x (równe punkty s¹ sortowane wed³ug wspó³rzêdnej y)
        Point2D[] pointsByX = new Point2D[N];
        for (int i = 0; i < N; i++) pointsByX[i] = points[i];
        Arrays.sort(pointsByX, Point2D.X_ORDER);

        // Wykrywanie punktów o tych samych wspó³rzêdnych
        for (int i = 0; i < N-1; i++) {
            if (pointsByX[i].equals(pointsByX[i+1])) {
                bestDistance = 0.0;
                best1 = pointsByX[i];
                best2 = pointsByX[i+1];
                return;
            }
        }

        // Sortowanie wed³ug wspó³rzêdnej y (jeszcze nieposortowanych punktów) 
        Point2D[] pointsByY = new Point2D[N];
        for (int i = 0; i < N; i++) pointsByY[i] = pointsByX[i];

        // Tablica pomocnicza
        Point2D[] aux = new Point2D[N];

        closest(pointsByX, pointsByY, aux, 0, N-1);
    }

    // Znajdowanie pary najbli¿szych punktów w tablicy pointsByX[lo..hi]
    // Warunek wstêpny:  pointsByX[lo..hi] i pointsByY[lo..hi] obejmuj¹ ten sam ci¹g punktów
    // Warunek wstêpny:  pointsByX[lo..hi] s¹ posortowane wed³ug wspó³rzêdnej x
    // Warunek koñcowy: pointsByY[lo..hi] s¹ posortowane wed³ug wspó³rzêdnej y
    private double closest(Point2D[] pointsByX, Point2D[] pointsByY, Point2D[] aux, int lo, int hi) {
        if (hi <= lo) return Double.POSITIVE_INFINITY;

        int mid = lo + (hi - lo) / 2;
        Point2D median = pointsByX[mid];

        // Wyznaczanie pary najbli¿szych punktów, które oba znajduj¹ siê w lewej lub prawej podtablicy
        double delta1 = closest(pointsByX, pointsByY, aux, lo, mid);
        double delta2 = closest(pointsByX, pointsByY, aux, mid+1, hi);
        double delta = Math.min(delta1, delta2);

        // Ponowne scalanie, tak aby punkty pointsByY[lo..hi] by³y posortowane wed³ug wspó³rzêdnej y
        Merge.merge(pointsByY, aux, lo, mid, hi);

        // aux[0..M-1] = ci¹g punktów oddalonych o mniej ni¿ delta i posortowanych wed³ug wspó³rzêdnej y
        int M = 0;
        for (int i = lo; i <= hi; i++) {
            if (Math.abs(pointsByY[i].x() - median.x()) < delta)
                aux[M++] = pointsByY[i];
        }

        // Porównywanie ka¿dego punktu z s¹siadami, których wspó³rzêdna y jest bli¿ej ni¿ o delta
        for (int i = 0; i < M; i++) {
            // Mo¿na udowodniæ, ¿e pêtla wykonuje najwy¿ej 7 powtórzeñ
            for (int j = i+1; (j < M) && (aux[j].y() - aux[i].y() < delta); j++) {
                double distance = aux[i].distanceTo(aux[j]);
                if (distance < delta) {
                    delta = distance;
                    if (distance < bestDistance) {
                        bestDistance = delta;
                        best1 = aux[i];
                        best2 = aux[j];
                        // StdOut.println("Lepsza odleg³oœæ = " + delta + " z " + best1 + " do " + best2);
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
