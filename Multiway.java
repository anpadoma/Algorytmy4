/*************************************************************************
 *  Kompilacja:  javac Multiway.java
 *  Wykonanie:    java Multiway m1.txt m2.txt m3.txt
 *  Zale¿noœci: MinPQplus.java In.java
 * 
 *
 *************************************************************************/

public class Multiway { 

    public static void merge(In[] streams) { 
        int N = streams.length; 
        IndexMinPQ<String> pq = new IndexMinPQ<String>(N); 
        for (int i = 0; i < N; i++) 
            if (!streams[i].isEmpty()) 
                pq.insert(i, streams[i].readString()); 

        // Pobieranie i wyœwietlanie minimum oraz wczytywanie nastêpnej wartoœci ze strumienia. 
        while (!pq.isEmpty()) {
            StdOut.print(pq.minKey() + " "); 
            int i = pq.delMin(); 
            if (!streams[i].isEmpty()) 
                pq.insert(i, streams[i].readString()); 
        }
        StdOut.println();
    } 


    public static void main(String[] args) { 
        int N = args.length; 
        In[] streams = new In[N]; 
        for (int i = 0; i < N; i++) 
            streams[i] = new In(args[i]); 
        merge(streams); 
    } 
} 
