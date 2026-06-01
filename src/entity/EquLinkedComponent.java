package entity;

import java.util.ArrayList;

public class EquLinkedComponent implements EquComponent{
    //(5*x)+y*z

    // block   curr val->  block 5*x  op: + -> y -> z

    //implement as a  modified linked list, with each node's value being a value/variable/linked list, joined by some operation.
    private final ArrayList<Complex> variables;
    private final EquComponent value;
    private EquComponent next;
    private final Operations op;

    public EquLinkedComponent(ArrayList<Complex> variables, EquComponent value, Operations op){
        this.variables = variables;
        this.value = value;
        this.op = op;
        this.next = null;
    }

    public void setNext(EquComponent next){
        this.next = next;
    }
    @Override
    public Complex solve() throws EquationError {
        Complex selfValue = this.value.solve();
        Complex nextValue = this.next.solve();

        switch(this.op){
            case Operations.ADD -> {
                return selfValue.add(nextValue);
            }
            case Operations.SUB -> {
                return selfValue.sub(nextValue);
            }
            case Operations.MUL -> {
                return selfValue.mul(nextValue);
            }
            case Operations.DIV -> {
                return selfValue.div(nextValue);
            }
            case Operations.POW -> {
                return selfValue.pow(nextValue);
            }
        }
        throw new EquationError("Operation " + this.op + " has not been implemented.");
    }

}
