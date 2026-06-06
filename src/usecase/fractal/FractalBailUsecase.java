package usecase.fractal;

import entity.EquationError;
import entity.fractalRenderer.FractalRenderer;

public class FractalBailUsecase {
    private final FractalRenderer fr;
    public FractalBailUsecase(FractalRenderer fr){
        this.fr = fr;
    }

    public void setMaxIteration(int i){
        if (i < 0)
            throw new EquationError("max iterations less than 0.");
        fr.setIterations(i);
    }

    public void changeBailVariable(int i){
        fr.setBailVariable(i);
    }

    public void changeBailMax(double m){
        fr.setBailMax(m);
    }

    public void changeBailMin(double m){
        fr.setBailMin(m);
    }
}

// render: [Main] [Preview[
//max iter [   ] bail var [  \/]
//bail min [     ] max val  [     ]
