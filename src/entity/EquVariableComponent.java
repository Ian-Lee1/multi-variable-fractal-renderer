package entity;

import java.util.List;

public class EquVariableComponent implements EquComponent{
    private final List<Complex> variables;
    private final int index;
    public EquVariableComponent(List<Complex> variables, int index){
        this.variables = variables;
        this.index = index;
    }

    @Override
    public Complex solve() {
        try {
            return variables.get(index);
        } catch (IndexOutOfBoundsException _) {
            throw new EquationError("Variable " + index + " is not defined.");
        }
    }

    @Override
    public boolean isComplete() {
        return true;
    }
}
