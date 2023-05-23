# Title: Building an Abstract Syntax Tree (AST) for Basic Mathematical Expressions

## Course: Formal Languages & Finite Automata

## Author: Cara Alexandr
# Theory
In the process of syntactical analysis, parsing plays a vital role in understanding the structure of the input text. One approach is to construct an Abstract Syntax Tree (AST), which represents the hierarchical structure of the text. ASTs are commonly used in the analysis of programs and various compilation processes.

# Objectives
1. Get familiar with parsing and how it can be implemented.
2. Understand the concept of an Abstract Syntax Tree (AST).
3. Implement the necessary data structures for an AST that can be used to represent basic mathematical expressions.
4. Develop a simple parser program that can extract the syntactic information from the input text.
5. Implementation Description

## Lexer:
```java
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


```
The Lexer class tokenizes the input mathematical expression using regular expressions. It categorizes the tokens based on their types (number, operators, parentheses) using the TokenType enum.

## Parser:
```java
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


```
The Parser class constructs an Abstract Syntax Tree (AST) for the mathematical expression. It uses recursive descent parsing and handles operator precedence to build the AST based on the grammar rules. The expression, term, and factor methods correspond to different levels of the grammar.

## Node:
```java
package AST;

public class Node {
    private Token token;
    private Node left;
    private Node right;

    public Node(Token token) {
        this.token = token;
    }

    public Node(Token token, Node left, Node right) {
        this.token = token;
        this.left = left;
        this.right = right;
    }

    public Token getToken() {
        return token;
    }

    public Node getLeft() {
        return left;
    }

    public Node getRight() {
        return right;
    }

    @Override
    public String toString() {
        if (left == null && right == null) {
            return token.getValue();
        } else {
            return "(" + left.toString() + " " + token.getValue() + " " + right.toString() + ")";
        }
    }

    public double evaluate() {
        switch (token.getType()) {
            case NUMBER:
                return Double.parseDouble(token.getValue());
            case PLUS:
                return getLeft().evaluate() + getRight().evaluate();
            case MINUS:
                return getLeft().evaluate() - getRight().evaluate();
            case MULTIPLY:
                return getLeft().evaluate() * getRight().evaluate();
            case DIVIDE:
                return getLeft().evaluate() / getRight().evaluate();
            default:
                throw new IllegalStateException("Invalid token type for evaluation: " + token.getType());
        }
    }
}

```
The Node class represents a node in the AST. Each node stores a Token object and references to its left and right child nodes, if applicable. The class also includes an evaluate method to calculate the result of the mathematical expression represented by the AST.

## Token:
```java
package AST;

public class Token {
    private TokenType type;
    private String value;

    public Token(TokenType type, String value) {
        this.type = type;
        this.value = value;
    }

    public TokenType getType() {
        return type;
    }

    public String getValue() {
        return value;
    }
}
```
The Token class represents a token in the mathematical expression. It stores the token type and value.

## TokenType:
```java
package AST;

public enum TokenType {
    NUMBER,
    PLUS,
    MINUS,
    MULTIPLY,
    DIVIDE,
    LPAREN,
    RPAREN
}
```
The TokenType enum represents the different types of tokens in the mathematical expression.

## Main:
```java
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
```

## Results:
((2 + (3 * 6)) - 5)
15.0

# Conclusion:
In this project, I have implemented a simple calculator using the Abstract Syntax Tree (AST) data structure. The calculator can handle basic arithmetic operations (+, -, *, /) and parentheses. The calculator is implemented in Java and uses regular expressions to tokenize the input mathematical expression. It then uses recursive descent parsing to construct an AST for the expression. Finally, it evaluates the AST to calculate the result of the expression.
