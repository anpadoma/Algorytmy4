
/*************************************************************************
 *  Kompilacja:  javac Average.java
 *  Wykonanie:    java Average < data.txt
 *  Zale�no�ci: StdIn.java StdOut.java
 *  
 *  Wczytuje ci�g liczb rzeczywistych i oblicza ich �redni�.
 *
 *  % java Average
 *  10.0 5.0 6.0
 *  3.0 7.0 32.0
 *  <Ctrl-d>
 *  �rednia wynosi 10.5

 *  Zauwa�, �e kombinacja <Ctrl-d> oznacza koniec pliku w Uniksie.
 *  W systemie Windows u�yj kombinacji <Ctrl-z>.
 *
 *************************************************************************/

public class Average { 
    public static void main(String[] args) { 
        int count = 0;       // Liczba warto�ci wej�ciowych.
        double sum = 0.0;    // Suma warto�ci wej�ciowych.

        // Wczytuje dane i oblicza statystyki.
        while (!StdIn.isEmpty()) {
            double value = StdIn.readDouble();
            sum += value;
            count++;
        }

        // Oblicza �redni�.
        double average = sum / count;

        // Wy�wietla wynik.
        StdOut.println("Srednia wynosi " + average);
    }
}
