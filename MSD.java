/***********************************************************************************
 *  Kompilacja: javac MSD.java
 *  Wykonanie:   java MSD < input.txt
 *
 *  Wczytuje ze standardowego wej�cia �a�cuchy znak�w z rozszerzonego zbioru ASCII i 
 *  sortuje je pozycyjnie metod� MSD.
 *
 *  % java MSD < shells.txt 
 *  are
 *  by
 *  sea
 *  seashells
 *  seashells
 *  sells
 *  sells
 *  she
 *  she
 *  shells
 *  shore
 *  surely
 *  the
 *  the
 *
 ***********************************************************************************/

public class MSD {
    private static final int R      = 256;   // Rozmiar alfabetu opartego na rozszerzonym zbiorze ASCII
    private static final int CUTOFF =  15;   // Prze��czanie na sortowanie przez wstawianie

    // Sortowanie tablicy �a�cuch�w znak�w
    public static void sort(String[] a) {
        int N = a.length;
        String[] aux = new String[N];
        sort(a, 0, N-1, 0, aux);
    }

    // Zwracanie d-tego znaku �a�cucha s (lub -1, je�li d = d�ugo�� �a�cucha)
    private static int charAt(String s, int d) {
        assert d >= 0 && d <= s.length();
        if (d == s.length()) return -1;
        return s.charAt(d);
    }

    // Sortowanie przedzia�u a[lo] do a[hi], pocz�wszy od d-tego znaku
    private static void sort(String[] a, int lo, int hi, int d, String[] aux) {

        // Prze��czanie na sortowanie przez wstawianie dla kr�tkich podtablic
        if (hi <= lo + CUTOFF) {
            insertion(a, lo, hi, d);
            return;
        }

        // Okre�lanie liczby wyst�pie�
        int[] count = new int[R+2];
        for (int i = lo; i <= hi; i++) {
            int c = charAt(a[i], d);
            count[c+2]++;
        }

        // Przekszta�canie liczb wyst�pie� na indeksy
        for (int r = 0; r < R+1; r++)
            count[r+1] += count[r];

        // Rozdzielanie
        for (int i = lo; i <= hi; i++) {
            int c = charAt(a[i], d);
            aux[count[c+1]++] = a[i];
        }

        // Kopiowanie z powrotem
        for (int i = lo; i <= hi; i++) 
            a[i] = aux[i - lo];


        // Rekurencyjne sortowanie na podstawie ka�dego znaku
        for (int r = 0; r < R; r++)
            sort(a, lo + count[r], lo + count[r+1] - 1, d+1, aux);
    }


    // Zwracanie d-tego znaku �a�cucha s (lub -1, je�li d = d�ugo�� �a�cucha znak�w)
    private static void insertion(String[] a, int lo, int hi, int d) {
        for (int i = lo; i <= hi; i++)
            for (int j = i; j > lo && less(a[j], a[j-1], d); j--)
                exch(a, j, j-1);
    }

    // Przestawianie a[i] z a[j]
    private static void exch(String[] a, int i, int j) {
        String temp = a[i];
        a[i] = a[j];
        a[j] = temp;
    }

    // Sprawdzanie (od znaku d), czy v jest mniejsze ni� w 
    private static boolean less(String v, String w, int d) {
        assert v.substring(0, d).equals(w.substring(0, d));
        return v.substring(d).compareTo(w.substring(d)) < 0; 
    }


    public static void main(String[] args) {
        String[] a = StdIn.readStrings();
        int N = a.length;
        sort(a);
        for (int i = 0; i < N; i++)
            System.out.println(a[i]);
    }
}
