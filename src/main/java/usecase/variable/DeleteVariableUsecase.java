package usecase.variable;

import presenter.VariablePresenter;
import usecase.Solver;

public class DeleteVariableUsecase {
    private final Solver solver;
    private final Solver colorSolver;
    private final VariablePresenter presenter;
    public DeleteVariableUsecase(Solver solver, Solver colorSolver, VariablePresenter presenter){
        this.solver = solver;
        this.colorSolver = colorSolver;
        this.presenter = presenter;
    }

    public void delete(int i){
        solver.removeVariable(i);
        if (colorSolver.getVariableCount() > 3)
            colorSolver.removeVariable(3);
        presenter.removeVariable();
    }
}
