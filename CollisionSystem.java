/*************************************************************************
 *  Kompilacja:  javac CollisionSystem.java
 *  Wykonanie:    java CollisionSystem N               (dla N losowych cz�steczek)
 *                java CollisionSystem < input.txt     (na podstawie pliku) 
 *  
 *  Tworzy N losowych cz�steczek i symuluje ich ruch zgodnie z prawami
 *  zderze� spr�ystych.
 *
 *************************************************************************/

import java.awt.Color;

public class CollisionSystem {
    private MinPQ<Event> pq;        // Kolejka priorytetowa
    private double t  = 0.0;        // Symulacja up�ywu czasu
    private double Hz = 0.5;        // Liczba zdarze� ponownego wy�wietlania na jednostk� czasu
    private Particle[] particles;   // Tablica cz�steczek

    // Tworzy nowy system zdarze� na podstawie danego zbioru cz�steczek
    public CollisionSystem(Particle[] particles) {
        this.particles = particles;
    }

    // Aktualizuje kolejk� priorytetow� za pomoc� wszystkich nowych zdarze� z udzia�em cz�steczki a
    private void predict(Particle a, double limit) {
        if (a == null) return;

        // Zderzenia cz�steczka-cz�steczka
        for(int i = 0; i < particles.length; i++) {
            double dt = a.timeToHit(particles[i]);
            if(t + dt <= limit)
                pq.insert(new Event(t + dt, a, particles[i]));
        }

        // Zderzenia cz�steczka-�ciana
        double dtX = a.timeToHitVerticalWall();
        double dtY = a.timeToHitHorizontalWall();
        if (t + dtX <= limit) pq.insert(new Event(t + dtX, a, null));
        if (t + dtY <= limit) pq.insert(new Event(t + dtY, null, a));
    }

    // Ponowne wy�wietlanie wszystkich cz�steczek
    private void redraw(double limit) {
        StdDraw.clear();
        for(int i = 0; i < particles.length; i++) {
            particles[i].draw();
        }
        StdDraw.show(20);
        if (t < limit) {
            pq.insert(new Event(t + 1.0 / Hz, null, null));
        }
    }

      
   /********************************************************************************
    *  Symulacja sterowana zdarzeniami dla ograniczonej liczby sekund
    ********************************************************************************/
    public void simulate(double limit) {
        
        // Inicjowanie kolejki priorytetowej za pomoc� zdarze� i zdarze� ponownego wy�wietlania
        pq = new MinPQ<Event>();
        for(int i = 0; i < particles.length; i++) {
            predict(particles[i], limit);
        }
        pq.insert(new Event(0, null, null));        // Zdarzenie ponownego wy�wietlania


        // G��wna p�tla symulacji sterowanej zdarzeniami
        while(!pq.isEmpty()) { 

            // Pobieranie oczekuj�cego zdarzenia i odrzucanie go, je�li zosta�o uniewa�nione
            Event e = pq.delMin();
            if(!e.isValid()) continue;
            Particle a = e.a;
            Particle b = e.b;

            // Fizyczne zderzenie, trzeba wi�c zaktualizowa� pozycje, a nast�pnie zegar
            for(int i = 0; i < particles.length; i++)
                particles[i].move(e.time - t);
            t = e.time;

            // Przetwarzanie zdarzenia
            if      (a != null && b != null) a.bounceOff(b);              // Zderzenie cz�steczka-cz�steczka
            else if (a != null && b == null) a.bounceOffVerticalWall();   // Zderzenie cz�steczka �ciana
            else if (a == null && b != null) b.bounceOffHorizontalWall(); // Zderzenie cz�steczka �ciana
            else if (a == null && b == null) redraw(limit);               // Ponowne wy�wietlanie

            // Aktualizowanie kolejki priorytetowej przy u�yciu nowych zderze� z udzia�em a lub b
            predict(a, limit);
            predict(b, limit);
        }
    }


   /*************************************************************************
    *  Zdarzenie w symulacji zderze� cz�steczek. Ka�de zdarzenie obejmuje czas jego
    *  zaj�cia (je�li do tego momentu nie wyst�pi� zak��caj�ce zdarzenia)
    *  i uwzgl�dniane cz�steczki a i b.
    *
    *    -  a i b to null:               ponowne wy�wietlanie
    *    -  a null, b r�ne od null:     zdarzenie z pionow� �cian�
    *    -  a r�ne od null, b null:     zdarzenie z poziom� �cian�
    *    -  a i b r�ne od null:         zdarzenie a z b
    *
    *************************************************************************/
    private class Event implements Comparable<Event> {
        private final double time;         // Zaplanowany czas zdarzenia
        private final Particle a, b;       // Cz�steczki uczestnicz�ce w zdarzeni (mo�na u�y� warto�ci null)
        private final int countA, countB;  // Liczba zderze� w momencie utworzenia zdarzenia
                
        
    // Tworzy nowe zdarzenie z udzia�em a i b, kt�re ma zaj�� w czasie t
    public Event(double t, Particle a, Particle b) {
        this.time = t;
        this.a    = a;
        this.b    = b;
        if (a != null) countA = a.count();
        else           countA = -1;
        if (b != null) countB = b.count();
        else           countB = -1;
    }

    // Por�wnuje czasy zaj�cia dw�ch zdarze�
    public int compareTo(Event that) {
        if      (this.time < that.time) return -1;
        else if (this.time > that.time) return +1;
        else                            return  0;
    }
        
    // Czy od momentu utworzenia zdarzenia do obecnej chwili zasz�y zdarzenia?
    public boolean isValid() {
        if (a != null && a.count() != countA) return false;
        if (b != null && b.count() != countB) return false;
        return true;
    }
   
}



   /********************************************************************************
    *  Przyk�adowy klient
    ********************************************************************************/
    public static void main(String[] args) {

        // Usuwanie ramki
        StdDraw.setXscale(1.0/22.0, 21.0/22.0);
        StdDraw.setYscale(1.0/22.0, 21.0/22.0);

        // W��czanie trybu animacji
        StdDraw.show(0);

        // Tablica cz�steczek
        Particle[] particles;

        // Tworzenie N losowych cz�steczek
        if (args.length == 1) {
            int N = Integer.parseInt(args[0]);
            particles = new Particle[N];
            for(int i = 0; i < N; i++) particles[i] = new Particle();
        }

        // lub wczytywanie danych ze standardowego wej�cia
        else {
            int N = StdIn.readInt();
            particles = new Particle[N];
            for(int i = 0; i < N; i++) {
                double rx     = StdIn.readDouble();
                double ry     = StdIn.readDouble();
                double vx     = StdIn.readDouble();
                double vy     = StdIn.readDouble();
                double radius = StdIn.readDouble();
                double mass   = StdIn.readDouble();
                int r         = StdIn.readInt();
                int g         = StdIn.readInt();
                int b         = StdIn.readInt();
                Color color   = new Color(r, g, b);
                particles[i] = new Particle(rx, ry, vx, vy, radius, mass, color);
            }
        }

        // Tworzenie systemu zdarze� i symulowanie jego dzia�ania
        CollisionSystem system = new CollisionSystem(particles);
        system.simulate(10000);
    }
      
}
