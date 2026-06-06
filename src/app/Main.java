package app;

import javax.swing.*;
public final class Main {
    public static void main(String[] args) {
        //UI panels -> controller -> input boundaries <|- usecase -> output boundary <|- presenter
    AppBuilder appBuilder = new AppBuilder();
    JFrame application = appBuilder
            .addVariableFrame()
            .addDefaultVariables()
            .constructFractalRenderer()
            .addFractalControlFrame()
            .addFractalView()
            .addSettingsButton()

            .build();
    application.pack();
    application.setLocationRelativeTo(null);
    application.setVisible(true);
    }
}

