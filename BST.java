
/*************************************************************************
 *  Kompilacja:  javac BST.java
 *  Wykonanie:    java BST
 *
 *  Tablica symboli zaimplementowana za pomoc¹ drzewa wyszukiwañ binarnych.
 * 
 *  % more tinyST.txt
 *  S E A R C H E X A M P L E
 *  
 *  % java BST < tinyST.txt
 *  A 8
 *  C 4
 *  E 12
 *  H 5
 *  L 11
 *  M 9
 *  P 10
 *  R 3
 *  S 0
 *  X 7
 *
 *************************************************************************/

public class BST<Key extends Comparable<Key>, Value> {
    private Node root;             // Korzeñ drzewa BST.

    private class Node {
        private Key key;           // Klucz s³u¿¹cy do sortowania.
        private Value val;         // Powi¹zane dane.
        private Node left, right;  // Lewe i prawe poddrzewo.
        private int N;             // Liczba wêz³ów w poddrzewie.

        public Node(Key key, Value val, int N) {
            this.key = key;
            this.val = val;
            this.N = N;
        }
    }

    // Czy tablica symboli jest pusta?
    public boolean isEmpty() {
        return size() == 0;
    }

    // Zwraca liczbê par klucz-wartoœæ z drzewa BST.
    public int size() {
        return size(root);
    }

    // return number of key-value pairs in BST rooted at x
    private int size(Node x) {
        if (x == null) return 0;
        else return x.N;
    }

   /***********************************************************************
    *  Wyszukuje dany klucz w drzewie BST i zwraca powi¹zan¹ wartoœæ, jeœli j¹ znajdzie
    *  (w przeciwnym razie zwraca null).
    ***********************************************************************/
    // Czy istnieje para klucz-wartoœæ o danym kluczu?
    public boolean contains(Key key) {
        return get(key) != null;
    }

    // Zwraca wartoœæ powi¹zan¹ z danym kluczem (lub null, jeœli klucz nie istnieje).
    public Value get(Key key) {
        return get(root, key);
    }

    private Value get(Node x, Key key) {
        if (x == null) return null;
        int cmp = key.compareTo(x.key);
        if      (cmp < 0) return get(x.left, key);
        else if (cmp > 0) return get(x.right, key);
        else              return x.val;
    }

   /***********************************************************************
    * Wstawia parê klucz-wartoœæ do drzewa BST.
    *  Jeœli klucz ju¿ istnieje, nale¿y zaktualizowaæ go za pomoc¹ nowej wartoœci.
    ***********************************************************************/
    public void put(Key key, Value val) {
        if (val == null) { delete(key); return; }
        root = put(root, key, val);
        assert isBST();
    }

    private Node put(Node x, Key key, Value val) {
        if (x == null) return new Node(key, val, 1);
        int cmp = key.compareTo(x.key);
        if      (cmp < 0) x.left  = put(x.left,  key, val);
        else if (cmp > 0) x.right = put(x.right, key, val);
        else              x.val   = val;
        x.N = 1 + size(x.left) + size(x.right);
        return x;
    }

   /***********************************************************************
    *  Usuwanie.
    ***********************************************************************/

    public void deleteMin() {
        if (isEmpty()) throw new RuntimeException("Brak elementów w tablicy symboli");
        root = deleteMin(root);
        assert isBST();
    }

    private Node deleteMin(Node x) {
        if (x.left == null) return x.right;
        x.left = deleteMin(x.left);
        x.N = size(x.left) + size(x.right) + 1;
        return x;
    }

    public void deleteMax() {
        if (isEmpty()) throw new RuntimeException("Brak elementow w tablicy symboli");
        root = deleteMax(root);
        assert isBST();
    }

    private Node deleteMax(Node x) {
        if (x.right == null) return x.left;
        x.right = deleteMax(x.right);
        x.N = size(x.left) + size(x.right) + 1;
        return x;
    }

    public void delete(Key key) {
        root = delete(root, key);
        assert isBST();
    }

    private Node delete(Node x, Key key) {
        if (x == null) return null;
        int cmp = key.compareTo(x.key);
        if      (cmp < 0) x.left  = delete(x.left,  key);
        else if (cmp > 0) x.right = delete(x.right, key);
        else { 
            if (x.right == null) return x.left;
            if (x.left  == null) return x.right;
            Node t = x;
            x = min(t.right);
            x.right = deleteMin(t.right);
            x.left = t.left;
        } 
        x.N = size(x.left) + size(x.right) + 1;
        return x;
    } 


   /***********************************************************************
    *  Minimum, maksimum, pod³oga i sufit.
    ***********************************************************************/
    public Key min() {
        if (isEmpty()) return null;
        return min(root).key;
    } 

    private Node min(Node x) { 
        if (x.left == null) return x; 
        else                return min(x.left); 
    } 

    public Key max() {
        if (isEmpty()) return null;
        return max(root).key;
    } 

    private Node max(Node x) { 
        if (x.right == null) return x; 
        else                 return max(x.right); 
    } 

    public Key floor(Key key) {
        Node x = floor(root, key);
        if (x == null) return null;
        else return x.key;
    } 

    private Node floor(Node x, Key key) {
        if (x == null) return null;
        int cmp = key.compareTo(x.key);
        if (cmp == 0) return x;
        if (cmp <  0) return floor(x.left, key);
        Node t = floor(x.right, key); 
        if (t != null) return t;
        else return x; 
    } 

    public Key ceiling(Key key) {
        Node x = ceiling(root, key);
        if (x == null) return null;
        else return x.key;
    }

    private Node ceiling(Node x, Key key) {
        if (x == null) return null;
        int cmp = key.compareTo(x.key);
        if (cmp == 0) return x;
        if (cmp < 0) { 
            Node t = ceiling(x.left, key); 
            if (t != null) return t;
            else return x; 
        } 
        return ceiling(x.right, key); 
    } 

   /***********************************************************************
    *  Pozycja i pobieranie.
    ***********************************************************************/
    public Key select(int k) {
        if (k < 0 || k >= size())  return null;
        Node x = select(root, k);
        return x.key;
    }

    // Zwraca klucz z pozycji k. 
    private Node select(Node x, int k) {
        if (x == null) return null; 
        int t = size(x.left); 
        if      (t > k) return select(x.left,  k); 
        else if (t < k) return select(x.right, k-t-1); 
        else            return x; 
    } 

    public int rank(Key key) {
        return rank(key, root);
    } 

    // Zwraca liczbê kluczy w poddrzewie mniejszych ni¿ x.key. 
    private int rank(Key key, Node x) {
        if (x == null) return 0; 
        int cmp = key.compareTo(x.key); 
        if      (cmp < 0) return rank(key, x.left); 
        else if (cmp > 0) return 1 + size(x.left) + rank(key, x.right); 
        else              return size(x.left); 
    } 

   /***********************************************************************
    *  Wyliczanie zakresu i wyszukiwanie zakresowe.
    ***********************************************************************/
    public Iterable<Key> keys() {
        return keys(min(), max());
    }

    public Iterable<Key> keys(Key lo, Key hi) {
        Queue<Key> queue = new Queue<Key>();
        keys(root, queue, lo, hi);
        return queue;
    } 

    private void keys(Node x, Queue<Key> queue, Key lo, Key hi) { 
        if (x == null) return; 
        int cmplo = lo.compareTo(x.key); 
        int cmphi = hi.compareTo(x.key); 
        if (cmplo < 0) keys(x.left, queue, lo, hi); 
        if (cmplo <= 0 && cmphi >= 0) queue.enqueue(x.key); 
        if (cmphi > 0) keys(x.right, queue, lo, hi); 
    } 

    public int size(Key lo, Key hi) {
        if (lo.compareTo(hi) > 0) return 0;
        if (contains(hi)) return rank(hi) - rank(lo) + 1;
        else              return rank(hi) - rank(lo);
    }



  /*************************************************************************
    *  Sprawdzanie integralnoœci drzew BST.
    *************************************************************************/
    // Czy drzewo jest drzewem BST?
    private boolean isBST() {
        if (isEmpty()) return true;
        if (!isBinaryTree())     StdOut.println("Liczby elementow poddrzew s¹ niespojne");
        if (!isOrdered())        StdOut.println("Niewlasciwe uporzadkowanie");
        if (!hasNoDuplicates())  StdOut.println("Wystepuj¹ powtarzajace siê klucze");
        if (!isRankConsistent()) StdOut.println("Pozycja jest niespojna");
        return isBinaryTree() && isOrdered() && hasNoDuplicates() && isRankConsistent();
    }

    // Czy pola z rozmiarem s¹ poprawne (czy drzewo jest binarne)?
    private boolean isBinaryTree() { return isBinaryTree(root); }
    private boolean isBinaryTree(Node x) {
        if (x == null) return true;
        if (x.N != size(x.left) + size(x.right) + 1) return false;
        return isBinaryTree(x.left) && isBinaryTree(x.right);
    } 

    // Czy porz¹dek w drzewie binarnym jest symetryczny?
    private boolean isOrdered() {
        assert isBinaryTree();
        return isOrdered(root, min(), max());
    }
    
    private boolean isOrdered(Node x, Key min, Key max) {
        if (x == null) return true;
        if (less(x.key, min) || less(max, x.key)) return false;
        return isOrdered(x.left, min, x.key) && isOrdered(x.right, x.key, max);
    } 

    // Sprawdzanie, czy nie wystêpuj¹ powtarzaj¹ce siê klucze.
    // Warunek wstêpny: przy przechodzeniu w porz¹dku inorder klucze s¹ podawane w odpowiedniej kolejnoœci.
    private boolean hasNoDuplicates() {
        assert isOrdered();
        for (int i = 1; i < size(); i++) {
            if (select(i).compareTo(select(i-1)) == 0) return false;
        }
        return true;
    }

    // Sprawdzanie, czy pozycje s¹ spójne.
    private boolean isRankConsistent() {
        for (int i = 0; i < size(); i++)
            if (i != rank(select(i))) return false;
        for (Key key : keys())
            if (key.compareTo(select(rank(key))) != 0) return false;
        return true;
    }

    private boolean less(Key x, Key y) {
        return x.compareTo(y) < 0;
    }



   /*****************************************************************************
    *  Klient testowy.
    *****************************************************************************/
    public static void main(String[] args) { 
        BST<String, Integer> st = new BST<String, Integer>();
        for (int i = 0; !StdIn.isEmpty(); i++) {
            String key = StdIn.readString();
            st.put(key, i);
        }
        for (String s : st.keys())
            StdOut.println(s + " " + st.get(s));
    }
}
