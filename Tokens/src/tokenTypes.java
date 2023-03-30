import java.util.HashMap;
import java.util.Map;

public class tokenTypes {
    public static final String ILLEGAL = "ILLEGAL";
    public static final String EOF = "EOF";

    public static final String IDENT = "IDENT";
    public static final String INT = "INT";

    public static final String ASSIGN = "=";

    public static final String COMMA = ",";
    public static final String DOT = ".";
    public static final String SEMICOLON = ";";
    public static final String LBRACE = "{";
    public static final String RBRACE = "}";
    public static final String COLON = ":";

    public static final String PLACE = "PLACE";
    public static final String TRAN = "TRAN";

    private static final Map<String, String> keywords;

    static {
        keywords = new HashMap<>();
        keywords.put("place", PLACE);
        keywords.put("tran", TRAN);
    }

    public static String lookupIdent(String ident) {
        return keywords.getOrDefault(ident, IDENT);
    }
}
