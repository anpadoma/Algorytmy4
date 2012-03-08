/*************************************************************************
 *  Kompilacja:  javac DeDup.java
 *  Wykonanie:    java DeDup < words.txt
 *  Zale�no�ci: SET StdIn.java
 *
 *  Wczytuje list� s��w ze standardowego wej�cia i wy�wietla ka�de
 *  s�owo, usuwaj�c przy tym powt�rzenia.
 *
 *************************************************************************/

public class DeDup {  
    public static void main(String[] args) {
        SET<String> set = new SET<String>();

        // Wczytywanie �a�cuch�w znak�w i dodawanie ich do zbioru
        while (!StdIn.isEmpty()) {
            String key = StdIn.readString();
            if (!set.contains(key)) {
                set.add(key);
                StdOut.println(key);
            }
        }
    }
}
