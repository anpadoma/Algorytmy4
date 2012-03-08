/***************************************************************
 *  Kompilacja:  javac RabinKarp.java
 *  Wykonanie:    java RabinKarp pat txt
 *
 *  Wczytuje dwa �a�cuchy znak�w, wzorzec i tekst wej�ciowy, a nast�pnie
 *  wyszukuje wzorzec w tek�cie za pomoc� algorytmu Rabina-Karpa. 
 *
 *  % java RabinKarp abracadabra abacadabrabracabracadabrabrabracad
 *  Wzorzec:     abracadabra
 *  Tekst:       abacadabrabracabracadabrabrabracad 
 *  Dopasowanie:               abracadabra          
 *
 *  % java RabinKarp rab abacadabrabracabracadabrabrabracad
 *  Wzorzec:     rab
 *  Tekst:       abacadabrabracabracadabrabrabracad 
 *  Dopasowanie:         rab                         
 *
 *  % java RabinKarp bcara abacadabrabracabracadabrabrabracad
 *  Wzorzec: bcara
 *  text:         abacadabrabracabracadabrabrabracad 
 *
 *  %  java RabinKarp rabrabracad abacadabrabracabracadabrabrabracad
 *  Tekst:   abacadabrabracabracadabrabrabracad
 *  Wzorzec:                        rabrabracad
 *
 *  % java RabinKarp abacad abacadabrabracabracadabrabrabracad
 *  Tekst:   abacadabrabracabracadabrabrabracad
 *  Wzorzec: abacad
 *
 ***************************************************************/

import java.util.Random;
import java.math.BigInteger;

public class RabinKarp {
    private String pat;      // Wzorzec
    private long patHash;    // Skr�t wzorca
    private int M;           // D�ugo�� wzorca
    private long Q;          // Du�a liczba pierwsza (na tyle ma�a, aby unikn�� przepe�nienia typu long)
    private int R;           // Podstawa
    private long RM;         // R^(M-1) % Q

    public RabinKarp(int R, char[] pattern) {
        throw new RuntimeException("Operacja nie jest jeszcze obslugiwana");
    }

    public RabinKarp(String pat) {
        R = 256;
        M = pat.length();
        Q = longRandomPrime();
        this.pat = pat;

        // Wst�pne obliczanie R^(M-1) % Q do u�ytku przy usuwaniu pierwszej cyfry
        RM = 1;
        for (int i = 1; i <= M-1; i++)
           RM = (R * RM) % Q;
        patHash = hash(pat, M);
    } 

    // Obliczanie skr�tu elementu key[0..M-1]. 
    private long hash(String key, int M) { 
        long h = 0; 
        for (int j = 0; j < M; j++) 
            h = (R * h + key.charAt(j)) % Q; 
        return h; 
    } 

    // Czy wzorzec dok�adnie pasuje do tekstu z danej pozycji?
    private boolean textMatch(String txt, int offset) {
        for (int i = 0; i < M; i++) 
            if (pat.charAt(i) != txt.charAt(offset + i)) 
                return false; 
        return true;
    }

    // Sprawdzanie dok�adnego dopasowania
    public int search(String txt) {
        int N = txt.length(); 
        if (N < M) return N;
        long txtHash = hash(txt, M); 

        // Sprawdzanie, czy dopasowano tekst na pozycji 0
        if ((patHash == txtHash) && textMatch(txt, 0))
            return 0;

        // Sprawdzanie dopasowania skr�t�w; je�li skr�ty pasuj�, nale�y sprawdzi� dok�adne dopasowanie
        for (int i = M; i < N; i++) {
            // Usuwanie pierwszej cyfry, dodawanie ko�cowej cyfry i sprawdzanie dopasowania
            txtHash = (txtHash + Q - RM*txt.charAt(i-M) % Q) % Q; 
            txtHash = (txtHash*R + txt.charAt(i)) % Q; 

            // Dopasowanie
            int offset = i - M + 1;
            if ((patHash == txtHash) && textMatch(txt, offset))
                return offset;
        }

        // Brak dopasowania
        return N;
    }


    // Losowa 31-bitowa liczba pierwsza
    private static long longRandomPrime() {
        BigInteger prime = new BigInteger(31, new Random());
        return prime.longValue();
    }

    // Klient testowy
    public static void main(String[] args) {
        String pat = args[0];
        String txt = args[1];
        char[] pattern = pat.toCharArray();
        char[] text    = txt.toCharArray();

        RabinKarp searcher = new RabinKarp(pat);
        int offset = searcher.search(txt);

        // Wy�wietlanie wynik�w
        StdOut.println("Tekst:    " + txt);
        
        StdOut.print("Wzorzec:  ");
        for (int i = 0; i < offset; i++)
            StdOut.print(" ");
        StdOut.println(pat);
    }
}
