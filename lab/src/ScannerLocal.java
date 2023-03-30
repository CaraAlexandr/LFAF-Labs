import java.util.List;

public class ScannerLocal    {
    public static void main(String[] args) {
        Lexer lexer = new Lexer();
        String input = "5 + (3 - 2) * 7 / 2";
        List<Token> tokens = lexer.tokenize(input);

        for (Token token : tokens) {
            System.out.println(token);
        }
    }
}
