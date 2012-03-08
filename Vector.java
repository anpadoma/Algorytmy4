/*************************************************************************
 *  Kompilacja:  javac Vector.java
 *  Wykonanie:    java Vector
 *
 *  Implementacja wektora liczb rzeczywistych.
 *
 *  Klasa jest zaimplementowana jako niezmienna. Kiedy program kliencki
 *  zainicjuje obiekt Vector, nie mo¿e zmieniæ ¿adnego pola
 *  (N lub data[i]) ani bezpoœrednio, ani poœrednio. Niezmiennoœæ to bardzo
 *  po¿¹dana cecha typu danych.
 *
 *  % java Vector
 *     x     = [ 1.0 2.0 3.0 4.0 ]
 *     y     = [ 5.0 2.0 4.0 1.0 ]
 *     z     = [ 6.0 4.0 7.0 5.0 ]
 *   10z     = [ 60.0 40.0 70.0 50.0 ]
 *    |x|    = 5.477225575051661
 *   <x, y>  = 25.0
 * 
 *
 *  Warto zauwa¿yæ, ¿e Vector to tak¿e nazwa niepowi¹zanej biblioteki klas Javy.
 *
 *************************************************************************/

public class Vector { 

    private int N;               // D³ugoœæ wektora
    private double[] data;       // Tablica komponentów wektora


    // Tworzy wektor o d³ugoœci n
    public Vector(int n) {
        N = n;
        data = new double[N];
    }

    // Tworzy wektor albo na podstawie tablicy, albo na podstawie listy argumentów
    public Vector(double[] d) {
        N = d.length;

        // Kopiowanie zabezpieczaj¹ce, aby klient nie zmieni³ kopii tablicy data[]
        data = new double[N];
        for (int i = 0; i < N; i++)
            data[i] = d[i];
    }

    // Tworzenie wektora na podstawie tablicy lub listy argumentów
    // Konstruktor wykorzystuje sk³adniê argumentów Javy, co umo¿liwia
    // obs³ugê ró¿nej liczby argumentów, na przyk³ad:
    // Vector x = new Vector(1.0, 2.0, 3.0, 4.0);
    // Vector y = new Vector(5.0, 2.0, 4.0, 1.0);
/*
    public Vector(double... d) {
        N = d.length;

        // Kopiowanie zabezpieczaj¹ce, tak aby klient nie móg³ zmieniæ kopii tablicy data[]
        data = new double[N];
        for (int i = 0; i < N; i++)
            data[i] = d[i];
    }
*/
    // Zwracanie d³ugoœci wektora
    public int length() {
        return N;
    }

    // Zwracanie iloczynu skalarnego danego wektora i wektora that
    public double dot(Vector that) {
        if (this.N != that.N) throw new RuntimeException("Niezgodne wymiary");
        double sum = 0.0;
        for (int i = 0; i < N; i++)
            sum = sum + (this.data[i] * that.data[i]);
        return sum;
    }

    // Zwracanie normy euklidesowej dla danego wektora
    public double magnitude() {
        return Math.sqrt(this.dot(this));
    }

    // Zwracanie odleg³oœci euklidesowej miêdzy danym wektorem a wektorem that
    public double distanceTo(Vector that) {
        if (this.N != that.N) throw new RuntimeException("Niezgodne wymiary");
        return this.minus(that).magnitude();
    }

    // Zwracanie this + that
    public Vector plus(Vector that) {
        if (this.N != that.N) throw new RuntimeException("Niezgodne wymiary");
        Vector c = new Vector(N);
        for (int i = 0; i < N; i++)
            c.data[i] = this.data[i] + that.data[i];
        return c;
    }

    // Zwracanie this - that
    public Vector minus(Vector that) {
        if (this.N != that.N) throw new RuntimeException("Niezgodne wymiary");
        Vector c = new Vector(N);
        for (int i = 0; i < N; i++)
            c.data[i] = this.data[i] - that.data[i];
        return c;
    }

    // Zwracanie okreœlonej wspó³rzêdnej
    public double cartesian(int i) {
        return data[i];
    }

    // Tworzenie i zwracanie nowego obiektu, którego wartoœæ to (dany wektor * factor)
    public Vector times(double factor) {
        Vector c = new Vector(N);
        for (int i = 0; i < N; i++)
            c.data[i] = factor * data[i];
        return c;
    }


    // Zwracanie odpowiadaj¹cej jednostki wektorowej
    public Vector direction() {
        if (this.magnitude() == 0.0) throw new RuntimeException("Wektor zerowy nie ma kierunku");
        return this.times(1.0 / this.magnitude());
    }


    // Zwracanie ³añcucha znaków reprezentuj¹cego dany wektor
    public String toString() {
        String s = "";
        for (int i = 0; i < N; i++)
            s = s + data[i] + " ";
        return s;
    }




    // Klient testowy
    public static void main(String[] args) {
        double[] xdata = { 1.0, 2.0, 3.0, 4.0 };
        double[] ydata = { 5.0, 2.0, 4.0, 1.0 };
        Vector x = new Vector(xdata);
        Vector y = new Vector(ydata);

        System.out.println("   x       = " + x);
        System.out.println("   y       = " + y);

        Vector z = x.plus(y);
        System.out.println("   z       = " + z);

        z = z.times(10.0);
        System.out.println(" 10z       = " + z);

        System.out.println("  |x|      = " + x.magnitude());
        System.out.println(" <x, y>    = " + x.dot(y));
        System.out.println("dist(x, y) = " + x.distanceTo(y));
        System.out.println("dir(x)     = " + x.direction());

    }
}
