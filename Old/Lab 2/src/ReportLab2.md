<h1 align='center'> 
Laboratory work nr. 2
</h1>

<p align="center"><i>my variant : </i><b> Variant 5</b></p>

```
VN={S, F, L}, 
VT={a, b, c, d},
P={ 
    S → bS
    S → aF
    S → d
    F → cF
    F → dF
    F → aL
    L → aL
    L → c
    F → b
}

Q = {q0,q1,q2,q3},
∑ = {a,b},
F = {q3},
δ(q0,a) = q1,
δ(q0,b) = q0,
δ(q1,a) = q2,
δ(q1,a) = q3,
δ(q2,a) = q3,
δ(q2,b) = q0.
```
## Objectives:
1. Understand what an automaton is and what it can be used for.

2. Continuing the work in the same repository and the same project, the following need to be added:
   a. Provide a function in your grammar type/class that could classify the grammar based on Chomsky hierarchy.

   b. For this you can use the variant from the previous lab.

3. According to your variant number (by universal convention it is register ID), get the finite automaton definition and do the following tasks:

   a. Implement conversion of a finite automaton to a regular grammar.

   b. Determine whether your FA is deterministic or non-deterministic.

   c. Implement some functionality that would convert an NDFA to a DFA.

   d. Represent the finite automaton graphically (Optional, and can be considered as a __*bonus point*__):

    - You can use external libraries, tools or APIs to generate the figures/diagrams.

    - Your program needs to gather and send the data about the automaton and the lib/tool/API return the visual representation.

This code is a Java program that demonstrates the conversion of a regular grammar to a finite automaton, and the checking of a given string to determine if it is valid according to the finite automaton. The code is divided into three main classes: FiniteAutomaton, RegularGrammar, and Main.

# FiniteAutomaton class:
This class represents a finite automaton with its states, final states, input alphabet, transition functions, and initial state. It has several methods:
- checkWord(String word): Checks if the given word is valid according to the finite automaton by verifying if the word can be accepted by following the transitions.
- convertGrammar(): Converts the finite automaton to a regular grammar by creating a dictionary of productions for each state.
- automatonType(): Determines the type of the finite automaton (DFA or NFA) based on its transitions.
- display(): Displays the finite automaton graphically using a JFrame and JPanel.
# RegularGrammar class:
This class represents a regular grammar with its nonterminal symbols (Vn), terminal symbols (Vt), production rules (P), and alphabet. It has several methods:
- generateWord(): Generates a word according to the grammar by following the production rules.
- convertFA(): Converts the regular grammar to a finite automaton by extracting the initial states and transition functions.
- chumskyType(): Determines the Chomsky type of the grammar by examining the production rules.
- getP(): Getter method to get the production rules.
# Main class:
This class serves as the entry point of the program and demonstrates the functionality of the FiniteAutomaton and RegularGrammar classes. It does the following:
Creates a regular grammar with given Vn, Vt, P, and alphabet.
- Converts the regular grammar to a finite automaton using the convertFA() method.
- Generates 5 strings based on the regular grammar and checks if they are valid according to the finite automaton.
- Accepts a user-provided string and checks if it is valid according to the finite automaton.
- Prints the production rules of the regular grammar and its Chomsky type.

In summary, this code demonstrates the conversion between a regular grammar and a finite automaton and allows checking the validity of a given string against the finite automaton.

