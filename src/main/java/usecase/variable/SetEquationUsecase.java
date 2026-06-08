package usecase.variable;

import usecase.Solver;

public class SetEquationUsecase {
    private final Solver solver;
    public SetEquationUsecase(Solver solver){
        this.solver = solver;
    }

    public void setEquation(int i, String equ){
        solver.setEquation(i, equ);
    }

}
