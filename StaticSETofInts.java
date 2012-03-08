/*************************************************************************
 *  Kompilacja:  javac StaticSetOfInts.java
 *  
 *  Typ danych do przechowywania zbioru liczb ca�kowitych.
 *
 *************************************************************************/

import java.util.Arrays;

public class StaticSETofInts {
    private int[] a;
    public StaticSETofInts(int[] keys) {
        // Kopiowanie zabezpieczaj�ce
        a = new int[keys.length];
        for (int i = 0; i < keys.length; i++)
            a[i] = keys[i];

        Arrays.sort(a);

        // Prawdopodobnie nale�y sprawdzi�, czy nie wyst�puj� powt�rzenia
    }

    public boolean contains(int key) {
        return rank(key) != -1;
    }

    // Wyszukiwanie binarne.
    public int rank(int key) {
        int lo = 0;
        int hi = a.length - 1;
        while (lo <= hi) {
            // Klucz znajduje si� w przedziale a[lo..hi] lub w og�le nie wyst�puje.
            int mid = lo + (hi - lo) / 2;
            if      (key < a[mid]) hi = mid - 1;
            else if (key > a[mid]) lo = mid + 1;
            else return mid;
        }
        return -1;
    }
}
