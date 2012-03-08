/*************************************************************************
 *  Kompilacja:  javac DeDup.java
 *  Wykonanie:    java DeDup < words.txt
 *  Zale¿noœci: SET StdIn.java
 *
 *  Wczytuje listê s³ów ze standardowego wejœcia i wyœwietla ka¿de
 *  s³owo, usuwaj¹c przy tym powtórzenia.
 *
 *************************************************************************/

public class DeDup {  
    public static void main(String[] args) {
        SET<String> set = new SET<String>();

        // Wczytywanie ³añcuchów znaków i dodawanie ich do zbioru
        while (!StdIn.isEmpty()) {
            String key = StdIn.readString();
            if (!set.contains(key)) {
                set.add(key);
                StdOut.println(key);
            }
        }
    }
}
