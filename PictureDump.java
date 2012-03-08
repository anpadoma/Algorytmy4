/*************************************************************************
 *  Kompilacja:  javac PictureDump.java
 *  Wykonanie:    java PictureDump width height < file
 *  Zale¿noœci: BinaryStdIn.java Picture.java
 *  Plik z danymi:    http://introcs.cs.princeton.edu/stdlib/abra.txt
 *  
 *  Wczytuje pliki binarne i zapisuje bity jako obraz w na h.
 *  Bity 1 to czarny, a bity 0 to bia³y.
 *
 *  % more abra.txt 
 *  ABRACADABRA!
 *
 *  % java PictureDump 16 6 < abra.txt
 *
 *************************************************************************/
import java.awt.Color;

public class PictureDump {

    public static void main(String[] args) {
        int width = Integer.parseInt(args[0]);
        int height = Integer.parseInt(args[1]);
        Picture pic = new Picture(width, height);
        int count = 0;
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                pic.set(j, i, Color.RED);
	        if (!BinaryStdIn.isEmpty()) {
                    count++;
                    boolean bit = BinaryStdIn.readBoolean();
		    if (bit) pic.set(j, i, Color.BLACK);
                    else     pic.set(j, i, Color.WHITE);
                }
            }
        }
        pic.show();
        StdOut.println("Liczba bitow: " + count);
    }
}
