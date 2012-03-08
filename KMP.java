/***************************************************************
 *
 *  Kompilacja:  javac KMP.java
 *  Wykonanie:    java KMP pattern text
 *
 *  Wczytuje dwa ³añcuchy znaków, wzorzec i tekst wejœciowy, a nastêpnie
 *  wyszukuje wzorzec w tekœcie za pomoc¹ algorytmu KMP. 
 *
 *  % java KMP abracadabra abacadabrabracabracadabrabrabracad
 *  Tekst:    abacadabrabracabracadabrabrabracad 
 *  Wzorzec:                abracadabra          
 *
 *  % java KMP rab abacadabrabracabracadabrabrabracad
 *  Tekst:    abacadabrabracabracadabrabrabracad 
 *  Wzorzec:          rab
 *
 *  % java KMP bcara abacadabrabracabracadabrabrabracad
 *  Tekst:    abacadabrabracabracadabrabrabracad 
 *  Wzorzec:                                    bcara
 *
 *  % java KMP rabrabracad abacadabrabracabracadabrabrabracad 
 *  Tekst:    abacadabrabracabracadabrabrabracad
 *  Wzorzec:                         rabrabracad
 *
 *  % java KMP abacad abacadabrabracabracadabrabrabracad
 *  Tekst:    abacadabrabracabracadabrabrabracad
 *  Wzorzec:  abacad
 *
 ***************************************************************/

public class KMP {
    private final int R;       // Podstawa
    private int[][] dfa;       // Automat dla algorytmu KMP

    private char[] pattern;    // Tablica znaków ze wzorcem
    private String pat;        // lub ³añcuch znaków ze wzorcem

    // Tworzy automat DFA na podstawie obiektu String
    public KMP(String pat) {
        this.R = 256;
        this.pat = pat;

        // Tworzy automat DFA na podstawie wzorca
        int M = pat.length();
        dfa = new int[R][M]; 
        dfa[pat.charAt(0)][0] = 1; 
        for (int X = 0, j = 1; j < M; j++) {
            for (int c = 0; c < R; c++) 
                dfa[c][j] = dfa[c][X];     // Przypadek dla niedopasowania. 
            dfa[pat.charAt(j)][j] = j+1;   // Przypadek dla dopasowania. 
            X = dfa[pat.charAt(j)][X];     // Aktualizowanie stanu wznawiania pracy. 
        } 
    } 

    // Tworzy automat DFA na podstawie tablicy znaków opartej na R-znakowym alfabecie
    public KMP(char[] pattern, int R) {
        this.R = R;
        this.pattern = new char[pattern.length];
        for (int j = 0; j < pattern.length; j++)
            this.pattern[j] = pattern[j];

        // Tworzy automat DFA na podstawie wzorca
        int M = pattern.length;
        dfa = new int[R][M]; 
        dfa[pattern[0]][0] = 1; 
        for (int X = 0, j = 1; j < M; j++) {
            for (int c = 0; c < R; c++) 
                dfa[c][j] = dfa[c][X];     // Przypadek dla niedopasowania. 
            dfa[pattern[j]][j] = j+1;      // Przypadek dla dopasowania. 
            X = dfa[pattern[j]][X];        // Aktualizowanie stanu wznawiania pracy. 
        } 
    } 

    // Zwraca pozycjê pierwszego dopasowania (lub N, jeœli nie wykryto dopasowania)
    public int search(String txt) {

        // Symulacja dzia³ania automatu DFA dla tekstu
        int M = pat.length();
        int N = txt.length();
        int i, j;
        for (i = 0, j = 0; i < N && j < M; i++) {
            j = dfa[txt.charAt(i)][j];
        }
        if (j == M) return i - M;    // Znaleziono
        return N;                    // Nie znaleziono
    }

    // Zwraca pozycjê pierwszego dopasowania (lub N, jeœli nie wykryto dopasowania)
    public int search(char[] text) {

        // Symulacja dzia³ania automatu DFA dla tekstu
        int M = pattern.length;
        int N = text.length;
        for (int i = 0, j = 0; i < N; i++) {
            j = dfa[text[i]][j];
            if (j == M) return i - M + 1;    // Znaleziono
        }
        return N;                            // Nie znaleziono
    }


    // Klient testowy
    public static void main(String[] args) {
        String pat = args[0];
        String txt = args[1];
        char[] pattern = pat.toCharArray();
        char[] text    = txt.toCharArray();

        KMP kmp1 = new KMP(pat);
        int offset1 = kmp1.search(txt);

        KMP kmp2 = new KMP(pattern, 256);
        int offset2 = kmp2.search(text);

        // Wyœwietlanie wyników
        StdOut.println("Tekst:   " + txt);

        StdOut.print("Wzorzec: ");
        for (int i = 0; i < offset1; i++)
            StdOut.print(" ");
        StdOut.println(pat);

        StdOut.print("Wzorzec: ");
        for (int i = 0; i < offset2; i++)
            StdOut.print(" ");
        StdOut.println(pat);
    }
}
