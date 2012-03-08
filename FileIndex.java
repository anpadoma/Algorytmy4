/*************************************************************************
 *  Kompilacja:  javac FileIndex.java
 *  Wykonanie:    java FileIndex file1.txt file2.txt file3.txt
 *  Zale�no�ci: ST.java SET.java In.java StdIn.java
 *
 *  % java FileIndex *.txt
 *
 *  % java FileIndex *.java
 *
 *  % java FileIndex ex*.txt
 *  age
 *   ex3.txt
 *   ex4.txt 
 * best
 *   ex1.txt 
 * was
 *   ex1.txt
 *   ex2.txt
 *   ex3.txt
 *   ex4.txt 
 *
 *************************************************************************/

import java.io.File;

public class FileIndex { 

    public static void main(String[] args) {

        // key = s�owo, value = zbi�r plik�w obejmuj�cych to s�owo
        ST<String, SET<File>> st = new ST<String, SET<File>>();

        // Tworzenie odwr�conego indeksu opartego na wszystkich plikach
        StdOut.println("Indeksowanie plikow");
        for (String filename : args) {
            StdOut.println("  " + filename);
            File file = new File(filename);
            In in = new In(file);
            while (!in.isEmpty()) {
                String word = in.readString();
                if (!st.contains(word)) st.put(word, new SET<File>());
                SET<File> set = st.get(word);
                set.add(file);
            }
        }

        // Wczytywanie zapyta� ze standardowego wej�cia (po jednym na wiersz)
        while (!StdIn.isEmpty()) {
            String query = StdIn.readString();
            if (st.contains(query)) {
                SET<File> set = st.get(query);
                for (File file : set) {
                    StdOut.println("  " + file.getName());
                }
            }
        }

    }

}
