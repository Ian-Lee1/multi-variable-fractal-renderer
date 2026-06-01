import entity.ComplexBasic;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
void main() {
        ComplexBasic z1 = new ComplexBasic(5,6);
    ComplexBasic z2 = new ComplexBasic(7,8);

    System.out.println(z1.pow(z2).getR());
    System.out.println(z1.pow(z2).getI());
    }

