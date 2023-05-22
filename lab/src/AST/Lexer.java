package AST;

import java.util.*;
import java.util.regex.*;

public class Lexer {
    private static final Pattern TOKEN_PATTERN = Pattern.compile("\\d+|[-+*/()]");

    public static List<Token> tokenize(String input) {
        Matcher matcher = TOKEN_PATTERN.matcher(input);
        List<Token> tokens = new ArrayList<>();

        while (matcher.find()) {
            String token = matcher.group();
            tokens.add(new Token(determineTokenType(token), token));
        }

        return tokens;
    }

    private static TokenType determineTokenType(String token) {
        switch (token) {
            case "+":
                return TokenType.PLUS;
            case "-":
                return TokenType.MINUS;
            case "*":
                return TokenType.MULTIPLY;
            case "/":
                return TokenType.DIVIDE;
            case "(":
                return TokenType.LPAREN;
            case ")":
                return TokenType.RPAREN;
            default:
                return TokenType.NUMBER;
        }
    }
}

