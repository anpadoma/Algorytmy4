/*************************************************************************
 *  Kompilacja:  javac GREP.java
 *  Wykonanie:    java GREP regexp < input.txt
 *  Zale�no�ci: NFA.java
 *
 *  Ten program pobiera wyra�enia regularne jako argument wiersza polece� i 
 *  wy�wietla wiersze ze standardowego wej�cia obejmuj�ce pod�a�cuch nale��cy
 *  do j�zyka opisywanego przez dane wyra�enie regularne. 
 *
 *************************************************************************/

public class GREP {
    public static void main(String[] args) { 
        String regexp = "(.*" + args[0] + ".*)";
        NFA nfa = new NFA(regexp);
        while (StdIn.hasNextLine()) { 
            String txt = StdIn.readLine();
            if (nfa.recognizes(txt)) {
                StdOut.println(txt);
            }
        }
    } 
} 
