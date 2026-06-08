package entity;

public enum Operations {
    ADD,
    SUB,
    MUL,
    DIV,
    POW;
    @Override
    public String toString() {
        return switch (name()) {
            case "ADD" -> "+";
            case "SUB" -> "-";
            case "MUL" -> "*";
            case "DIV" -> "/";
            case "POW" -> "^";
            default -> super.toString();
        };
    }
}
