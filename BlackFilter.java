/*************************************************************************
 *  Kompilacja:  javac BlackFilter.java
 *  Wykonanie:    java BlackFilter blacklist.txt < input.txt
 *  Zale�no�ci: SET In.java StdIn.java
 *
 *  Wczytuje czarn� list� s��w z pliku. Nast�pnie wczytuje list� s��w
 *  ze standardowego wej�cia i wy�wietla wszystkie s�owa, kt�re nie
 *  wyst�puj� w pierwszym pliku.
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

        // Wczytuje �a�cuchy znak�w i dodaje je do zbioru.
        In in = new In(args[0]);
        while (!in.isEmpty()) {
            String word = in.readString();
            set.add(word);
        }

        // Wczytuje �a�cuch znak�w ze standardowego wej�cia i wy�wietla wszystkie wyj�tki.
        while (!StdIn.isEmpty()) {
            String word = StdIn.readString();
            if (!set.contains(word))
                StdOut.println(word);
        }
    }
}
