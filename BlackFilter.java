/*************************************************************************
 *  Kompilacja:  javac BlackFilter.java
 *  Wykonanie:    java BlackFilter blacklist.txt < input.txt
 *  Zale¿noœci: SET In.java StdIn.java
 *
 *  Wczytuje czarn¹ listê s³ów z pliku. Nastêpnie wczytuje listê s³ów
 *  ze standardowego wejœcia i wyœwietla wszystkie s³owa, które nie
 *  wystêpuj¹ w pierwszym pliku.
 * 
 *  % more list.txt 
 *  was it the of 
 * 
 *  % java BlackFilter list.txt < tinyTale.txt 
 *  best times worst times 
 *  age wisdom age foolishness 
 *  epoch belief epoch incredulity 
 *  season light season darkness 
 *  spring hope winter despair 
 *
 *************************************************************************/

public class BlackFilter {  
    public static void main(String[] args) {
        SET<String> set = new SET<String>();

        // Wczytuje ³añcuchy znaków i dodaje je do zbioru.
        In in = new In(args[0]);
        while (!in.isEmpty()) {
            String word = in.readString();
            set.add(word);
        }

        // Wczytuje ³añcuch znaków ze standardowego wejœcia i wyœwietla wszystkie wyj¹tki.
        while (!StdIn.isEmpty()) {
            String word = StdIn.readString();
            if (!set.contains(word))
                StdOut.println(word);
        }
    }
}
