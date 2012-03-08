/*************************************************************************
 *  Kompilacja:   javac ThreeSum.java
 *  Wykonanie:    java ThreeSum input.txt
 *  Pliki z danymi:   http://algs4.cs.princeton.edu/14analysis/1Kints.txt
 *                http://algs4.cs.princeton.edu/14analysis/2Kints.txt
 *                http://algs4.cs.princeton.edu/14analysis/4Kints.txt
 *                http://algs4.cs.princeton.edu/14analysis/8Kints.txt
 *                http://algs4.cs.princeton.edu/14analysis/16Kints.txt
 *                http://algs4.cs.princeton.edu/14analysis/32Kints.txt
 *                http://algs4.cs.princeton.edu/14analysis/1Mints.txt
 *
 *  Program o z³o¿onoœci szeœciennej. Wczytuje N liczb ca³kowitych
 *  i okreœla liczbê trójek, które sumuj¹ siê dok³adnie do 0
 *  (nie uwzglêdnia przepe³nienia typu int).
 *
 *  % java ThreeSum 1Kints.txt 
 *  70
 *
 *  % java ThreeSum 2Kints.txt 
 *  528
 *
 *  % java ThreeSum 4Kints.txt 
 *  4039
 *
 *************************************************************************/

public class ThreeSum {

    // Wyœwietla ró¿ne trójki (i, j, k), takie ¿e a[i] + a[j] + a[k] = 0
    public static void printAll(int[] a) {
        int N = a.length;
        for (int i = 0; i < N; i++) {
            for (int j = i+1; j < N; j++) {
                for (int k = j+1; k < N; k++) {
                    if (a[i] + a[j] + a[k] == 0) {
                        StdOut.println(a[i] + " " + a[j] + " " + a[k]);
                    }
                }
            }
        }
    } 

    // Zwraca liczbê ró¿nych trójek (i, j, k), takich ¿e a[i] + a[j] + a[k] = 0
    public static int count(int[] a) {
        int N = a.length;
        int cnt = 0;
        for (int i = 0; i < N; i++) {
            for (int j = i+1; j < N; j++) {
                for (int k = j+1; k < N; k++) {
                    if (a[i] + a[j] + a[k] == 0) {
                        cnt++;
                    }
                }
            }
        }
        return cnt;
    } 

    public static void main(String[] args)  { 
        int[] a = In.readInts(args[0]);

        Stopwatch timer = new Stopwatch();
        int cnt = count(a);
        StdOut.println("Czas wykonania = " + timer.elapsedTime());
        StdOut.println(cnt);
    } 
} 
