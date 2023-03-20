import java.util.*;

public class Main {
    public static void main(String[] args) {
        List<String> vn = Arrays.asList("S", "F", "L");
        List<String> vt = Arrays.asList("a", "b", "c", "d");
        Map<String, List<String>> p = new HashMap<>();
        p.put("S", Arrays.asList("bS", "aF", "d"));
        p.put("F", Arrays.asList("cF", "dF", "aL", "b"));
        p.put("L", Arrays.asList("aL", "c"));
        List<String> alphabet = vt; // You can use vt as the alphabet

        System.out.println("---------------------");
        RegularGrammar newGrammar = new RegularGrammar(vn, vt, p, alphabet);
        FiniteAutomaton newAutomaton = newGrammar.convertFA();


        System.out.println("---------------------");
        Scanner scanner = new Scanner(System.in);
        System.out.print("Generate 5 strings (y/n): ");
        String userInput = scanner.nextLine();
        if (userInput.toLowerCase().equals("y")) {
            System.out.println();
            System.out.println("Generating 5 strings");
            System.out.println("--------------------------------------------------");
            for (int i = 0; i < 5; i++) {
                String word = newGrammar.generateWord();
                if (newAutomaton.checkWord(word)) {
                    System.out.println("Valid word");
                }
                System.out.println("--------------------------------------------------");
            }
        }

        System.out.print("Test a string (y/n): ");
        userInput = scanner.nextLine();
        if (userInput.toLowerCase().equals("y")) {
            System.out.print("Your string: ");
            String userWord = scanner.nextLine();

            System.out.println();
            if (newAutomaton.checkWord(userWord)) {
                System.out.println("!!!   user word is valid  !!!");
            } else {
                System.out.println("!!!  user word not valid  !!!");
            }
            System.out.println();
            System.out.println("---------------------");
            System.out.println();
        }

        for (String key : newGrammar.getP().keySet()) {
            System.out.printf("%s -> %s%n", key, newGrammar.getP().get(key));
        }
        System.out.println("---------------------");
        System.out.printf("grammar type: %s%n", newGrammar.chumskyType());
        System.out.println("---------------------");

        //implement automatonType method in FiniteAutomaton class
        System.out.println(newAutomaton.automatonType());
        System.out.println("---------------------");






    }
}
