package entity;

public class ComplexBasic implements Complex{

    public final double r;
    public final double i;

    public ComplexBasic(double r, double i){
        this.r = r;
        this.i = i;
    }
    @Override
    public Complex add(Complex x) {
        return new ComplexBasic(this.r + x.getR(), this.i + x.getI());
    }
    @Override
    public Complex sub(Complex x) {
        return new ComplexBasic(this.r - x.getR(), this.i - x.getI());
    }

    @Override
    public Complex mul(Complex x) {
        double real = this.r * x.getR() - this.i * x.getI();
        double imag = this.r * x.getI() + this.i * x.getR();
        return new ComplexBasic(real, imag);
    }

    @Override
    public Complex div(Complex x) {
        // (a + bi) / (c + di) = [(ac + bd) / (c^2 + d^2)] + [(bc - ad) / (c^2 + d^2)]i
        double denominator = x.getR() * x.getR() + x.getI() * x.getI();
        if (denominator == 0) {
            throw new EquationError("Division by zero in complex division");
        }
        double real = (this.r * x.getR() + this.i * x.getI()) / denominator;
        double imag = (this.i * x.getR() - this.r * x.getI()) / denominator;
        return new ComplexBasic(real, imag);
    }

    @Override
    public Complex pow(Complex x) {
        // Convert this complex number to polar form
        double modulus = Math.sqrt(this.r * this.r + this.i * this.i);
        double logI = Math.atan2(this.i, this.r);

        if (modulus == 0.0)
            return new ComplexBasic(0,0);
        // Natural log of this complex number: ln(z) = ln|z| + i*arg
        double logR = Math.log(modulus);

        // Multiply by the exponent (x = c + di)
        double a = x.getR();
        double b = x.getI();

        double expR = logR * a - logI * b;
        double expI = logR * b + logI * a;

        // Exponentiate: e^(expR + i*expI) = e^expR * (cos(expI) + i*sin(expI))
        double real = Math.exp(expR) * Math.cos(expI);
        double imag = Math.exp(expR) * Math.sin(expI);

        return new ComplexBasic(real, imag);
    }

    @Override
    public double getR() {
        return r;
    }
    @Override
    public double getI() {
        return i;
    }

    @Override
    public double lengthSq(){
        return r*r + i*i;
    }

    @Override
    public Complex sin() {
        // sin(a + bi) = sin(a)cosh(b) + i cos(a)sinh(b)
        double real = Math.sin(this.r) * Math.cosh(this.i);
        double imag = Math.cos(this.r) * Math.sinh(this.i);
        return new ComplexBasic(real, imag);
    }

    @Override
    public Complex cos() {
        // cos(a + bi) = cos(a)cosh(b) - i sin(a)sinh(b)
        double real = Math.cos(this.r) * Math.cosh(this.i);
        double imag = -Math.sin(this.r) * Math.sinh(this.i);
        return new ComplexBasic(real, imag);
    }

    @Override
    public Complex tan(){
        return sin().div(cos());
    }

    @Override
    public Complex sqrt() {
        double modulus = Math.sqrt(this.r * this.r + this.i * this.i);

        double real = Math.sqrt((modulus + this.r) / 2.0);
        double imag = Math.sqrt((modulus - this.r) / 2.0);

        // Preserve the sign of the imaginary part
        if (this.i < 0) {
            imag = -imag;
        }

        return new ComplexBasic(real, imag);
    }

    @Override
    public String toString(){
        return "(" + r + "," + i + "i" + ")";
    }

}
