import java.util.*;
import javax.swing.*;
import java.awt.*;
import java.util.List;


public class FiniteAutomaton {
    private String q0;
    private Set<String> F;
    private List<String> sigma;
    private List<List<String>> delta;
    private static List<String> Q;

    public FiniteAutomaton(String q0, Set<String> F, List<String> sigma, List<List<String>> delta, List<String> Q) {
        this.q0 = q0;
        this.F = F;
        this.sigma = sigma;
        this.delta = delta;
        this.Q = Q;
    }

    public boolean checkWord(String word) {
        if (!q0.contains(Character.toString(word.charAt(0)))) {
            return false;
        }

        if (!F.contains(Character.toString(word.charAt(word.length() - 1)))) {
            return false;
        }

        for (char letter : word.toCharArray()) {
            if (!sigma.contains(Character.toString(letter))) {
                return false;
            }
        }

        List<List<String>> transitions = new ArrayList<>();
        for (char letter : word.toCharArray()) {
            transitions.add(new ArrayList<>(Arrays.asList("", Character.toString(letter), "")));
        }

        transitions.get(0).set(0, "S");
        transitions.get(transitions.size() - 1).remove(transitions.get(transitions.size() - 1).size() - 1);

        for (int i = 0; i < transitions.size() - 1; i++) {
            for (List<String> state : delta) {
                if (transitions.get(i).get(0).equals(state.get(0)) && transitions.get(i).get(1).equals(state.get(1))) {
                    transitions.get(i).set(2, state.get(2));
                    transitions.get(i + 1).set(0, state.get(2));
                    break;
                }
            }
            if (transitions.get(i).get(transitions.get(i).size() - 1).equals("")) {
                return false;
            }
        }

        System.out.println("Transitions tried by the automaton: " + transitions);
        return true;
    }




    public RegularGrammar convertGrammar() {
        Map<String, List<String>> p = new HashMap<>();
        for (String key : Q) {
            List<String> finals = new ArrayList<>();
            for (List<String> transition : delta) {
                if (transition.get(0).equals(key)) {
                    StringBuilder str = new StringBuilder();
                    for (int i = 1; i < transition.size(); i++) {
                        str.append(transition.get(i));
                    }
                    finals.add(str.toString());
                }
            }
            p.put(key, finals);
        }

        // Creating the regular grammar from the dictionary of productions
        RegularGrammar regGram = new RegularGrammar(Q, (List<String>) F, p, sigma);

        return regGram;
    }

    public String automatonType() {
        for (String letter : Q) {
            List<String> inputs = new ArrayList<>();
            for (List<String> transition : delta) {
                if (transition.get(0).equals(letter)) {
                    if (inputs.contains(transition.get(1))) {
                        return "NFA";
                    }
                    inputs.add(transition.get(1));
                }
            }

            if (!inputs.containsAll(sigma) || !sigma.containsAll(inputs)) {
                return "NFA";
            }
        }

        return "DFA";
    }


    public void display() {
        JFrame frame = new JFrame("Finite Automaton");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);

        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);

                int nodeRadius = 30;
                int centerX = getWidth() / 2;
                int centerY = getHeight() / 2;
                int automatonSize = Q.size();

                // Draw nodes
                for (int i = 0; i < automatonSize; i++) {
                    double angle = 2 * Math.PI * i / automatonSize;
                    int x = centerX + (int) (200 * Math.cos(angle)) - nodeRadius / 2;
                    int y = centerY + (int) (200 * Math.sin(angle)) - nodeRadius / 2;

                    if (F.contains(Q.get(i))) {
                        g.setColor(Color.RED);
                    } else {
                        g.setColor(Color.BLUE);
                    }

                    g.fillOval(x, y, nodeRadius, nodeRadius);
                    g.setColor(Color.BLACK);
                    g.drawString(Q.get(i), x + nodeRadius / 4, y + (3 * nodeRadius) / 4);
                }

                // Draw edges (simple lines without labels for simplicity)
                g.setColor(Color.BLACK);
                for (List<String> transition : delta) {
                    int fromIndex = Q.indexOf(transition.get(0));
                    int toIndex = Q.indexOf(transition.get(2));

                    double angleFrom = 2 * Math.PI * fromIndex / automatonSize;
                    double angleTo = 2 * Math.PI * toIndex / automatonSize;

                    int x1 = centerX + (int) (200 * Math.cos(angleFrom));
                    int y1 = centerY + (int) (200 * Math.sin(angleFrom));
                    int x2 = centerX + (int) (200 * Math.cos(angleTo));
                    int y2 = centerY + (int) (200 * Math.sin(angleTo));

                    g.drawLine(x1, y1, x2, y2);
                }
            }
        };

        frame.add(panel);
        frame.setVisible(true);
    }
}

