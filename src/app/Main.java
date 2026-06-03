package app;

import javax.swing.*;
public final class Main {
    public static void main(String[] args) {
        //UI panels -> controller -> input boundaries <|- usecase -> output boundary <|- presenter
    AppBuilder appBuilder = new AppBuilder();
    JFrame application = appBuilder.addVariableFrame().build();
    application.pack();
    application.setLocationRelativeTo(null);
    application.setVisible(true);
    }

    //Plan:
        //variable:   drop down menu 0... 1... 2... add
        //button: delete  [default r value] [default i value] [equation]
}

