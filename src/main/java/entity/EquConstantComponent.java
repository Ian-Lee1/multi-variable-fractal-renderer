package entity;

public class EquConstantComponent implements EquComponent{

    private final Complex value;

    public EquConstantComponent(Complex value){
        this.value = value;
    }

    @Override
    public Complex solve() {
        return this.value;
    }

    @Override
    public boolean isComplete() {
        return true;
    }

    @Override
    public String toString(){
        return "c" + value;
    }
}
