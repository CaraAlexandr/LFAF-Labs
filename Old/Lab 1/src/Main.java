import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Main {
    public Main() {
    }

    public static void main(String[] args) {
        Set<String> nonterminals = new HashSet(Arrays.asList("S", "F", "L"));
        Set<String> terminals = new HashSet(Arrays.asList("a", "b", "c", "d"));
        Map<String, List<String>> productions = new HashMap();
        productions.put("S", Arrays.asList("bS", "aF", "d"));
        productions.put("F", Arrays.asList("cF", "dF", "aL", "b"));
        productions.put("L", Arrays.asList("aL", "c"));
        Grammar grammar = new Grammar(nonterminals, terminals, productions, "S");

        String testString1;
        for(int i = 0; i < 5; ++i) {
            testString1 = grammar.generateString();
            System.out.println("Valid word " + (i + 1) + ": " + testString1);
        }

        FiniteAutomaton automaton = grammar.toFiniteAutomaton();
        testString1 = "bda";
        String testString2 = "acccb";
        boolean result1 = automaton.stringBelongToLanguage(testString1);
        boolean result2 = automaton.stringBelongToLanguage(testString2);
        System.out.println("Does " + testString1 + " belong to the language? " + result1);
        System.out.println("Does " + testString2 + " belong to the language? " + result2);
    }
}