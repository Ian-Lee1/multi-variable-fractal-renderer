package usecase.variable;

import presenter.VariablePresenter;
import usecase.Solver;

public class AddVariableUsecase {
    private final Solver solver;
    private final VariablePresenter presenter;
    public AddVariableUsecase(Solver solver, VariablePresenter presenter){
    this.solver = solver;
    this.presenter = presenter;
    }

    public void addVariable(){
        solver.addVariable();
        presenter.addVariable();
    }
}
