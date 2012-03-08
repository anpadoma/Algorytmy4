/*************************************************************************
 *  Kompilacja:  javac LRS.java
 *  Wykonanie:    java LRS < file.txt
 *  Zale¿noœci: StdIn.java SuffixArray.java
 *  Pliki z danymi:   http://algs4.cs.princeton.edu/63suffix/tinyTale.txt
 *                http://algs4.cs.princeton.edu/63suffix/mobydick.txt
 *  
 *  Wczytuje tekst ze standardowego wejœcia, zastêpuje wszystkie bloki kilku odstêpów
 *  pojedynczymi odstêpami, a nastêpnie wyznacza najd³u¿szy
 *  powtarzaj¹cy siê pod³añcuch, u¿ywaj¹c tablicy przyrostkowej.
 * 
 *  % java LRS < tinyTale.txt 
 *  'st of times it was the '
 *
 *  % java LRS < mobydick.txt
 *  ',- Such a funny, sporty, gamy, jesty, joky, hoky-poky lad, is the Ocean, oh! Th'
 * 
 *  % java LRS 
 *  aaaaaaaaa
 *  'aaaaaaaa'
 *
 *  % java LRS
 *  abcdefg
 *  ''
 *
 *************************************************************************/


public class LRS {

    public static void main(String[] args) {
        String text = StdIn.readAll().replaceAll("\\s+", " ");
        SuffixArray sa = new SuffixArray(text);

        int N = sa.length();

        String substring = "";
        for (int i = 1; i < N; i++) {
            int length = sa.lcp(i);
            if (length > substring.length())
                substring = sa.select(i).substring(0, length);
        }
        
        StdOut.println("'" + substring + "'");
    }
}
