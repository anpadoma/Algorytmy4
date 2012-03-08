/*************************************************************************
 *  Kompilacja:  javac Shell.java
 *  Wykonanie:    java Shell N
 *  
 *  Porz�dkuje przez sortowanie Shella N losowych liczb rzeczywistych z przedzia�u od 0 do 1.
 *
 *  Wykorzystano ci�g odst�p�w zaproponowany przez Sedgewicka i Incerpiego.
 *  N-ty element ci�gu to najmniejsza liczba ca�kowita >= 2,5^n
 *  wzgl�dnie pierwsza do wszystkich wcze�niejszych warto�ci ci�gu.
 *  Przyk�adowo, incs[4] to 41, poniewa� 2,5^4 = 39,0625, a 41 to
 *  nast�pna liczba ca�kowita wzgl�dnie pierwsza do 3, 7 i 16.
 *
 *************************************************************************/

public class Shell {

    // Sortowanie tablicy a[] w porz�dku rosn�cym przez sortowanie Shella
    public static void sort(Comparable[] a) {
        int N = a.length;

        // Ci�g odst�p�w 3x+1:  1, 4, 13, 40, 121, 364, 1093, ... 
        int h = 1;
        while (h < N/3) h = 3*h + 1; 

        while (h >= 1) {
            // h-sortowanie tablicy
            for (int i = h; i < N; i++) {
                for (int j = i; j >= h && less(a[j], a[j-h]); j -= h) {
                    exch(a, j, j-h);
                }
            }
            assert isSorted(a, h); 
            h /= 3;
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

    // Czy tablicya jest h-posortowana?
    private static boolean isSorted(Comparable[] a, int h) {
        for (int i = h; i < a.length; i++)
            if (less(a[i], a[i-h])) return false;
        return true;
    }


    // Klient testowy
    public static void main(String[] args) {

        // Genrowanie tablicy N losowych liczb rzeczywistych z przedzia�u od 0 do 1
        int N = Integer.parseInt(args[0]);
        Double[] a = new Double[N];
        for (int i = 0; i < N; i++) {
            a[i] = Math.random();
        }
        
        // Sortowanie tablicy
        sort(a);

        // Wy�wietlanie wynik�w
        for (int i = 0; i < N; i++) {
            System.out.println(a[i]);
        }
        System.out.println("isSorted = " + isSorted(a));
    }

}


