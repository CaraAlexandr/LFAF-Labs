import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Stack;

public class Grammar {
    private Set<String> V_n;
    private Set<String> V_t;
    private Map<String, List<String>> P;
    private String S;

    public Grammar(Set<String> V_n, Set<String> V_t, Map<String, List<String>> P, String S) {
        this.V_n = V_n;
        this.V_t = V_t;
        this.P = P;
        this.S = S;
    }

    public String generateString() {
        StringBuilder sb = new StringBuilder();
        Stack<String> stack = new Stack();
        stack.push(this.S);

        while(true) {
            while(!stack.isEmpty()) {
                String curr = (String)stack.pop();
                if (this.V_t.contains(curr)) {
                    sb.append(curr);
                } else {
                    List<String> productions = (List)this.P.get(curr);
                    int rand = (int)(Math.random() * (double)productions.size());
                    String prod = (String)productions.get(rand);

                    for(int i = prod.length() - 1; i >= 0; --i) {
                        stack.push(Character.toString(prod.charAt(i)));
                    }
                }
            }

            return sb.reverse().toString();
        }
    }

    public FiniteAutomaton toFiniteAutomaton() {
        Set<String> Q = new HashSet();
        Set<String> Sigma = this.V_t;
        Map<String, Map<String, String>> delta = new HashMap();
        String q0 = this.S;
        Set<String> F = new HashSet();
        Iterator var6 = this.V_n.iterator();

        String symbol;
        while(var6.hasNext()) {
            symbol = (String)var6.next();
            Q.add(symbol);
        }

        var6 = this.V_t.iterator();

        while(var6.hasNext()) {
            symbol = (String)var6.next();
            Q.add(symbol + "'");
            delta.put(symbol + "'", new HashMap());
            ((Map)delta.get(symbol + "'")).put(symbol, symbol);
        }

        var6 = this.P.entrySet().iterator();

        label43:
        while(var6.hasNext()) {
            Map.Entry<String, List<String>> entry = (Map.Entry)var6.next();
            String from = (String)entry.getKey();
            List<String> toList = (List)entry.getValue();
            Iterator var10 = toList.iterator();

            while(true) {
                while(true) {
                    if (!var10.hasNext()) {
                        continue label43;
                    }

                    String to = (String)var10.next();
                    if (to.equals("ε")) {
                        F.add(from);
                    } else {
                        for(int i = 0; i < to.length(); ++i) {
                            String curr = Character.toString(to.charAt(i));
                            String next;
                            if (i == to.length() - 1) {
                                next = from;
                            } else {
                                next = curr + "'";
                            }

                            delta.putIfAbsent(curr, new HashMap());
                            ((Map)delta.get(curr)).put(next, next);
                            Q.add(next);
                        }
                    }
                }
            }
        }

        return new FiniteAutomaton(Q, Sigma, delta, q0, F);
    }
}
