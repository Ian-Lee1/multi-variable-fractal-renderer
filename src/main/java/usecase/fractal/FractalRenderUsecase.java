package usecase.fractal;

import entity.fractalRenderer.FractalRenderer;

public class FractalRenderUsecase {
    private final FractalRenderer fr;
    public FractalRenderUsecase(FractalRenderer fr){
        this.fr = fr;
    }

    public void renderMain(){
        fr.updateMain();
    }

    public void renderPreview(){
        fr.updatePreview();
    }

}
