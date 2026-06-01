package entity;

import usecase.Solver;

import java.util.ArrayList;

public class EquLinkedComponentSolver implements Solver {
    private String equation;
    private EquComponent equationLink;
    private ArrayList<Complex> variables;

    public EquLinkedComponentSolver(String equation){
        this.equation = equation;
        compile();
    }
    @Override
    public void setEquation(String equation) throws EquationError {
        compile();
    }

    @Override
    public void updateVariable(int i, double value) {

    }

    @Override
    public Complex solve() {
        return null;
    }

    private void compile(){

    }
}
