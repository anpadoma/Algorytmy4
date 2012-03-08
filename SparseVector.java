/*************************************************************************
 *  Kompilacja:  javac SparseVector.java
 *  Wykonanie:    java SparseVector
 *  
 *  Wektor rzadki zaimplementowany za pomoc¹ tablicy symboli.
 *
 *  [Nie wiadomo, czy zmienna egzemplarza N jest potrzebna do czegoœ
 *   oprócz sprawdzania b³êdów.]
 *
 *************************************************************************/

public class SparseVector {
    private int N;                   // D³ugoœæ
    private ST<Integer, Double> st;  // Wektor reprezentowany przez pary indeks-wartoœæ

    // Inicjowanie wektora o d³ugoœci N sk³adaj¹cego siê z samych zer
    public SparseVector(int N) {
        this.N  = N;
        this.st = new ST<Integer, Double>();
    }

    // Zapisywanie st[i] = value
    public void put(int i, double value) {
        if (i < 0 || i >= N) throw new RuntimeException("Niedozwolony indeks");
        if (value == 0.0) st.delete(i);
        else              st.put(i, value);
    }

    // Zwracanie st[i]
    public double get(int i) {
        if (i < 0 || i >= N) throw new RuntimeException("Niedozwolony indeks");
        if (st.contains(i)) return st.get(i);
        else                return 0.0;
    }

    // Zwracanie liczby niezerowych elementów
    public int nnz() {
        return st.size();
    }

    // Zwracanie wielkoœci wektora
    public int size() {
        return N;
    }

    // Zwracanie iloczynu wektorowego danego wektora i wektora that
    public double dot(SparseVector that) {
        if (this.N != that.N) throw new RuntimeException("Rozne dlugosci wektorow");
        double sum = 0.0;

        // Przechodzenie po wektorze o najmniejszej liczbie niezerowych elementów
        if (this.st.size() <= that.st.size()) {
            for (int i : this.st.keys())
                if (that.st.contains(i)) sum += this.get(i) * that.get(i);
        }
        else  {
            for (int i : that.st.keys())
                if (this.st.contains(i)) sum += this.get(i) * that.get(i);
        }
        return sum;
    }


    // Zwracanie iloczynu wektorowego danego wektora i tablicy that
    public double dot(double[] that) {
        double sum = 0.0;
        for (int i : st.keys())
            sum += that[i] * this.get(i);
        return sum;
    }


    // Zwracanie normy euklidesowej
    public double norm() {
        SparseVector a = this;
        return Math.sqrt(a.dot(a));
    }

    // Zwracanie wartoœci alpha * this
    public SparseVector scale(double alpha) {
        SparseVector c = new SparseVector(N);
        for (int i : this.st.keys()) c.put(i, alpha * this.get(i));
        return c;
    }

    // Zwracanie wartoœci this + that
    public SparseVector plus(SparseVector that) {
        if (this.N != that.N) throw new RuntimeException("Rozne dlugosci wektorow");
        SparseVector c = new SparseVector(N);
        for (int i : this.st.keys()) c.put(i, this.get(i));                // c = this
        for (int i : that.st.keys()) c.put(i, that.get(i) + c.get(i));     // c = c + that
        return c;
    }

    // Zwracanie ³añcucha znaków reprezentuj¹cego wektor
    public String toString() {
        String s = "";
        for (int i : st.keys()) {
            s += "(" + i + ", " + st.get(i) + ") ";
        }
        return s;
    }


    // Klient testowy
    public static void main(String[] args) {
        SparseVector a = new SparseVector(10);
        SparseVector b = new SparseVector(10);
        a.put(3, 0.50);
        a.put(9, 0.75);
        a.put(6, 0.11);
        a.put(6, 0.00);
        b.put(3, 0.60);
        b.put(4, 0.90);
        System.out.println("a = " + a);
        System.out.println("b = " + b);
        System.out.println("a dot b = " + a.dot(b));
        System.out.println("a + b   = " + a.plus(b));
    }

}
