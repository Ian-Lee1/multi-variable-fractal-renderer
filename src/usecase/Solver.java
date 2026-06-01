package usecase;

import entity.Complex;
import entity.EquationError;

public interface Solver {
    void setEquation(String equation) throws EquationError;
    void updateVariable(int i, double value);
    Complex solve();
}
