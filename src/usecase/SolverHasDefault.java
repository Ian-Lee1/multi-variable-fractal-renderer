package usecase;

import entity.Complex;

public interface SolverHasDefault {
    void setDefault(int i, Complex value);
    void resetVariable();
    Complex getDefault(int i);
}
