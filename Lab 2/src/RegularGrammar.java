import java.util.*;

public class RegularGrammar {
    private List<String> Vn;
    private List<String> Vt;
    private Map<String, List<String>> P;
    private List<String> alphabet;
    private List<List<String>> wordList;
    private String word;

    public RegularGrammar(List<String> Vn, List<String> Vt, Map<String, List<String>> P, List<String> alphabet) {
        this.Vn = Vn;
        this.Vt = Vt;
        this.P = P;
        this.alphabet = alphabet;
        this.wordList = new ArrayList<>();
        this.word = "";
    }

    public String generateWord() {
        wordList.clear();
        word = "S";
        wordList.add(Collections.singletonList(word));
        Random random = new Random();

        while (Character.isUpperCase(word.charAt(word.length() - 1))) {
            List<String> aux = new ArrayList<>();
            aux.add(Character.toString(word.charAt(word.length() - 1)));
            String chosen = P.get(Character.toString(word.charAt(word.length() - 1))).get(random.nextInt(P.get(Character.toString(word.charAt(word.length() - 1))).size()));
            word = word.substring(0, word.length() - 1) + chosen;

            if (Character.isUpperCase(word.charAt(word.length() - 1))) {
                aux.add(Character.toString(word.charAt(word.length() - 2)));
                aux.add(Character.toString(word.charAt(word.length() - 1)));
            } else {
                aux.add(Character.toString(word.charAt(word.length() - 1)));
            }
            wordList.add(aux);
        }

        wordList.remove(0);
        System.out.println("Generated word: " + word);
        System.out.println("*************");
        System.out.println("Used transitions for created word: " + wordList);
        return word;
    }

    public FiniteAutomaton convertFA() {
        List<String> initialStates = new ArrayList<>(); // List of initial states
        for (String state : P.get("S")) {
            initialStates.add(Character.toString(state.charAt(0)));
        }

        List<List<String>> transitionFunctions = new ArrayList<>(); // List of transition functions
        for (String key : P.keySet()) {
            for (String state : P.get(key)) {
                List<String> aux = new ArrayList<>();
                aux.add(key);
                aux.addAll(Arrays.asList(state.split("")));
                transitionFunctions.add(aux);
            }
        }

        // Print the valid transitions
        System.out.println("Valid transitions:");
        System.out.println(transitionFunctions);

        // Convert Vt to a Set<String>
        Set<String> VtSet = new HashSet<>(Vt);

        // Create a FiniteAutomata object with the generated initial states, transition functions, and other attributes
        FiniteAutomaton automaton = new FiniteAutomaton(String.join("", initialStates),
                VtSet, // Pass the converted Set<String> for Vt
                alphabet,
                transitionFunctions,
                Vn);
        return automaton;
    }

    public int chumskyType() {
        int chumType = 3; // Assume the regular grammar is of Chomsky type 3 by default
        for (String key : P.keySet()) {
            if (key.length() >= 2) {
                chumType = 1;
            }
            for (String state : P.get(key)) {
                if (state.isEmpty() && chumType == 1) {
                    return 0;
                }
            }
        }

        if (chumType == 1) {
            return 1;
        }

        for (String key : P.keySet()) {
            for (String state : P.get(key)) {
                if (state.chars().filter(Character::isUpperCase).count() > 1) {
                    return 2;
                }
            }
        }

        for (String key : P.keySet()) {
            int upperNumber = (int) P.get(key).get(0).chars().filter(Character::isUpperCase).count();
            if (upperNumber > 1) {
                return 2;
            }
            int upperPos = -1;
            for (int i = 0; i < P.get(key).get(0).length(); i++) {
                if (Character.isUpperCase(P.get(key).get(0).charAt(i))) {
                    upperPos = i;
                    break;
                }
            }
            if (upperPos != 0 && upperPos != -1) {
                return 2;
            }
            for (int i = 1; i < P.get(key).size(); i++) {
                if (!P.get(key).get(i).isEmpty() && !Character.isLowerCase(P.get(key).get(i).charAt(0))) {
                    if ((int) P.get(key).get(i).chars().filter(Character::isUpperCase).count() > 1) {
                        return 2;
                    }
                    int tempUpperPos = -1;
                    for (int j = 0; j < P.get(key).get(i).length(); j++) {
                        if (Character.isUpperCase(P.get(key).get(i).charAt(j))) {
                            tempUpperPos = j;
                            break;
                        }
                    }
                    if (tempUpperPos != upperPos) {
                        return 2;
                    }
                }
            }
        }
        return chumType;
    }
    public Map<String, List<String>> getP() {
        return P;
    }

}
