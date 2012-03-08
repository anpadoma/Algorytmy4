/****************************************************************************
 *  Kompilacja:  javac UF.java
 *  Wykonanie:  java UF < input.txt
 *  Zale¿noœci: StdIn.java StdOut.java
 *
 *  Szybka metoda Union z wagami (bez kompresji œcie¿ki).
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
 *  Liczba sk³adowych: 2
 *
 ****************************************************************************/


/**
 *  Klasa <tt>UF</tt> reprezentuje strukturê danych union-find.
 *  Udostêpnia operacje <em>union</em> i <em>find</em>,
 *  a tak¿e metodê do okreœlania liczby roz³¹cznych
 *  zbiorów.
 *  <p>
 *  W tej implementacji wykorzystano wa¿on¹ szybk¹ metodê Union.
 *  Czas tworzenia struktury danych o N obiektach roœnie liniowo.
 *  PóŸniej wszystkie operacje w najgorszym razie wymagaj¹ czasu logarytmicznego.
 *  <p>
 *  Dodatkow¹ dokumentacjê znajdziesz w <a href="http://algs4.cs.princeton.edu/15uf">podrozdziale 1.5</a> ksi¹¿ki
 *  <i>Algorytmy, wydanie czwarte</i> Roberta Sedgewicka i Kevina Wayne'a.
 */

public class UF {
    private int[] id;    // id[i] = rodzic i
    private int[] sz;    // sz[i] = liczba obiektów w poddrzewie o korzeniu w i
    private int count;   // Liczba sk³adowych

   /**
     * Tworzenie pustej struktury danych Union-Find za pomoc¹ N izolowanych zbiorów.
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
     * Zwracanie identyfikatora komponentu odpowiadaj¹cego obiektowi p.
     */
    public int find(int p) {
        while (p != id[p])
            p = id[p];
        return p;
    }

   /**
     * zwracanie liczby roz³¹cznych zbiorów.
     */
    public int count() {
        return count;
    }

  
   /**
     * Czy obiekty p i q znajduj¹ siê w tym samym zbiorze?
     */
    public boolean connected(int p, int q) {
        return find(p) == find(q);
    }

  
   /**
     * Zastêpowanie zbiorów zawieraj¹cych p i q ich sum¹.
     */
    public void union(int p, int q) {
        int i = find(p);
        int j = find(q);
        if (i == j) return;

        // Ustawianie mniejszego korzenia, tak aby prowadzi³ do wiêkszego
        if   (sz[i] < sz[j]) { id[i] = j; sz[j] += sz[i]; }
        else                 { id[j] = i; sz[i] += sz[j]; }
        count--;
    }


    public static void main(String[] args) {
        int N = StdIn.readInt();
        UF uf = new UF(N);

        // Wczytywanie ci¹gu par liczb ca³kowitych (ka¿da z przedzia³u od 0 do N-1),
        // i wywo³anie dla ka¿dej pary metody find(). Jeœli elementy z pary nie s¹ po³¹czone, 
        // nale¿y wywo³aæ union() i wyœwietliæ parê.
        while (!StdIn.isEmpty()) {
            int p = StdIn.readInt();
            int q = StdIn.readInt();
            if (uf.connected(p, q)) continue;
            uf.union(p, q);
            StdOut.println(p + " " + q);
        }
        StdOut.println("Liczba sk³adowych: " + uf.count());
    }

}

