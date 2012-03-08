/*************************************************************************
 *  Kompilacja:  javac DepthFirstOrder.java
 *  Wykonanie:    java DepthFirstOrder V E F
 *  Zale�no�ci: Digraph.java EdgeWeightedDigraph.java DirectedEdge.java
 *                Queue.java Stack.java StdOut.java
 *
 *  Wyznaczanie porz�dku preorder i postorder dla digrafu lub digrafu wa�onego.
 *  Dzia�a w czasie O(E + V).
 *
 *
 *************************************************************************/

public class DepthFirstOrder {
    private boolean[] marked;          // marked[v] = czy v oznaczono w algorytmie DFS?
    private int[] pre;                 // pre[v]    = numer v w porz�dku preorder
    private int[] post;                // post[v]   = numer v w porz�dku postorder
    private Queue<Integer> preorder;   // Wierzcho�ki w kolejno�ci preorder
    private Queue<Integer> postorder;  // Wierzcho�ki w kolejno�ci postorder
    private int preCounter;            // Licznik dla numerowania w porz�dku preorder
    private int postCounter;           // Licznik dla numerowania w porz�dku postorder

    // Wyznaczanie porz�dk�w preorder i postorder w digrafie przez przeszukiwanie w g��b 
    public DepthFirstOrder(Digraph G) {
        pre    = new int[G.V()];
        post   = new int[G.V()];
        postorder = new Queue<Integer>();
        preorder  = new Queue<Integer>();
        marked    = new boolean[G.V()];
        for (int v = 0; v < G.V(); v++)
            if (!marked[v]) dfs(G, v);
    }

    // Wyznaczanie porz�dk�w preorder i postorder w digrafie wa�onym przez przeszukiwanie w g��b
    public DepthFirstOrder(EdgeWeightedDigraph G) {
        pre    = new int[G.V()];
        post   = new int[G.V()];
        postorder = new Queue<Integer>();
        preorder  = new Queue<Integer>();
        marked    = new boolean[G.V()];
        for (int v = 0; v < G.V(); v++)
            if (!marked[v]) dfs(G, v);
    }

    // Uruchamianie algorytmu DFS w digrafie G w wierzcho�ku v oraz wyznaczanie porz�dku preorder i postorder
    private void dfs(Digraph G, int v) {
        marked[v] = true;
        pre[v] = preCounter++;
        preorder.enqueue(v);
        for (int w : G.adj(v)) {
            if (!marked[w]) {
                dfs(G, w);
            }
        }
        postorder.enqueue(v);
        post[v] = postCounter++;
    }

    // Uruchamianie algorytmu DFS w digrafie wa�onym G w wierzcho�ku v oraz wyznaczanie porz�dku preorder i postorder
    private void dfs(EdgeWeightedDigraph G, int v) {
        marked[v] = true;
        pre[v] = preCounter++;
        preorder.enqueue(v);
        for (DirectedEdge e : G.adj(v)) {
            int w = e.to();
            if (!marked[w]) {
                dfs(G, w);
            }
        }
        postorder.enqueue(v);
        post[v] = postCounter++;
    }

    public int pre(int v) {
        return pre[v];
    }

    public int post(int v) {
        return post[v];
    }

    // Zwracanie wierzcho�k�w w porz�dku postorder jako obiektu Iterable
    public Iterable<Integer> postorder() {
        return postorder;
    }

    // Zwracanie wierzcho�k�w w porz�dku preorder jako obiektu Iterable
    public Iterable<Integer> preorder() {
        return preorder;
    }

    // Zwracanie wierzcho�k�w w odwr�conym porz�dku postorder jako obiektu Iterable
    public Iterable<Integer> reversePostorder() {
        Stack<Integer> reverse = new Stack<Integer>();
        for (int v : postorder)
            reverse.push(v);
        return reverse;
    }

    // Sprawdzanie, czy digraf jest albo acykliczny, albo obejmuje cykl skierowany
    private boolean check(Digraph G) {

        // Sprawdzanie, czy metoda postorder() jest sp�jna z metod� rank()
        int r = 0;
        for (int v : postorder()) {
            if (post(v) != r) {
                System.err.println("postorder() i post() s� niesp�jne");
                return false;
            }
            r++;
        }

        // Sprawdzanie, czy metoda preorder() jest sp�jna z metod� pre()
        r = 0;
        for (int v : preorder()) {
            if (pre(v) != r) {
                System.err.println("preorder() i pre() s� niesp�jne");
                return false;
            }
            r++;
        }


        return true;
    }

    public static void main(String[] args) {

        // Tworzenie losowego grafu DAG o V wierzcho�kach i E kraw�dziach
        int V = Integer.parseInt(args[0]);
        int E = Integer.parseInt(args[1]);
        Digraph G = new Digraph(V);
        for (int i = 0; i < E; i++) {
            int v = StdRandom.uniform(V);
            int w = StdRandom.uniform(V);
            G.addEdge(v, w);
        }

        StdOut.println(G);

        DepthFirstOrder dfs = new DepthFirstOrder(G);
        StdOut.println("   v  pre post");
        StdOut.println("--------------");
        for (int v = 0; v < G.V(); v++) {
            System.out.printf("%4d %4d %4d\n", v, dfs.pre(v), dfs.post(v));
        }

        StdOut.print("Postorder: ");
        for (int v : dfs.postorder()) {
            StdOut.print(v + " ");
        }
        StdOut.println();

        StdOut.print("Preorder:  ");
        for (int v : dfs.preorder()) {
            StdOut.print(v + " ");
        }
        StdOut.println();


    }

}
