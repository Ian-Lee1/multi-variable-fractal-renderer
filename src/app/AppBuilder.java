package app;

import presenter.VariablePresenter;
import usecase.EquLinkedComponentSolver;
import usecase.Solver;
import usecase.SolverHasDefault;
import usecase.variable.AddVariableUsecase;
import usecase.variable.ChangeVariableDefaultUsecase;
import usecase.variable.DeleteVariableUsecase;
import usecase.variable.SetEquationUsecase;

import javax.swing.*;
import java.awt.*;

public class AppBuilder {
    private final JPanel panel = new JPanel();
    private final JPanel fractalView = new JPanel();
    private final JPanel settings = new JPanel();
    private final Solver solver = new EquLinkedComponentSolver();

    public AppBuilder() {
        //final BorderLayout borderLayout = new BorderLayout();
        //panel.setLayout(borderLayout);
        panel.setPreferredSize(new Dimension(800, 800));
    }

    private class Popup{
        private Popup(String name, String msg, Point location){
            JFrame frame = new JFrame(name);
            frame.add(new JLabel(msg));
            frame.setMinimumSize(new Dimension(200, 150));
            frame.setLocation(location.x, location.y);
            frame.pack();
            frame.setVisible(true);
        }
    }

    public AppBuilder addFractalView(){
        panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
        fractalView.setMinimumSize(new Dimension(600, 800));
        fractalView.setPreferredSize(new Dimension(600, 800));
        panel.add(fractalView);
        return this;
    }

    public AppBuilder addVariableFrame(){
        final JPanel variablePanel = new JPanel();
        final JPanel variableTopPanel = new JPanel(); //"Variable:" [drop box] [add] [delete]
        final JPanel variableBottomPanel = new JPanel(); // [Default r value] \n [default i value] \n [equation]

        final JButton addButton = new JButton("add");
        final JButton deleteButton = new JButton("delete");

        final JTextField defaultR = new JTextField(20);
        final JTextField defaultI = new JTextField(20);
        final JTextField equationField = new JTextField(20);

        defaultI.setText("0.0");
        defaultR.setText("0.0");

        final JComboBox<Integer> dropdown = new JComboBox<>();
        VariablePresenter variablePresenter;
        if (solver instanceof SolverHasDefault)
            variablePresenter = new VariablePresenter((SolverHasDefault)solver, dropdown, deleteButton, defaultR, defaultI, equationField);
        else {
            variablePresenter = null;
            throw new RuntimeException("Bad builder");
        }
        final AddVariableUsecase addVariableUsecase = new AddVariableUsecase(solver, variablePresenter);
        final DeleteVariableUsecase deleteVariableUsecase = new DeleteVariableUsecase(solver, variablePresenter);
        final ChangeVariableDefaultUsecase changeDefault = new ChangeVariableDefaultUsecase(solver);
        final SetEquationUsecase setEquationUsecase = new SetEquationUsecase(solver);

        addButton.addActionListener(_ -> addVariableUsecase.addVariable());
        deleteButton.addActionListener(_ -> deleteVariableUsecase.delete((int)dropdown.getSelectedItem()));
        dropdown.addActionListener(_ -> variablePresenter.updateFields());

        defaultR.addActionListener(_ -> {
            try{ changeDefault.changeDefault((int)dropdown.getSelectedItem(), defaultR.getText(), defaultI.getText());
            } catch (Exception e) {
                Popup popup = new Popup("Error", e.getMessage(), panel.getLocationOnScreen());
            }});
        defaultI.addActionListener(_ -> {
            try{ changeDefault.changeDefault((int)dropdown.getSelectedItem(), defaultR.getText(), defaultI.getText());
            } catch (Exception e) {
                Popup popup = new Popup("Error", e.getMessage(), panel.getLocationOnScreen());
            }});
        equationField.addActionListener(_ -> {
            try{
                equationField.setForeground(Color.black);
                setEquationUsecase.setEquation((int)dropdown.getSelectedItem(), equationField.getText());
            } catch (Exception e) {
                equationField.setForeground(Color.red);
                Popup popup = new Popup("Error", e.getMessage(), panel.getLocationOnScreen());
            }});

        addVariableUsecase.addVariable();
        variableTopPanel.setLayout(new BoxLayout(variableTopPanel, BoxLayout.X_AXIS));
        variableTopPanel.add(new JLabel("Variable:"));
        variableTopPanel.add(dropdown);
        variableTopPanel.add(addButton);
        variableTopPanel.add(deleteButton);
        variableTopPanel.setPreferredSize(new Dimension(200, 20));
        variableBottomPanel.setLayout(new BoxLayout(variableBottomPanel, BoxLayout.Y_AXIS));
        variableBottomPanel.add(defaultR);
        variableBottomPanel.add(defaultI);
        variableBottomPanel.add(equationField);
        variableBottomPanel.setPreferredSize(new Dimension(200, 60));
        variablePanel.setLayout(new BoxLayout(variablePanel, BoxLayout.Y_AXIS));
        variablePanel.add(variableTopPanel);
        variablePanel.add(variableBottomPanel);

        JToggleButton settingsButton = new JToggleButton("settings");
        settingsButton.addActionListener( _ -> variablePanel.setVisible(settingsButton.isSelected()));
        settings.add(variablePanel);
        settings.add(settingsButton);
        variablePanel.setVisible(false);
        panel.add(settings);
        deleteButton.setEnabled(false);
        return this;
    }




    public JFrame build() {
        final JFrame application = new JFrame("Fractal Renderer");
        application.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        //application.setSize(new Dimension(800, 800));
        application.add(panel);

        return application;
    }
}
