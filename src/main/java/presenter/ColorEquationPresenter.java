package presenter;

import usecase.Solver;

import javax.swing.*;

public class ColorEquationPresenter {
    private JTextField REquation;
    private JTextField GEquation;
    private JTextField BEquation;

    private Solver colorSolver;

    public ColorEquationPresenter(JTextField r, JTextField g, JTextField b, Solver colorSolver){
        this.REquation = r;
        this.GEquation = g;
        this.BEquation = b;
        this.colorSolver = colorSolver;
    }

    public void updateFields(){
        REquation.setText(colorSolver.getEquation(0));
        GEquation.setText(colorSolver.getEquation(1));
        BEquation.setText(colorSolver.getEquation(2));

    }
}
