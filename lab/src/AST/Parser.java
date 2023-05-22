package AST;

import java.util.*;

public class Parser {
    private static final Map<TokenType, Integer> PRECEDENCE = new HashMap<>();
    static {
        PRECEDENCE.put(TokenType.PLUS, 1);
        PRECEDENCE.put(TokenType.MINUS, 1);
        PRECEDENCE.put(TokenType.MULTIPLY, 2);
        PRECEDENCE.put(TokenType.DIVIDE, 2);
    }

    private List<Token> tokens;
    private int index;


    public Parser(List<Token> tokens) {
        this.tokens = tokens;
    }

    public Node parse() {
        return expression();
    }

    private Node expression() {
        Node node = term();

        while (true) {
            Token token = peek();
            if (token == null || (token.getType() != TokenType.PLUS && token.getType() != TokenType.MINUS)) {
                break;
            }
            token = consume();

            Node rightNode = term();
            node = new Node(token, node, rightNode);
        }

        return node;
    }

    private Node term() {
        Node node = factor();

        while (true) {
            Token token = peek();
            if (token == null || (token.getType() != TokenType.MULTIPLY && token.getType() != TokenType.DIVIDE)) {
                break;
            }
            token = consume();

            Node rightNode = factor();
            node = new Node(token, node, rightNode);
        }

        return node;
    }

    private Node factor() {
        Token token = consume();

        if (token.getType() == TokenType.NUMBER) {
            return new Node(token);
        }

        if (token.getType() == TokenType.LPAREN) {
            Node node = expression();
            token = consume();
            if (token.getType() != TokenType.RPAREN) {
                throw new RuntimeException("Expected )");
            }
            return node;
        }

        throw new RuntimeException("Unexpected token: " + token.getValue());
    }

    private Token consume() {
        return tokens.get(index++);
    }

    private Token peek() {
        if (index == tokens.size()) {
            return null;
        }
        return tokens.get(index);
    }
}

