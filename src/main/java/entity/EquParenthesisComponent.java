package entity;

public class EquParenthesisComponent implements EquComponent{
    //implement as a  modified linked list, with each node's value being a value/variable/linked list, joined by some operation.
    private final EquComponent value;
    private EquComponent prev;
    private Functions func;

    public EquParenthesisComponent(EquComponent value, Functions func){
        this.value = value;
        this.prev = null;
        this.func = func;
    }

    public void setPrev(EquComponent prev){
        this.prev=prev;
    }

    @Override
    public Complex solve() {
        Complex temp = value.solve();

        switch (func){
            case Functions.SIN -> {
                return temp.sin();
            }
            case Functions.COS -> {
                return temp.cos();
            }
            case Functions.TAN -> {
                return temp.tan();
            }
            case Functions.LEN -> {
                return new ComplexBasic(Math.sqrt(temp.lengthSq()), 0);
            }
            case Functions.LEN2 -> {
                return new ComplexBasic(temp.lengthSq(), 0);
            }
            case Functions.SQRT -> {
                return temp.sqrt();
            }
            case Functions.GETR -> {
                return new ComplexBasic(temp.getR(), 0);
            }
            case Functions.GETI -> {
                return new ComplexBasic(temp.getI(), 0);
            }
            case Functions.INT -> {
                return new ComplexBasic(Math.floor(temp.getR()), Math.floor(temp.getI()));
            }
            case Functions.NONE -> {
                return temp;
            }
        }
        return temp;
    }

    @Override
    public boolean isComplete() {
        return true;
    }

    @Override
    public String toString(){
        return (func.toString() + "(" + value.toString() + ")");
    }
}
