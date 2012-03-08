/*************************************************************************
 *  Kompilacja:  javac RedBlackBST.java
 *  Wykonanie:    java RedBlackBST < input.txt
 *  
 *  Tablica symboli zaimplementowana za pomoc¹ przechylonych w lewo drzew czerwono-czarnych.
 *  Jest to wersja 2-3.
 *
 *  ród³o: Algorytmy, wydanie czwarte, podrozdzia³ 4.3.
 *
 *  % more tinyST.txt
 *  S E A R C H E X A M P L E
 *  
 *  % java RedBlackBST < tinyST.txt
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

public class RedBlackBST<Key extends Comparable<Key>, Value> {

    private static final boolean RED   = true;
    private static final boolean BLACK = false;

    private Node root;     // Korzeñ drzewa BST

    // Pomocniczy typ danych dla wêz³ów drzew BST
    private class Node {
        private Key key;           // Klucz
        private Value val;         // Powi¹zane dane
        private Node left, right;  // Odnoœniki do lewego i prawego poddrzewa
        private boolean color;     // Kolor odnoœnika z rodzica
        private int N;             // Licznik dla poddrzewa

        public Node(Key key, Value val, boolean color, int N) {
            this.key = key;
            this.val = val;
            this.color = color;
            this.N = N;
        }
    }

   /*************************************************************************
    *  Metody pomocnicze dla wêz³a
    *************************************************************************/
    // Czy wêze³ x jest czerwony (zwraca false, jeœli x to null)?
    private boolean isRed(Node x) {
        if (x == null) return false;
        return (x.color == RED);
    }

    // Liczba wêz³ów w poddrzewie o korzeniu w x (0, jeœli x to null)
    private int size(Node x) {
        if (x == null) return 0;
        return x.N;
    } 


   /*************************************************************************
    *  Metody zwi¹zane z wielkoœci¹
    *************************************************************************/

    // Zwraca liczbê par klucz-wartoœæ w tablicy symboli
    public int size() { return size(root); }

    // Czy tablica symboli jest pusta?
    public boolean isEmpty() {
        return root == null;
    }

   /*************************************************************************
    *  Standardowe wyszukiwanie w drzewie BST
    *************************************************************************/

    // Wartoœæ powi¹zana z danym kluczem (null, jeœli klucz nie istnieje).
    public Value get(Key key) { return get(root, key); }

    // Wartoœæ powi¹zana z danym kluczem w poddrzewie o korzeniu w x (null, jeœli klucz nie istnieje).
    private Value get(Node x, Key key) {
        while (x != null) {
            int cmp = key.compareTo(x.key);
            if      (cmp < 0) x = x.left;
            else if (cmp > 0) x = x.right;
            else              return x.val;
        }
        return null;
    }

    // Czy istnieje para klucz-wartoœæ o danym kluczu?
    public boolean contains(Key key) {
        return (get(key) != null);
    }

    // Czy istnieje para klucz-wartoœæ o danym kluczu w poddrzewie o korzeniu w x?
    private boolean contains(Node x, Key key) {
        return (get(x, key) != null);
    }

   /*************************************************************************
    *  Wstawianie do drzewa czerwono-czarnego
    *************************************************************************/

    // Wstawianie pary klucz-wartoœæ. Jeœli klucz ju¿ istnieje, dawna wartoœæ
    // jest nadpisywana now¹
    public void put(Key key, Value val) {
        root = put(root, key, val);
        root.color = BLACK;
        assert isRedBlackBST();
    }

    // Wstawianie pary klucz-wartoœæ w poddrzewie o korzeniu w h
    private Node put(Node h, Key key, Value val) { 
        if (h == null) return new Node(key, val, RED, 1);

        int cmp = key.compareTo(h.key);
        if      (cmp < 0) h.left  = put(h.left,  key, val); 
        else if (cmp > 0) h.right = put(h.right, key, val); 
        else              h.val   = val;

        // Naprawianie odnoœników skierowanych w prawo
        if (isRed(h.right) && !isRed(h.left))      h = rotateLeft(h);
        if (isRed(h.left)  &&  isRed(h.left.left)) h = rotateRight(h);
        if (isRed(h.left)  &&  isRed(h.right))     flipColors(h);
        h.N = size(h.left) + size(h.right) + 1;

        return h;
    }

   /*************************************************************************
    *  Usuwanie z drzewa czerwono-czarnego
    *************************************************************************/

    // Usuwanie pary klucz-wartoœæ o minimalnym kluczu
    public void deleteMin() {
        if (isEmpty()) throw new RuntimeException("Brak elementow w drzewie BST");

        // Jeœli ka¿de dziecko korzenia jest czarne, korzeñ ma byæ czerwony
        if (!isRed(root.left) && !isRed(root.right))
            root.color = RED;

        root = deleteMin(root);
        if (!isEmpty()) root.color = BLACK;
        assert isRedBlackBST();
    }

    // Usuwanie pary klucz-wartoœæ o minimalnym kluczu z poddrzewa o korzeniu h
    private Node deleteMin(Node h) { 
        if (h.left == null)
            return null;

        if (!isRed(h.left) && !isRed(h.left.left))
            h = moveRedLeft(h);

        h.left = deleteMin(h.left);
        return balance(h);
    }


    // Usuwanie pary klucz-wartoœæ o maksymalnym kluczu
    public void deleteMax() {
        if (isEmpty()) throw new RuntimeException("Brak elementow w drzewie BST");

        // Jeœli dzieci korzenia s¹ czarne, korzeñ ma byæ czerwony
        if (!isRed(root.left) && !isRed(root.right))
            root.color = RED;

        root = deleteMax(root);
        if (!isEmpty()) root.color = BLACK;
        assert isRedBlackBST();
    }

    // Usuwanie pary klucz-wartoœæ o maksymalnym kluczu z poddrzewa o korzeniu w h
    private Node deleteMax(Node h) { 
        if (isRed(h.left))
            h = rotateRight(h);

        if (h.right == null)
            return null;

        if (!isRed(h.right) && !isRed(h.right.left))
            h = moveRedRight(h);

        h.right = deleteMax(h.right);

        return balance(h);
    }

    // Usuwanie pary klucz-wartoœæ o danym kluczu
    public void delete(Key key) { 
        if (!contains(key)) {
            System.err.println("Tablica symboli nie obejmuje klucza " + key);
            return;
        }

        // Jeœli dzieci korzenia s¹ czarne, korzeñ ma byæ czerwony
        if (!isRed(root.left) && !isRed(root.right))
            root.color = RED;

        root = delete(root, key);
        if (!isEmpty()) root.color = BLACK;
        assert isRedBlackBST();
    }

    // Usuwanie pary klucz-wartoœæ o danym kluczu z poddrzewa o korzeniu w h
    private Node delete(Node h, Key key) { 
        assert contains(h, key);

        if (key.compareTo(h.key) < 0)  {
            if (!isRed(h.left) && !isRed(h.left.left))
                h = moveRedLeft(h);
            h.left = delete(h.left, key);
        }
        else {
            if (isRed(h.left))
                h = rotateRight(h);
            if (key.compareTo(h.key) == 0 && (h.right == null))
                return null;
            if (!isRed(h.right) && !isRed(h.right.left))
                h = moveRedRight(h);
            if (key.compareTo(h.key) == 0) {
                h.val = get(h.right, min(h.right).key);
                h.key = min(h.right).key;
                h.right = deleteMin(h.right);
            }
            else h.right = delete(h.right, key);
        }
        return balance(h);
    }

   /*************************************************************************
    *  Funkcje pomocnicze dla drzewa czerwono-czarnych
    *************************************************************************/

    // Powoduje skierowanie w prawo odnoœnika skierowanego w lewo
    private Node rotateRight(Node h) {
        assert (h != null) && isRed(h.left);
        Node x = h.left;
        h.left = x.right;
        x.right = h;
        x.color = x.right.color;
        x.right.color = RED;
        x.N = h.N;
        h.N = size(h.left) + size(h.right) + 1;
        return x;
    }

    // Powoduje skierowanie w lewo odnoœnika skierowanego w prawo
    private Node rotateLeft(Node h) {
        assert (h != null) && isRed(h.right);
        Node x = h.right;
        h.right = x.left;
        x.left = h;
        x.color = x.left.color;
        x.left.color = RED;
        x.N = h.N;
        h.N = size(h.left) + size(h.right) + 1;
        return x;
    }

    // Zamienia kolory wêz³a i jego dwóch dzieci
    private void flipColors(Node h) {
        // h musi mieæ kolor inny ni¿ dzieci
        assert (h != null) && (h.left != null) && (h.right != null);
        assert (!isRed(h) &&  isRed(h.left) &&  isRed(h.right)) ||
                (isRed(h) && !isRed(h.left) && !isRed(h.right));
        h.color = !h.color;
        h.left.color = !h.left.color;
        h.right.color = !h.right.color;
    }

    // Jeœli h jest czerwony, a h.left i h.left.left
    // s¹ czarne, metoda ustawia h.left lub jedno z dzieci na kolor czerwony.
    private Node moveRedLeft(Node h) {
        assert (h != null);
        assert isRed(h) && !isRed(h.left) && !isRed(h.left.left);

        flipColors(h);
        if (isRed(h.right.left)) { 
            h.right = rotateRight(h.right);
            h = rotateLeft(h);
            // flipColors(h);
        }
        return h;
    }

    // Jeœli h jest czerwony, a h.right i h.right.left
    // s¹ czarne, metoda ustawia h.right lub jedno z jego dzieci na kolor czerwony.
    private Node moveRedRight(Node h) {
        assert (h != null);
        assert isRed(h) && !isRed(h.right) && !isRed(h.right.left);
        flipColors(h);
        if (isRed(h.left.left)) { 
            h = rotateRight(h);
            // flipColors(h);
        }
        return h;
    }

    // Przywracanie niezmiennika drzewa czerwono-czarnych
    private Node balance(Node h) {
        assert (h != null);

        if (isRed(h.right))                      h = rotateLeft(h);
        if (isRed(h.left) && isRed(h.left.left)) h = rotateRight(h);
        if (isRed(h.left) && isRed(h.right))     flipColors(h);

        h.N = size(h.left) + size(h.right) + 1;
        return h;
    }


   /*************************************************************************
    *  Funkcje narzêdziowe
    *************************************************************************/

    // Zwraca wysokoœæ drzewa (0, jeœli drzewo jest puste)
    public int height() { return height(root); }
    private int height(Node x) {
        if (x == null) return 0;
        return 1 + Math.max(height(x.left), height(x.right));
    }

   /*************************************************************************
    *  Metody dla uporz¹dkowanej tablicy symboli.
    *************************************************************************/

    // Najmniejszy klucz (null, jeœli taki klucz nie istnieje)
    public Key min() {
        if (isEmpty()) return null;
        return min(root).key;
    } 

    // Zwraca najmniejszy klucz poddrzewa o korzeniu w x (lub null, jeœli taki klucz nie istnieje)
    private Node min(Node x) { 
        assert x != null;
        if (x.left == null) return x; 
        else                return min(x.left); 
    } 

    // Zwraca najwiêkszy klucz (lub null if no such key
    public Key max() {
        if (isEmpty()) return null;
        return max(root).key;
    } 

    // Najwiêkszy klucz w poddrzewie o korzeniu w x (lub null, jeœli taki klucz nie istnieje)
    private Node max(Node x) { 
        assert x != null;
        if (x.right == null) return x; 
        else                 return max(x.right); 
    } 

    // Zwraca najwiêkszy klucz wiêkszy lub równy wzglêdem danego
    public Key floor(Key key) {
        Node x = floor(root, key);
        if (x == null) return null;
        else           return x.key;
    }    

    // Zwraca najwiêkszy klucz wiêkszy lub równy wzglêdem danego w poddrzewie o korzeniu w x
    private Node floor(Node x, Key key) {
        if (x == null) return null;
        int cmp = key.compareTo(x.key);
        if (cmp == 0) return x;
        if (cmp < 0)  return floor(x.left, key);
        Node t = floor(x.right, key);
        if (t != null) return t; 
        else           return x;
    }

    // Najmniejszy klucz wiêkszy lub równy wzglêdem danego
    public Key ceiling(Key key) {  
        Node x = ceiling(root, key);
        if (x == null) return null;
        else           return x.key;  
    }

    // Najmniejszy klucz wiêkszy lub równy wzglêdem danego w poddrzewie o korzeniu w x 
    private Node ceiling(Node x, Key key) {  
        if (x == null) return null;
        int cmp = key.compareTo(x.key);
        if (cmp == 0) return x;
        if (cmp > 0)  return ceiling(x.right, key);
        Node t = ceiling(x.left, key);
        if (t != null) return t; 
        else           return x;
    }


    // Klucz z pozycji k
    public Key select(int k) {
        if (k < 0 || k >= size())  return null;
        Node x = select(root, k);
        return x.key;
    }

    // Klucz o pozycji k w poddrzewie o korzeniu w x
    private Node select(Node x, int k) {
        assert x != null;
        assert k >= 0 && k < size(x);
        int t = size(x.left); 
        if      (t > k) return select(x.left,  k); 
        else if (t < k) return select(x.right, k-t-1); 
        else            return x; 
    } 

    // Liczba kluczy mniejszych ni¿ key
    public int rank(Key key) {
        return rank(key, root);
    } 

    // Liczba kluczy mniejszych od danego w poddrzewie o korzeniu w x
    private int rank(Key key, Node x) {
        if (x == null) return 0; 
        int cmp = key.compareTo(x.key); 
        if      (cmp < 0) return rank(key, x.left); 
        else if (cmp > 0) return 1 + size(x.left) + rank(key, x.right); 
        else              return size(x.left); 
    } 

   /***********************************************************************
    *  Wyliczanie elementów zakresu i wyszukiwanie zakresowe
    ***********************************************************************/

    // Wszystkie klucze (jako obiekt Iterable)
    public Iterable<Key> keys() {
        return keys(min(), max());
    }

    // Klucze z przedzia³u lo - hi (jako obiekt Iterable)
    public Iterable<Key> keys(Key lo, Key hi) {
        Queue<Key> queue = new Queue<Key>();
        // if (isEmpty() || lo.compareTo(hi) > 0) return queue;
        keys(root, queue, lo, hi);
        return queue;
    } 

    // Dodaje do kolejki klucze z przedzia³u lo - hi z poddrzewa o korzeniu w x    
    private void keys(Node x, Queue<Key> queue, Key lo, Key hi) { 
        if (x == null) return; 
        int cmplo = lo.compareTo(x.key); 
        int cmphi = hi.compareTo(x.key); 
        if (cmplo < 0) keys(x.left, queue, lo, hi); 
        if (cmplo <= 0 && cmphi >= 0) queue.enqueue(x.key); 
        if (cmphi > 0) keys(x.right, queue, lo, hi); 
    } 

    // Liczba kluczy z przedzia³u lo - hi
    public int size(Key lo, Key hi) {
        if (lo.compareTo(hi) > 0) return 0;
        if (contains(hi)) return rank(hi) - rank(lo) + 1;
        else              return rank(hi) - rank(lo);
    }


   /*************************************************************************
    *  Sprawdzanie integralnoœci drzew czerwono-czarnych 
    *************************************************************************/
    // Czy drzewo jest drzewem czerwono-czarnym?
    private boolean isRedBlackBST() {
        if (!isBST())      StdOut.println("To nie drzewo BST");
        if (!is23())       StdOut.println("To nie drzewo 2-3");
        if (!isBalanced()) StdOut.println("Drzewo nie jest zbalansowane");
        return isBST() && is23() && isBalanced();
    }

    // Czy drzewo jest drzewem BST?
    private boolean isBST() {
        if (isEmpty()) return true;
        if (!isBinaryTree())     StdOut.println("Liczniki w poddrzewach s¹ niespojne");
        if (!isOrdered())        StdOut.println("Brak symetrii");
        if (!hasNoDuplicates())  StdOut.println("Wystepuja powtarzajace sie klucze");
        if (!isRankConsistent()) StdOut.println("Pozycje s¹ niespojne");
        return isBinaryTree() && isOrdered() && hasNoDuplicates() && isRankConsistent();
    }

    // Czy pola z rozmiarem s¹ prawid³owe (czy drzewo jest binarne)?
    private boolean isBinaryTree() { return isBinaryTree(root); }
    private boolean isBinaryTree(Node x) {
        if (x == null) return true;
        if (x.N != size(x.left) + size(x.right) + 1) return false;
        return isBinaryTree(x.left) && isBinaryTree(x.right);
    } 

    // Czy drzewo binarne spe³nia warunek symetrycznoœci?
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
    // Warunek wstêpny: przy przechodzeniu metod¹ inorder klucze s¹ uporz¹dkowane 
    private boolean hasNoDuplicates() {
        assert isOrdered();
        for (int i = 1; i < size(); i++) {
            if (select(i).compareTo(select(i-1)) == 0) return false;
        }
        return true;
    }

    // Sprawdzanie, czy pozycje s¹ spójne
    private boolean isRankConsistent() {
        for (int i = 0; i < size(); i++)
            if (i != rank(select(i))) return false;
        for (Key key : keys())
            if (key.compareTo(select(rank(key))) != 0) return false;
        return true;
    }

    // Czy w drzewie nie wystêpuj¹ czerwone odnoœniki skierowane w prawo i czy most one (left)
    // wystêpuje najwy¿ej jeden (lewy) czerwony odnoœnik pod rz¹d na dowolnej œcie¿ce?
    private boolean is23() { return is23(root); }
    private boolean is23(Node x) {
        if (x == null) return true;
        if (isRed(x.right)) return false;
        if (x != root && isRed(x) && isRed(x.left))
            return false;
        return is23(x.left) && is23(x.right);
    } 

    // Czy wszystkie œcie¿ki z korzenia do liœcia maj¹ tê sam¹ liczbê czarnych krawêdzi?
    private boolean isBalanced() { 
        int black = 0;     // Liczba czarnych odnoœników na œcie¿ce z korzenia do minimum
        Node x = root;
        while (x != null) {
            if (!isRed(x)) black++;
            x = x.left;
        }
        return isBalanced(root, black);
    }

    // Czy ka¿da œcie¿ka z korzenia do liœcia ma okreœlon¹ liczbê czarnych odnoœników?
    private boolean isBalanced(Node x, int black) {
        if (x == null) return black == 0;
        if (!isRed(x)) black--;
        return isBalanced(x.left, black) && isBalanced(x.right, black);
    } 

    private boolean less(Key x, Key y) {
        return x.compareTo(y) < 0;
    }


   /*****************************************************************************
    *  Klient testowy
    *****************************************************************************/
    public static void main(String[] args) { 
        RedBlackBST<String, Integer> st = new RedBlackBST<String, Integer>();
        for (int i = 0; !StdIn.isEmpty(); i++) {
            String key = StdIn.readString();
            st.put(key, i);
        }
        for (String s : st.keys())
            StdOut.println(s + " " + st.get(s));
    }
}
