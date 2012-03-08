/*************************************************************************
 *  Kompilacja:  javac LZW.java
 *  Wykonanie:    java LZW - < input.txt   (compress)
 *  Wykonanie:    java LZW + < input.txt   (expand)
 *  Zale�no�ci: BinaryIn.java BinaryOut.java
 *
 *  Kompresuje lub rozpakowywuje metod� LZW dane binarne ze standardowego wej�cia.
 *
 *
 *************************************************************************/

public class LZW {
    private static final int R = 256;        // Liczba wej�ciowych znak�w
    private static final int L = 4096;       // Liczba s��w kodowych = 2^W
    private static final int W = 12;         // D�ugo�� s�owa kodowego

    public static void compress() { 
        String input = BinaryStdIn.readString();
        TST<Integer> st = new TST<Integer>();
        for (int i = 0; i < R; i++)
            st.put("" + (char) i, i);
        int code = R+1;  // R to s�owo kodowe ko�ca pliku

        while (input.length() > 0) {
            String s = st.longestPrefixOf(input);  // Znajdowanie maksymalnego przedrostka pasuj�cego do s.
            BinaryStdOut.write(st.get(s), W);      // Wy�wietlanie kodu dla s.
            int t = s.length();
            if (t < input.length() && code < L)    // Dodawanie s do tablicy symboli.
                st.put(input.substring(0, t + 1), code++);
            input = input.substring(t);            // Sprawdzanie poza s w danych wej�ciowych.
        }
        BinaryStdOut.write(R, W);
        BinaryStdOut.close();
    } 


    public static void expand() {
        String[] st = new String[L];
        int i; // Nast�pna dost�pna warto�� s�owa kodowego

        // Inicjowanie tablicy symboli wszystkimi �a�cuchami jednoznakowymi
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
