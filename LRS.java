/*************************************************************************
 *  Kompilacja:  javac LRS.java
 *  Wykonanie:    java LRS < file.txt
 *  Zale�no�ci: StdIn.java SuffixArray.java
 *  Pliki z danymi:   http://algs4.cs.princeton.edu/63suffix/tinyTale.txt
 *                http://algs4.cs.princeton.edu/63suffix/mobydick.txt
 *  
 *  Wczytuje tekst ze standardowego wej�cia, zast�puje wszystkie bloki kilku odst�p�w
 *  pojedynczymi odst�pami, a nast�pnie wyznacza najd�u�szy
 *  powtarzaj�cy si� pod�a�cuch, u�ywaj�c tablicy przyrostkowej.
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
