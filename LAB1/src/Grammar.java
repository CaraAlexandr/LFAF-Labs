import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class Grammar {
    private List<String> VN;  // Non-terminal symbols
    private List<String> VT;  // Terminal symbols
    private Map<String, List<String>> P;  // Production rules
    private String S;  // Start symbol

    public Grammar(List<String> VN, List<String> VT, Map<String, List<String>> P, String S) {
        this.VN = VN;
        this.VT = VT;
        this.P = P;
        this.S = S;
    }

    public String generateString(int maxLength) {
        Random random = new Random();
        StringBuilder sb = new StringBuilder();

        // Start with the start symbol
        String symbol = S;

        int length = 0;
        while (length < maxLength) {
            List<String> productions = P.get(symbol);
            if (productions == null) {
                break;  // Symbol is a terminal symbol
            }

            String production = productions.get(random.nextInt(productions.size()));
            for (int i = 0; i < production.length() && length < maxLength; i++) {
                char c = production.charAt(i);
                if (VT.contains(Character.toString(c))) {
                    sb.append(c);
                    length++;
                } else {
                    symbol = Character.toString(c);
                }
            }
        }

        return sb.toString();
    }

    public FiniteAutomaton toFiniteAutomaton() {
        // Implement conversion from grammar to finite automaton here
        return null;
    }
}
