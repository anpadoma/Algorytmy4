/*************************************************************************
 *  Kompilacja:  javac Merge.java
 *  Wykonanie:    java Merge N
 *  
 *  Generowanie N liczb pseudolosowych z przedzia³u od 0 i 1 oraz sortowanie ich przez scalanie.
 *
 *************************************************************************/

public class Merge {

    // Stabilne scalanie a[lo .. mid] z a[mid+1 .. hi] za pomoc¹ aux[lo .. hi]
    public static void merge(Comparable[] a, Comparable[] aux, int lo, int mid, int hi) {

        // Warunek wstêpny: a[lo .. mid] i a[mid+1 .. hi] to posortowane podtablice
        assert isSorted(a, lo, mid);
        assert isSorted(a, mid+1, hi);

        // Kopiowanie do aux[]
        for (int k = lo; k <= hi; k++) {
            aux[k] = a[k]; 
        }

        // Scalanie z powrotem do a[]
        int i = lo, j = mid+1;
        for (int k = lo; k <= hi; k++) {
            if      (i > mid)              a[k] = aux[j++];
            else if (j > hi)               a[k] = aux[i++];
            else if (less(aux[j], aux[i])) a[k] = aux[j++];
            else                           a[k] = aux[i++];
        }

        // Warunek koñcowy: a[lo .. hi] jest posortowana
        assert isSorted(a, lo, hi);
    }

    // Sortowanie przez scalanie tablicy a[lo..hi] za pomoc¹ tablicy pomocniczej aux[lo..hi]
    private static void sort(Comparable[] a, Comparable[] aux, int lo, int hi) {
        if (hi <= lo) return;
        int mid = lo + (hi - lo) / 2;
        sort(a, aux, lo, mid);
        sort(a, aux, mid + 1, hi);
        merge(a, aux, lo, mid, hi);
    }

    public static void sort(Comparable[] a) {
        Comparable[] aux = new Comparable[a.length];
        sort(a, aux, 0, a.length-1);
        assert isSorted(a);
    }


   /***********************************************************************
    *  Pomocnicze funkcje do sortowania
    ***********************************************************************/
    
    // Czy v < w ?
    private static boolean less(Comparable v, Comparable w) {
        return (v.compareTo(w) < 0);
    }
        
    // Przestawianie a[i] z a[j]
    private static void exch(Object[] a, int i, int j) {
        Object swap = a[i];
        a[i] = a[j];
        a[j] = swap;
    }


   /***********************************************************************
    *  Sprawdzanie, czy tablica jest posortowana - przydatne przy diagnozowaniu
    ***********************************************************************/
    private static boolean isSorted(Comparable[] a) {
        return isSorted(a, 0, a.length - 1);
    }

    private static boolean isSorted(Comparable[] a, int lo, int hi) {
        for (int i = lo + 1; i <= hi; i++)
            if (less(a[i], a[i-1])) return false;
        return true;
    }


   /***********************************************************************
    *  Sortowanie przez scalanie
    ***********************************************************************/
    // Stabilne scalanie a[lo .. mid] z a[mid+1 .. hi] za pomoc¹ aux[lo .. hi]
    private static void merge(Comparable[] a, int[] index, int[] aux, int lo, int mid, int hi) {

        // Kopiowanie do aux[]
        for (int k = lo; k <= hi; k++) {
            aux[k] = index[k]; 
        }

        // Scalanie z powrotem do a[]
        int i = lo, j = mid+1;
        for (int k = lo; k <= hi; k++) {
            if      (i > mid)                    index[k] = aux[j++];
            else if (j > hi)                     index[k] = aux[i++];
            else if (less(a[aux[j]], a[aux[i]])) index[k] = aux[j++];
            else                                 index[k] = aux[i++];
        }
    }

    // Zwracanie permutacji, w której elementy tablicy a[] s¹ w kolejnoœci rosn¹cej.
    // Bez zmieniania pierwotnej tablicy a[]
    public static int[] indexSort(Comparable[] a) {
        int N = a.length;
        int[] index = new int[N];
        for (int i = 0; i < N; i++)
            index[i] = i;

        int[] aux = new int[N];
        sort(a, index, aux, 0, N-1);
        return index;
    }

    // Sortowanie przez scalanie tablicy a[lo..hi] za pomoc¹ tablicy pomocniczej aux[lo..hi]
    private static void sort(Comparable[] a, int[] index, int[] aux, int lo, int hi) {
        if (hi <= lo) return;
        int mid = lo + (hi - lo) / 2;
        sort(a, index, aux, lo, mid);
        sort(a, index, aux, mid + 1, hi);
        merge(a, index, aux, lo, mid, hi);
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
