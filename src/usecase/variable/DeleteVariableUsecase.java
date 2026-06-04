package usecase.variable;

import presenter.VariablePresenter;
import usecase.Solver;

public class DeleteVariableUsecase {
    private final Solver solver;
    private final VariablePresenter presenter;
    public DeleteVariableUsecase(Solver solver, VariablePresenter presenter){
        this.solver = solver;
        this.presenter = presenter;
    }

    public void delete(int i){
        solver.removeVariable(i);
        presenter.removeVariable();
    }
}
