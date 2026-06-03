package app;

import presenter.VariablePresenter;
import usecase.EquLinkedComponentSolver;
import usecase.Solver;
import usecase.variable.AddVariableUsecase;
import usecase.variable.DeleteVariableUsecase;

import javax.swing.*;
import java.awt.*;

public class AppBuilder {
    private final JPanel panel = new JPanel();
    private final Solver solver = new EquLinkedComponentSolver();

    public AppBuilder() {
        //final BorderLayout borderLayout = new BorderLayout();
        //panel.setLayout(borderLayout);
        panel.setPreferredSize(new Dimension(800, 800));
    }

    private class Popup{
        private Popup(String name, String msg){
            JFrame frame = new JFrame(name);
            frame.add(new JLabel(msg));
            frame.setSize(new Dimension(200, 150));
            frame.pack();
            frame.setVisible(true);
        }
    }
    public AppBuilder addVariableFrame(){
        final JPanel variablePanel = new JPanel();
        final JPanel variableTopPanel = new JPanel(); //"Variable:" [drop box] [add] [delete]
        final JPanel variableBottomPanel = new JPanel(); // [Default r value] \n [default i value] \n [equation]

        final JButton addButton = new JButton("add");
        final JButton deleteButton = new JButton("delete");


        final JComboBox<Integer> dropdown = new JComboBox<>();
        final VariablePresenter variablePresenter = new VariablePresenter(dropdown, deleteButton);

        final AddVariableUsecase addVariableUsecase = new AddVariableUsecase(solver, variablePresenter);
        final DeleteVariableUsecase deleteVariableUsecase = new DeleteVariableUsecase(solver, variablePresenter);



        addButton.addActionListener(_ -> addVariableUsecase.addVariable());
        deleteButton.addActionListener(_ -> deleteVariableUsecase.delete((int)dropdown.getSelectedItem()));


        addVariableUsecase.addVariable();
        variableTopPanel.setLayout(new BoxLayout(variableTopPanel, BoxLayout.X_AXIS));
        variableTopPanel.add(new JLabel("Variable:"));
        variableTopPanel.add(dropdown);
        variableTopPanel.add(addButton);
        variableTopPanel.add(deleteButton);
        variableTopPanel.setPreferredSize(new Dimension(200, 20));
        variableBottomPanel.setLayout(new BoxLayout(variableBottomPanel, BoxLayout.Y_AXIS));
        variableBottomPanel.add(new JTextField(20));
        variableBottomPanel.add(new JTextField(20));
        variableBottomPanel.add(new JTextField(20));
        variableBottomPanel.setPreferredSize(new Dimension(200, 60));
        variablePanel.setLayout(new BoxLayout(variablePanel, BoxLayout.Y_AXIS));
        variablePanel.add(variableTopPanel);
        variablePanel.add(variableBottomPanel);

        panel.add(variablePanel);
        deleteButton.setEnabled(false);
        return this;
    }
    public JFrame build() {
        final JFrame application = new JFrame("Fractal Renderer");
        application.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        application.setSize(new Dimension(800, 800));
        application.add(panel);

        return application;
    }
}
