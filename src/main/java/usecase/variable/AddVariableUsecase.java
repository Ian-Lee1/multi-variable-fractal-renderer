package usecase.variable;

import presenter.VariablePresenter;
import usecase.Solver;

public class AddVariableUsecase {
    private final Solver solver;
    private final Solver colorSolver;
    private final VariablePresenter presenter;
    public AddVariableUsecase(Solver solver, VariablePresenter presenter, Solver colorSolver){
    this.solver = solver;
    this.presenter = presenter;
    this.colorSolver = colorSolver;
    }

    public void addVariable(){
        if (solver.getVariableCount() == colorSolver.getVariableCount())
            colorSolver.addVariable();
        solver.addVariable();

        presenter.addVariable();
    }
}
