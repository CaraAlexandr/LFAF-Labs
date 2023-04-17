# Chomsky Normal Form

**Course:** Formal Languages & Finite Automata  
**Author:** Cara Alexandr

## Theory

Chomsky Normal Form (CNF) is a simplified form of context-free grammars that is useful in both the study and the development of algorithms for parsing and other language-processing tasks. A context-free grammar is said to be in Chomsky Normal Form if all its production rules are in one of the following two forms:

1. A -> BC, where A, B, and C are non-terminal symbols.
2. A -> a, where A is a non-terminal symbol, and a is a terminal symbol. 

The main advantage of CNF is its simplicity, which makes it easier to develop algorithms that work with context-free grammars. Any context-free grammar can be converted into an equivalent grammar in Chomsky Normal Form. The conversion process involves the following steps:

1. Eliminate ε-productions: Replace any production rule of the form A -> ε with alternative productions that generate the same language without the ε-production.
2. Eliminate renaming (unit productions): Remove production rules of the form A -> B, where A and B are non-terminal symbols, and substitute the production rules for B in place of A.
3. Eliminate inaccessible symbols: Remove any non-terminal symbols that cannot be reached from the start symbol in the grammar.
4. Eliminate non-productive symbols: Remove any non-terminal symbols that cannot derive any terminal strings.
5. Convert remaining rules to CNF: Break down production rules with more than two symbols on the right-hand side into multiple rules that conform to the CNF format.

By following these steps, we can transform any context-free grammar into an equivalent grammar in Chomsky Normal Form without altering the language it generates.

## Objectives

1. Implement a method for normalizing an input grammar by the rules of CNF (Chomsky Normal Form).
2. Encapsulate the implementation in a method with an appropriate signature (also ideally in an appropriate class/type).
3. Execute and test the implemented functionality.
4. (Bonus) Create unit tests that validate the functionality of the project.
5. (Bonus) Make the function accept any grammar, not only the one from the student's variant.

## Implementation description

### Eliminate Epsilon Productions

The `eliminateEpsilonProductions` method is responsible for removing ε-productions (rules of the form A -> ε) from the grammar. It identifies all non-terminal symbols that generate ε directly or indirectly and substitutes them in all other production rules, effectively removing the need for ε-productions.
```java
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
```

### Generate Combinations of Nullable Symbols

The generateCombinations method is a helper function that generates all possible combinations of the right-hand side (RHS) of a production rule with nullable symbols removed. This method is used during the elimination of epsilon productions in the grammar.

It takes a list of strings representing the RHS of a production rule and a set of nullable symbols as its input. The method initializes the result with the original RHS, and then iterates through each symbol in the RHS. If the symbol is nullable, it creates a new set of combinations with that symbol removed from the existing combinations. It ensures that the new combination is not empty before adding it to the result.

Finally, the method returns the list of all possible combinations of the RHS without the nullable symbols.



```java
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
```

### Eliminate Renaming
The `eliminateRenaming` method removes unit productions (rules of the form A -> B, where A and B are non-terminal symbols) from the grammar. It does so by replacing the unit production with all the production rules of the referenced non-terminal symbol. This process is repeated until all unit productions are eliminated.

```java
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
```
### Eliminate Inaccessible Symbols

The `eliminateInaccessibleSymbols` method removes non-terminal symbols that are not reachable from the start symbol of the grammar. It starts with the start symbol and iteratively finds all non-terminal symbols reachable from it. Then, it removes any production rules containing non-reachable symbols.

```java
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
```

### Eliminate Non-Productive Symbols

The `eliminateNonProductiveSymbols` method removes non-terminal symbols that cannot derive any terminal strings. It first identifies all non-productive symbols and then removes any production rules containing them. This step ensures that every non-terminal symbol in the grammar can derive at least one terminal string.

```java
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
```

### Convert to Chomsky Normal Form
The `convertToChomskyNormalForm` method converts the remaining production rules to the CNF format. It does so by breaking down rules with more than two symbols on the right-hand side into multiple rules that conform to CNF. Additionally, it introduces new non-terminal symbols for terminal symbols within rules containing more than one symbol on the right-hand side.

```java
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
```

These methods, when executed in sequence, transform the input grammar into an equivalent grammar in Chomsky Normal Form.

# Results:
```
Normalized Grammar (Chomsky Normal Form):
NEW1 -> d
NEW2 -> d
NEW3 -> a
NEW4 -> d
NEW5 -> a
NEW6 -> d
NEW7 -> d
NEW8 -> a
NEW9 -> d
NEW10 -> a
S -> NEW1B
A -> d
A -> NEW2A
A -> NEW3NEW11
NEW11 -> BNEW12
NEW12 -> NEW4B
B -> a
B -> NEW5A
B -> AC
S -> NEW6B
A -> d
A -> NEW7A
A -> NEW8NEW13
NEW13 -> BNEW14
NEW14 -> NEW9B
B -> a
B -> NEW10A
B -> AC
```

# Conclusions
In this project, we have successfully implemented a method for normalizing an input grammar by the rules of Chomsky Normal Form (CNF). We have addressed each step of the conversion process, including eliminating ε-productions, unit productions, inaccessible symbols, and non-productive symbols. Finally, we have converted the remaining production rules to CNF.

The implementation is modular, with each step encapsulated in a separate method. This approach not only makes the code more maintainable but also allows for easier testing and modification if needed. The Chomsky Normal Form is a powerful tool in the study of formal languages and automata, as it simplifies the grammar structure, making it easier to develop algorithms for parsing and language processing tasks.

As a result of this project, we have gained a deeper understanding of context-free grammars, their properties, and the process of converting them to Chomsky Normal Form. This knowledge will be valuable in future projects and studies related to formal languages and automata theory.