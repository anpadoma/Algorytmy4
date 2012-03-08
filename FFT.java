/*************************************************************************
 *  Kompilacja:  javac FFT.java
 *  Wykonanie:    java FFT N
 *  Zale�no�ci: Complex.java
 *
 *  Obliczanie szybkiej transformaty Fouriera (FFT) i odwrotnej FFT.
 *  Prosta implementacja o z�o�ono�ci O(N log N). Celem jest maksymalizacja
 *  przejrzysto�ci kodu, a nie wydajno�ci.
 *
 *  Ograniczenia
 *  -----------
 *   -  N jest pot�ga dw�jki
 *
 *   -  Nie jest to najwydajniejszy algorytm ze wzgl�du na pami�� (poniewa� wykorzystano
 *      typ obiektowy do reprezentowania liczb zespolonych, kt�ry wymaga
 *      ponownego przydzia�u pami�ci na podtablic�, zamiast wykonywa� operacje
 *      w miejscu lub wielokrotnie wykorzystywa� jedn� tablic� tymczasow�)
 *  
 *************************************************************************/

public class FFT {

    // Obliczanie FFT dla x[] (przy za�o�eniu, �e d�ugo�� tablicy to pot�ga liczby 2)
    public static Complex[] fft(Complex[] x) {
        int N = x.length;

        // Przypadek podstawowy
        if (N == 1) return new Complex[] { x[0] };

        // Algorytm Cooleya-Tukeya przy podstawie 2
        if (N % 2 != 0) { throw new RuntimeException("N nie jest potega 2"); }

        // FFT dla wyraz�w parzystych
        Complex[] even = new Complex[N/2];
        for (int k = 0; k < N/2; k++) {
            even[k] = x[2*k];
        }
        Complex[] q = fft(even);

        // FFT dla wyraz�w nieparzystych
        Complex[] odd  = even;  // Ponowne wykorzystanie tablicy
        for (int k = 0; k < N/2; k++) {
            odd[k] = x[2*k + 1];
        }
        Complex[] r = fft(odd);

        // ��czenie
        Complex[] y = new Complex[N];
        for (int k = 0; k < N/2; k++) {
            double kth = -2 * k * Math.PI / N;
            Complex wk = new Complex(Math.cos(kth), Math.sin(kth));
            y[k]       = q[k].plus(wk.times(r[k]));
            y[k + N/2] = q[k].minus(wk.times(r[k]));
        }
        return y;
    }

    // Obliczanie odwrotnej FFT dla x[] (przy za�o�eniu, �e d�ugo�� tablicy to pot�ga 2)
    public static Complex[] ifft(Complex[] x) {
        int N = x.length;
        Complex[] y = new Complex[N];

        // Obliczanie liczby sprz�onej
        for (int i = 0; i < N; i++) {
            y[i] = x[i].conjugate();
        }

        // Obliczanie prostej FFT
        y = fft(y);

        // Ponowne obliczanie liczby sprz�onej
        for (int i = 0; i < N; i++) {
            y[i] = y[i].conjugate();
        }

        // Dzielenie przez N
        for (int i = 0; i < N; i++) {
            y[i] = y[i].times(1.0 / N);
        }

        return y;

    }

    // Obliczanie splotu ko�owego dla x i y
    public static Complex[] cconvolve(Complex[] x, Complex[] y) {

        // Prawdopodobnie nale�y dope�ni� x i y zerami, aby mia�y t� sam� d�ugo��
        // i by�y pot�gami liczby 2
        if (x.length != y.length) { throw new RuntimeException("Niezgodne wymiary"); }

        int N = x.length;

        // Obliczanie FFT dla ka�dego ci�gu
        Complex[] a = fft(x);
        Complex[] b = fft(y);

        // Mno�enie par punkt�w
        Complex[] c = new Complex[N];
        for (int i = 0; i < N; i++) {
            c[i] = a[i].times(b[i]);
        }

        // Obliczanie odwrotnej FFT
        return ifft(c);
    }


    // Obliczanie splotu liniowego dla x i y
    public static Complex[] convolve(Complex[] x, Complex[] y) {
        Complex ZERO = new Complex(0, 0);

        Complex[] a = new Complex[2*x.length];
        for (int i = 0;        i <   x.length; i++) a[i] = x[i];
        for (int i = x.length; i < 2*x.length; i++) a[i] = ZERO;

        Complex[] b = new Complex[2*y.length];
        for (int i = 0;        i <   y.length; i++) b[i] = y[i];
        for (int i = y.length; i < 2*y.length; i++) b[i] = ZERO;

        return cconvolve(a, b);
    }

    // Wy�wietlanie tablicy liczb zespolonych w standardowym wyj�ciu
    public static void show(Complex[] x, String title) {
        System.out.println(title);
        System.out.println("-------------------");
        for (int i = 0; i < x.length; i++) {
            System.out.println(x[i]);
        }
        System.out.println();
    }


   /*********************************************************************
    *  Klient testowy i przyk�adowe dzia�anie
    *
    *  % java FFT 4
    *  x
    *  -------------------
    *  -0.03480425839330703
    *  0.07910192950176387
    *  0.7233322451735928
    *  0.1659819820667019
    *
    *  y = fft(x)
    *  -------------------
    *  0.9336118983487516
    *  -0.7581365035668999 + 0.08688005256493803i
    *  0.44344407521182005
    *  -0.7581365035668999 - 0.08688005256493803i
    *
    *  z = ifft(y)
    *  -------------------
    *  -0.03480425839330703
    *  0.07910192950176387 + 2.6599344570851287E-18i
    *  0.7233322451735928
    *  0.1659819820667019 - 2.6599344570851287E-18i
    *
    *  c = cconvolve(x, x)
    *  -------------------
    *  0.5506798633981853
    *  0.23461407150576394 - 4.033186818023279E-18i
    *  -0.016542951108772352
    *  0.10288019294318276 + 4.033186818023279E-18i
    *
    *  d = convolve(x, x)
    *  -------------------
    *  0.001211336402308083 - 3.122502256758253E-17i
    *  -0.005506167987577068 - 5.058885073636224E-17i
    *  -0.044092969479563274 + 2.1934338938072244E-18i
    *  0.10288019294318276 - 3.6147323062478115E-17i
    *  0.5494685269958772 + 3.122502256758253E-17i
    *  0.240120239493341 + 4.655566391833896E-17i
    *  0.02755001837079092 - 2.1934338938072244E-18i
    *  4.01805098805014E-17i
    *
    *********************************************************************/

    public static void main(String[] args) { 
        int N = Integer.parseInt(args[0]);
        Complex[] x = new Complex[N];

        // Pierwotne dane
        for (int i = 0; i < N; i++) {
            x[i] = new Complex(i, 0);
            x[i] = new Complex(-2*Math.random() + 1, 0);
        }
        show(x, "x");

        // FFT dla pierwotnych danych
        Complex[] y = fft(x);
        show(y, "y = fft(x)");

        // Obliczanie odwrotnej FFT
        Complex[] z = ifft(y);
        show(z, "z = ifft(y)");

        // Splot ko�owy x z nim samym
        Complex[] c = cconvolve(x, x);
        show(c, "c = cconvolve(x, x)");

        // Splot liniowy x z nim samym
        Complex[] d = convolve(x, x);
        show(d, "d = convolve(x, x)");
    }

}
