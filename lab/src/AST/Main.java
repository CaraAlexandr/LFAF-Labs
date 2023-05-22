package AST;

import java.util.List;

public class Main {
        public static void main(String[] args) {
            String input = "2+3*6-5";
            List<Token> tokens = Lexer.tokenize(input);
            Parser parser = new Parser(tokens);
            Node ast = parser.parse();
            // You now have the AST for the input expression
            System.out.println(ast);

            // Evaluate the AST
            double result = ast.evaluate();
            System.out.println(result);
        }
    }
