/*************************************************************************
 *  Kompilacja:  javac Cat.java
 *  Wykonanie:    java Cat file1.txt file2.txt file3.txt output.txt
 *  Zale¿noœci: In.java
 *
 *  Wczytuje pliki tekstowe podane jako pierwsze argumenty wiersza poleceñ,
 *  ³¹czy je i zapisuje wynikw w pliku podanym jako
 *  ostatni argument.
 *
 *  % more in1.txt
 *  This is
 *
 *  % more in2.txt 
 *  a tiny
 *  test.
 * 
 *  % java Cat in1.txt in2.txt out.txt
 *
 *  % more out.txt .
 *  This is
 *  a tiny
 *  test.
 *
 *************************************************************************/

public class Cat { 

    public static void main(String[] args) { 
        Out out = new Out(args[args.length - 1]);
        for (int i = 0; i < args.length - 1; i++) {
            In in = new In(args[i]);
            String s = in.readAll();
            out.println(s);
            in.close();
        }
        out.close();
    }
	
}
