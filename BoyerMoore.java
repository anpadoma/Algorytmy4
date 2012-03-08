/***************************************************************
 *  Kompilacja:  javac BoyerMoore.java
 *  Wykonanie:    java BoyerMoore wzorzec tekst
 *
 *  Wczytuje dwa ³añcuchy znaków (wzorzec i tekst), a nastêpnie
 *  wyszukuje wzorzec w tekœcie za pomoc¹ regu³y 
 *  obs³ugi niedopasowania znaków z algorytmu Boyera-Moore'a
 *  (bez implementacji mocnej regu³y dobrych przyrostków).
 *
 *  % java BoyerMoore abracadabra abacadabrabracabracadabrabrabracad
 *  tekst:    abacadabrabracabracadabrabrabracad 
 *  wzorzec:                abracadabra
 *
 *  % java BoyerMoore rab abacadabrabracabracadabrabrabracad
 *  tekst:    abacadabrabracabracadabrabrabracad 
 *  wzorzec:          rab
 *
 *  % java BoyerMoore bcara abacadabrabracabracadabrabrabracad
 *  tekst:    abacadabrabracabracadabrabrabracad 
 *  wzorzec:                                    bcara
 *
 *  % java BoyerMoore rabrabracad abacadabrabracabracadabrabrabracad
 *  tekst:    abacadabrabracabracadabrabrabracad
 *  wzorzec:                         rabrabracad
 *
 *  % java BoyerMoore abacad abacadabrabracabracadabrabrabracad
 *  tekst:    abacadabrabracabracadabrabrabracad
 *  wzorzec:  abacad
 *
 ***************************************************************/

public class BoyerMoore {
    private final int R;     // Podstawa.
    private int[] right;     // Tablica przeskoków przy napotkaniu niepasuj¹cego znaku.

    private char[] pattern;  // Zapisywanie wzorca jako tablicy znaków
    private String pat;      // lub obiektu String.

    // Wzorzec udostêpniany jako ³añcuch znaków.
    public BoyerMoore(String pat) {
        this.R = 256;
        this.pat = pat;

        // Pozycja pierwszego od prawej wyst¹pienia c we wzorcu.
        right = new int[R];
        for (int c = 0; c < R; c++)
            right[c] = -1;
        for (int j = 0; j < pat.length(); j++)
            right[pat.charAt(j)] = j;
    }

    // Wzorzec udostêpniany jako tablica znaków.
    public BoyerMoore(char[] pattern, int R) {
        this.R = R;
        this.pattern = new char[pattern.length];
        for (int j = 0; j < pattern.length; j++)
            this.pattern[j] = pattern[j];

        // Pozycja pierwszego od prawej wyst¹pienia c we wzorcu.
        right = new int[R];
        for (int c = 0; c < R; c++)
            right[c] = -1;
        for (int j = 0; j < pattern.length; j++)
            right[pattern[j]] = j;
    }

    // Zwraca pozycjê pierwszego dopasowania (N, jeœli nie ma dopasowania).
    public int search(String txt) {
        int M = pat.length();
        int N = txt.length();
        int skip;
        for (int i = 0; i <= N - M; i += skip) {
            skip = 0;
            for (int j = M-1; j >= 0; j--) {
                if (pat.charAt(j) != txt.charAt(i+j)) {
                    skip = Math.max(1, j - right[txt.charAt(i+j)]);
                    break;
                }
            }
            if (skip == 0) return i;    // Znaleziono.
        }
        return N;                       // Nie znaleziono.
    }


    // Zwraca pozycjê pierwszego dopasowania (N, jeœli nie ma dopasowania).
    public int search(char[] text) {
        int M = pattern.length;
        int N = text.length;
        int skip;
        for (int i = 0; i <= N - M; i += skip) {
            skip = 0;
            for (int j = M-1; j >= 0; j--) {
                if (pattern[j] != text[i+j]) {
                    skip = Math.max(1, j - right[text[i+j]]);
                    break;
                }
            }
            if (skip == 0) return i;    // Znaleziono.
        }
        return N;                       // Nie znaleziono.
    }



    // Klient testowy.
    public static void main(String[] args) {
        String pat = args[0];
        String txt = args[1];
        char[] pattern = pat.toCharArray();
        char[] text    = txt.toCharArray();

        BoyerMoore boyermoore1 = new BoyerMoore(pat);
        BoyerMoore boyermoore2 = new BoyerMoore(pattern, 256);
        int offset1 = boyermoore1.search(txt);
        int offset2 = boyermoore2.search(text);

        // Wyœwietla wyniki.
        StdOut.println("tekst:    " + txt);

        StdOut.print("wzorzec:  ");
        for (int i = 0; i < offset1; i++)
            StdOut.print(" ");
        StdOut.println(pat);

        StdOut.print("wzorzec:  ");
        for (int i = 0; i < offset2; i++)
            StdOut.print(" ");
        StdOut.println(pat);
    }
}
