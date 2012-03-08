/*************************************************************************
 *  Kompilacja:  javac Genome.java
 *  Wykonanie:    java Genome - < input.txt   (kompresja)
 *  Wykonanie:    java Genome + < input.txt   (rozpakowywanie)
 *  Zale¿noœci: BinaryIn.java BinaryOut.java
 *
 *  Kompresowanie i rozpakowywanie genomu za pomoc¹ kodu 2-bitowego.
 *
 *  % more genomeTiny.txt
 *  ATAGATGCATAGCGCATAGCTAGATGTGCTAGC
 *
 *  % java Genome - < genomeTiny.txt | java Genome +
 *  ATAGATGCATAGCGCATAGCTAGATGTGCTAGC
 *
 *************************************************************************/

public class Genome {

    public static void compress() { 
        Alphabet DNA = new Alphabet("ACTG");
        String s = BinaryStdIn.readString();
        int N = s.length();
        BinaryStdOut.write(N);

        // Zapisywanie 2-bitowego kodu znaku. 
        for (int i = 0; i < N; i++) {
            int d = DNA.toIndex(s.charAt(i));
            BinaryStdOut.write(d, 2);
        }
        BinaryStdOut.close();
    } 

    public static void expand() {
        Alphabet DNA = new Alphabet("ACTG");
        int N = BinaryStdIn.readInt();
        // Wczytywanie dwóch bitów i zapisywanie znaku. 
        for (int i = 0; i < N; i++) {
            char c = BinaryStdIn.readChar(2);
            BinaryStdOut.write(DNA.toChar(c));
        }
        BinaryStdOut.close();
    }


    public static void main(String[] args) {
        if      (args[0].equals("-")) compress();
        else if (args[0].equals("+")) expand();
        else throw new RuntimeException("Niedozwolony argument wiersza polecen");
    }

}
