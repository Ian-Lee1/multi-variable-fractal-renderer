package app;


import javax.swing.*;

public final class Main {
    public static void main(String[] args) {

    AppBuilder appBuilder = new AppBuilder("C:/Users/User/Desktop/multi-var-fractal/");
    JFrame application = appBuilder
            .addVariableFrame()
            .addDefaultVariables()
            .constructFractalRenderer()
            .addFractalControlFrame()
            .addFractalView()
            .addSettingsButton()
            .addKeyframePanel()

            .build();
    application.pack();
    application.setLocationRelativeTo(null);
    application.setVisible(true);


   }
}

