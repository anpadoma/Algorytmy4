
/*************************************************************************
 *  Kompilacja:  javac Average.java
 *  Wykonanie:    java Average < data.txt
 *  Zale¿noœci: StdIn.java StdOut.java
 *  
 *  Wczytuje ci¹g liczb rzeczywistych i oblicza ich œredni¹.
 *
 *  % java Average
 *  10.0 5.0 6.0
 *  3.0 7.0 32.0
 *  <Ctrl-d>
 *  Œrednia wynosi 10.5

 *  Zauwa¿, ¿e kombinacja <Ctrl-d> oznacza koniec pliku w Uniksie.
 *  W systemie Windows u¿yj kombinacji <Ctrl-z>.
 *
 *************************************************************************/

public class Average { 
    public static void main(String[] args) { 
        int count = 0;       // Liczba wartoœci wejœciowych.
        double sum = 0.0;    // Suma wartoœci wejœciowych.

        // Wczytuje dane i oblicza statystyki.
        while (!StdIn.isEmpty()) {
            double value = StdIn.readDouble();
            sum += value;
            count++;
        }

        // Oblicza œredni¹.
        double average = sum / count;

        // Wyœwietla wynik.
        StdOut.println("Srednia wynosi " + average);
    }
}
