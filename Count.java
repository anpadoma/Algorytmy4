/*************************************************************************
 *  Kompilacja:  javac Count.java
 *  Wykonanie:    java Count alpha < input.txt
 *
 *  Tworzenie alfabetu podanego w wierszu poleceñ, wczytywanie ci¹gu
 *  znaków opartego na tym alfabecie (z pominiêciem znaków spoza
 *  alfabetu), wyznaczanie liczby wyst¹pieñ ka¿dego znaku i
 *  wyœwietlanie wyników.
 *
 *  %  java Count ABCDR < abra.txt 
 *  A 5
 *  B 2
 *  C 1
 *  D 1
 *  R 2
 *
 *  % java Count 0123456789 < pi.txt
 *  0 99959
 *  1 99757
 *  2 100026
 *  3 100230
 *  4 100230
 *  5 100359
 *  6 99548
 *  7 99800
 *  8 99985
 *  9 100106
 *
 *************************************************************************/



public class Count {
    public static void main(String[] args) {
        Alphabet alpha = new Alphabet(args[0]);
        int R = alpha.R();
        int[] count = new int[R];
        String a = StdIn.readAll();
        int N = a.length();
        for (int i = 0; i < N; i++)
            if (alpha.contains(a.charAt(i)))
                count[alpha.toIndex(a.charAt(i))]++;
        for (int c = 0; c < R; c++)
            StdOut.println(alpha.toChar(c) + " " + count[c]);
    }
}

