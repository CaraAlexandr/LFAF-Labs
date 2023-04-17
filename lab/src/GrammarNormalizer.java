import java.util.*;
import java.util.stream.Collectors;



class Rule {
    String lhs;
    List<String> rhs;

    Rule(String lhs, List<String> rhs) {
        this.lhs = lhs;
        this.rhs = rhs;
    }

    @Override
    public String toString() {
        return lhs + " -> " + String.join("", rhs);
    }
}

class Grammar {
    Set<String> Vn;
    Set<String> Vt;
    List<Rule> P;
    String S;

    Grammar(Set<String> Vn, Set<String> Vt, List<Rule> P, String S) {
        this.Vn = Vn;
        this.Vt = Vt;
        this.P = P;
        this.S = S;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Normalized Grammar (Chomsky Normal Form):\n");
        for (Rule rule : P) {
            sb.append(rule).append('\n');
        }
        return sb.toString();
    }
}

public class GrammarNormalizer {

    public static Grammar normalizeToCNF(Grammar grammar) {
        Grammar newGrammar = new Grammar(new HashSet<>(grammar.Vn), new HashSet<>(grammar.Vt), new ArrayList<>(grammar.P), grammar.S);

        eliminateEpsilonProductions(newGrammar);
        eliminateRenaming(newGrammar);
        eliminateInaccessibleSymbols(newGrammar);
        eliminateNonProductiveSymbols(newGrammar);
        convertToChomskyNormalForm(newGrammar);

        return newGrammar;
    }

    private static void eliminateEpsilonProductions(Grammar grammar) {
        // Step 1: Eliminate epsilon productions
        Set<String> nullableSymbols = grammar.P.stream()
                .filter(rule -> rule.rhs.isEmpty())
                .map(rule -> rule.lhs)
                .collect(Collectors.toSet());

        List<Rule> newRules = new ArrayList<>(grammar.P);
        for (Rule rule : grammar.P) {
            List<List<String>> combinations = generateCombinations(rule.rhs, nullableSymbols);
            for (List<String> combination : combinations) {
                Rule newRule = new Rule(rule.lhs, combination);
                if (!newRules.contains(newRule) && !newRule.rhs.isEmpty()) {
                    newRules.add(newRule);
                }
            }
        }
        grammar.P = newRules;
    }

    private static List<List<String>> generateCombinations(List<String> rhs, Set<String> nullableSymbols) {
        List<List<String>> result = new ArrayList<>();
        result.add(rhs);
        for (String symbol : rhs) {
            if (nullableSymbols.contains(symbol)) {
                List<List<String>> newResult = new ArrayList<>();
                for (List<String> combination : result) {
                    List<String> newCombination = new ArrayList<>(combination);
                    newCombination.remove(symbol);
                    if (!newCombination.isEmpty()) {
                        newResult.add(newCombination);
                    }
                }
                result.addAll(newResult);
            }
        }
        return result;
    }

    private static void eliminateRenaming(Grammar grammar) {
        // Step 2: Eliminate any renaming
        Map<String, String> renaming = new HashMap<>();
        for (Rule rule : grammar.P) {
            if (rule.rhs.size() == 1 && grammar.Vn.contains(rule.rhs.get(0))) {
                renaming.put(rule.lhs, rule.rhs.get(0));
            }
        }

        List<Rule> newRules = new ArrayList<>();
        for (Rule rule : grammar.P) {
            List<String> newRhs = rule.rhs.stream()
                    .map(symbol -> renaming.getOrDefault(symbol, symbol))
                    .collect(Collectors.toList());
            if (newRhs.size() != 1 || !grammar.Vn.contains(newRhs.get(0))) {
                newRules.add(new Rule(rule.lhs, newRhs));
            }
        }
        grammar.P = newRules;
    }

    private static void eliminateInaccessibleSymbols(Grammar grammar) {
        // Step 3: Eliminate inaccessible symbols
        Set<String> accessibleSymbols = new HashSet<>();
        accessibleSymbols.add(grammar.S);
        boolean foundNewAccessible;
        do {
            foundNewAccessible = false;
            for (Rule rule : grammar.P) {
                if (accessibleSymbols.contains(rule.lhs)) {
                    for (String symbol : rule.rhs) {
                        if (grammar.Vn.contains(symbol) && accessibleSymbols.add(symbol)) {
                            foundNewAccessible = true;
                        }
                    }
                }
            }
        } while (foundNewAccessible);
        grammar.P.removeIf(rule -> !accessibleSymbols.contains(rule.lhs));
        grammar.Vn.retainAll(accessibleSymbols);
    }
    private static void eliminateNonProductiveSymbols(Grammar grammar) {
        // Step 4: Eliminate non-productive symbols
        Set<String> productiveSymbols = grammar.P.stream()
                .filter(rule -> rule.rhs.stream().allMatch(symbol -> grammar.Vt.contains(symbol)))
                .map(rule -> rule.lhs)
                .collect(Collectors.toSet());

        boolean foundNewProductive;
        do {
            foundNewProductive = false;
            for (Rule rule : grammar.P) {
                if (!productiveSymbols.contains(rule.lhs) && rule.rhs.stream().allMatch(symbol -> grammar.Vt.contains(symbol) || productiveSymbols.contains(symbol))) {
                    foundNewProductive = productiveSymbols.add(rule.lhs);
                }
            }
        } while (foundNewProductive);

        grammar.P.removeIf(rule -> !productiveSymbols.contains(rule.lhs));
        grammar.Vn.retainAll(productiveSymbols);
    }

    private static void convertToChomskyNormalForm(Grammar grammar) {
        // Step 5: Obtain the Chomsky Normal Form
        List<Rule> newRules = new ArrayList<>();
        int newVarCounter = 1;

        // Replace terminal symbols with new non-terminal symbols in rules with more than 1 symbol on the RHS
        for (Rule rule : grammar.P) {
            List<String> newRhs = new ArrayList<>();
            for (String symbol : rule.rhs) {
                if (grammar.Vt.contains(symbol) && rule.rhs.size() > 1) {
                    String newVar = "NEW" + newVarCounter++;
                    newRhs.add(newVar);
                    grammar.Vn.add(newVar);
                    newRules.add(new Rule(newVar, Collections.singletonList(symbol)));
                } else {
                    newRhs.add(symbol);
                }
            }
            rule.rhs = newRhs;
        }

        // Convert rules with more than 2 symbols on the RHS to CNF rules
        for (Rule rule : grammar.P) {
            if (rule.rhs.size() > 2) {
                String prevVar = rule.lhs;
                for (int i = 0; i < rule.rhs.size() - 2; i++) {
                    String newVar = "NEW" + newVarCounter++;
                    newRules.add(new Rule(prevVar, Arrays.asList(rule.rhs.get(i), newVar)));
                    prevVar = newVar;
                    grammar.Vn.add(newVar);
                }
                newRules.add(new Rule(prevVar, Arrays.asList(rule.rhs.get(rule.rhs.size() - 2), rule.rhs.get(rule.rhs.size() - 1))));
            } else {
                newRules.add(rule);
            }
        }

        grammar.P = newRules;
    }

}

