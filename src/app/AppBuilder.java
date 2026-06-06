package app;

import entity.fractalRenderer.FractalRenderer;
import presenter.FractalPresenter;
import presenter.VariablePresenter;
import usecase.EquLinkedComponentSolver;
import usecase.Solver;
import usecase.SolverHasDefault;
import usecase.fractal.FractalBailUsecase;
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
    private VariablePresenter variablePresenter;
    private AddVariableUsecase addVariableUsecase;
    private DeleteVariableUsecase deleteVariableUsecase;
    private ChangeVariableDefaultUsecase changeDefault;
    private SetEquationUsecase setEquationUsecase;

    private JComboBox<Integer> bailDropdown = new JComboBox<>();
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
        FractalRenderUsecase renderUsecase = new FractalRenderUsecase(fr);

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
                fractalView.grabFocus();
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
                if (e.getKeyChar() == 'w')
                    moveUsecase.moveByKey(0, -1);
                else if (e.getKeyChar() == 's')
                    moveUsecase.moveByKey(0, 1);
                else if (e.getKeyChar() == 'a')
                    moveUsecase.moveByKey(-1, 0);
                else if (e.getKeyChar() == 'd')
                    moveUsecase.moveByKey(1, 0);
                else if (e.getKeyChar() == 'r')
                    moveUsecase.changeRadius(-1);
                else if (e.getKeyChar() == 'f')
                    moveUsecase.changeRadius(1);
                else if (e.getKeyChar() == 'z'){
                    try{renderUsecase.renderMain();} catch (Exception er) {
                        Popup popup = new Popup("Error!", er.getMessage(), fractalView.getLocationOnScreen());
                    }
                    presenter.updateImage();
                    fractalView.repaint();
                }                 else if (e.getKeyChar() == 'x'){
                    try{renderUsecase.renderPreview();} catch (Exception er) {
                        Popup popup = new Popup("Error!", er.getMessage(), fractalView.getLocationOnScreen());
                    }
                    presenter.updateImage();
                    fractalView.repaint();
                }


                presenter.updateImage();
                fractalView.repaint();
            }
        });

        panel.add(fractalView);
        return this;
    }

    public AppBuilder addDefaultVariables(){
        addVariableUsecase.addVariable();
        addVariableUsecase.addVariable();
        addVariableUsecase.addVariable();
        addVariableUsecase.addVariable();
        addVariableUsecase.addVariable();
        setEquationUsecase.setEquation(1, "v2+v3");
        setEquationUsecase.setEquation(2, "v2^c(2)+v0");
        setEquationUsecase.setEquation(3, "v3^c(2)+c(0.3,0.6)+v0*v4");
        setEquationUsecase.setEquation(4, "c(0)");
        changeDefault.changeDefault(4, "1", "0");
        bailDropdown.setSelectedItem(1);
        variablePresenter.updateFields();
        return this;
    }
    public AppBuilder constructFractalRenderer(){
        fr = new FractalRenderer(800, 800, 1, 4, 0.00001, solver);
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


            try{renderUsecase.renderMain();} catch (Exception e) {
                Popup popup = new Popup("Error!", e.getMessage(), renderMainButton.getLocationOnScreen());
            }
            presenter.updateImage();
            fractalView.repaint();
        });
        renderPrevButton.addActionListener(_ -> {

            try{renderUsecase.renderPreview();} catch (Exception e) {
                Popup popup = new Popup("Error!", e.getMessage(), renderPrevButton.getLocationOnScreen());
            }
            presenter.updateImage();
            fractalView.repaint();
        });


        FractalBailUsecase bail = new FractalBailUsecase(fr);

        JPanel mid = new JPanel();
        JPanel bottom = new JPanel();

        mid.setPreferredSize(new Dimension(200, 40));
        mid.setMaximumSize(new Dimension(200, 40));
        bottom.setPreferredSize(new Dimension(200, 40));
        bottom.setMaximumSize(new Dimension(200, 40));
        JLabel txtIteration = new JLabel("Max iter");
        JLabel txtBVariable = new JLabel("Bail var");
        JTextField fieldIteration = new JTextField(3);


        JLabel txtMinAmt = new JLabel("Bail min");
        JLabel txtMaxAmt = new JLabel("max");
        JTextField fieldMin = new JTextField(3);
        JTextField fieldMax = new JTextField(3);

        fieldIteration.setText("50");
        bail.setMaxIteration(50);
        fieldMin.setText("0.001");
        bail.changeBailMin(0.001);
        fieldMax.setText("4");
        bail.changeBailMax(4);

        fieldIteration.addActionListener(_ -> {
            try{
                bail.setMaxIteration(Integer.parseInt(fieldIteration.getText()));

            } catch (Exception e) {
                Popup popup = new Popup("Error", "Invalid input. Max iteration must be a positive integer.", fieldIteration.getLocationOnScreen());
            }
        });

        bailDropdown.addActionListener(_ -> bail.changeBailVariable((int)bailDropdown.getSelectedItem()));

        fieldMin.addActionListener(_ -> {
            try{
                bail.changeBailMin(Double.parseDouble(fieldMin.getText()));

            } catch (Exception e) {
                Popup popup = new Popup("Error", "Invalid input. Bail min must be a double.", fieldMin.getLocationOnScreen());
            }
        });
        fieldMax.addActionListener(_ -> {
            try{
                bail.changeBailMax(Double.parseDouble(fieldMax.getText()));

            } catch (Exception e) {
                Popup popup = new Popup("Error", "Invalid input. Bail max must be a double.", fieldMin.getLocationOnScreen());
            }
        });


        mid.add(txtIteration);
        mid.add(fieldIteration);

        mid.add(txtBVariable);
        mid.add(bailDropdown);

        bottom.add(txtMinAmt);
        bottom.add(fieldMin);
        bottom.add(txtMaxAmt);
        bottom.add(fieldMax);


        fractalPanel.setLayout(new BoxLayout(fractalPanel, BoxLayout.Y_AXIS));
        fractalPanel.add(mid);
        fractalPanel.add(bottom);
        fractalPanel.add(textPanel);
        fractalPanel.setVisible(false);
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
        if (solver instanceof SolverHasDefault) {
            variablePresenter = new VariablePresenter((SolverHasDefault) solver, dropdown, deleteButton, defaultR, defaultI, equationField);
            variablePresenter.setBailDropdown(bailDropdown);
        }
        else {
            variablePresenter = null;
            throw new RuntimeException("Bad builder");
        }

        addVariableUsecase = new AddVariableUsecase(solver, variablePresenter);
        deleteVariableUsecase = new DeleteVariableUsecase(solver, variablePresenter);
        changeDefault = new ChangeVariableDefaultUsecase(solver);
        setEquationUsecase = new SetEquationUsecase(solver);


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
        return this;
    }

    public AppBuilder addSettingsButton(){
        JToggleButton settingsButton = new JToggleButton("settings");
        settingsButton.addActionListener( _ -> {
            variablePanel.setVisible(settingsButton.isSelected());
            fractalPanel.setVisible(settingsButton.isSelected());
        });
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
