/*************************************************************************
 *  Kompilacja:  javac TopM.java
 *  Wykonanie:    java TopM M < input.txt
 *  Zale¿noœci: MinPQ.java
 *  Plik z danymi:   http://algs4.cs.princeton.edu/24pq/tinyBatch.txt
 * 
 *  Na podstawie liczby ca³kowitej M z wiersza poleceñ i strumienia wejœciowego, w którym
 *  ka¿dy wiersz obejmuje ³añcuch znaków i wartoœæ typu long, niniejszy klient klasy MinPQ
 *  wyœwietla M wierszy z najwiêkszymi liczbami.
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

    // Wyœwietla M pierwszych wierszy ze strumienia wejœciowego. 
    public static void main(String[] args) {
        int M = Integer.parseInt(args[0]); 
        MinPQ<Transaction> pq = new MinPQ<Transaction>(M+1); 

        while (StdIn.hasNextLine()) {
            // Tworzy element na podstawie nastêpnego wiersza i umieszcza go w kolejce priorytetowej. 
            String line = StdIn.readLine();
            Transaction transaction = new Transaction(line);
            pq.insert(transaction); 

            // Usuwanie minimum, jeœli w kolejce priorytetowej znajduje siê M+1 elementów
            if (pq.size() > M) 
                pq.delMin();
        }   // M pierwszych elementów znajduje siê w kolejce priorytetowej

        // Wyœwietlanie elementów z kolejki priorytetowej w odwrotnej kolejnoœci
        Stack<Transaction> stack = new Stack<Transaction>();
        for (Transaction transaction : pq)
            stack.push(transaction);
        for (Transaction transaction : stack)
            StdOut.println(transaction);
    } 
} 

