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
