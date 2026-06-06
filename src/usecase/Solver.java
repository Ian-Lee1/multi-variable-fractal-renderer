package usecase;

import entity.Complex;
import entity.EquationError;

public interface Solver {
    void setEquation(int i, String equation) throws EquationError;
    void updateVariable(int i, Complex value);
    void addVariable();
    void removeVariable(int i);
    Complex readVariable(int i);
    int getVariableCount();
    void solve();
    String getEquation(int i);
}
