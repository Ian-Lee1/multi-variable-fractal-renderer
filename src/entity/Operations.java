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
            case "ADD" -> "add/+";
            case "SUB" -> "sub/-";
            case "MUL" -> "multiply/*";
            case "DIV" -> "divide/ /";
            case "POW" -> "power/^";
            default -> super.toString();
        };
    }
}
