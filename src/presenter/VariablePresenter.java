package presenter;

import usecase.Solver;
import usecase.SolverHasDefault;

import javax.swing.*;
import java.awt.*;

public class VariablePresenter {
    private final SolverHasDefault solver;
    private final JComboBox<Integer> dropdown;
    private final JButton deleteButton;
    private final JTextField defaultR;
    private final JTextField defaultI;
    private final JTextField equation;
    public VariablePresenter(SolverHasDefault solver, JComboBox<Integer> dropdown, JButton deleteButton, JTextField dR, JTextField dI, JTextField eq){
        this.dropdown = dropdown;
        this.deleteButton = deleteButton;
        this.solver = solver;
        this.defaultI = dI;
        this.defaultR = dR;
        this.equation = eq;
    }
    public void addVariable(){
        this.dropdown.addItem(dropdown.getItemCount());
        this.dropdown.setSelectedItem(dropdown.getItemCount() - 1);
        deleteButton.setEnabled(true);
        this.defaultI.setText("0.0");
        this.defaultR.setText("0.0");
        this.equation.setText("");
    }

    public void updateFields(){
        int index = (int)dropdown.getSelectedItem();
        this.defaultI.setText("" + solver.getDefault(index).getI());
        this.defaultR.setText("" + solver.getDefault(index).getR());
        if (solver instanceof Solver) {
            String equ = ((Solver) solver).getEquation(index);
            this.equation.setText(equ);
            if (equ.startsWith("Broken equation!"))
                equation.setForeground(Color.red);

        }

    }
    public void removeVariable(){
        this.dropdown.removeItem(dropdown.getItemCount()-1);
        if (dropdown.getItemCount() == 1)
            deleteButton.setEnabled(false);
        updateFields();
    }
}
