package app;


import javax.swing.*;
import java.awt.*;

public final class Main {
    public static void main(String[] args) {

    AppBuilder appBuilder = new AppBuilder("C:/Users/User/Desktop/multi-var-fractal/");
    JFrame application = appBuilder
            .addVariableFrame()
            .addDefaultVariables()
            .constructFractalRenderer()
            .addFractalControlFrame()
            .addFractalView()
            .addKeyframePanel()
            .addSettingsButton()

            .build();
    application.pack();
    application.setLocationRelativeTo(null);
    application.setVisible(true);


   }
}

