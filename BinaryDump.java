/*************************************************************************
 *  Kompilacja:  javac BinaryDump.java
 *  Wykonanie:    java BinaryDump N < file
 *  Zale¿noœci: BinaryStdIn.java
 *  Plik z danymi:    http://introcs.cs.princeton.edu/stdlib/abra.txt
 *  
 *  Wczytuje plik binarny i zapisuje bity po N na wiersz.
 *
 *  % more abra.txt 
 *  ABRACADABRA!
 *
 *  % java BinaryDump 16 < abra.txt
 *  0100000101000010
 *  0101001001000001
 *  0100001101000001
 *  0100010001000001
 *  0100001001010010
 *  0100000100100001
 *  Liczba bitow: 96
 *
 *************************************************************************/

public class BinaryDump {

    public static void main(String[] args) {
        int BITS_PER_LINE = 16;
        if (args.length == 1) {
            BITS_PER_LINE = Integer.parseInt(args[0]);
        }

        int count;
        for (count = 0; !BinaryStdIn.isEmpty(); count++) {
            if (BITS_PER_LINE == 0) { BinaryStdIn.readBoolean(); continue; }
            else if (count != 0 && count % BITS_PER_LINE == 0) StdOut.println();
            if (BinaryStdIn.readBoolean()) StdOut.print(1);
            else                           StdOut.print(0);
        }
        if (BITS_PER_LINE != 0) StdOut.println();
        StdOut.println("Liczba bitow: " + count);
    }
}
