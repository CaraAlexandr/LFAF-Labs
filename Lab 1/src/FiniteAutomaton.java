import java.util.Map;
import java.util.Set;

public class FiniteAutomaton {
    private Set<String> Q;
    private Set<String> Sigma;
    private Map<String, Map<String, String>> delta;
    private String q0;
    private Set<String> F;

    public FiniteAutomaton(Set<String> Q, Set<String> Sigma, Map<String, Map<String, String>> delta, String q0, Set<String> F) {
        this.Q = Q;
        this.Sigma = Sigma;
        this.delta = delta;
        this.q0 = q0;
        this.F = F;
    }

    public boolean stringBelongToLanguage(String inputString) {
        String curr = this.q0;

        for(int i = 0; i < inputString.length(); ++i) {
            String symbol = Character.toString(inputString.charAt(i));
            if (!this.Sigma.contains(symbol)) {
                return false;
            }

            curr = (String)((Map)this.delta.get(symbol)).get(curr);
            if (curr == null) {
                return false;
            }
        }

        return this.F.contains(curr);
    }
}
