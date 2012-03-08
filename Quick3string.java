/***********************************************************************************
 *  Kompilacja: javac Quick3string.java
 *  Wykonanie:   java Quick3string < input.txt
 *
 *  Wczytuje ³añcuchy znaków ze standardowego wejœcia i sortuje je za pomoc¹ 
 *  sortowania szybkiego z podzia³em na trzy czêœci.
 *
 *  % java Quick3string < shell.txt
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

public class Quick3string {
    private static final int CUTOFF =  15;   // Prze³¹czanie na sortowanie przez wstawianie

    // Sortowanie tablicy ³añcuchów znaków a[]
    public static void sort(String[] a) {
        // StdRandom.shuffle(a);
        sort(a, 0, a.length-1, 0);
        assert isSorted(a);
    }

    // Zwracanie d-tego znaku ³añcucha s (lub -1, jeœli d = d³ugoœæ ³añcucha s)
    private static int charAt(String s, int d) { 
        assert d >= 0 && d <= s.length();
        if (d == s.length()) return -1;
        return s.charAt(d);
    }


    // Sortowanie szybkie ³añcuchów znaków z podzia³em na trzy czêœci a[lo..hi], pocz¹wszy od d-tego znaku
    private static void sort(String[] a, int lo, int hi, int d) { 

        // Prze³¹czenie na sortowanie przez wstawianie dla krótkich podtablic
        if (hi <= lo + CUTOFF) {
            insertion(a, lo, hi, d);
            return;
        }

        int lt = lo, gt = hi;
        int v = charAt(a[lo], d);
        int i = lo + 1;
        while (i <= gt) {
            int t = charAt(a[i], d);
            if      (t < v) exch(a, lt++, i++);
            else if (t > v) exch(a, i, gt--);
            else              i++;
        }

        // a[lo..lt-1] < v = a[lt..gt] < a[gt+1..hi]. 
        sort(a, lo, lt-1, d);
        if (v >= 0) sort(a, lt, gt, d+1);
        sort(a, gt+1, hi, d);
    }

    // Sortowanie ³añcuchów od a[lo] do a[hi], pocz¹wszy od d-tego znaku
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

    // Sprawdzanie (pocz¹wszy od znaku d), czy v jest mniejsze od w
    private static boolean less(String v, String w, int d) {
        assert v.substring(0, d).equals(w.substring(0, d));
        return v.substring(d).compareTo(w.substring(d)) < 0; 
    }


    // Czy tablica jest posortowana?
    private static boolean isSorted(String[] a) {
        for (int i = 1; i < a.length; i++)
            if (a[i].compareTo(a[i-1]) < 0) return false;
        return true;
    }


    public static void main(String[] args) {

        // Wczytywanie ³añcuchów znaków ze standardowego wejœcia
        String[] a = StdIn.readStrings();
        int N = a.length;

        // Sortowanie ³añcuchów znaków
        sort(a);

        // Wyœwietlanie wyników
        for (int i = 0; i < N; i++)
            StdOut.println(a[i]);
    }
}
