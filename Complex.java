/*************************************************************************
 *  Kompilacja:  javac Complex.java
 *  Wykonanie:    java Complex
 *
 *  Typ danych dla liczb zespolonych.
 *
 *  Typ danych jest niezmienny - po utworzeniu i zainicjowaniu
 *  obiektu Complex nie mo¿na go zmodyfikowaæ. S³owo kluczowe "final"
 *  w deklaracji zmiennych re i im wymusza tê regu³ê. Próba zmiany
 *  pola .re lub .im po ich zainicjowaniu prowadzi do b³êdu
 *  czasu kompilacji.
 *
 *  % java Complex
 *  a            = 5.0 + 6.0i
 *  b            = -3.0 + 4.0i
 *  Re(a)        = 5.0
 *  Im(a)        = 6.0
 *  b + a        = 2.0 + 10.0i
 *  a - b        = 8.0 + 2.0i
 *  a * b        = -39.0 + 2.0i
 *  b * a        = -39.0 + 2.0i
 *  a / b        = 0.36 - 1.52i
 *  (a / b) * b  = 5.0 + 6.0i
 *  conj(a)      = 5.0 - 6.0i
 *  |a|          = 7.810249675906654
 *  tan(a)       = -6.685231390246571E-6 + 1.0000103108981198i
 *
 *************************************************************************/

public class Complex {
    private final double re;   // Liczba rzeczywista
    private final double im;   // Czêœæ urojona

    // Tworzenie nowego obiektu na podstawie czêœci rzeczywistej i urojonej
    public Complex(double real, double imag) {
        re = real;
        im = imag;
    }

    // Zwracanie ³añcucha znaków reprezentuj¹cego dany obiekt Complex
    public String toString() {
        if (im == 0) return re + "";
        if (re == 0) return im + "i";
        if (im <  0) return re + " - " + (-im) + "i";
        return re + " + " + im + "i";
    }

    // Zwracanie modu³u i argumentu
    public double abs()   { return Math.hypot(re, im); }  // Math.sqrt(re*re + im*im)
    public double phase() { return Math.atan2(im, re); }  // Miêdzy -pi a pi

    // Zwracanie nowego obiektu Complex, którego wartoœæ to (this + b)
    public Complex plus(Complex b) {
        Complex a = this;             // Dany obiekt
        double real = a.re + b.re;
        double imag = a.im + b.im;
        return new Complex(real, imag);
    }

    // Zwracanie nowego obiektu Complex o wartoœci (this - b)
    public Complex minus(Complex b) {
        Complex a = this;
        double real = a.re - b.re;
        double imag = a.im - b.im;
        return new Complex(real, imag);
    }

    // Zwracanie nowego obiektu Complex o wartoœci (this * b)
    public Complex times(Complex b) {
        Complex a = this;
        double real = a.re * b.re - a.im * b.im;
        double imag = a.re * b.im + a.im * b.re;
        return new Complex(real, imag);
    }

    // Mno¿enie.
    // Zwracanie nowego obiektu o wartoœci (this * alpha)
    public Complex times(double alpha) {
        return new Complex(alpha * re, alpha * im);
    }

    // Zwracanie nowego obiektu Complex, którego wartoœæ to sprzê¿enie danego
    public Complex conjugate() {  return new Complex(re, -im); }

    // Zwracanie nowego obiektu Complex, którego wartoœæ to odwrotnoœæ danego
    public Complex reciprocal() {
        double scale = re*re + im*im;
        return new Complex(re / scale, -im / scale);
    }

    // Zwracanie czêœci rzeczywistej lub urojonej
    public double re() { return re; }
    public double im() { return im; }

    // Zwracanie a / b
    public Complex divides(Complex b) {
        Complex a = this;
        return a.times(b.reciprocal());
    }

    // Zwracanie nowego obiektu Complex, którego wartoœæ to postaæ wyk³adnicza danego
    public Complex exp() {
        return new Complex(Math.exp(re) * Math.cos(im), Math.exp(re) * Math.sin(im));
    }

    // Zwracanie nowego obiektu Complex, którego wartoœæ to postaæ trygonometryczna danego (dla sinusa)
    public Complex sin() {
        return new Complex(Math.sin(re) * Math.cosh(im), Math.cos(re) * Math.sinh(im));
    }

    // Zwracanie nowego obiektu Complex, którego wartoœæ to postaæ trygonometryczna danego (dla cosinusa)
    public Complex cos() {
        return new Complex(Math.cos(re) * Math.cosh(im), -Math.sin(re) * Math.sinh(im));
    }

    // Zwracanie nowego obiektu Complex, którego wartoœæ to postaæ trygonometryczna danego (dla tangensa)
    public Complex tan() {
        return sin().divides(cos());
    }
    


    // Statyczna wersja dodawania
    public static Complex plus(Complex a, Complex b) {
        double real = a.re + b.re;
        double imag = a.im + b.im;
        Complex sum = new Complex(real, imag);
        return sum;
    }



    // Przyk³adowy klient testowy
    public static void main(String[] args) {
        Complex a = new Complex(5.0, 6.0);
        Complex b = new Complex(-3.0, 4.0);

        System.out.println("a            = " + a);
        System.out.println("b            = " + b);
        System.out.println("Re(a)        = " + a.re());
        System.out.println("Im(a)        = " + a.im());
        System.out.println("b + a        = " + b.plus(a));
        System.out.println("a - b        = " + a.minus(b));
        System.out.println("a * b        = " + a.times(b));
        System.out.println("b * a        = " + b.times(a));
        System.out.println("a / b        = " + a.divides(b));
        System.out.println("(a / b) * b  = " + a.divides(b).times(b));
        System.out.println("conj(a)      = " + a.conjugate());
        System.out.println("|a|          = " + a.abs());
        System.out.println("tan(a)       = " + a.tan());
    }

}
