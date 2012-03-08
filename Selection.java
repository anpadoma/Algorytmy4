/*************************************************************************
 *  Kompilacja:  javac Selection.java
 *  Wykonanie:    java  Selection N
 *
 *  Generuje N losowych liczb z przedzia³u od 0 do 1 i sortowanie ich przez wybieranie.
 *
 *************************************************************************/

public class Selection {

    // Sortowanie przez wybieranie
    public static void sort(Comparable[] a) {
        int N = a.length;
        for (int i = 0; i < N; i++) {
            int min = i;
            for (int j = i+1; j < N; j++) {
                if (less(a[j], a[min])) min = j;
            }
            exch(a, i, min);
            assert isSorted(a, 0, i);
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
        return isSorted(a, 0, a.length - 1);
    }
        
    // Czy tablica jest posortowana w przedziale od a[lo] do a[hi]
    private static boolean isSorted(Comparable[] a, int lo, int hi) {
        for (int i = lo + 1; i <= hi; i++)
            if (less(a[i], a[i-1])) return false;
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
        
        // Sortowanie tablicz
        sort(a);

        // Wyœwietlanie wyników
        for (int i = 0; i < N; i++) {
            System.out.println(a[i]);
        }
        System.out.println("isSorted = " + isSorted(a));
    }

}
