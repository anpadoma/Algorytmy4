/*************************************************************************
 *  Kompilacja:  javac WhiteFilter.java
 *  Wykonanie:    java WhiteFilter whitelist.txt < input.txt
 *  Zale¿noœci: SET In.java StdIn.java
 *
 *  Wczytuje z pliku bia³¹ listê s³ów. Nastêpnie wczytuje listê s³ów
 *  ze standardowego wejœcia i wyœwietla wszystkie s³owa, które znajduj¹
 *  siê w pierwszym pliku.
 * 
 *  % more list.txt 
 *  was it the of 
 * 
 *  % java WhiteFilter list.txt < tinyTale.txt 
 *  it was the of it was the of
 *  it was the of it was the of
 *  it was the of it was the of
 *  it was the of it was the of
 *  it was the of it was the of
 *
 *************************************************************************/

public class WhiteFilter {  
    public static void main(String[] args) {
        SET<String> set = new SET<String>();

        // Wczytywanie ³añcuchów znaków i dodawanie ich do zbioru
        In in = new In(args[0]);
        while (!in.isEmpty()) {
            String word = in.readString();
            set.add(word);
        }

        // Wczytywanie ³añcucha znaków ze standardowego wejœcia i wyœwietlanie wszystkich 
		// wyst¹pieñ ³añcuchów ze zbioru
        while (!StdIn.isEmpty()) {
            String word = StdIn.readString();
            if (set.contains(word))
                StdOut.println(word);
        }
    }
}
