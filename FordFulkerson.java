/*************************************************************************
 *  Kompilacja:  javac FordFulkerson.java
 *  Wykonanie:    java FordFulkerson V E
 *  Zale¿noœci: FlowNetwork.java FlowEdge.java Queue.java
 *
 *  Algorytm Forda-Fulkersona do wyznaczania przep³ywu maksymalnego i
 *  przekroju minimalnego za pomoc¹ regu³y najkrótszej œcie¿ki powiêkszaj¹cej.
 *
 *********************************************************************/

public class FordFulkerson {
    private boolean[] marked;     // marked[v] = prawda, jeœli œcie¿ka s->v wystêpuje w grafie rezydualnym
    private FlowEdge[] edgeTo;    // edgeTo[v] = ostatnia krawêdŸ na najkrótszej œcie¿ce rezydualnej s->v
    private double value;         // Bie¿¹ca wartoœæ przep³ywu maksymalnego
  
    // Przep³yw maksymalny z s do t w sieci przep³ywowej G 
    public FordFulkerson(FlowNetwork G, int s, int t) {
        value = excess(G, t);
        if (!isFeasible(G, s, t)) {
            throw new RuntimeException("Pocz¹tkowy przep³yw jest niemo¿liwy");
        }

        // Dopóki istnieje œcie¿ka powiêkszaj¹ca, nale¿y jej u¿ywaæ
        while (hasAugmentingPath(G, s, t)) {

            // Obliczanie przepustowoœci bêd¹cej w¹skim gard³em
            double bottle = Double.POSITIVE_INFINITY;
            for (int v = t; v != s; v = edgeTo[v].other(v)) {
                bottle = Math.min(bottle, edgeTo[v].residualCapacityTo(v));
            }

            // Powiêkszanie przep³ywu
            for (int v = t; v != s; v = edgeTo[v].other(v)) {
                edgeTo[v].addResidualFlowTo(v, bottle); 
            }

            value += bottle;
        }

        // Sprawdzanie warunków optymalnoœci
        assert check(G, s, t);
    }

    // Zwracanie wartoœci przep³ywu maksymalnego
    public double value()  {
        return value;
    }

    // Czy v znajduje siê po stronie s minimalnego przekroju s-t?
    public boolean inCut(int v)  {
        return marked[v];
    }


    // Zwraca œcie¿kê powiêkszaj¹c¹, jeœli istnieje (w przeciwnym razie zwraca null)
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

        // Czy istnieje œcie¿ka powiêkszaj¹ca?
        return marked[t];
    }



    // Zwraca nadmiarowy przep³yw w wierzcho³ku v
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

        // Sprawdza, czy ograniczenia przepustowoœci s¹ spe³nione
        for (int v = 0; v < G.V(); v++) {
            for (FlowEdge e : G.adj(v)) {
                if (e.flow() < 0 || e.flow() > e.capacity()) {
                    System.err.println("KrawêdŸ nie spe³nia ograniczeñ przepustowoœci: " + e);
                    return false;
                }
            }
        }

        // Sprawdza, czy przep³yw netto w wierzcho³ku jest równy zero (nie dotyczy to Ÿród³a i ujœcia)
        if (Math.abs(value + excess(G, s)) > EPSILON) {
            System.err.println("Nadmiar w Ÿródle    = " + excess(G, s));
            System.err.println("Przep³yw maksymalny = " + value);
            return false;
        }
        if (Math.abs(value - excess(G, t)) > EPSILON) {
            System.err.println("Nadmiar w ujœciu    = " + excess(G, t));
            System.err.println("Przep³yw maksymalny = " + value);
            return false;
        }
        for (int v = 0; v < G.V(); v++) {
            if (v == s || v == t) continue;
            else if (Math.abs(excess(G, v)) > EPSILON) {
                System.err.println("Przep³yw netto w " + v + " nie jest równy zero");
                return false;
            }
        }
        return true;
    }



    // Sprawdzanie warunków optymalnoœci
    private boolean check(FlowNetwork G, int s, int t) {

        // Sprawdzanie, czy przep³yw jest mo¿liwy
        if (!isFeasible(G, s, t)) {
            System.err.println("Przep³yw jest niemo¿liwy");
            return false;
        }

        // Sprawdzanie, czy s znajduje siê po stronie Ÿród³a w przekroju minimalnym,
        //	a t nie znajduje siê po tej stronie
        if (!inCut(s)) {
            System.err.println("Ÿród³o " + s + " nie znajduje siê po stronie Ÿród³a w przekroju minimalnym");
            return false;
        }
        if (inCut(t)) {
            System.err.println("ujœcie " + t + " znajduje siê po stroie Ÿród³a w przekoju minimalnym");
            return false;
        }

        // Sprawdzanie, czy wartoœæ przekroju minimalnego = wartoœæ przekroju maksymalnego
        double mincutValue = 0.0;
        for (int v = 0; v < G.V(); v++) {
            for (FlowEdge e : G.adj(v)) {
                if ((v == e.from()) && inCut(e.from()) && !inCut(e.to()))
                    mincutValue += e.capacity();
            }
        }

        double EPSILON = 1E-11;
        if (Math.abs(mincutValue - value) > EPSILON) {
            System.err.println("Wartoœæ przekroju maksymalnego = " + value + ", wartoœæ przekroju minimalnego = " + mincutValue);
            return false;
        }

        return true;
    }


    // Klient testowy, który tworzy losow¹ sieæ, rozwi¹zuje problem
    //	przekroju maksymalnego i wyœwietla wyniki
    public static void main(String[] args) {

        // Tworzy sieæ przep³ywow¹ o V wierzcho³kach i E krawêdziach
        int V = Integer.parseInt(args[0]);
        int E = Integer.parseInt(args[1]);
        int s = 0, t = V-1;
        FlowNetwork G = new FlowNetwork(V, E);
        StdOut.println(G);

        // Oblicza przep³yw maksymalny i przekrój minimalny
        FordFulkerson maxflow = new FordFulkerson(G, s, t);
        StdOut.println("Przep³yw maksymalny z " + s + " do " + t);
        for (int v = 0; v < G.V(); v++) {
            for (FlowEdge e : G.adj(v)) {
                if ((v == e.from()) && e.flow() > 0)
                    StdOut.println("   " + e);
            }
        }

        // Wyœwietlanie przekroju minimalnego
        StdOut.print("Przekrój minimalny: ");
        for (int v = 0; v < G.V(); v++) {
            if (maxflow.inCut(v)) StdOut.print(v + " ");
        }
        StdOut.println();

        StdOut.println("Wartoœæ przep³ywu maksymalnego = " +  maxflow.value());
    }

}
