/*************************************************************************
 *  Kompilacja:  javac RunLength.java
 *  Wykonanie:    java RunLength - < input.txt   (kompresowanie)
 *  Wykonanie:    java RunLength + < input.txt   (wypakowywanie)
 *  Zale¿noœci: BinaryIn.java BinaryOut.java
 *
 *  Kompresowanie lub rozpakowywanie danych binarnych ze standardowego wejœcia 
 *  za pomoc¹ kodowania d³ugoœci serii.
 *
 *  % java BinaryDump 40 < 4runs.bin 
 *  0000000000000001111111000000011111111111
 *  Liczba bitów: 40
 *
 *  Ten ci¹g obejmuje 15 cyfr 0, 7 cyfr 1, 7 cyfr 0 i 11 cyfr 1.
 *
 *  % java RunLength - < 4runs.bin | java HexDump
 *  0f 07 07 0b
 *  Liczba bajtów: 4
 *
 *************************************************************************/

public class RunLength {
    private static final int R   = 256;
    private static final int lgR = 8;

    public static void expand() { 
        boolean b = false; 
        while (!BinaryStdIn.isEmpty()) {
            int run = BinaryStdIn.readInt(lgR);
            for (int i = 0; i < run; i++)
                BinaryStdOut.write(b);
            b = !b;
        }
        BinaryStdOut.close();
    }

    public static void compress() { 
        char run = 0; 
        boolean old = false;
        while (!BinaryStdIn.isEmpty()) { 
            boolean b = BinaryStdIn.readBoolean();
            if (b != old) {
                BinaryStdOut.write(run, lgR);
                run = 1;
                old = !old;
            }
            else { 
                if (run == R-1) { 
                    BinaryStdOut.write(run, lgR);
                    run = 0;
                    BinaryStdOut.write(run, lgR);
                }
                run++;
            } 
        } 
        BinaryStdOut.write(run, lgR);
        BinaryStdOut.close();
    }


    public static void main(String[] args) {
        if      (args[0].equals("-")) compress();
        else if (args[0].equals("+")) expand();
        else throw new RuntimeException("Niedozwolony argument wiersza polecen");
    }

}
