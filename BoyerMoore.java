/***************************************************************
 *  Kompilacja:  javac BoyerMoore.java
 *  Wykonanie:    java BoyerMoore wzorzec tekst
 *
 *  Wczytuje dwa �a�cuchy znak�w (wzorzec i tekst), a nast�pnie
 *  wyszukuje wzorzec w tek�cie za pomoc� regu�y 
 *  obs�ugi niedopasowania znak�w z algorytmu Boyera-Moore'a
 *  (bez implementacji mocnej regu�y dobrych przyrostk�w).
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
    private int[] right;     // Tablica przeskok�w przy napotkaniu niepasuj�cego znaku.

    private char[] pattern;  // Zapisywanie wzorca jako tablicy znak�w
    private String pat;      // lub obiektu String.

    // Wzorzec udost�pniany jako �a�cuch znak�w.
    public BoyerMoore(String pat) {
        this.R = 256;
        this.pat = pat;

        // Pozycja pierwszego od prawej wyst�pienia c we wzorcu.
        right = new int[R];
        for (int c = 0; c < R; c++)
            right[c] = -1;
        for (int j = 0; j < pat.length(); j++)
            right[pat.charAt(j)] = j;
    }

    // Wzorzec udost�pniany jako tablica znak�w.
    public BoyerMoore(char[] pattern, int R) {
        this.R = R;
        this.pattern = new char[pattern.length];
        for (int j = 0; j < pattern.length; j++)
            this.pattern[j] = pattern[j];

        // Pozycja pierwszego od prawej wyst�pienia c we wzorcu.
        right = new int[R];
        for (int c = 0; c < R; c++)
            right[c] = -1;
        for (int j = 0; j < pattern.length; j++)
            right[pattern[j]] = j;
    }

    // Zwraca pozycj� pierwszego dopasowania (N, je�li nie ma dopasowania).
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


    // Zwraca pozycj� pierwszego dopasowania (N, je�li nie ma dopasowania).
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

        // Wy�wietla wyniki.
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
