package entity;

public enum Functions {
    SIN,
    COS,
    TAN,
    LEN,
    LEN2,
    SQRT,
    GETR,
    GETI,
    INT,
    NONE;
    @Override
    public String toString() {
        return switch (name()) {
            case "SIN" -> "sin";
            case "COS" -> "cos";
            case "TAN" -> "tan";
            case "LEN" -> "len";
            case "LEN2" -> "len2";
            case "SQRT" -> "sqr";
            case "GETR" -> "getR";
            case "GETI" -> "getI";
            case "INT" -> "int";
            case "NONE" -> "";
            default -> super.toString();
        };
    }
}
