
/*************************************************************************
 *  Kompilacja:  javac Shuffle.java
 *  Wykonanie:    java Shuffle < list.txt
 *  
 *  Wczytuje list� �a�cuch�w znak�w i wy�wietla je w losowej kolejno�ci.
 *  Algorytm tasuj�cy Knutha (lub Fishera-Yatesa) gwarantuje
 *  losowe uporz�dkowanie element�w (przy za�o�eniu, �e metoda
 *  Math.random() generuje liczby z przedzia�u 0 - 1
 *  o rozk�adzie r�wnomiernym).
 *
 *  % more cards.txt
 *  2C 3C 4C 5C 6C 7C 8C 9C 10C JC QC KC AC
 *  2D 3D 4D 5D 6D 7D 8D 9D 10D JD QD KD AD
 *  2H 3H 4H 5H 6H 7H 8H 9H 10H JH QH KH AH
 *  2S 3S 4S 5S 6S 7S 8S 9S 10S JS QS KS AS
 *
 *  % java Shuffle < cards.txt
 *  6H
 *  9C
 *  8H
 *  7C
 *  JS
 *  ...
 *  KH
 *
 *************************************************************************/

public class Shuffle { 
    public static void main(String[] args) {

        // Wczytywanie danych
        String[] a = StdIn.readAll().split("\\s+");
        int N = a.length;

        // Tasowanie
        for (int i = 0; i < N; i++) {
            // Liczba ca�kowita z pozosta�ej cz�ci talii
            int r = i + (int) (Math.random() * (N - i));
            String swap = a[r];
            a[r] = a[i];
            a[i] = swap;
        }

        // Wy�wietlanie permutacji
        for (int i = 0; i < N; i++)
            System.out.println(a[i]);
    }
}
