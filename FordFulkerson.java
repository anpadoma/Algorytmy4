/*************************************************************************
 *  Kompilacja:  javac FordFulkerson.java
 *  Wykonanie:    java FordFulkerson V E
 *  Zale�no�ci: FlowNetwork.java FlowEdge.java Queue.java
 *
 *  Algorytm Forda-Fulkersona do wyznaczania przep�ywu maksymalnego i
 *  przekroju minimalnego za pomoc� regu�y najkr�tszej �cie�ki powi�kszaj�cej.
 *
 *********************************************************************/

public class FordFulkerson {
    private boolean[] marked;     // marked[v] = prawda, je�li �cie�ka s->v wyst�puje w grafie rezydualnym
    private FlowEdge[] edgeTo;    // edgeTo[v] = ostatnia kraw�d� na najkr�tszej �cie�ce rezydualnej s->v
    private double value;         // Bie��ca warto�� przep�ywu maksymalnego
  
    // Przep�yw maksymalny z s do t w sieci przep�ywowej G 
    public FordFulkerson(FlowNetwork G, int s, int t) {
        value = excess(G, t);
        if (!isFeasible(G, s, t)) {
            throw new RuntimeException("Pocz�tkowy przep�yw jest niemo�liwy");
        }

        // Dop�ki istnieje �cie�ka powi�kszaj�ca, nale�y jej u�ywa�
        while (hasAugmentingPath(G, s, t)) {

            // Obliczanie przepustowo�ci b�d�cej w�skim gard�em
            double bottle = Double.POSITIVE_INFINITY;
            for (int v = t; v != s; v = edgeTo[v].other(v)) {
                bottle = Math.min(bottle, edgeTo[v].residualCapacityTo(v));
            }

            // Powi�kszanie przep�ywu
            for (int v = t; v != s; v = edgeTo[v].other(v)) {
                edgeTo[v].addResidualFlowTo(v, bottle); 
            }

            value += bottle;
        }

        // Sprawdzanie warunk�w optymalno�ci
        assert check(G, s, t);
    }

    // Zwracanie warto�ci przep�ywu maksymalnego
    public double value()  {
        return value;
    }

    // Czy v znajduje si� po stronie s minimalnego przekroju s-t?
    public boolean inCut(int v)  {
        return marked[v];
    }


    // Zwraca �cie�k� powi�kszaj�c�, je�li istnieje (w przeciwnym razie zwraca null)
    private boolean hasAugmentingPath(FlowNetwork G, int s, int t) {
        edgeTo = new FlowEdge[G.V()];
        marked = new boolean[G.V()];

        // Przeszukiwanie wszerz
        Queue<Integer> q = new Queue<Integer>();
        q.enqueue(s);
        marked[s] = true;
        while (!q.isEmpty()) {
            int v = q.dequeue();

            for (FlowEdge e : G.adj(v)) {
                int w = e.other(v);
                
                if (e.residualCapacityTo(w) > 0) {
                    if (!marked[w]) {
                        edgeTo[w] = e;
                        marked[w] = true;
                        q.enqueue(w);
                    }
                }
            }
        }

        // Czy istnieje �cie�ka powi�kszaj�ca?
        return marked[t];
    }



    // Zwraca nadmiarowy przep�yw w wierzcho�ku v
    private double excess(FlowNetwork G, int v) {
        double excess = 0.0;
        for (FlowEdge e : G.adj(v)) {
            if (v == e.from()) excess -= e.flow();
            else               excess += e.flow();
        }
        return excess;
    }

    private boolean isFeasible(FlowNetwork G, int s, int t) {
        double EPSILON = 1E-11;

        // Sprawdza, czy ograniczenia przepustowo�ci s� spe�nione
        for (int v = 0; v < G.V(); v++) {
            for (FlowEdge e : G.adj(v)) {
                if (e.flow() < 0 || e.flow() > e.capacity()) {
                    System.err.println("Kraw�d� nie spe�nia ogranicze� przepustowo�ci: " + e);
                    return false;
                }
            }
        }

        // Sprawdza, czy przep�yw netto w wierzcho�ku jest r�wny zero (nie dotyczy to �r�d�a i uj�cia)
        if (Math.abs(value + excess(G, s)) > EPSILON) {
            System.err.println("Nadmiar w �r�dle    = " + excess(G, s));
            System.err.println("Przep�yw maksymalny = " + value);
            return false;
        }
        if (Math.abs(value - excess(G, t)) > EPSILON) {
            System.err.println("Nadmiar w uj�ciu    = " + excess(G, t));
            System.err.println("Przep�yw maksymalny = " + value);
            return false;
        }
        for (int v = 0; v < G.V(); v++) {
            if (v == s || v == t) continue;
            else if (Math.abs(excess(G, v)) > EPSILON) {
                System.err.println("Przep�yw netto w " + v + " nie jest r�wny zero");
                return false;
            }
        }
        return true;
    }



    // Sprawdzanie warunk�w optymalno�ci
    private boolean check(FlowNetwork G, int s, int t) {

        // Sprawdzanie, czy przep�yw jest mo�liwy
        if (!isFeasible(G, s, t)) {
            System.err.println("Przep�yw jest niemo�liwy");
            return false;
        }

        // Sprawdzanie, czy s znajduje si� po stronie �r�d�a w przekroju minimalnym,
        //	a t nie znajduje si� po tej stronie
        if (!inCut(s)) {
            System.err.println("�r�d�o " + s + " nie znajduje si� po stronie �r�d�a w przekroju minimalnym");
            return false;
        }
        if (inCut(t)) {
            System.err.println("uj�cie " + t + " znajduje si� po stroie �r�d�a w przekoju minimalnym");
            return false;
        }

        // Sprawdzanie, czy warto�� przekroju minimalnego = warto�� przekroju maksymalnego
        double mincutValue = 0.0;
        for (int v = 0; v < G.V(); v++) {
            for (FlowEdge e : G.adj(v)) {
                if ((v == e.from()) && inCut(e.from()) && !inCut(e.to()))
                    mincutValue += e.capacity();
            }
        }

        double EPSILON = 1E-11;
        if (Math.abs(mincutValue - value) > EPSILON) {
            System.err.println("Warto�� przekroju maksymalnego = " + value + ", warto�� przekroju minimalnego = " + mincutValue);
            return false;
        }

        return true;
    }


    // Klient testowy, kt�ry tworzy losow� sie�, rozwi�zuje problem
    //	przekroju maksymalnego i wy�wietla wyniki
    public static void main(String[] args) {

        // Tworzy sie� przep�ywow� o V wierzcho�kach i E kraw�dziach
        int V = Integer.parseInt(args[0]);
        int E = Integer.parseInt(args[1]);
        int s = 0, t = V-1;
        FlowNetwork G = new FlowNetwork(V, E);
        StdOut.println(G);

        // Oblicza przep�yw maksymalny i przekr�j minimalny
        FordFulkerson maxflow = new FordFulkerson(G, s, t);
        StdOut.println("Przep�yw maksymalny z " + s + " do " + t);
        for (int v = 0; v < G.V(); v++) {
            for (FlowEdge e : G.adj(v)) {
                if ((v == e.from()) && e.flow() > 0)
                    StdOut.println("   " + e);
            }
        }

        // Wy�wietlanie przekroju minimalnego
        StdOut.print("Przekr�j minimalny: ");
        for (int v = 0; v < G.V(); v++) {
            if (maxflow.inCut(v)) StdOut.print(v + " ");
        }
        StdOut.println();

        StdOut.println("Warto�� przep�ywu maksymalnego = " +  maxflow.value());
    }

}
