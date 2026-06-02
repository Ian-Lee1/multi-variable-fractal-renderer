package entity;

public class EquParenthesisComponent implements EquComponent{
    //implement as a  modified linked list, with each node's value being a value/variable/linked list, joined by some operation.
    private final EquComponent value;
    private EquComponent prev;

    public EquParenthesisComponent(EquComponent value){
        this.value = value;
        this.prev = null;
    }

    public void setPrev(EquComponent prev){
        this.prev=prev;
    }

    @Override
    public Complex solve() {
        return value.solve();
    }

    @Override
    public boolean isComplete() {
        return true;
    }

    @Override
    public String toString(){
        return ("(" + value.toString() + ")");
    }
}
