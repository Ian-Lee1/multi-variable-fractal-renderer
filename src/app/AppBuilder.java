package app;

import entity.fractalRenderer.FractalRenderer;
import presenter.FractalPresenter;
import presenter.VariablePresenter;
import usecase.EquLinkedComponentSolver;
import usecase.Solver;
import usecase.SolverHasDefault;
import usecase.fractal.FractalMoveUsecase;
import usecase.fractal.FractalRenderUsecase;
import usecase.variable.AddVariableUsecase;
import usecase.variable.ChangeVariableDefaultUsecase;
import usecase.variable.DeleteVariableUsecase;
import usecase.variable.SetEquationUsecase;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;

public class AppBuilder {
    private final JPanel panel = new JPanel();
    private final JPanel fractalView = new JPanel();
    private final JPanel settings = new JPanel();
    private final Solver solver = new EquLinkedComponentSolver();
    final JPanel variablePanel = new JPanel();
    final JPanel fractalPanel = new JPanel();
    final ImageIcon fractalImage = new ImageIcon();
    private FractalRenderer fr;

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
        fractalView.setPreferredSize(new Dimension(800, 800));

        JLabel fractalLabel = new JLabel();

        FractalPresenter presenter = new FractalPresenter(fr, fractalImage);
        FractalMoveUsecase moveUsecase = new FractalMoveUsecase(fr);


        BufferedImage image = fr.getScreen();

        fractalImage.setImage(image);
        fractalLabel.setIcon(fractalImage);
        fractalView.add(fractalLabel);
        fractalLabel.setFocusable(false);
        fractalView.setFocusable(true);
        fractalView.addMouseWheelListener(e -> {
            moveUsecase.changeRadius(e.getWheelRotation());
            presenter.updateImage();
            fractalView.repaint();});
        fractalView.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                moveUsecase.setMousePos(e.getX(), e.getY());
                presenter.updateImage();
                fractalView.repaint();
            }
        });
        fractalView.addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                moveUsecase.moveByMouse(e.getX(), e.getY());
                presenter.updateImage();
                fractalView.repaint();
            }
        });

        fractalView.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e){
                if (e.getKeyChar() == 'w') {
                    moveUsecase.moveByKey(0, -1);
                } else if (e.getKeyChar() == 's') {
                    moveUsecase.moveByKey(0, 1);
                } else if (e.getKeyChar() == 'a') {
                    moveUsecase.moveByKey(-1, 0);
                } else if (e.getKeyChar() == 'd') {
                    moveUsecase.moveByKey(1, 0);
                }
                presenter.updateImage();
                fractalView.repaint();
            }
        });

        panel.add(fractalView);
        return this;
    }

    public AppBuilder constructFractalRenderer(){
        fr = new FractalRenderer(800, 800, solver);
        return this;
    }

    public AppBuilder addFractalControlFrame(){
        JPanel textPanel = new JPanel();
        JLabel renderText = new JLabel("Render:");
        JButton renderMainButton = new JButton("Main");
        JButton renderPrevButton = new JButton("Preview");
        FractalPresenter presenter = new FractalPresenter(fr, fractalImage);

        textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.X_AXIS));
        textPanel.add(renderText);
        textPanel.add(renderMainButton);
        textPanel.add((renderPrevButton));

        FractalRenderUsecase renderUsecase = new FractalRenderUsecase(fr);
        renderMainButton.addActionListener(_ -> {
            renderUsecase.renderMain();
            presenter.updateImage();
            fractalView.repaint();
        });
        renderPrevButton.addActionListener(_ -> {
            renderUsecase.renderPreview();
            presenter.updateImage();
            fractalView.repaint();
        });


        fractalPanel.setLayout(new BoxLayout(fractalPanel, BoxLayout.Y_AXIS));
        fractalPanel.add(textPanel);
        settings.add(fractalPanel);
        return this;
    }

    public AppBuilder addVariableFrame(){
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
        addVariableUsecase.addVariable();
        addVariableUsecase.addVariable();
        setEquationUsecase.setEquation(1, "v1^v2+v0");
        setEquationUsecase.setEquation(2, "v2^v1-v0");
        variablePresenter.updateFields();


        variableTopPanel.setLayout(new BoxLayout(variableTopPanel, BoxLayout.X_AXIS));
        variableTopPanel.add(new JLabel("Variable:"));
        variableTopPanel.add(dropdown);
        variableTopPanel.add(addButton);
        variableTopPanel.add(deleteButton);
        variableTopPanel.setPreferredSize(new Dimension(200, 40));
        variableTopPanel.setMaximumSize(new Dimension(200, 40));
        variableBottomPanel.setLayout(new BoxLayout(variableBottomPanel, BoxLayout.Y_AXIS));
        variableBottomPanel.add(defaultR);
        variableBottomPanel.add(defaultI);
        variableBottomPanel.add(equationField);
        variableBottomPanel.setPreferredSize(new Dimension(200, 60));
        variableBottomPanel.setMaximumSize(new Dimension(200, 60));
        variablePanel.setLayout(new BoxLayout(variablePanel, BoxLayout.Y_AXIS));
        variablePanel.add(variableTopPanel);
        variablePanel.add(variableBottomPanel);


        settings.setLayout(new BoxLayout(settings, BoxLayout.Y_AXIS));
        settings.add(variablePanel);

        variablePanel.setVisible(false);
        panel.add(settings);
        deleteButton.setEnabled(false);
        return this;
    }

    public AppBuilder addSettingsButton(){
        JToggleButton settingsButton = new JToggleButton("settings");
        settingsButton.addActionListener( _ -> {
            variablePanel.setVisible(settingsButton.isSelected());
            fractalPanel.setVisible(settingsButton.isSelected());
        });
        settings.add(settingsButton);
        settings.setLayout(new BoxLayout(settings, BoxLayout.Y_AXIS));
        settings.add(settingsButton);
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
