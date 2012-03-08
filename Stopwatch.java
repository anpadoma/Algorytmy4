/*************************************************************************
 *  Kompilacja:  javac Stopwatch.java
 *
 *
 *************************************************************************/

/**
 *  <i>Stopwatch</i>. Ta klasa to typ danych do pomiaru czasu
 *  wykonania programu.
 *  <p>
 *  Dodatkow¹ dokumentacjê znajdziesz w 
 *  <a href="http://introcs.cs.princeton.edu/32class">podrozdziale 3.2</a> ksi¹¿ki
 *  <i>Introduction to Programming in Java: An Interdisciplinary Approach</i>
 *  Roberta Sedgewicka i Kevina Wayne'a.
 */



public class Stopwatch { 

    private final long start;

   /**
     * Tworzenie obiektu Stopwatch.
     */
    public Stopwatch() {
        start = System.currentTimeMillis();
    } 


   /**
     * Zwracanie czasu (w sekundach) od utworzenia obiektu.
     */
    public double elapsedTime() {
        long now = System.currentTimeMillis();
        return (now - start) / 1000.0;
    }

} 
