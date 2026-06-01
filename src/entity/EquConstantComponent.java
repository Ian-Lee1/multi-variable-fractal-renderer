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

}
