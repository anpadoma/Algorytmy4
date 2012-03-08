 /*************************************************************************
 *  Kompilacja:  javac Particle.java
 *      
 *  Poruszaj�ca si� w polu jednostkowym cz�steczka o pozycji, pr�dko�ci,
 *  promieniu i masie.
 *
 *************************************************************************/

import java.awt.Color;

public class Particle {
    private final static double INFINITY = Double.POSITIVE_INFINITY;

    private double rx, ry;    // Pozycja
    private double vx, vy;    // Pr�dko��
    private double radius;    // Promie�
    private double mass;      // Masa
    private Color color;      // Kolor
    private int count;        // Liczba dotyczasowych zderze�


    // Tworzenie nowej cz�steczki o danych parametrach
    public Particle(double rx, double ry, double vx, double vy, double radius, double mass, Color color) {
        this.vx = vx;
        this.vy = vy;
        this.rx = rx;
        this.ry = ry;
        this.radius = radius;
        this.mass   = mass;
        this.color  = color;
     }
         
    // Tworzenie losowej cz�steczki w polu jednostkowym (program nie sprawdza pokrywania si� granic)
    public Particle() {
        rx     = Math.random();
        ry     = Math.random();
        vx     = 0.01 * (Math.random() - 0.5);
        vy     = 0.01 * (Math.random() - 0.5);
        radius = 0.01;
        mass   = 0.5;
        color  = Color.BLACK;
     }

    // Aktualizowanie pozycji
    public void move(double dt) {
        rx += vx * dt;
        ry += vy * dt;
    }

    // Rysowanie cz�steczki
    public void draw() {
        StdDraw.setPenColor(color);
        StdDraw.filledCircle(rx, ry, radius);
    }

    // Zwracanie liczby zderze� z udzia�em cz�steczek
    public int count() { return count; }
        
  
    // Ile czasu zosta�o do zderzenia dan� cz�steczk� (a) a b?
    public double timeToHit(Particle b) {
        Particle a = this;
        if (a == b) return INFINITY;
        double dx  = b.rx - a.rx;
        double dy  = b.ry - a.ry;
        double dvx = b.vx - a.vx;
        double dvy = b.vy - a.vy;
        double dvdr = dx*dvx + dy*dvy;
        if(dvdr > 0) return INFINITY;
        double dvdv = dvx*dvx + dvy*dvy;
        double drdr = dx*dx + dy*dy;
        double sigma = a.radius + b.radius;
        double d = (dvdr*dvdr) - dvdv * (drdr - sigma*sigma);
        // if (drdr < sigma*sigma) System.out.println("Cz�steczki pokrywaj� si�");
        if (d < 0) return INFINITY;
        return -(dvdr + Math.sqrt(d)) / dvdv;
    }
	
	
    // Ile czasu zosta�o do zderzenia danej cz�steczki z pionow� �cian�?
    public double timeToHitVerticalWall() {
        if      (vx > 0) return (1.0 - rx - radius) / vx;
        else if (vx < 0) return (radius - rx) / vx;  
        else             return INFINITY;
    }
      
    // Ile czasu zosta�o do zderzenia danej cz�steczki z poziom� �cian�?
    public double timeToHitHorizontalWall() {
        if      (vy > 0) return (1.0 - ry - radius) / vy;
        else if (vy < 0) return (radius - ry) / vy;
        else             return INFINITY;
    }
	
        
    // Aktualizowanie pr�dko�ci po zderzeniu cz�steczek this i that
    public void bounceOff(Particle that) {
        double dx  = that.rx - this.rx;
        double dy  = that.ry - this.ry;
        double dvx = that.vx - this.vx;
        double dvy = that.vy - this.vy;
        double dvdr = dx*dvx + dy*dvy;             
        double dist = this.radius + that.radius;   // Odleg�o�� mi�dzy �rodkami cz�steczek w momencie zderzenia

        // Si�a F oraz si�y w kierunkach x i y
        double F = 2 * this.mass * that.mass * dvdr / ((this.mass + that.mass) * dist);
        double Fx = F * dx / dist;
        double Fy = F * dy / dist;

        // Aktualizowanie szybko�ci wed�ug normalnej si�y
        this.vx += Fx / this.mass;
        this.vy += Fy / this.mass;
        that.vx -= Fx / that.mass;
        that.vy -= Fy / that.mass;

        // Aktualizowanie liczby zderze�
        this.count++;
        that.count++;
    }
	
    // Aktualizowanie szybko�ci cz�steczki po zderzeniu z pionow� �cian�
    public void bounceOffVerticalWall() {
        vx = -vx;
        count++;
    }

    // Aktualizowanie szybko�ci cz�steczki po zderzeniu z poziom� �cian�
    public void bounceOffHorizontalWall() {
        vy = -vy;
        count++;
    }


    // Zwracanie energii kinetycznej powi�zanej z dan� cz�steczk�
    public double kineticEnergy() { return 0.5 * mass * (vx*vx + vy*vy); }

}
