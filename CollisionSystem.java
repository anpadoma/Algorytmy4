/*************************************************************************
 *  Kompilacja:  javac CollisionSystem.java
 *  Wykonanie:    java CollisionSystem N               (dla N losowych cz¹steczek)
 *                java CollisionSystem < input.txt     (na podstawie pliku) 
 *  
 *  Tworzy N losowych cz¹steczek i symuluje ich ruch zgodnie z prawami
 *  zderzeñ sprê¿ystych.
 *
 *************************************************************************/

import java.awt.Color;

public class CollisionSystem {
    private MinPQ<Event> pq;        // Kolejka priorytetowa
    private double t  = 0.0;        // Symulacja up³ywu czasu
    private double Hz = 0.5;        // Liczba zdarzeñ ponownego wyœwietlania na jednostkê czasu
    private Particle[] particles;   // Tablica cz¹steczek

    // Tworzy nowy system zdarzeñ na podstawie danego zbioru cz¹steczek
    public CollisionSystem(Particle[] particles) {
        this.particles = particles;
    }

    // Aktualizuje kolejk¹ priorytetow¹ za pomoc¹ wszystkich nowych zdarzeñ z udzia³em cz¹steczki a
    private void predict(Particle a, double limit) {
        if (a == null) return;

        // Zderzenia cz¹steczka-cz¹steczka
        for(int i = 0; i < particles.length; i++) {
            double dt = a.timeToHit(particles[i]);
            if(t + dt <= limit)
                pq.insert(new Event(t + dt, a, particles[i]));
        }

        // Zderzenia cz¹steczka-œciana
        double dtX = a.timeToHitVerticalWall();
        double dtY = a.timeToHitHorizontalWall();
        if (t + dtX <= limit) pq.insert(new Event(t + dtX, a, null));
        if (t + dtY <= limit) pq.insert(new Event(t + dtY, null, a));
    }

    // Ponowne wyœwietlanie wszystkich cz¹steczek
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
        
        // Inicjowanie kolejki priorytetowej za pomoc¹ zdarzeñ i zdarzeñ ponownego wyœwietlania
        pq = new MinPQ<Event>();
        for(int i = 0; i < particles.length; i++) {
            predict(particles[i], limit);
        }
        pq.insert(new Event(0, null, null));        // Zdarzenie ponownego wyœwietlania


        // G³ówna pêtla symulacji sterowanej zdarzeniami
        while(!pq.isEmpty()) { 

            // Pobieranie oczekuj¹cego zdarzenia i odrzucanie go, jeœli zosta³o uniewa¿nione
            Event e = pq.delMin();
            if(!e.isValid()) continue;
            Particle a = e.a;
            Particle b = e.b;

            // Fizyczne zderzenie, trzeba wiêc zaktualizowaæ pozycje, a nastêpnie zegar
            for(int i = 0; i < particles.length; i++)
                particles[i].move(e.time - t);
            t = e.time;

            // Przetwarzanie zdarzenia
            if      (a != null && b != null) a.bounceOff(b);              // Zderzenie cz¹steczka-cz¹steczka
            else if (a != null && b == null) a.bounceOffVerticalWall();   // Zderzenie cz¹steczka œciana
            else if (a == null && b != null) b.bounceOffHorizontalWall(); // Zderzenie cz¹steczka œciana
            else if (a == null && b == null) redraw(limit);               // Ponowne wyœwietlanie

            // Aktualizowanie kolejki priorytetowej przy u¿yciu nowych zderzeñ z udzia³em a lub b
            predict(a, limit);
            predict(b, limit);
        }
    }


   /*************************************************************************
    *  Zdarzenie w symulacji zderzeñ cz¹steczek. Ka¿de zdarzenie obejmuje czas jego
    *  zajœcia (jeœli do tego momentu nie wyst¹pi¹ zak³ócaj¹ce zdarzenia)
    *  i uwzglêdniane cz¹steczki a i b.
    *
    *    -  a i b to null:               ponowne wyœwietlanie
    *    -  a null, b ró¿ne od null:     zdarzenie z pionow¹ œcian¹
    *    -  a ró¿ne od null, b null:     zdarzenie z poziom¹ œcian¹
    *    -  a i b ró¿ne od null:         zdarzenie a z b
    *
    *************************************************************************/
    private class Event implements Comparable<Event> {
        private final double time;         // Zaplanowany czas zdarzenia
        private final Particle a, b;       // Cz¹steczki uczestnicz¹ce w zdarzeni (mo¿na u¿yæ wartoœci null)
        private final int countA, countB;  // Liczba zderzeñ w momencie utworzenia zdarzenia
                
        
    // Tworzy nowe zdarzenie z udzia³em a i b, które ma zajœæ w czasie t
    public Event(double t, Particle a, Particle b) {
        this.time = t;
        this.a    = a;
        this.b    = b;
        if (a != null) countA = a.count();
        else           countA = -1;
        if (b != null) countB = b.count();
        else           countB = -1;
    }

    // Porównuje czasy zajœcia dwóch zdarzeñ
    public int compareTo(Event that) {
        if      (this.time < that.time) return -1;
        else if (this.time > that.time) return +1;
        else                            return  0;
    }
        
    // Czy od momentu utworzenia zdarzenia do obecnej chwili zasz³y zdarzenia?
    public boolean isValid() {
        if (a != null && a.count() != countA) return false;
        if (b != null && b.count() != countB) return false;
        return true;
    }
   
}



   /********************************************************************************
    *  Przyk³adowy klient
    ********************************************************************************/
    public static void main(String[] args) {

        // Usuwanie ramki
        StdDraw.setXscale(1.0/22.0, 21.0/22.0);
        StdDraw.setYscale(1.0/22.0, 21.0/22.0);

        // W³¹czanie trybu animacji
        StdDraw.show(0);

        // Tablica cz¹steczek
        Particle[] particles;

        // Tworzenie N losowych cz¹steczek
        if (args.length == 1) {
            int N = Integer.parseInt(args[0]);
            particles = new Particle[N];
            for(int i = 0; i < N; i++) particles[i] = new Particle();
        }

        // lub wczytywanie danych ze standardowego wejœcia
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

        // Tworzenie systemu zdarzeñ i symulowanie jego dzia³ania
        CollisionSystem system = new CollisionSystem(particles);
        system.simulate(10000);
    }
      
}
