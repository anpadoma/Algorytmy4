/*************************************************************************
 *  Kompilacja:  javac Quick3way.java
 *  Wykonanie:    java Quick3way N
 *  
 *  Generuje N losowych liczb rzeczywistych z przedzia³u od 0 do 1 i sortuje je przez sortowanie szybkie.
 *  Wykorzystano sortowanie szybkie z randomizacj¹ i podzia³em na trzy czêœci Dijkstry. 
 *
 *************************************************************************/

public class Quick3way {

    // Sortowanie szybkie tablicy a[] z wykorzystaniem podzia³u na trzy czêœci
    public static void sort(Comparable[] a) {
        sort(a, 0, a.length - 1);
        assert isSorted(a);
    }

    // Sortowanie szybkie podtablicy a[lo .. hi] z podzia³em na trzy czêœci
    private static void sort(Comparable[] a, int lo, int hi) { 
        if (hi <= lo) return;
        int lt = lo, gt = hi;
        Comparable v = a[lo];
        int i = lo;
        while (i <= gt) {
            int cmp = a[i].compareTo(v);
            if      (cmp < 0) exch(a, lt++, i++);
            else if (cmp > 0) exch(a, i, gt--);
            else              i++;
        }

        // a[lo..lt-1] < v = a[lt..gt] < a[gt+1..hi]. 
        sort(a, lo, lt-1);
        sort(a, gt+1, hi);
        assert isSorted(a, lo, hi);
    }



   /***********************************************************************
    *  Funkcje pomocnicze do sortowania
    ***********************************************************************/
    
    // Czy v < w ?
    private static boolean less(Comparable v, Comparable w) {
        return (v.compareTo(w) < 0);
    }

    // Czy v == w ?
    private static boolean eq(Comparable v, Comparable w) {
        return (v.compareTo(w) == 0);
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

    private static boolean isSorted(Comparable[] a, int lo, int hi) {
        for (int i = lo + 1; i <= hi; i++)
            if (less(a[i], a[i-1])) return false;
        return true;
    }



    // Klient testowy
    public static void main(String[] args) {

        // Generowanie tablicy N losowych liczb ca³kowitych od 0 do 99
        int N = Integer.parseInt(args[0]);
        Double[] a = new Double[N];
        for (int i = 0; i < N; i++) {
            a[i] = (double) StdRandom.uniform(100);
        }
        
        // Sortowanie tablicy
        Quick3way.sort(a);

        // Wyœwietlanie wyników
        for (int i = 0; i < N; i++) {
            System.out.println(a[i]);
        }
        System.out.println("isSorted = " + isSorted(a));
    }

}
