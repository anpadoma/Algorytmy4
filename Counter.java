/*************************************************************************
 *  Kompilacja:  javac Counter.java
 *  Wykonanie:    java Counter N T
 *  Zale¿noœci: StdRandom.java StdOut.java
 *
 *  Zmienny typ danych reprezentuj¹cy ca³kowitoliczbowy licznik.
 *
 *  Klienty testowe tworz¹ N liczników i wykonuj¹ T operacji
 *  inkrementacji na losowych licznikach.
 *
 *  % java Counter 6 600000
 *  0: 99870
 *  1: 99948
 *  2: 99738
 *  3: 100283
 *  4: 100185
 *  5: 99976
 *
 *************************************************************************/

public class Counter implements Comparable<Counter> {

    private final String name;     // Nazwa licznika
    private int count;             // Bie¿¹ca wartoœæ

    // Tworzenie nowego licznika
    public Counter(String id) {
        name = id;
    } 

    // Zwiêkszanie licznika o 1
    public void increment() {
        count++;
    } 

    // Zwracanie aktualnej wartoœci
    public int tally() {
        return count;
    } 

    // Zwracanie ³añcucha znaków reprezentuj¹cego dany licznik
    public String toString() {
        return count + " " + name;
    } 

    // Porównywanie dwóch obiektów Counter na podstawie ich wartoœci
    public int compareTo(Counter that) {
        if      (this.count < that.count) return -1;
        else if (this.count > that.count) return +1;
        else                              return  0;
    }


    // Klient testowy
    public static void main(String[] args) { 
        int N = Integer.parseInt(args[0]);
        int T = Integer.parseInt(args[1]);

        // Tworzenie N liczników
        Counter[] hits = new Counter[N];
        for (int i = 0; i < N; i++) {
            hits[i] = new Counter("licznik" + i);
        }

        // Zwiêkszanie T losowych liczników
        for (int t = 0; t < T; t++) {
            hits[StdRandom.uniform(N)].increment();
        }

        // Wyœwietlanie wyników
        for (int i = 0; i < N; i++) {
            StdOut.println(hits[i]);
        }
   } 
} 
