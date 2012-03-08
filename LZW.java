/*************************************************************************
 *  Kompilacja:  javac LZW.java
 *  Wykonanie:    java LZW - < input.txt   (compress)
 *  Wykonanie:    java LZW + < input.txt   (expand)
 *  Zale¿noœci: BinaryIn.java BinaryOut.java
 *
 *  Kompresuje lub rozpakowywuje metod¹ LZW dane binarne ze standardowego wejœcia.
 *
 *
 *************************************************************************/

public class LZW {
    private static final int R = 256;        // Liczba wejœciowych znaków
    private static final int L = 4096;       // Liczba s³ów kodowych = 2^W
    private static final int W = 12;         // D³ugoœæ s³owa kodowego

    public static void compress() { 
        String input = BinaryStdIn.readString();
        TST<Integer> st = new TST<Integer>();
        for (int i = 0; i < R; i++)
            st.put("" + (char) i, i);
        int code = R+1;  // R to s³owo kodowe koñca pliku

        while (input.length() > 0) {
            String s = st.longestPrefixOf(input);  // Znajdowanie maksymalnego przedrostka pasuj¹cego do s.
            BinaryStdOut.write(st.get(s), W);      // Wyœwietlanie kodu dla s.
            int t = s.length();
            if (t < input.length() && code < L)    // Dodawanie s do tablicy symboli.
                st.put(input.substring(0, t + 1), code++);
            input = input.substring(t);            // Sprawdzanie poza s w danych wejœciowych.
        }
        BinaryStdOut.write(R, W);
        BinaryStdOut.close();
    } 


    public static void expand() {
        String[] st = new String[L];
        int i; // Nastêpna dostêpna wartoœæ s³owa kodowego

        // Inicjowanie tablicy symboli wszystkimi ³añcuchami jednoznakowymi
        for (i = 0; i < R; i++)
            st[i] = "" + (char) i;
        st[i++] = "";                        

        int codeword = BinaryStdIn.readInt(W);
        String val = st[codeword];

        while (true) {
            BinaryStdOut.write(val);
            codeword = BinaryStdIn.readInt(W);
            if (codeword == R) break;
            String s = st[codeword];
            if (i == codeword) s = val + val.charAt(0);   // Sztuczka na potrzeby przypadku specjalnego
            if (i < L) st[i++] = val + s.charAt(0);
            val = s;
        }
        BinaryStdOut.close();
    }



    public static void main(String[] args) {
        if      (args[0].equals("-")) compress();
        else if (args[0].equals("+")) expand();
        else throw new RuntimeException("Niedozwolony argument wiersza polecen");
    }

}
