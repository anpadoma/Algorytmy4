/*************************************************************************
 *  Kompilacja:  javac BinarySearch.java
 *  Wykonanie:    java BinarySearch whitelist.txt < input.txt
 *  Pliki z danymi:   http://algs4.cs.princeton.edu/11model/tinyW.txt
 *                http://algs4.cs.princeton.edu/11model/tinyT.txt
 *                http://algs4.cs.princeton.edu/11model/largeW.txt
 *                http://algs4.cs.princeton.edu/11model/largeT.txt
 *
 *  % java BinarySearch tinyW.txt < tinyT.txt
 *  50
 *  99
 *  13
 *
 *  % java BinarySearch largeW.txt < largeT.txt | more
 *  499569
 *  984875
 *  295754
 *  207807
 *  140925
 *  161828
 *  [��cznie 3 675 966 warto�ci]
 *  
 *************************************************************************/

import java.util.Arrays;

public class BinarySearch {

    // Warunek wst�pny: tablica a[] jest posortowana.
    public static int rank(int key, int[] a) {
        int lo = 0;
        int hi = a.length - 1;
        while (lo <= hi) {
            // Klucz znajduje si� w a[lo..hi] lub nie istnieje.
            int mid = lo + (hi - lo) / 2;
            if      (key < a[mid]) hi = mid - 1;
            else if (key > a[mid]) lo = mid + 1;
            else return mid;
         }
         return -1;
    }

    public static void main(String[] args) {
        int[] whitelist = In.readInts(args[0]);

        Arrays.sort(whitelist);

        // Wczytywanie klucza i wy�wietlanie go, je�li nie znajduje si� na bia�ej li�cie.
        while (!StdIn.isEmpty()) {
            int key = StdIn.readInt();
            if (rank(key, whitelist) == -1)
                StdOut.println(key);
            }
        }
}
