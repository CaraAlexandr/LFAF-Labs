import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Main {
    public static void main(String[] args) {
        List<String> VN = List.of("S", "F", "L");
        List<String> VT = List.of("a", "b", "c", "d");
        Map<String, List<String>> P = new HashMap<>();
        P.put("S", List.of("bS", "aF", "d"));
        P.put("F", List.of("cF", "dF", "aL", "b"));
        P.put("L", List.of("aL", "c"));
        String S = "S";

        Grammar grammar = new Grammar(VN, VT, P, S);

        for (int i = 0; i < 5; i++) {
            String str = grammar.generateString();
            System.out.println(str);
        }
    }

}