/*************************************************************************
 *  Kompilacja:  javac Alphabet.java
 *
 *  Typ danych dla alfabetu znak�w.
 *
 *************************************************************************/



public class Alphabet {
    public final static Alphabet BINARY         = new Alphabet("01");
    public final static Alphabet OCTAL          = new Alphabet("01234567");
    public final static Alphabet DECIMAL        = new Alphabet("0123456789");
    public final static Alphabet HEXADECIMAL    = new Alphabet("0123456789ABCDEF");
    public final static Alphabet DNA            = new Alphabet("ACTG");
    public final static Alphabet LOWERCASE      = new Alphabet("abcdefghijklmnopqrstuvwxyz");
    public final static Alphabet UPPERCASE      = new Alphabet("ABCDEFGHIJKLMNOPQRSTUVWXYZ");
    public final static Alphabet PROTEIN        = new Alphabet("ACDEFGHIKLMNPQRSTVWY");
    public final static Alphabet BASE64         = new Alphabet("ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/");
    public final static Alphabet ASCII          = new Alphabet(128);
    public final static Alphabet EXTENDED_ASCII = new Alphabet(256);
    public final static Alphabet UNICODE16      = new Alphabet(65536);

    private char[] alphabet;     // Znaki alfabetu.
    private int[] inverse;       // Indeksy.
    private int R;               // Podstawa alfabetu.

    // Tworzy nowy obiekt Alphabet na podstawie znak�w �a�cucha.
    public Alphabet(String alpha) {

        // Sprawdza, czy alfabet nie obejmuje powtarzaj�cych si� znak�w.
        boolean[] unicode = new boolean[Character.MAX_VALUE];
        for (int i = 0; i < alpha.length(); i++) {
            char c = alpha.charAt(i);
            if (unicode[c]) throw new RuntimeException("Nieprawid�owy alfabet: znak = '" + c + "'");
            else unicode[c] = true;
        }


        alphabet = alpha.toCharArray();
        R = alpha.length();
        inverse = new int[Character.MAX_VALUE];
        for (int i = 0; i < inverse.length; i++)
            inverse[i] = -1;

        // Nie mo�na u�y� typu char, poniewa� R mo�e wynosi� nawet 65 536.
        for (int c = 0; c < R; c++)
            inverse[alphabet[c]] = c;
    }

    // Tworzy nowy obiekt Alphabet na podstawie znak�w Unicode od 0 do R-1.
    private Alphabet(int R) {
       alphabet = new char[R];
       inverse = new int[R];
       this.R = R;

       // Nie mo�na u�y� typu char, poniewa� R mo�e wynosi� nawet 65 536.
       for (int i = 0; i < R; i++)
           alphabet[i] = (char) i;
       for (int i = 0; i < R; i++)
           inverse[i] = i;
    }

    // Tworzy nowy obiekt Alphabet na podstawie znak�w Unicode od 0 do 255 (rozszerzony zbi�r ASCII).
    public Alphabet() {
       this(256);
    }

    // Czy znak c nale�y do alfabetu?
    public boolean contains(char c) {
        return inverse[c] != -1;
    }

    // Zwraca podstaw� R.
    public int R() {
        return R;
    }

    // Zwraca liczb� bit�w reprezentuj�cych indeks.
    public int lgR() {
        int lgR = 0;
        for (int t = R; t > 1; t/=2)
            lgR++;
        return lgR;
    }

    // Przekszta�ca c na indeks z przedzia�u od 0 do R-1.
    public int toIndex(char c) {
        if (c < 0 || c >= inverse.length || inverse[c] == -1) {
            throw new RuntimeException("Znak " + c + " nie nale�y do alfabetu");
        }
        return inverse[c];
    }

    // Przekszta�ca obiekt String s w danym alfabecie na liczb� ca�kowit� o podstawie R.
    public int[] toIndices(String s) {
        char[] source = s.toCharArray();
        int[] target  = new int[s.length()];
        for (int i = 0; i < source.length; i++)
            target[i] = toIndex(source[i]);
        return target;
    }

    // Przekszta�ca indeks z przedzia�u od 0 do R-1 na znak z danego alfabetu.
    public char toChar(int index) {
        if (index < 0 || index >= R) {
            throw new RuntimeException("Indeks poza granicami");
        }
        return alphabet[index];
    }

    // Przekszta�ca liczb� ca�kowit� o podstawie R na obiekt String w danym alfabecie.
    public String toChars(int[] indices) {
        StringBuilder s = new StringBuilder(indices.length);
        for (int i = 0; i < indices.length; i++)
            s.append(toChar(indices[i]));
        return s.toString();
    }


    public static void main(String[] args) {
        int[] encoded1  = Alphabet.BASE64.toIndices("NowIsTheTimeForAllGoodMen");
        String decoded1 = Alphabet.BASE64.toChars(encoded1);
        System.out.println(decoded1);
 
        int[] encoded2  = Alphabet.DNA.toIndices("AACGAACGGTTTACCCCG");
        String decoded2 = Alphabet.DNA.toChars(encoded2);
        System.out.println(decoded2);

        int[] encoded3 = Alphabet.DECIMAL.toIndices("01234567890123456789");
        String decoded3 = Alphabet.DECIMAL.toChars(encoded3);
        System.out.println(decoded3);
    }
}
