/*************************************************************************
 *  Kompilacja:  javac Topoological.java
 *  Zale¿noœci: Digraph.java DepthFirstOrder.java DirectedCycle.java
 *                EdgeWeightedDigraph.java EdgeWeightedDirectedCycle.java
 *
 *  Wyznacza porz¹dek topologiczny grafów DAG.
 *  Z³o¿onoœæ O(E + V).
 *
 *
 *************************************************************************/

public class Topological {
    private Iterable<Integer> order;    // Porz¹dek topologiczny.

    // Sortowanie topologiczne digrafu.
    public Topological(Digraph G) {
        DirectedCycle finder = new DirectedCycle(G);
        if (!finder.hasCycle()) {
            DepthFirstOrder dfs = new DepthFirstOrder(G);
            order = dfs.reversePostorder();
        }
    }

    // Sortowanie topologiczne digrafu wa¿onego.
    public Topological(EdgeWeightedDigraph G) {
        EdgeWeightedDirectedCycle finder = new EdgeWeightedDirectedCycle(G);
        if (!finder.hasCycle()) {
            DepthFirstOrder dfs = new DepthFirstOrder(G);
            order = dfs.reversePostorder();
        }
    }

    // Zwraca porz¹dek topologiczny grafu DAG (lub null).
    public Iterable<Integer> order() {
        return order;
    }

    // Czy w digrafie mo¿na wyznaczyæ porz¹dek topologiczny?
    public boolean hasOrder() {
        return order != null;
    }



    public static void main(String[] args) {

        // Tworzy losowy graf DAG o V wierzcho³kach i E krawêdziach.
        int V = Integer.parseInt(args[0]);
        int E = Integer.parseInt(args[1]);
        Digraph G = new Digraph(V);
        int[] vertices = new int[V];
        for (int i = 0; i < V; i++) vertices[i] = i;
        StdRandom.shuffle(vertices);

        for (int i = 0; i < E; i++) {
            int v, w;
            do {
                v = StdRandom.uniform(V);
                w = StdRandom.uniform(V);
            } while (v >= w);
            G.addEdge(vertices[v], vertices[w]);
        }

        System.out.println("Graf acykliczny G");
        System.out.println("-----------------");
        System.out.println(G);

        System.out.println();
        System.out.println("Wierzcho³ki w porz¹dku topologicznym przy tworzeniu");
        System.out.println("---------------------------------------------------");
        for (int i = 0; i < V; i++) 
            System.out.print(vertices[i] + " ");
        System.out.println();
        System.out.println();

        // Wyznacza porz¹dek topologiczny.
        // Mo¿e nie pasowaæ do wczeœniejszego, poniewa¿ mo¿e istnieæ wiele porz¹dków topologicznych.
        System.out.println("Wierzcho³ki w porz¹dku topologicznym na podstawie algorytmu");
        System.out.println("-----------------------------------------------------------");
        Topological topological = new Topological(G);
        for (int v : topological.order()) {
            System.out.print(v + " ");
        }
        System.out.println();
    }

}
