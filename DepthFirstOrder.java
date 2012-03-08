/*************************************************************************
 *  Kompilacja:  javac DepthFirstOrder.java
 *  Wykonanie:    java DepthFirstOrder V E F
 *  Zale¿noœci: Digraph.java EdgeWeightedDigraph.java DirectedEdge.java
 *                Queue.java Stack.java StdOut.java
 *
 *  Wyznaczanie porz¹dku preorder i postorder dla digrafu lub digrafu wa¿onego.
 *  Dzia³a w czasie O(E + V).
 *
 *
 *************************************************************************/

public class DepthFirstOrder {
    private boolean[] marked;          // marked[v] = czy v oznaczono w algorytmie DFS?
    private int[] pre;                 // pre[v]    = numer v w porz¹dku preorder
    private int[] post;                // post[v]   = numer v w porz¹dku postorder
    private Queue<Integer> preorder;   // Wierzcho³ki w kolejnoœci preorder
    private Queue<Integer> postorder;  // Wierzcho³ki w kolejnoœci postorder
    private int preCounter;            // Licznik dla numerowania w porz¹dku preorder
    private int postCounter;           // Licznik dla numerowania w porz¹dku postorder

    // Wyznaczanie porz¹dków preorder i postorder w digrafie przez przeszukiwanie w g³¹b 
    public DepthFirstOrder(Digraph G) {
        pre    = new int[G.V()];
        post   = new int[G.V()];
        postorder = new Queue<Integer>();
        preorder  = new Queue<Integer>();
        marked    = new boolean[G.V()];
        for (int v = 0; v < G.V(); v++)
            if (!marked[v]) dfs(G, v);
    }

    // Wyznaczanie porz¹dków preorder i postorder w digrafie wa¿onym przez przeszukiwanie w g³¹b
    public DepthFirstOrder(EdgeWeightedDigraph G) {
        pre    = new int[G.V()];
        post   = new int[G.V()];
        postorder = new Queue<Integer>();
        preorder  = new Queue<Integer>();
        marked    = new boolean[G.V()];
        for (int v = 0; v < G.V(); v++)
            if (!marked[v]) dfs(G, v);
    }

    // Uruchamianie algorytmu DFS w digrafie G w wierzcho³ku v oraz wyznaczanie porz¹dku preorder i postorder
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

    // Uruchamianie algorytmu DFS w digrafie wa¿onym G w wierzcho³ku v oraz wyznaczanie porz¹dku preorder i postorder
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

    // Zwracanie wierzcho³ków w porz¹dku postorder jako obiektu Iterable
    public Iterable<Integer> postorder() {
        return postorder;
    }

    // Zwracanie wierzcho³ków w porz¹dku preorder jako obiektu Iterable
    public Iterable<Integer> preorder() {
        return preorder;
    }

    // Zwracanie wierzcho³ków w odwróconym porz¹dku postorder jako obiektu Iterable
    public Iterable<Integer> reversePostorder() {
        Stack<Integer> reverse = new Stack<Integer>();
        for (int v : postorder)
            reverse.push(v);
        return reverse;
    }

    // Sprawdzanie, czy digraf jest albo acykliczny, albo obejmuje cykl skierowany
    private boolean check(Digraph G) {

        // Sprawdzanie, czy metoda postorder() jest spójna z metod¹ rank()
        int r = 0;
        for (int v : postorder()) {
            if (post(v) != r) {
                System.err.println("postorder() i post() s¹ niespójne");
                return false;
            }
            r++;
        }

        // Sprawdzanie, czy metoda preorder() jest spójna z metod¹ pre()
        r = 0;
        for (int v : preorder()) {
            if (pre(v) != r) {
                System.err.println("preorder() i pre() s¹ niespójne");
                return false;
            }
            r++;
        }


        return true;
    }

    public static void main(String[] args) {

        // Tworzenie losowego grafu DAG o V wierzcho³kach i E krawêdziach
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
