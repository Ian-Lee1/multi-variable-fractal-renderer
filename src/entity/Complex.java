package entity;

public interface Complex {
    Complex add(Complex x);
    Complex sub(Complex x);
    Complex mul(Complex x);
    Complex div(Complex x);
    Complex pow(Complex x);
    double getR();
    double getI();
}
