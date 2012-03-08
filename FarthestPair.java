
/*************************************************************************
 *  Kompilacja:  javac FarthestPair.java
 *  Wykonanie:    java FarthestPair < input.txt
 *  Zale¿noœci: GrahamScan.java Point.java
 *  
 *  Na podstawie zbioru N punktów w przestrzeni znajduje najbardziej oddalon¹ parê
 *  (wyznacza œrednicê zbioru punktów).
 *
 *  Wyznacza otoczkê wypuk³¹ dla zbioru punktów i wykorzystuje metodê
 *  "rotating callipers" do znalezienia wszystkich par przeciwleg³ych punktów
 *  i najbardziej oddalonej pary.
 *
 *************************************************************************/

public class FarthestPair {

    // Para najbardziej oddalonych punktów i odleg³oœæ miêdzy nimi
    private Point best1, best2;
    private double bestDistance = Double.NEGATIVE_INFINITY;

    public FarthestPair(Point[] points) {
        GrahamScan graham = new GrahamScan(points);

        // Pojedynczy punkt
        if (points.length <= 1) return;

        // Liczba punktów w otoczce
        int M = 0;
        for (Point p : graham.hull())
            M++;

        // Punkty otoczki w kolejnoœci przeciwnej do ruchu wskazówek zegara
        Point[] hull = new Point[M+1];
        int m = 1;
        for (Point p : graham.hull()) {
            hull[m++] = p;
        }

        // Wszystkie punkty s¹ równe
        if (M == 1) return;

        // Punkty s¹ wspó³liniowe
        if (M == 2) {
            best1 = hull[1];
            best2 = hull[2];
            bestDistance = best1.distanceTo(best2);
            return;
        }

        // k = najdalszy wierzcho³ek od krawêdzi od hull[1] do hull[M]		
        int k = 2;
        while (Point.area2(hull[M], hull[k+1], hull[1]) > Point.area2(hull[M], hull[k], hull[1])) {
            k++;
        }

        int j = k;
        for (int i = 1; i <= k; i++) {
            // StdOut.println("hull[i] + " i " + hull[j] + " s¹ przeciwleg³e");
            if (hull[i].distanceTo(hull[j]) > bestDistance) {
                best1 = hull[i];
                best2 = hull[j];
                bestDistance = hull[i].distanceTo(hull[j]);
            }
            while ((j < M) && Point.area2(hull[i], hull[j+1], hull[i+1]) > Point.area2(hull[i], hull[j], hull[i+1])) {
                j++;
                // StdOut.println(hull[i] + " i " + hull[j] + " s¹ przeciwleg³e");
                double distance = hull[i].distanceTo(hull[j]);
                if (distance > bestDistance) {
                    best1 = hull[i];
                    best2 = hull[j];
                    bestDistance = hull[i].distanceTo(hull[j]);
                }
            }
        }
    }

    public Point either()    { return best1;        }
    public Point other()     { return best2;        }
    public double distance() { return bestDistance; }


    public static void main(String[] args) {
        int N = StdIn.readInt();
        Point[] points = new Point[N];
        for (int i = 0; i < N; i++) {
            int x = StdIn.readInt();
            int y = StdIn.readInt();
            points[i] = new Point(x, y);
        }
        FarthestPair farthest = new FarthestPair(points);
        System.out.println(farthest.distance() + " z " + farthest.either() + " do " + farthest.other());
    }

}
