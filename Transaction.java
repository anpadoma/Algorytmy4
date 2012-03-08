/*************************************************************************
 *  Kompilacja:  javac Transaction.java
 *  Wykonanie:    java Transaction
 *  
 *  Typ danych dla transakcji komercyjnych.
 *
 *************************************************************************/

import java.util.Arrays;
import java.util.Comparator;


public class Transaction implements Comparable<Transaction> {
    private final String  who;      // Klient
    private final Date    when;     // Data
    private final double  amount;   // Warto��

    public Transaction(String who, Date when, double amount) {
        this.who    = who;
        this.when   = when;
        this.amount = amount;
    }

    // Tworzy now� transakcj� przez przetworzenie �a�cucha znak�w w postaci: nazwisko,
    // data, liczba rzeczywista (dane s� rozdzielone odst�pami)
    public Transaction(String transaction) {
        String[] a = transaction.split("\\s+");
        who    = a[0];
        when   = new Date(a[1]);
        amount = Double.parseDouble(a[2]);
    }

    // Akcesory
    public String  who()    { return who;      }
    public Date    when()   { return when;     }
    public double  amount() { return amount;   }

    public String toString() {
        return String.format("%-10s %10s %8.2f", who, when, amount);
    }

    public int compareTo(Transaction that) {
        if      (this.amount < that.amount) return -1;
        else if (this.amount > that.amount) return +1;
        else                                return  0;
    }    

    // Czy transakcja ma warto�� x?
    public boolean equals(Object x) {
        if (x == this) return true;
        if (x == null) return false;
        if (x.getClass() != this.getClass()) return false;
        Transaction that = (Transaction) x;
        return (this.amount == that.amount) && (this.who.equals(that.who))
                                            && (this.when.equals(that.when));
    }




    public int hashCode() {
        int hash = 17;
        hash = 31*hash + who.hashCode();
        hash = 31*hash + when.hashCode();
        hash = 31*hash + ((Double) amount).hashCode();
        return hash;
    }

    // Rosn�co wed�ug nazwiska
    public static class WhoOrder implements Comparator<Transaction> {
        public int compare(Transaction v, Transaction w) {
            return v.who.compareTo(w.who);
        }
    }

    // Rosn�co wed�ug czasu
    public static class WhenOrder implements Comparator<Transaction> {
        public int compare(Transaction v, Transaction w) {
            return v.when.compareTo(w.when);
        }
    }

    // Rosn�co wed�ug warto�ci
    public static class HowMuchOrder implements Comparator<Transaction> {
        public int compare(Transaction v, Transaction w) {
            if      (v.amount < w.amount) return -1;
            else if (v.amount > w.amount) return +1;
            else                          return  0;
        }
    }


    // Klient testowy
    public static void main(String[] args) {
        Transaction[] a = new Transaction[4];
        a[0] = new Transaction("Turing   6/17/1990  644.08");
        a[1] = new Transaction("Tarjan   3/26/2002  4121.85");
        a[2] = new Transaction("Knuth    6/14/1999  288.34");
        a[3] = new Transaction("Dijkstra 8/22/2007  2678.40");

        StdOut.println("Bez sortowania");
        for (int i = 0; i < a.length; i++)
            StdOut.println(a[i]);
        StdOut.println();
        
        StdOut.println("Posortowane wed�ug dat");
        Arrays.sort(a, new Transaction.WhenOrder());
        for (int i = 0; i < a.length; i++)
            StdOut.println(a[i]);
        StdOut.println();

        StdOut.println("Posortowane wed�ug nazwisk");
        Arrays.sort(a, new Transaction.WhoOrder());
        for (int i = 0; i < a.length; i++)
            StdOut.println(a[i]);
        StdOut.println();

        StdOut.println("Posortowane wed�ug warto�ci");
        Arrays.sort(a, new Transaction.HowMuchOrder());
        for (int i = 0; i < a.length; i++)
            StdOut.println(a[i]);
        StdOut.println();
    }

}

