/***********************************************************************************
 *  Kompilacja: javac LSD.java
 *  Wykonanie:   java LSD < input.txt
 *
 *  Sortowanie pozycyjne metod� LSD tablicy �a�cuch�w o d�ugo�ci w znak�w z rozszerzonego zbioru ASCII.
 *
 *  % java LSD < words3.txt
 *  all
 *  bad
 *  bed
 *  bug
 *  dad
 *  ...
 *  yes
 *  yet
 *  zoo
 *
 ***********************************************************************************/

public class LSD {

    // Sortowanie pozycyjne metod� LSD
    public static void sort(String[] a, int W) {
        int N = a.length;
        int R = 256;   // Rozmiar alfabetu opartego na rozszerzonym zbiorze ASCII
        String[] aux = new String[N];

        for (int d = W-1; d >= 0; d--) {
            // Sortowanie przez zliczanie oparte na d-tym znaku

            // Okre�lanie liczby wyst�pie�
            int[] count = new int[R+1];
            for (int i = 0; i < N; i++)
                count[a[i].charAt(d) + 1]++;

            // Obliczanie skumulowanych warto�ci
            for (int r = 0; r < R; r++)
                count[r+1] += count[r];

            // Przenoszenie danych
            for (int i = 0; i < N; i++)
                aux[count[a[i].charAt(d)]++] = a[i];

            // Kopiowanie z powrotem
            for (int i = 0; i < N; i++)
                a[i] = aux[i];
        }
    }


    public static void main(String[] args) {
        String[] a = StdIn.readStrings();
        int N = a.length;

        // Sprawdzanie, czy �a�cuchy maj� sta�� d�ugo��
        int W = a[0].length();
        for (int i = 0; i < N; i++)
            assert a[i].length() == W : "Lancuchy musza miec stala dlugosc";

        // Sortowanie �a�cuch�w
        sort(a, W);

        // Wy�wietlanie wynik�w
        for (int i = 0; i < N; i++)
            System.out.println(a[i]);
    }
}
