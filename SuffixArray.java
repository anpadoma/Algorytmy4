/*************************************************************************
 *  Kompilacja:  javac SuffixArray.java
 *  Wykonanie:    java SuffixArray < input.txt
 *  
 *  Typ danych do tworzenia tablicy przyrostkowej dla ³añcucha znaków.
 *
 *  % java SuffixArray < abra.txt 
 *    i ind lcp rnk  select
 *  ---------------------------
 *    0  11   -   0  !
 *    1  10   0   1  A!
 *    2   7   1   2  ABRA!
 *    3   0   4   3  ABRACADABRA!
 *    4   3   1   4  ACADABRA!
 *    5   5   1   5  ADABRA!
 *    6   8   0   6  BRA!
 *    7   1   3   7  BRACADABRA!
 *    8   4   0   8  CADABRA!
 *    9   6   0   9  DABRA!
 *   10   9   0  10  RA!
 *   11   2   2  11  RACADABRA!
 *
 *************************************************************************/

import java.util.Arrays;

public class SuffixArray {
    private final String[] suffixes;
    private final int N;

    public SuffixArray(String s) {
        N = s.length();
        suffixes = new String[N];
        for (int i = 0; i < N; i++)
            suffixes[i] = s.substring(i);
        Arrays.sort(suffixes);
    }

    // D³ugoœæ ³añcucha znaków
    public int length() { return N; }

    // Indeks i-tego przyrostka na posortowanej liœcie
    public int index(int i) { return N - suffixes[i].length(); }

    // I-ty przyrostek na posortowanej liœcie
    public String select(int i) { return suffixes[i]; }

    // Liczba przyrostków mniejszych ni¿ podany
    public int rank(String query) {
        int lo = 0, hi = N - 1;
        while (lo <= hi) {
            int mid = lo + (hi - lo) / 2;
            int cmp = query.compareTo(suffixes[mid]);
            if      (cmp < 0) hi = mid - 1;
            else if (cmp > 0) lo = mid + 1;
            else return mid;
        }
        return lo;
    } 

   // D³ugoœæ najd³u¿szego wspólnego przyrostka ³añcuchów s i t
    private static int lcp(String s, String t) {
        int N = Math.min(s.length(), t.length());
        for (int i = 0; i < N; i++)
            if (s.charAt(i) != t.charAt(i)) return i;
        return N;
    }

    // Najd³u¿szy wspólny przedrostek ³añcuchów suffixes(i) i suffixes(i-1)
    public int lcp(int i) {
        return lcp(suffixes[i], suffixes[i-1]);
    }

    // Najd³u¿szy wspólny przedrostek ³añcuchów suffixes(i) i suffixes(j)
    public int lcp(int i, int j) {
        return lcp(suffixes[i], suffixes[j]);
    }




    public static void main(String[] args) {
        String s = StdIn.readAll().trim();
        SuffixArray suffix = new SuffixArray(s);

        StdOut.println("  i ind lcp rnk  select");
        StdOut.println("---------------------------");
        StdOut.printf("%3d %3d %3s %3d  %s\n", 0, suffix.index(0), "-", suffix.rank(suffix.select(0)), suffix.select(0));
        for (int i = 1; i < s.length(); i++) {
            int index = suffix.index(i);
            String ith = suffix.select(i);
            int lcp = suffix.lcp(i, i-1);
            int rank = suffix.rank(ith);
            StdOut.printf("%3d %3d %3d %3d  %s\n", i, index, lcp, rank, ith);
        }
    }

}
