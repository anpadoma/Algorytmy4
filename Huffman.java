/*************************************************************************
 *  Kompilacja:  javac Huffman.java
 *  Wykonanie:    java Huffman - < input.txt   (kompresja)
 *  Wykonanie:    java Huffman + < input.txt   (rozpakowywanie)
 *  Zale¿noœci: BinaryIn.java BinaryOut.java
 *  Pliki z danymi:   http://algs4.cs.princeton.edu/55compression/abra.txt
 *                http://algs4.cs.princeton.edu/55compression/tinytinyTale.txt
 *
 *  Kompresuje i rozpakowuje binarne strumienie wejœciowe za pomoc¹ algorytmu Huffmana.
 *
 *  % java Huffman - < abra.txt | java BinaryDump 60
 *  010100000100101000100010010000110100001101010100101010000100
 *  000000000000000000000000000110001111100101101000111110010100
 *  Liczba bitow: 120
 *
 *  % java Huffman - < abra.txt | java Huffman +
 *  ABRACADABRA!
 *
 *************************************************************************/

public class Huffman {

    // Rozmiar alfabetu dla rozszerzonego zbioru ASCII
    private static final int R = 256;

    // Wêze³ drzewa trie dla algorytmu Huffman
    private static class Node implements Comparable<Node> {
        private final char ch;
        private final int freq;
        private final Node left, right;

        Node(char ch, int freq, Node left, Node right) {
            this.ch    = ch;
            this.freq  = freq;
            this.left  = left;
            this.right = right;
        }

        // Czy wêze³ jest liœciem?
        private boolean isLeaf() {
            assert (left == null && right == null) || (left != null && right != null);
            return (left == null && right == null);
        }

        // Porównanie wed³ug liczby wyst¹pieñ
        public int compareTo(Node that) {
            return this.freq - that.freq;
        }
    }


    // Kompresja bajtów ze standardowego wejœcia i zapisywanie danych do standardowego wyjœcia
    public static void compress() {
        // Wczytywanie danych wejœciowych
        String s = BinaryStdIn.readString();
        char[] input = s.toCharArray();

        int[] freq = new int[R];
        for (int i = 0; i < input.length; i++)
            freq[input[i]]++;

        // Budowanie drzewa trie Huffmana
        Node root = buildTrie(freq);

        // Tworzenie tablicy kodów
        String[] st = new String[R];
        buildCode(st, root, "");

        // Wyœwietlanie drzewa do odkodowywania
        writeTrie(root);

        // Wyœwietlanie liczby bajtów w pierwotnej nieskompresowanej wiadomoœci
        BinaryStdOut.write(input.length);

        // Korzystanie z kodu Huffmana do zakodowania danych wejœciowych
        for (int i = 0; i < input.length; i++) {
            String code = st[input[i]];
            for (int j = 0; j < code.length(); j++) {
                if (code.charAt(j) == '0') {
                    BinaryStdOut.write(false);
                }
                else if (code.charAt(j) == '1') {
                    BinaryStdOut.write(true);
                }
                else throw new RuntimeException("Niedozwolony stan");
            }
        }

        // Opró¿nianie strumienia wyjœciowego 
        BinaryStdOut.flush();
    }

    // Tworzenie drzewa trie dla kodowania Huffmana na podstawie liczby wyst¹pieñ
    private static Node buildTrie(int[] freq) {

        // Inicjowanie kolejki priorytetowej drzewami o jednym wêŸle
        MinPQ<Node> pq = new MinPQ<Node>();
        for (char i = 0; i < R; i++)
            if (freq[i] > 0)
                pq.insert(new Node(i, freq[i], null, null));

        // Scalanie dwóch najmniejszych drzew
        while (pq.size() > 1) {
            Node left  = pq.delMin();
            Node right = pq.delMin();
            Node parent = new Node('\0', left.freq + right.freq, left, right);
            pq.insert(parent);
        }
        return pq.delMin();
    }


    // Zapis zakodowanego drzewa trie do standardowego wyjœcia
    private static void writeTrie(Node x) {
        if (x.isLeaf()) {
            BinaryStdOut.write(true);
            BinaryStdOut.write(x.ch);
            return;
        }
        BinaryStdOut.write(false);
        writeTrie(x.left);
        writeTrie(x.right);
    }

    // Tworzenie tablicy wyszukiwania na podstawie symboli i ich kodów
    private static void buildCode(String[] st, Node x, String s) {
        if (!x.isLeaf()) {
            buildCode(st, x.left,  s + '0');
            buildCode(st, x.right, s + '1');
        }
        else {
            st[x.ch] = s;
        }
    }


    // Rozpakowywanie zakodowanych metod¹ Huffmana danych ze standardowego wejœcia i zapisywanie ich do standardowego wyjœcia
    public static void expand() {

        // Wczytywanie z wejœciowego strumienia drzewa drzewa trie dla algorytmu Huffmana 
        Node root = readTrie(); 

        // Liczba bajtów do zapisania
        int length = BinaryStdIn.readInt();

        // Odkodowywanie za pomoc¹ drzewa trie dla kodowania Huffmana
        for (int i = 0; i < length; i++) {
            Node x = root;
            while (!x.isLeaf()) {
                boolean bit = BinaryStdIn.readBoolean();
                if (bit) x = x.right;
                else     x = x.left;
            }
            BinaryStdOut.write(x.ch);
        }
        BinaryStdOut.flush();
    }


    private static Node readTrie() {
        boolean isLeaf = BinaryStdIn.readBoolean();
        if (isLeaf) {
            return new Node(BinaryStdIn.readChar(), -1, null, null);
        }
        else {
            return new Node('\0', -1, readTrie(), readTrie());
        }
    }


    public static void main(String[] args) {
        if      (args[0].equals("-")) compress();
        else if (args[0].equals("+")) expand();
        else throw new RuntimeException("Niedozwolony argument wiersza polecen");
    }

}
