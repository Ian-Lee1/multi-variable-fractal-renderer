package app;

import usecase.EquLinkedComponentSolver;
import usecase.Solver;

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

    public AppBuilder addVariableFrame(){
        final JPanel variablePanel = new JPanel();
        final JPanel variableTopPanel = new JPanel(); //"Variable:" [drop box] [add] [delete]
        final JPanel variableBottomPanel = new JPanel(); // [Default r value] \n [default i value] \n [equation]

        variableTopPanel.setLayout(new BoxLayout(variableTopPanel, BoxLayout.X_AXIS));
        variableTopPanel.add(new JLabel("Variable:"));
        variableTopPanel.add(new JComboBox<>());
        variableTopPanel.add(new JButton("add"));
        variableTopPanel.add(new JButton("delete"));
        variableTopPanel.setPreferredSize(new Dimension(200, 20));
        variableBottomPanel.setLayout(new BoxLayout(variableBottomPanel, BoxLayout.Y_AXIS));
        variableBottomPanel.add(new JTextField(20));
        variableBottomPanel.add(new JTextField(20));
        variableBottomPanel.add(new JTextField(20));
        variableBottomPanel.setPreferredSize(new Dimension(200, 60));
        variablePanel.setLayout(new BoxLayout(variablePanel, BoxLayout.Y_AXIS));
        variablePanel.add(variableTopPanel);
        variablePanel.add(variableBottomPanel);
        //variablePanel.setPreferredSize(new Dimension(200, 80));
        panel.add(variablePanel);
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
