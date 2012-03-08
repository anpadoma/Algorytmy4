/*************************************************************************
 *  Kompilacja:   javac Whitelist.java
 *  Wykonanie:    java Whitelist whitelist.txt < data.txt
 *  Zale�no�ci:   StaticSetOfInts.java In.java StdOut.java
 *  Pliki z danymi:   http://algs4.cs.princeton.edu/11model/tinyW.txt
 *                    http://algs4.cs.princeton.edu/11model/tinyT.txt
 *                    http://algs4.cs.princeton.edu/11model/largeW.txt
 *                    http://algs4.cs.princeton.edu/11model/largeT.txt
 *
 *  Filtrowanie na podstawie bia�ej listy.
 *
 *
 *  % java Whitelist tinyW.txt < tinyT.txt
 *  50
 *  99
 *  13
 *
 *  % java Whitelist largeW.txt < largeT.txt | more
 *  499569
 *  984875
 *  295754
 *  207807
 *  140925
 *  161828
 *  [��cznie 3 675 966 warto�ci]
 *
 *************************************************************************/

public class Whitelist {
    public static void main(String[] args) {
        int[] w = In.readInts(args[0]);
        StaticSETofInts set = new StaticSETofInts(w);

        // Wczytywanie klucza i wy�wietlanie go, je�li nie wyst�puje na bia�ej li�cie.
        while (!StdIn.isEmpty()) {
            int key = StdIn.readInt();
            if (!set.contains(key))
                StdOut.println(key);
        }
    }
}
