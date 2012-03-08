/*************************************************************************
 *  Kompilacja:  javac FrequencyCounter.java
 *  Wykonanie:    java FrequencyCounter L < input.txt
 *  Zale¿noœci: ST.java StdIn.java
 *
 *  Wczytuje listê s³ów ze standardowego wejœcia i wyœwietla 
 *  najczêœciej wystêpuj¹ce s³owo.
 *
 *  % java FrequencyCounter 1 < tinyTale.txt
 *  it 10
 *
 *  % java FrequencyCounter 8 < tale.txt
 *  business 122
 *
 *  % java FrequencyCounter 10 < leipzig1M.txt
 *  government 24763
 *
 *  Pliki z danymi
 *  ------------------------
 *  http://www.cs.princeton.edu/algs4/41elementary/tnyTale.txt
 *  http://www.cs.princeton.edu/algs4/41elementary/tale.txt
 *  http://www.cs.princeton.edu/algs4/41elementary/tale.txt
 *  http://www.cs.princeton.edu/introcs/data/leipzig/leipzig100k.txt
 *  http://www.cs.princeton.edu/introcs/data/leipzig/leipzig300k.txt
 *  http://www.cs.princeton.edu/introcs/data/leipzig/leipzig1m.txt
 *
 *************************************************************************/

public class FrequencyCounter {

    public static void main(String[] args) {
        int distinct = 0, words = 0;
        int minlen = Integer.parseInt(args[0]);
        ST<String, Integer> st = new ST<String, Integer>();

        // Okreœlanie liczby wyst¹pieñ
        while (!StdIn.isEmpty()) {
            String key = StdIn.readString();
            if (key.length() < minlen) continue;
            words++;
            if (st.contains(key)) { st.put(key, st.get(key) + 1); }
            else                  { st.put(key, 1); distinct++; }
        }

        // Znajdowanie klucza o najwiêkszej liczbie wyst¹pieñ
        String max = "";
        st.put(max, 0);
        for (String word : st.keys()) {
            if (st.get(word) > st.get(max))
                max = word;
        }

        System.out.println(max + " " + st.get(max));
        System.out.println("rozne slowa = " + distinct);
        System.out.println("slowa lacznie  = " + words);
    }
}
