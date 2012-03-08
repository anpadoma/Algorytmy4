/*************************************************************************
 *  Kompilacja:  javac Point.java
 *  Wykonanie:    java Point
 *
 *  Implementacja punktu o wsp�rz�dnych ca�kowitoliczbowych w przestrzeni dwuwymiarowej.
 *
 *  Jest to ilustracja korzystania z niestatycznego obiektu zgodnego z interfejsem Comparator,
 *  kt�ry porz�dkuje punkty rosn�co wed�ug odleg�o�ci od punktu bazowego.
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

    // Zwraca odleg�o�� euklidesow� mi�dzy danym punktem a punktem that
    public double distanceTo(Point that) {
       double dx = this.x - that.x;
       double dy = this.y - that.y;
       return Math.sqrt(dx*dx + dy*dy);
    }

    // Czy a->b->c biegnie przeciwnie do ruchu wskaz�wek zegara?
    // -1 je�li zgodne z ruchem wskaz�wek zegara, +1 je�li przeciwne, 0 je�li wsp�liniowe
    public static int ccw(Point a, Point b, Point c) {
       int area2 = (b.x-a.x)*(c.y-a.y) - (b.y-a.y)*(c.x-a.x);
       if      (area2 < 0) return -1;
       else if (area2 > 0) return +1;
       else                return  0;
    }

    // Dwukrotno�� dodatniego pola a-b-c
    public static int area2(Point a, Point b, Point c) {
       return (b.x-a.x)*(c.y-a.y) - (b.y-a.y)*(c.x-a.x);
    }


    // Przekszta�canie na �a�cuch znak�w
    public String toString() {
       return "(" + x + ", " + y + ")";
    }

    // Wy�wietlanie punktu za pomoc� StdDraw
    public void draw() {
       StdDraw.point(x, y);
    }

    // Wy�wietlanie linii od danego punktu do punktu that
    public void drawTo(Point that) {
       StdDraw.line(this.x, this.y, that.x, that.y);
    }

    // Porz�dek naturalny: por�wnuje wed�ug wsp�rz�dnej y; r�wne 
	// punkty por�wnuje wed�ug wsp�rz�dnej x
    public int compareTo(Point that) {
       if (this.y < that.y) return -1;
       if (this.y > that.y) return +1;
       if (this.x > that.x) return -1;
       if (this.x < that.x) return +1;
       return 0;
    }

    // Czy wsp�rz�dne x i y dw�ch punkt�w s� takie same?
    public boolean equals(Object y) {
       if (y == this) return true;
       if (y == null) return false;
       if (y.getClass() != this.getClass()) return false;
       Point that = (Point) y;
       return (this.x == that.x) && (this.y == that.y);
    }

    /**********************************************************************
     *  Obiekt Comparator por�wnuj�cy punkty wed�ug k�ta biegunowego
     *  wzgl�dem danego punktu. K�t biegunowy jest mierzony wzgl�dem
     *  promienia biegn�cego z danego punktu w prawo. Punkty o r�wnych 
     *  k�tach s� por�wnywane wzgl�dem odleg�o�ci od danego punktu.
     *
     *  Regu�a por�wnywania wed�ug odleg�o�ci jest wykorzystywana tylko dla 
     *  "zdegenerowanych" przypadk�w (je�li wyst�puj� trzy wsp�liniowe 
     *  punkty na otoczce wypuk�ej, mo�na wykry� tylko pierwszy i ostatni punkt).
     *
     *  Warunek wst�pny:  q1 i q2 znajduj� si� wy�ej wzgl�dem p.
     *
     *  Technicznie narusza to kontrakt dla metody compare(), je�li
     *  zostanie na wywo�ana dla punkt�w o wsp�rz�dnej y mniejszej ni� p.
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

          // Zak�adamy, �e dy1 >= 0 && dy2 >= 0;  
		  // inaczej naruszony jest kontrakt dla metody compare().
          // R�wne k�ty wymagaj� por�wnania punkt�w wed�ug odleg�o�ci od danego punktu.
          // Mo�liwe powinno by� zast�pienie obliczania odleg�o�ci 
          // projekcj�, poniewa� punkty s� wsp�liniowe
          if      (dx1*dx1 + dy1*dy1 < dx2*dx2 + dy2*dy2) return -1;
          else if (dx1*dx1 + dy1*dy1 > dx2*dx2 + dy2*dy2) return +1;
          else                                            return  0;
       }
    }

    
    
    // Por�wnuje punkty wed�ug odleg�o�ci do danego punktu
    private class ByDistanceToComparator implements Comparator<Point> {
        public int compare(Point p, Point q) {
            if      (distanceTo(p) < distanceTo(q)) return -1;
            else if (distanceTo(p) > distanceTo(q)) return +1;
            else                                    return  0;
        }
    }

    // Por�wnuje punkty wed�ug wsp�rz�dnej x, a r�wne - wed�ug wsp�rz�dnej y
    private static class ByXComparator implements Comparator<Point> {
        public int compare(Point p, Point q) {
           if (p.x < q.x) return -1;
           if (p.x > q.x) return +1;
           if (p.y < q.y) return -1;
           if (p.y > q.y) return +1;
           return 0;
        }
    }

    // Por�wnuje punkty wed�ug wsp�rz�dnej y, a r�wne - wed�ug wsp�rz�dnej x
    private static class ByYComparator implements Comparator<Point> {
        public int compare(Point p, Point q) {
           if (p.y < q.y) return -1;
           if (p.y > q.y) return +1;
           if (p.x < q.x) return -1;
           if (p.x > q.x) return +1;
           return 0;
        }
    }



   // Generuje N losowych punkt�w i sortuje je wed�ug odleg�o�ci do (500, 500)
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
