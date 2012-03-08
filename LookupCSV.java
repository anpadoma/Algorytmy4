/*************************************************************************
 *  Kompilacja:  javac LookupCSV.java
 *  Wykonanie:    java LookupCSV file.csv keyField valField
 *  
 *  Wczytuje zbiór par klucz-wartoœæ z dwukolumnowego pliku CSV
 *  podanego w wierszu poleceñ; nastêpnie wczytuje klucz ze standardowego
 *  wejœcia i wyœwietla odpowiednie wartoœci.
 * 
 *  % java LookupCSV amino.csv 0 3     % java LookupCSV ip.csv 0 1 
 *  TTA                                www.google.com 
 *  Leucine                            216.239.41.99 
 *  ABC                               
 *  Not found                          % java LookupCSV ip.csv 1 0 
 *  TCT                                216.239.41.99 
 *  Serine                             www.google.com 
 *                                 
 *  % java LookupCSV amino.csv 3 0     % java LookupCSV DJIA.csv 0 1 
 *  Glycine                            29-Oct-29 
 *  GGG                                252.38 
 *                                     20-Oct-87 
 *                                     1738.74
 *
 *  Pliki z danymi:
 *  http://www.cs.princeton.edu/algs4/45applications/DJIA.csv
 *  http://www.cs.princeton.edu/algs4/45applications/UPC.csv
 *  http://www.cs.princeton.edu/algs4/45applications/amino.csv
 *  http://www.cs.princeton.edu/algs4/45applications/elements.csv
 *  http://www.cs.princeton.edu/algs4/45applications/ip.csv
 *  http://www.cs.princeton.edu/algs4/45applications/morse.csv
 *  http://www.cs.princeton.edu/introcs/data/airports.csv
 *
 *************************************************************************/

public class LookupCSV {
    public static void main(String[] args) {
        int keyField = Integer.parseInt(args[1]);
        int valField = Integer.parseInt(args[2]);

        // Tablica symboli
        ST<String, String> st = new ST<String, String>();

        // Wczytuje dane z pliku CSV
        In in = new In(args[0]);
        while (in.hasNextLine()) {
            String line = in.readLine();
            String[] tokens = line.split(",");
            String key = tokens[keyField];
            String val = tokens[valField];
            st.put(key, val);
        }

        while (!StdIn.isEmpty()) {
            String s = StdIn.readString();
            if (st.contains(s)) StdOut.println(st.get(s));
            else                StdOut.println("Nie znaleziono");
        }
    }
}
