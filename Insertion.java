/*************************************************************************
 *  Kompilacja:  javac Insertion.java
 *  Wykonanie:    java Insertion N
 *  
 *  Sortowanie przez wstawianie N losowych liczb ca³kowitych z przedzia³u od 0 do 1.
 *
 *************************************************************************/

import java.util.Comparator;

public class Insertion {

    // Wykorzystanie porz¹dku naturalnego i interfejsu Comparable
    public static void sort(Comparable[] a) {
        int N = a.length;
        for (int i = 0; i < N; i++) {
            for (int j = i; j > 0 && less(a[j], a[j-1]); j--) {
                exch(a, j, j-1);
            }
            assert isSorted(a, 0, i);
        }
        assert isSorted(a);
    }

    // Wykorzystanie niestandardowego porz¹dku i interfejsu Comparator - zobacz podrozdzia³ 3.5
    public static void sort(Object[] a, Comparator c) {
        int N = a.length;
        for (int i = 0; i < N; i++) {
            for (int j = i; j > 0 && less(c, a[j], a[j-1]); j--) {
                exch(a, j, j-1);
            }
            assert isSorted(a, c, 0, i);
        }
        assert isSorted(a, c);
    }

    // Zwracanie permutacji, w której elementy z a[] maj¹ kolejnoœæ rosn¹c¹.
    // Pierwotna tablica a[] nie jest modyfikowana
    public static int[] indexSort(Comparable[] a) {
        int N = a.length;
        int[] index = new int[N];
        for (int i = 0; i < N; i++)
            index[i] = i;

        for (int i = 0; i < N; i++)
            for (int j = i; j > 0 && less(a[index[j]], a[index[j-1]]); j--)
                exch(index, j, j-1);

        return index;
    }

   /***********************************************************************
    *  Pomocnicze funkcje sortuj¹ce
    ***********************************************************************/
    
    // Czy v < w ?
    private static boolean less(Comparable v, Comparable w) {
        return (v.compareTo(w) < 0);
    }

    // Czy v < w ?
    private static boolean less(Comparator c, Object v, Object w) {
        return (c.compare(v, w) < 0);
    }
        
    // Przestawianie a[i] z a[j]
    private static void exch(Object[] a, int i, int j) {
        Object swap = a[i];
        a[i] = a[j];
        a[j] = swap;
    }

    // Przestawianie a[i] z a[j]
    private static void exch(int[] a, int i, int j) {
        int swap = a[i];
        a[i] = a[j];
        a[j] = swap;
    }

   /***********************************************************************
    *  Sprawdzanie, czy tablica jest posortowana - przydatne przy diagnozowaniu
    ***********************************************************************/
    private static boolean isSorted(Comparable[] a) {
        return isSorted(a, 0, a.length - 1);
    }

    // Czy posortowana jest tablica od a[lo] do a[hi]
    private static boolean isSorted(Comparable[] a, int lo, int hi) {
        for (int i = lo + 1; i <= hi; i++)
            if (less(a[i], a[i-1])) return false;
        return true;
    }

    private static boolean isSorted(Object[] a, Comparator c) {
        return isSorted(a, c, 0, a.length - 1);
    }

    // Czy posortowana jest tablica od a[lo] do a[hi]
    private static boolean isSorted(Object[] a, Comparator c, int lo, int hi) {
        for (int i = lo + 1; i <= hi; i++)
            if (less(c, a[i], a[i-1])) return false;
        return true;
    }


    // Klient testowy
    public static void main(String[] args) {

        // Generowanie tablicy N losowych liczb rzeczywistych z przedzia³u od 0 do 1
        int N = Integer.parseInt(args[0]);
        Double[] a = new Double[N];
        for (int i = 0; i < N; i++) {
            a[i] = Math.random();
        }
        
        // Sortowanie tablicy
        sort(a);

        // Wyœwietlanie wyników
        for (int i = 0; i < N; i++) {
            System.out.println(a[i]);
        }
        System.out.println("isSorted = " + isSorted(a));
    }
}


