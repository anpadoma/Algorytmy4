/*************************************************************************
 *  Kompilacja:  javac LookupIndex.java
 *  Wykonanie:    java LookupIndex movies.txt "/"
 *  Zale¿noœci: ST.java Bag.java In.java StdIn.java StdOut.java
 *
 *************************************************************************/

public class LookupIndex { 

    public static void main(String[] args) {
        String filename  = args[0];
        String separator = args[1];
        In in = new In(filename);

        ST<String, Bag<String>> st = new ST<String, Bag<String>>();
        ST<String, Bag<String>> ts = new ST<String, Bag<String>>();

        while (in.hasNextLine()) {
            String line = in.readLine();
            String[] fields = line.split(separator);
            String key = fields[0];
            for (int i = 1; i < fields.length; i++) {
                String val = fields[i];
                if (!st.contains(key)) st.put(key, new Bag<String>());
                if (!ts.contains(val)) ts.put(val, new Bag<String>());
                st.get(key).add(val);
                ts.get(val).add(key);
            }
        }

        StdOut.println("Zakonczono indeksowanie");

        // Wczytywanie zapytañ ze standardowego wejœcia (po jednym na wiersz)
        while (!StdIn.isEmpty()) {
            String query = StdIn.readLine();
            if (st.contains(query)) 
                for (String vals : st.get(query))
                    StdOut.println(" " + vals);
            if (ts.contains(query)) 
                for (String keys : ts.get(query))
                    StdOut.println(" " + keys);
        }

    }

}
