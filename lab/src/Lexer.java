import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Lexer {
    private static final Pattern TOKEN_PATTERN = Pattern.compile(
            "\\s*(=>|\\{|\\}|\\[|\\]|;|,|\\d+|[\\-+\\*/()]|\\w+)\\s*"
    );

    public List<Token> tokenize(String input) {
        List<Token> tokens = new ArrayList<>();
        Matcher matcher = TOKEN_PATTERN.matcher(input);

        while (matcher.find()) {
            String token = matcher.group().trim();

            switch (token) {
                case "(" -> tokens.add(new Token(Token.Type.LPAREN, token));
                case ")" -> tokens.add(new Token(Token.Type.RPAREN, token));
                case "+" -> tokens.add(new Token(Token.Type.PLUS, token));
                case "-" -> tokens.add(new Token(Token.Type.MINUS, token));
                case "*" -> tokens.add(new Token(Token.Type.MULTIPLY, token));
                case "/" -> tokens.add(new Token(Token.Type.DIVIDE, token));
                default -> {
                    if (token.matches("\\d+")) {
                        tokens.add(new Token(Token.Type.NUMBER, token));
                    }
                }
            }
        }

        tokens.add(new Token(Token.Type.EOF, ""));
        return tokens;
    }
}
