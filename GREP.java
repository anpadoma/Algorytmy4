/*************************************************************************
 *  Kompilacja:  javac GREP.java
 *  Wykonanie:    java GREP regexp < input.txt
 *  Zale¿noœci: NFA.java
 *
 *  Ten program pobiera wyra¿enia regularne jako argument wiersza poleceñ i 
 *  wyœwietla wiersze ze standardowego wejœcia obejmuj¹ce pod³añcuch nale¿¹cy
 *  do jêzyka opisywanego przez dane wyra¿enie regularne. 
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
