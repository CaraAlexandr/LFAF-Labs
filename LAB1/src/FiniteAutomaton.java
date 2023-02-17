import java.util.*;

public class FiniteAutomaton {
    private Set<String> Q;           // set of states
    private Set<Character> Sigma;   // set of input symbols
    private Map<StateSymbolPair, Set<String>> delta;   // transition function
    private String q0;              // start state
    private Set<String> F;          // set of accept states

    public FiniteAutomaton(Set<String> Q, Set<Character> Sigma,
                           Map<StateSymbolPair, Set<String>> delta,
                           String q0, Set<String> F) {
        this.Q = Q;
        this.Sigma = Sigma;
        this.delta = delta;
        this.q0 = q0;
        this.F = F;
    }

    // Check if a given input string is accepted by the FA
    public boolean stringBelongToLanguage(String inputString) {
        String currentState = q0;
        for (char c : inputString.toCharArray()) {
            Set<String> nextStates = delta.get(new StateSymbolPair(currentState, c));
            if (nextStates == null) {
                return false;
            }
            currentState = nextStates.iterator().next();
        }
        return F.contains(currentState);
    }

    // Private class to represent a pair of state and input symbol
    private class StateSymbolPair {
        private String state;
        private char symbol;

        public StateSymbolPair(String state, char symbol) {
            this.state = state;
            this.symbol = symbol;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            StateSymbolPair that = (StateSymbolPair) o;
            return symbol == that.symbol && Objects.equals(state, that.state);
        }

        @Override
        public int hashCode() {
            return Objects.hash(state, symbol);
        }
    }
}
