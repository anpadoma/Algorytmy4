/*************************************************************************
 *  Kompilacja:  javac TopM.java
 *  Wykonanie:    java TopM M < input.txt
 *  Zale�no�ci: MinPQ.java
 *  Plik z danymi:   http://algs4.cs.princeton.edu/24pq/tinyBatch.txt
 * 
 *  Na podstawie liczby ca�kowitej M z wiersza polece� i strumienia wej�ciowego, w kt�rym
 *  ka�dy wiersz obejmuje �a�cuch znak�w i warto�� typu long, niniejszy klient klasy MinPQ
 *  wy�wietla M wierszy z najwi�kszymi liczbami.
 * 
 *  % java TopM 5 < tinyBatch.txt 
 *  Thompson    2/27/2000  4747.08
 *  vonNeumann  2/12/1994  4732.35
 *  vonNeumann  1/11/1999  4409.74
 *  Hoare       8/18/1992  4381.21
 *  vonNeumann  3/26/2002  4121.85
 *
 *************************************************************************/

public class TopM {   

    // Wy�wietla M pierwszych wierszy ze strumienia wej�ciowego. 
    public static void main(String[] args) {
        int M = Integer.parseInt(args[0]); 
        MinPQ<Transaction> pq = new MinPQ<Transaction>(M+1); 

        while (StdIn.hasNextLine()) {
            // Tworzy element na podstawie nast�pnego wiersza i umieszcza go w kolejce priorytetowej. 
            String line = StdIn.readLine();
            Transaction transaction = new Transaction(line);
            pq.insert(transaction); 

            // Usuwanie minimum, je�li w kolejce priorytetowej znajduje si� M+1 element�w
            if (pq.size() > M) 
                pq.delMin();
        }   // M pierwszych element�w znajduje si� w kolejce priorytetowej

        // Wy�wietlanie element�w z kolejki priorytetowej w odwrotnej kolejno�ci
        Stack<Transaction> stack = new Stack<Transaction>();
        for (Transaction transaction : pq)
            stack.push(transaction);
        for (Transaction transaction : stack)
            StdOut.println(transaction);
    } 
} 

