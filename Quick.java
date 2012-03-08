/*************************************************************************
 *  Kompilacja:  javac Quick.java
 *  Wykonanie:    java Quick N
 *  Zale�no�ci: StdRandom.java
 *  
 *  Generowanie N losowych liczb rzeczywistych z przedzia�u od 0 do 1 i szybkie ich sortowanie.
 *
 *************************************************************************/

public class Quick {

    // Sortowanie szybkie tablicy.
    public static void sort(Comparable[] a) {
        StdRandom.shuffle(a);
        sort(a, 0, a.length - 1);
    }

    // Sortowanie szybkie podtablicy od a[lo] do a[hi]
    private static void sort(Comparable[] a, int lo, int hi) { 
        if (hi <= lo) return;
        int j = partition(a, lo, hi);
        sort(a, lo, j-1);
        sort(a, j+1, hi);
        assert isSorted(a, lo, hi);
    }

    // Podzia� podtablicy a[lo .. hi] przez zwr�cenie indeksu j,
    // tak aby a[lo .. j-1] <= a[j] <= a[j+1 .. hi]
    private static int partition(Comparable[] a, int lo, int hi) {
        int i = lo;
        int j = hi + 1;
        Comparable v = a[lo];
        while (true) { 

            // Znajdowanie w lo elementu do przestawienia
            while (less(a[++i], v))
                if (i == hi) break;

            // Znajdowanie w hi elementu do przestawienia
            while (less(v, a[--j]))
                if (j == lo) break;      // Zb�dne, poniewa� a[lo] pe�ni funkcj� wartownika

            // Sprawdzanie, czy wska�niki si� przeci�y
            if (i >= j) break;

            exch(a, i, j);
        }

        // Umieszczanie v = a[j] na odpowiedniej pozycji
        exch(a, lo, j);

        // Przy a[lo .. j-1] <= a[j] <= a[j+1 .. hi]
        return j;
    }

   /***********************************************************************
    *  Zmienia uporz�dkowanie element�w w a, tak aby a[k] by� k-tym najmniejszym elementem,
    *  warto�ci od a[0] do a[k-1] by�y mniejsze lub r�wne a[k], a warto�ci
    *  od a[k+1] do a[n-1] - wi�ksze lub r�wne a[k].
    ***********************************************************************/
    public static Comparable select(Comparable[] a, int k) {
        if (k < 0 || k >= a.length) {
            throw new RuntimeException("Wybrany element wychodzi poza zakres tablicy");
        }
        StdRandom.shuffle(a);
        int lo = 0, hi = a.length - 1;
        while (hi > lo) {
            int i = partition(a, lo, hi);
            if      (i > k) hi = i - 1;
            else if (i < k) lo = i + 1;
            else return a[i];
        }
        return a[lo];
    }



   /***********************************************************************
    *  Funkcje pomocnicze do sortowania
    ***********************************************************************/
    
    // Czy v < w?
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



    // Klient testowy
    public static void main(String[] args) {

        // Generuje tablic� N losowych liczb rzeczywistych z przedzia�u od 0 do 1
        int N = Integer.parseInt(args[0]);
        Double[] a = new Double[N];
        for (int i = 0; i < N; i++) {
            a[i] = Math.random();
        }
        
        // Sortowanie tablicy
        Quick.sort(a);

        // Wy�wietlanie wynik�w
        for (int i = 0; i < N; i++) {
            System.out.println(a[i]);
        }
        System.out.println();
        System.out.println("isSorted = " + isSorted(a));
        System.out.println();

        // Wy�wietlanie wynik�w za pomoc� metody select()
        for (int i = 0; i < N; i++) {
            Double ith = (Double) Quick.select(a, i);
            System.out.println(ith);
        }
        System.out.println();
    }
}
