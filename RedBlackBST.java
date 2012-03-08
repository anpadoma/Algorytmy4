/*************************************************************************
 *  Kompilacja:  javac RedBlackBST.java
 *  Wykonanie:    java RedBlackBST < input.txt
 *  
 *  Tablica symboli zaimplementowana za pomoc� przechylonych w lewo drzew czerwono-czarnych.
 *  Jest to wersja 2-3.
 *
 *  �r�d�o: Algorytmy, wydanie czwarte, podrozdzia� 4.3.
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

    private Node root;     // Korze� drzewa BST

    // Pomocniczy typ danych dla w�z��w drzew BST
    private class Node {
        private Key key;           // Klucz
        private Value val;         // Powi�zane dane
        private Node left, right;  // Odno�niki do lewego i prawego poddrzewa
        private boolean color;     // Kolor odno�nika z rodzica
        private int N;             // Licznik dla poddrzewa

        public Node(Key key, Value val, boolean color, int N) {
            this.key = key;
            this.val = val;
            this.color = color;
            this.N = N;
        }
    }

   /*************************************************************************
    *  Metody pomocnicze dla w�z�a
    *************************************************************************/
    // Czy w�ze� x jest czerwony (zwraca false, je�li x to null)?
    private boolean isRed(Node x) {
        if (x == null) return false;
        return (x.color == RED);
    }

    // Liczba w�z��w w poddrzewie o korzeniu w x (0, je�li x to null)
    private int size(Node x) {
        if (x == null) return 0;
        return x.N;
    } 


   /*************************************************************************
    *  Metody zwi�zane z wielko�ci�
    *************************************************************************/

    // Zwraca liczb� par klucz-warto�� w tablicy symboli
    public int size() { return size(root); }

    // Czy tablica symboli jest pusta?
    public boolean isEmpty() {
        return root == null;
    }

   /*************************************************************************
    *  Standardowe wyszukiwanie w drzewie BST
    *************************************************************************/

    // Warto�� powi�zana z danym kluczem (null, je�li klucz nie istnieje).
    public Value get(Key key) { return get(root, key); }

    // Warto�� powi�zana z danym kluczem w poddrzewie o korzeniu w x (null, je�li klucz nie istnieje).
    private Value get(Node x, Key key) {
        while (x != null) {
            int cmp = key.compareTo(x.key);
            if      (cmp < 0) x = x.left;
            else if (cmp > 0) x = x.right;
            else              return x.val;
        }
        return null;
    }

    // Czy istnieje para klucz-warto�� o danym kluczu?
    public boolean contains(Key key) {
        return (get(key) != null);
    }

    // Czy istnieje para klucz-warto�� o danym kluczu w poddrzewie o korzeniu w x?
    private boolean contains(Node x, Key key) {
        return (get(x, key) != null);
    }

   /*************************************************************************
    *  Wstawianie do drzewa czerwono-czarnego
    *************************************************************************/

    // Wstawianie pary klucz-warto��. Je�li klucz ju� istnieje, dawna warto��
    // jest nadpisywana now�
    public void put(Key key, Value val) {
        root = put(root, key, val);
        root.color = BLACK;
        assert isRedBlackBST();
    }

    // Wstawianie pary klucz-warto�� w poddrzewie o korzeniu w h
    private Node put(Node h, Key key, Value val) { 
        if (h == null) return new Node(key, val, RED, 1);

        int cmp = key.compareTo(h.key);
        if      (cmp < 0) h.left  = put(h.left,  key, val); 
        else if (cmp > 0) h.right = put(h.right, key, val); 
        else              h.val   = val;

        // Naprawianie odno�nik�w skierowanych w prawo
        if (isRed(h.right) && !isRed(h.left))      h = rotateLeft(h);
        if (isRed(h.left)  &&  isRed(h.left.left)) h = rotateRight(h);
        if (isRed(h.left)  &&  isRed(h.right))     flipColors(h);
        h.N = size(h.left) + size(h.right) + 1;

        return h;
    }

   /*************************************************************************
    *  Usuwanie z drzewa czerwono-czarnego
    *************************************************************************/

    // Usuwanie pary klucz-warto�� o minimalnym kluczu
    public void deleteMin() {
        if (isEmpty()) throw new RuntimeException("Brak elementow w drzewie BST");

        // Je�li ka�de dziecko korzenia jest czarne, korze� ma by� czerwony
        if (!isRed(root.left) && !isRed(root.right))
            root.color = RED;

        root = deleteMin(root);
        if (!isEmpty()) root.color = BLACK;
        assert isRedBlackBST();
    }

    // Usuwanie pary klucz-warto�� o minimalnym kluczu z poddrzewa o korzeniu h
    private Node deleteMin(Node h) { 
        if (h.left == null)
            return null;

        if (!isRed(h.left) && !isRed(h.left.left))
            h = moveRedLeft(h);

        h.left = deleteMin(h.left);
        return balance(h);
    }


    // Usuwanie pary klucz-warto�� o maksymalnym kluczu
    public void deleteMax() {
        if (isEmpty()) throw new RuntimeException("Brak elementow w drzewie BST");

        // Je�li dzieci korzenia s� czarne, korze� ma by� czerwony
        if (!isRed(root.left) && !isRed(root.right))
            root.color = RED;

        root = deleteMax(root);
        if (!isEmpty()) root.color = BLACK;
        assert isRedBlackBST();
    }

    // Usuwanie pary klucz-warto�� o maksymalnym kluczu z poddrzewa o korzeniu w h
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

    // Usuwanie pary klucz-warto�� o danym kluczu
    public void delete(Key key) { 
        if (!contains(key)) {
            System.err.println("Tablica symboli nie obejmuje klucza " + key);
            return;
        }

        // Je�li dzieci korzenia s� czarne, korze� ma by� czerwony
        if (!isRed(root.left) && !isRed(root.right))
            root.color = RED;

        root = delete(root, key);
        if (!isEmpty()) root.color = BLACK;
        assert isRedBlackBST();
    }

    // Usuwanie pary klucz-warto�� o danym kluczu z poddrzewa o korzeniu w h
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

    // Powoduje skierowanie w prawo odno�nika skierowanego w lewo
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

    // Powoduje skierowanie w lewo odno�nika skierowanego w prawo
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

    // Zamienia kolory w�z�a i jego dw�ch dzieci
    private void flipColors(Node h) {
        // h musi mie� kolor inny ni� dzieci
        assert (h != null) && (h.left != null) && (h.right != null);
        assert (!isRed(h) &&  isRed(h.left) &&  isRed(h.right)) ||
                (isRed(h) && !isRed(h.left) && !isRed(h.right));
        h.color = !h.color;
        h.left.color = !h.left.color;
        h.right.color = !h.right.color;
    }

    // Je�li h jest czerwony, a h.left i h.left.left
    // s� czarne, metoda ustawia h.left lub jedno z dzieci na kolor czerwony.
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

    // Je�li h jest czerwony, a h.right i h.right.left
    // s� czarne, metoda ustawia h.right lub jedno z jego dzieci na kolor czerwony.
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
    *  Funkcje narz�dziowe
    *************************************************************************/

    // Zwraca wysoko�� drzewa (0, je�li drzewo jest puste)
    public int height() { return height(root); }
    private int height(Node x) {
        if (x == null) return 0;
        return 1 + Math.max(height(x.left), height(x.right));
    }

   /*************************************************************************
    *  Metody dla uporz�dkowanej tablicy symboli.
    *************************************************************************/

    // Najmniejszy klucz (null, je�li taki klucz nie istnieje)
    public Key min() {
        if (isEmpty()) return null;
        return min(root).key;
    } 

    // Zwraca najmniejszy klucz poddrzewa o korzeniu w x (lub null, je�li taki klucz nie istnieje)
    private Node min(Node x) { 
        assert x != null;
        if (x.left == null) return x; 
        else                return min(x.left); 
    } 

    // Zwraca najwi�kszy klucz (lub null if no such key
    public Key max() {
        if (isEmpty()) return null;
        return max(root).key;
    } 

    // Najwi�kszy klucz w poddrzewie o korzeniu w x (lub null, je�li taki klucz nie istnieje)
    private Node max(Node x) { 
        assert x != null;
        if (x.right == null) return x; 
        else                 return max(x.right); 
    } 

    // Zwraca najwi�kszy klucz wi�kszy lub r�wny wzgl�dem danego
    public Key floor(Key key) {
        Node x = floor(root, key);
        if (x == null) return null;
        else           return x.key;
    }    

    // Zwraca najwi�kszy klucz wi�kszy lub r�wny wzgl�dem danego w poddrzewie o korzeniu w x
    private Node floor(Node x, Key key) {
        if (x == null) return null;
        int cmp = key.compareTo(x.key);
        if (cmp == 0) return x;
        if (cmp < 0)  return floor(x.left, key);
        Node t = floor(x.right, key);
        if (t != null) return t; 
        else           return x;
    }

    // Najmniejszy klucz wi�kszy lub r�wny wzgl�dem danego
    public Key ceiling(Key key) {  
        Node x = ceiling(root, key);
        if (x == null) return null;
        else           return x.key;  
    }

    // Najmniejszy klucz wi�kszy lub r�wny wzgl�dem danego w poddrzewie o korzeniu w x 
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

    // Liczba kluczy mniejszych ni� key
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
    *  Wyliczanie element�w zakresu i wyszukiwanie zakresowe
    ***********************************************************************/

    // Wszystkie klucze (jako obiekt Iterable)
    public Iterable<Key> keys() {
        return keys(min(), max());
    }

    // Klucze z przedzia�u lo - hi (jako obiekt Iterable)
    public Iterable<Key> keys(Key lo, Key hi) {
        Queue<Key> queue = new Queue<Key>();
        // if (isEmpty() || lo.compareTo(hi) > 0) return queue;
        keys(root, queue, lo, hi);
        return queue;
    } 

    // Dodaje do kolejki klucze z przedzia�u lo - hi z poddrzewa o korzeniu w x    
    private void keys(Node x, Queue<Key> queue, Key lo, Key hi) { 
        if (x == null) return; 
        int cmplo = lo.compareTo(x.key); 
        int cmphi = hi.compareTo(x.key); 
        if (cmplo < 0) keys(x.left, queue, lo, hi); 
        if (cmplo <= 0 && cmphi >= 0) queue.enqueue(x.key); 
        if (cmphi > 0) keys(x.right, queue, lo, hi); 
    } 

    // Liczba kluczy z przedzia�u lo - hi
    public int size(Key lo, Key hi) {
        if (lo.compareTo(hi) > 0) return 0;
        if (contains(hi)) return rank(hi) - rank(lo) + 1;
        else              return rank(hi) - rank(lo);
    }


   /*************************************************************************
    *  Sprawdzanie integralno�ci drzew czerwono-czarnych 
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
        if (!isBinaryTree())     StdOut.println("Liczniki w poddrzewach s� niespojne");
        if (!isOrdered())        StdOut.println("Brak symetrii");
        if (!hasNoDuplicates())  StdOut.println("Wystepuja powtarzajace sie klucze");
        if (!isRankConsistent()) StdOut.println("Pozycje s� niespojne");
        return isBinaryTree() && isOrdered() && hasNoDuplicates() && isRankConsistent();
    }

    // Czy pola z rozmiarem s� prawid�owe (czy drzewo jest binarne)?
    private boolean isBinaryTree() { return isBinaryTree(root); }
    private boolean isBinaryTree(Node x) {
        if (x == null) return true;
        if (x.N != size(x.left) + size(x.right) + 1) return false;
        return isBinaryTree(x.left) && isBinaryTree(x.right);
    } 

    // Czy drzewo binarne spe�nia warunek symetryczno�ci?
    private boolean isOrdered() {
        assert isBinaryTree();
        return isOrdered(root, min(), max());
    }

    private boolean isOrdered(Node x, Key min, Key max) {
        if (x == null) return true;
        if (less(x.key, min) || less(max, x.key)) return false;
        return isOrdered(x.left, min, x.key) && isOrdered(x.right, x.key, max);
    } 

    // Sprawdzanie, czy nie wyst�puj� powtarzaj�ce si� klucze.
    // Warunek wst�pny: przy przechodzeniu metod� inorder klucze s� uporz�dkowane 
    private boolean hasNoDuplicates() {
        assert isOrdered();
        for (int i = 1; i < size(); i++) {
            if (select(i).compareTo(select(i-1)) == 0) return false;
        }
        return true;
    }

    // Sprawdzanie, czy pozycje s� sp�jne
    private boolean isRankConsistent() {
        for (int i = 0; i < size(); i++)
            if (i != rank(select(i))) return false;
        for (Key key : keys())
            if (key.compareTo(select(rank(key))) != 0) return false;
        return true;
    }

    // Czy w drzewie nie wyst�puj� czerwone odno�niki skierowane w prawo i czy most one (left)
    // wyst�puje najwy�ej jeden (lewy) czerwony odno�nik pod rz�d na dowolnej �cie�ce?
    private boolean is23() { return is23(root); }
    private boolean is23(Node x) {
        if (x == null) return true;
        if (isRed(x.right)) return false;
        if (x != root && isRed(x) && isRed(x.left))
            return false;
        return is23(x.left) && is23(x.right);
    } 

    // Czy wszystkie �cie�ki z korzenia do li�cia maj� t� sam� liczb� czarnych kraw�dzi?
    private boolean isBalanced() { 
        int black = 0;     // Liczba czarnych odno�nik�w na �cie�ce z korzenia do minimum
        Node x = root;
        while (x != null) {
            if (!isRed(x)) black++;
            x = x.left;
        }
        return isBalanced(root, black);
    }

    // Czy ka�da �cie�ka z korzenia do li�cia ma okre�lon� liczb� czarnych odno�nik�w?
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
