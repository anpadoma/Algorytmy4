/*************************************************************************
 *  Kompilacja:  javac WhiteFilter.java
 *  Wykonanie:    java WhiteFilter whitelist.txt < input.txt
 *  Zale�no�ci: SET In.java StdIn.java
 *
 *  Wczytuje z pliku bia�� list� s��w. Nast�pnie wczytuje list� s��w
 *  ze standardowego wej�cia i wy�wietla wszystkie s�owa, kt�re znajduj�
 *  si� w pierwszym pliku.
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

        // Wczytywanie �a�cuch�w znak�w i dodawanie ich do zbioru
        In in = new In(args[0]);
        while (!in.isEmpty()) {
            String word = in.readString();
            set.add(word);
        }

        // Wczytywanie �a�cucha znak�w ze standardowego wej�cia i wy�wietlanie wszystkich 
		// wyst�pie� �a�cuch�w ze zbioru
        while (!StdIn.isEmpty()) {
            String word = StdIn.readString();
            if (set.contains(word))
                StdOut.println(word);
        }
    }
}
