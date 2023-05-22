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
