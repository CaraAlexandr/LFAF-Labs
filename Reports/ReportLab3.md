# Lexer and Scanner for Arithmetic Expressions

### Course: Formal Languages & Finite Automata
### Author: Cara Alexandr

----

## Theory

A lexer and scanner are essential components of a compiler or interpreter, responsible for converting source code into a stream of tokens. These tokens are then processed and parsed to generate an abstract syntax tree (AST), which is used for further analysis, optimization, and code generation. In this work, we've implemented a simple lexer and scanner for arithmetic expressions.

## Objectives:

* Create a `Token` class to represent different types of tokens.
* Implement a `Lexer` class to tokenize arithmetic expressions.
* Develop a `Scanner` class to use the lexer and display the tokens.

## Implementation description

* The `Token` class represents different types of tokens that can be encountered in arithmetic expressions. It stores a token type (e.g., NUMBER, PLUS, MINUS, etc.) and a value (the text of the token).

```java
public class Token {
    public enum Type {
        NUMBER, PLUS, MINUS, MULTIPLY, DIVIDE, LPAREN, RPAREN, EOF
    }

    private Type type;
    private String value;

    public Token(Type type, String value) {
        this.type = type;
        this.value = value;
    }

    public Type getType() {
        return type;
    }

    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return String.format("%s('%s')", type.name(), value);
    }
}
```
* The `Lexer` class tokenizes the input string using a regular expression pattern. It recognizes and creates tokens for numbers and arithmetic operators, such as addition, subtraction, multiplication, and division. It also recognizes parentheses and the end of file (EOF) token.

```java 
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
```
* The `Scanner` class uses the Lexer class to tokenize a given input string and prints the tokens to the console.
  import java.util.List;
```java
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
```
# Conclusions 
The implemented lexer and scanner can successfully tokenize arithmetic expressions. The output shows a list of tokens with their types and values. This implementation serves as a basis for further development, such as parsing the tokens and performing arithmetic operations based on the parsed abstract syntax tree.