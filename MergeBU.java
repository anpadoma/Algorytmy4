/*************************************************************************
 *  Kompilacja:  javac MergeBU.java
 *  Wykonanie:    java MergeBU N
 *  
 *  Generuje N liczb pseudolosowych z przedzia³u od 0 do 1 i sortuje je przez scalanie.
 *  Sortowanie przez scalanie od do³u do góry (metoda wstêpuj¹ca).
 *
 *************************************************************************/

public class MergeBU {



    // Stabilne scalanie a[lo..m] z a[m+1..hi] przy u¿yciu aux[lo..hi]
    private static void merge(Comparable[] a, Comparable[] aux, int lo, int m, int hi) {

        // Kopiowanie do aux[]
        for (int k = lo; k <= hi; k++) {
            aux[k] = a[k]; 
        }

        // Scalanie z powrotem do a[]
        int i = lo, j = m+1;
        for (int k = lo; k <= hi; k++) {
            if      (i > m)                a[k] = aux[j++];
            else if (j > hi)               a[k] = aux[i++];
            else if (less(aux[j], aux[i])) a[k] = aux[j++];
            else                           a[k] = aux[i++];
        }

    }

    // Sortowanie przez scalanie od do³u do góry
    public static void sort(Comparable[] a) {
        int N = a.length;
        Comparable[] aux = new Comparable[N];
        for (int n = 1; n < N; n = n+n) {
            for (int i = 0; i < N-n; i += n+n) {
                int lo = i;
                int m  = i+n-1;
                int hi = Math.min(i+n+n-1, N-1);
                merge(a, aux, lo, m, hi);
            }
        }
        assert isSorted(a);
    }

  /***********************************************************************
    *  Funkcje pomocnicze do sortowania
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
    *  Sprawdzanie, czy tablica jest posortowana (przydatne przy diagnozowaniu)
    ***********************************************************************/
    private static boolean isSorted(Comparable[] a) {
        for (int i = 1; i < a.length; i++)
            if (less(a[i], a[i-1])) return false;
        return true;
    }


    // Generowanie N liczb rzeczywistych z przedzia³u od 0 do 1 i sortowanie ich przez scalanie
    public static void main(String[] args) {
        int N = Integer.parseInt(args[0]);
        Double[] a = new Double[N];
        for (int i = 0; i < N; i++)
            a[i] = Math.random();
        MergeBU.sort(a);
        for (int i = 0; i < N; i++)
            System.out.println(a[i]);

        System.out.println(MergeBU.isSorted(a));
    }
}


