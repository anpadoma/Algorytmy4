/*************************************************************************
 *  Kompilacja:  javac Counter.java
 *  Wykonanie:    java Counter N T
 *  Zale�no�ci: StdRandom.java StdOut.java
 *
 *  Zmienny typ danych reprezentuj�cy ca�kowitoliczbowy licznik.
 *
 *  Klienty testowe tworz� N licznik�w i wykonuj� T operacji
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
    private int count;             // Bie��ca warto��

    // Tworzenie nowego licznika
    public Counter(String id) {
        name = id;
    } 

    // Zwi�kszanie licznika o 1
    public void increment() {
        count++;
    } 

    // Zwracanie aktualnej warto�ci
    public int tally() {
        return count;
    } 

    // Zwracanie �a�cucha znak�w reprezentuj�cego dany licznik
    public String toString() {
        return count + " " + name;
    } 

    // Por�wnywanie dw�ch obiekt�w Counter na podstawie ich warto�ci
    public int compareTo(Counter that) {
        if      (this.count < that.count) return -1;
        else if (this.count > that.count) return +1;
        else                              return  0;
    }


    // Klient testowy
    public static void main(String[] args) { 
        int N = Integer.parseInt(args[0]);
        int T = Integer.parseInt(args[1]);

        // Tworzenie N licznik�w
        Counter[] hits = new Counter[N];
        for (int i = 0; i < N; i++) {
            hits[i] = new Counter("licznik" + i);
        }

        // Zwi�kszanie T losowych licznik�w
        for (int t = 0; t < T; t++) {
            hits[StdRandom.uniform(N)].increment();
        }

        // Wy�wietlanie wynik�w
        for (int i = 0; i < N; i++) {
            StdOut.println(hits[i]);
        }
   } 
} 
