package usecase.fractal;

import entity.ComplexBasic;
import entity.fractalRenderer.FractalRenderer;

public class FractalMoveUsecase {
    private final FractalRenderer fr;
    int lastMouseX, lastMouseY;

   public void changeRadius(int dir){
       double factor;
       if (dir > 0)
           factor = 1/0.9;
       else factor = 0.9;
       fr.setRadius(fr.getRadius() * factor);
   }
    public FractalMoveUsecase(FractalRenderer fr){
        this.fr = fr;
    }

    public void moveByKey(int x, int y){
        double moveDist = fr.getRadius()/15;
        fr.move(new ComplexBasic(moveDist*x, moveDist*y));
    }

    public void setMousePos(int x, int y){
        lastMouseX = x;
        lastMouseY = y;
    }
    public void moveByMouse(int x, int y){
        double moveDistX = (lastMouseX - x)*fr.getRadius()*2/fr.getWidth();
        double moveDistY = (lastMouseY - y)*fr.getRadius()*2/fr.getHeight();
        setMousePos(x, y);
        fr.move(new ComplexBasic(moveDistX, moveDistY));
    }

    public void moveTo(double r, double i){
       fr.setCenter(new ComplexBasic(r, i));
    }

    public void setRadius(double r){
       fr.setRadius(r);
    }


}
