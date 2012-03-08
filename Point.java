/*************************************************************************
 *  Kompilacja:  javac Point.java
 *  Wykonanie:    java Point
 *
 *  Implementacja punktu o wspó³rzêdnych ca³kowitoliczbowych w przestrzeni dwuwymiarowej.
 *
 *  Jest to ilustracja korzystania z niestatycznego obiektu zgodnego z interfejsem Comparator,
 *  który porz¹dkuje punkty rosn¹co wed³ug odleg³oœci od punktu bazowego.
 * 
 *  % java Point 10
 *  165.8463143998081 (584, 643)
 *  202.85216291674092 (665, 382)
 *  207.88458336298052 (320, 604)
 *  225.39077177204928 (724, 475)
 *  348.8624370722649 (536, 847)
 *  430.5090010673412 (917, 607)
 *  431.26442004876776 (533, 930)
 *  452.3593704125073 (898, 285)
 *  506.5984603213871 (19, 341)
 *  570.7092079159053 (897, 910)
 *
 *************************************************************************/

import java.util.Comparator;
import java.util.Arrays;

public class Point implements Comparable<Point> { 
    private final int x;
    private final int y;

    public final Comparator<Point> BY_DISTANCE_TO = new ByDistanceToComparator();
    public final Comparator<Point> BY_CCW         = new ByCCWComparator();

    public static final Comparator<Point> BY_X = new ByXComparator();
    public static final Comparator<Point> BY_Y = new ByYComparator();

    // Tworzenie i inicjowanie punktu na podstawie pary (x, y)
    public Point(int x, int y) {
       this.x = x;
       this.y = y;
    }

    // Zwraca odleg³oœæ euklidesow¹ miêdzy danym punktem a punktem that
    public double distanceTo(Point that) {
       double dx = this.x - that.x;
       double dy = this.y - that.y;
       return Math.sqrt(dx*dx + dy*dy);
    }

    // Czy a->b->c biegnie przeciwnie do ruchu wskazówek zegara?
    // -1 jeœli zgodne z ruchem wskazówek zegara, +1 jeœli przeciwne, 0 jeœli wspó³liniowe
    public static int ccw(Point a, Point b, Point c) {
       int area2 = (b.x-a.x)*(c.y-a.y) - (b.y-a.y)*(c.x-a.x);
       if      (area2 < 0) return -1;
       else if (area2 > 0) return +1;
       else                return  0;
    }

    // Dwukrotnoœæ dodatniego pola a-b-c
    public static int area2(Point a, Point b, Point c) {
       return (b.x-a.x)*(c.y-a.y) - (b.y-a.y)*(c.x-a.x);
    }


    // Przekszta³canie na ³añcuch znaków
    public String toString() {
       return "(" + x + ", " + y + ")";
    }

    // Wyœwietlanie punktu za pomoc¹ StdDraw
    public void draw() {
       StdDraw.point(x, y);
    }

    // Wyœwietlanie linii od danego punktu do punktu that
    public void drawTo(Point that) {
       StdDraw.line(this.x, this.y, that.x, that.y);
    }

    // Porz¹dek naturalny: porównuje wed³ug wspó³rzêdnej y; równe 
	// punkty porównuje wed³ug wspó³rzêdnej x
    public int compareTo(Point that) {
       if (this.y < that.y) return -1;
       if (this.y > that.y) return +1;
       if (this.x > that.x) return -1;
       if (this.x < that.x) return +1;
       return 0;
    }

    // Czy wspó³rz¹dne x i y dwóch punktów s¹ takie same?
    public boolean equals(Object y) {
       if (y == this) return true;
       if (y == null) return false;
       if (y.getClass() != this.getClass()) return false;
       Point that = (Point) y;
       return (this.x == that.x) && (this.y == that.y);
    }

    /**********************************************************************
     *  Obiekt Comparator porównuj¹cy punkty wed³ug k¹ta biegunowego
     *  wzglêdem danego punktu. K¹t biegunowy jest mierzony wzglêdem
     *  promienia biegn¹cego z danego punktu w prawo. Punkty o równych 
     *  k¹tach s¹ porównywane wzglêdem odleg³oœci od danego punktu.
     *
     *  Regu³a porównywania wed³ug odleg³oœci jest wykorzystywana tylko dla 
     *  "zdegenerowanych" przypadków (jeœli wystêpuj¹ trzy wspó³liniowe 
     *  punkty na otoczce wypuk³ej, mo¿na wykryæ tylko pierwszy i ostatni punkt).
     *
     *  Warunek wstêpny:  q1 i q2 znajduj¹ siê wy¿ej wzglêdem p.
     *
     *  Technicznie narusza to kontrakt dla metody compare(), jeœli
     *  zostanie na wywo³ana dla punktów o wspó³rzêdnej y mniejszej ni¿ p.
     *
     **********************************************************************/
    private class ByCCWComparator implements Comparator<Point> {
       public int compare(Point q1, Point q2) {
          int ccw = ccw(Point.this, q1, q2);
          if (ccw == -1) return -1;
          if (ccw == +1) return +1;

          int dx1 = q1.x - x;
          int dx2 = q2.x - x;
          int dy1 = q1.y - y;
          int dy2 = q2.y - y;

          // Zak³adamy, ¿e dy1 >= 0 && dy2 >= 0;  
		  // inaczej naruszony jest kontrakt dla metody compare().
          // Równe k¹ty wymagaj¹ porównania punktów wed³ug odleg³oœci od danego punktu.
          // Mo¿liwe powinno byæ zast¹pienie obliczania odleg³oœci 
          // projekcj¹, poniewa¿ punkty s¹ wspó³liniowe
          if      (dx1*dx1 + dy1*dy1 < dx2*dx2 + dy2*dy2) return -1;
          else if (dx1*dx1 + dy1*dy1 > dx2*dx2 + dy2*dy2) return +1;
          else                                            return  0;
       }
    }

    
    
    // Porównuje punkty wed³ug odleg³oœci do danego punktu
    private class ByDistanceToComparator implements Comparator<Point> {
        public int compare(Point p, Point q) {
            if      (distanceTo(p) < distanceTo(q)) return -1;
            else if (distanceTo(p) > distanceTo(q)) return +1;
            else                                    return  0;
        }
    }

    // Porównuje punkty wed³ug wspó³rzêdnej x, a równe - wed³ug wspó³rzêdnej y
    private static class ByXComparator implements Comparator<Point> {
        public int compare(Point p, Point q) {
           if (p.x < q.x) return -1;
           if (p.x > q.x) return +1;
           if (p.y < q.y) return -1;
           if (p.y > q.y) return +1;
           return 0;
        }
    }

    // Porównuje punkty wed³ug wspó³rzêdnej y, a równe - wed³ug wspó³rzêdnej x
    private static class ByYComparator implements Comparator<Point> {
        public int compare(Point p, Point q) {
           if (p.y < q.y) return -1;
           if (p.y > q.y) return +1;
           if (p.x < q.x) return -1;
           if (p.x > q.x) return +1;
           return 0;
        }
    }



   // Generuje N losowych punktów i sortuje je wed³ug odleg³oœci do (500, 500)
   public static void main(String[] args) {
      int N = Integer.parseInt(args[0]);
      Point[] points = new Point[N];
      for (int i = 0; i < N; i++) {
          int x = StdRandom.uniform(1000);
          int y = StdRandom.uniform(1000);
          points[i] = new Point(x, y);
      }
      Point base = new Point(500, 500);
      Arrays.sort(points, base.BY_DISTANCE_TO);
      for (int i = 0; i < N; i++) {
          StdOut.println(base.distanceTo(points[i]) + " " + points[i]);
      }
   }
}
