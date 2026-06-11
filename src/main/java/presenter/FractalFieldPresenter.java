package presenter;

import entity.fractalRenderer.FractalRenderer;

import javax.swing.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class FractalFieldPresenter implements PropertyChangeListener {
    private JTextField centerR;
    private JTextField centerI;
    private JTextField radius;
    private JTextField iteration;
    private FractalRenderer fr;
    public FractalFieldPresenter(JTextField centerR, JTextField centerI, JTextField radius, JTextField iteration, FractalRenderer fr){
        this.iteration = iteration;
        this.centerI = centerI;
        this.centerR = centerR;
        this.radius = radius;
        this.fr = fr;
        fr.addListener(this);
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        centerR.setText(fr.getCenter().getR() + "");
        centerI.setText(fr.getCenter().getI() + "");
        radius.setText(fr.getRadius() + "");
        iteration.setText(fr.getMaxIteration() + "");
    }
}
