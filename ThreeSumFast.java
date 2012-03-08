/*************************************************************************
 *  Kompilacja:   javac ThreeSumFast.java
 *  Wykonanie:    java ThreeSumFast input.txt
 *  Plik z danymi:   http://algs4.cs.princeton.edu/14analysis/1Kints.txt
 *                http://algs4.cs.princeton.edu/14analysis/2Kints.txt
 *                http://algs4.cs.princeton.edu/14analysis/4Kints.txt
 *                http://algs4.cs.princeton.edu/14analysis/8Kints.txt
 *                http://algs4.cs.princeton.edu/14analysis/16Kints.txt
 *                http://algs4.cs.princeton.edu/14analysis/32Kints.txt
 *                http://algs4.cs.princeton.edu/14analysis/1Mints.txt
 *
 *  Program o z³o¿onoœci N^2 log N. Wczytuje N liczb ca³kowitych
 *  i okreœla liczbê trójek, któych suma wynosi dok³adnie 0.
 *
 *  Ograniczenia
 *  -----------
 *     - pomijane jest przepe³nienie typu int
 *     - nie obs³uguje danych wejœciowych z powtórzeniami
 *
 *
 *  % java ThreeSumFast 1Kints.txt
 *  70
 *  
 *  % java ThreeSumFast 2Kints.txt
 *  528
 *                
 *  % java ThreeSumFast 4Kints.txt
 *  4039
 * 
 *  % java ThreeSumFast 8Kints.txt
 *  32074
 *
 *  % java ThreeSumFast 16Kints.txt
 *  255181
 *
 *  % java ThreeSumFast 32Kints.txt
 *  2052358
 *
 *************************************************************************/

import java.util.Arrays;

public class ThreeSumFast {

    // Wyœwietla ró¿ne trójki (i, j, k), w których a[i] + a[j] + a[k] = 0
    public static void printAll(int[] a) {
        int N = a.length;
        Arrays.sort(a);
        for (int i = 0; i < N; i++) {
            for (int j = i+1; j < N; j++) {
                int k = Arrays.binarySearch(a, -(a[i] + a[j]));
                if (k > j) StdOut.println(a[i] + " " + a[j] + " " + a[k]);
            }
        }
    } 

    // Zwraca liczbê ró¿nych trójek (i, j, k), w których a[i] + a[j] + a[k] = 0
    public static int count(int[] a) {
        int N = a.length;
        Arrays.sort(a);
        int cnt = 0;
        for (int i = 0; i < N; i++) {
            for (int j = i+1; j < N; j++) {
                int k = Arrays.binarySearch(a, -(a[i] + a[j]));
                if (k > j) cnt++;
            }
        }
        return cnt;
    } 

    public static void main(String[] args)  { 
        int[] a = In.readInts(args[0]);
        int cnt = count(a);
        StdOut.println(cnt);
    } 
} 
