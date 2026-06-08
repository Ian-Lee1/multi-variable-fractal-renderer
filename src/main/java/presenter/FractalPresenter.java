package presenter;

import entity.fractalRenderer.FractalRenderer;

import javax.swing.*;

public class FractalPresenter {
    private final FractalRenderer fr;
    private final ImageIcon ic;
    public FractalPresenter(FractalRenderer fr, ImageIcon ic){
        this.fr = fr;
        this.ic = ic;
    }

    public void updateImage(){
        fr.updateScreen();
        ic.setImage(fr.getScreen());
    }
}
