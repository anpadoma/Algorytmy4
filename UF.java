/****************************************************************************
 *  Kompilacja:  javac UF.java
 *  Wykonanie:  java UF < input.txt
 *  Zale�no�ci: StdIn.java StdOut.java
 *
 *  Szybka metoda Union z wagami (bez kompresji �cie�ki).
 *
 *  % java UF < tinyUF.txt
 *  4 3
 *  3 8
 *  6 5
 *  9 4
 *  2 1
 *  5 0
 *  7 2
 *  6 1
 *  Liczba sk�adowych: 2
 *
 ****************************************************************************/


/**
 *  Klasa <tt>UF</tt> reprezentuje struktur� danych union-find.
 *  Udost�pnia operacje <em>union</em> i <em>find</em>,
 *  a tak�e metod� do okre�lania liczby roz��cznych
 *  zbior�w.
 *  <p>
 *  W tej implementacji wykorzystano wa�on� szybk� metod� Union.
 *  Czas tworzenia struktury danych o N obiektach ro�nie liniowo.
 *  P�niej wszystkie operacje w najgorszym razie wymagaj� czasu logarytmicznego.
 *  <p>
 *  Dodatkow� dokumentacj� znajdziesz w <a href="http://algs4.cs.princeton.edu/15uf">podrozdziale 1.5</a> ksi��ki
 *  <i>Algorytmy, wydanie czwarte</i> Roberta Sedgewicka i Kevina Wayne'a.
 */

public class UF {
    private int[] id;    // id[i] = rodzic i
    private int[] sz;    // sz[i] = liczba obiekt�w w poddrzewie o korzeniu w i
    private int count;   // Liczba sk�adowych

   /**
     * Tworzenie pustej struktury danych Union-Find za pomoc� N izolowanych zbior�w.
     */
    public UF(int N) {
        count = N;
        id = new int[N];
        sz = new int[N];
        for (int i = 0; i < N; i++) {
            id[i] = i;
            sz[i] = 1;
        }
    }

   /**
     * Zwracanie identyfikatora komponentu odpowiadaj�cego obiektowi p.
     */
    public int find(int p) {
        while (p != id[p])
            p = id[p];
        return p;
    }

   /**
     * zwracanie liczby roz��cznych zbior�w.
     */
    public int count() {
        return count;
    }

  
   /**
     * Czy obiekty p i q znajduj� si� w tym samym zbiorze?
     */
    public boolean connected(int p, int q) {
        return find(p) == find(q);
    }

  
   /**
     * Zast�powanie zbior�w zawieraj�cych p i q ich sum�.
     */
    public void union(int p, int q) {
        int i = find(p);
        int j = find(q);
        if (i == j) return;

        // Ustawianie mniejszego korzenia, tak aby prowadzi� do wi�kszego
        if   (sz[i] < sz[j]) { id[i] = j; sz[j] += sz[i]; }
        else                 { id[j] = i; sz[i] += sz[j]; }
        count--;
    }


    public static void main(String[] args) {
        int N = StdIn.readInt();
        UF uf = new UF(N);

        // Wczytywanie ci�gu par liczb ca�kowitych (ka�da z przedzia�u od 0 do N-1),
        // i wywo�anie dla ka�dej pary metody find(). Je�li elementy z pary nie s� po��czone, 
        // nale�y wywo�a� union() i wy�wietli� par�.
        while (!StdIn.isEmpty()) {
            int p = StdIn.readInt();
            int q = StdIn.readInt();
            if (uf.connected(p, q)) continue;
            uf.union(p, q);
            StdOut.println(p + " " + q);
        }
        StdOut.println("Liczba sk�adowych: " + uf.count());
    }

}

