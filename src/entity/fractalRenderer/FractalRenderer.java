package entity.fractalRenderer;

import entity.Complex;
import entity.ComplexBasic;
import usecase.Solver;
import usecase.SolverHasDefault;

import java.awt.*;
import java.awt.image.BufferedImage;

public class FractalRenderer {
    private Complex center;

    private Complex mainCenter;
    private double mainRadius;

    private Complex previewCenter;
    private double previewRadius;
    private int width, height, iterations, previewFactor;
    private double radius;
    private BufferedImage screen, main, preview;
    private Solver solver;

    public FractalRenderer(int w, int h, Solver solver){
        this.width = w;
        this.height = h;
        this.iterations = 50;
        this.solver = solver;
        this.previewFactor = 3;
        screen = new BufferedImage(w, h, BufferedImage.TYPE_3BYTE_BGR);
        main = new BufferedImage(w, h, BufferedImage.TYPE_3BYTE_BGR);
        preview = new BufferedImage(w/previewFactor, h/previewFactor, BufferedImage.TYPE_3BYTE_BGR);

        center = new ComplexBasic(0,0);
        radius = 2;
        updateMain();
        updatePreview();
        updateScreen();
    }

    public void setCenter(Complex c){
        this.center = c;
    }

    public void move(Complex dC){
        center = center.add(dC);
    }

    public double getRadius(){
        return radius;
    }

    public int getWidth(){
       return width;
    }
    public int getHeight(){
        return height;
    }

    public void setRadius(double radius){
        this.radius = radius;
    }

    public void setWidth(int w){
        this.width = w;
    }
    public void setHeight(int h){
        this.height = h;
    }
    public void setIterations(int i){
        this.iterations = i;
    }

    public BufferedImage getScreen(){
        return screen;
    }
    public void refreshScreen(){
        Graphics g = screen.getGraphics();
        g.setColor(new Color(40,10,10));
        g.fillRect(0,0, width, height);
        g.setColor(Color.MAGENTA);
        double dx = 2.0*radius/width;
        double dy = 2.0*radius/height;

        double x = center.sub(new ComplexBasic(radius, radius)).getR();
        double y = center.sub(new ComplexBasic(radius, radius)).getI();
        for (int i = 0 ; i < width; i++){
                if (Math.abs(x - (int)x)/radius < 0.005) {
                    g.setColor(new Color(80, 40, 20));
                    g.fillRect(i, 0, 1, height);
                }
            x+= dx;
        }
        for (int i = 0 ; i < height; i++){
            if (Math.abs(y - (int)y)/radius < 0.005) {
                g.setColor(new Color(80, 40, 20));
                g.fillRect(0, i, width, 1);
            }
            y+= dy;
        }
    }
    public void updateScreen(){
        refreshScreen();
        Graphics g = screen.getGraphics();
        Complex topR = previewCenter.sub(new ComplexBasic(previewRadius, previewRadius));
        Complex offset = topR.sub(center.sub(new ComplexBasic(radius, radius)));
        int pixelOffsetX = (int)(offset.getR()/ ((2.0*radius)/(width)));
        int pixelOffsetY = (int)(offset.getI()/ ((2.0*radius)/(height)));
        double scale = previewRadius/radius;
        g.drawImage(preview, pixelOffsetX, pixelOffsetY, (int)(width*scale), (int)(height*scale), null);

        topR = mainCenter.sub(new ComplexBasic(mainRadius, mainRadius));
        offset = topR.sub(center.sub(new ComplexBasic(radius, radius)));
        pixelOffsetX = (int)(offset.getR()/ ((2.0*radius)/(width)));
        pixelOffsetY = (int)(offset.getI()/ ((2.0*radius)/(height)));
        scale = mainRadius/radius;
        g.drawImage(main, pixelOffsetX, pixelOffsetY, (int)(width*scale), (int)(height*scale), null);

    }


    public void updateMain(){
        draw(1, main);
    }
    public void updatePreview(){
        draw(6, preview);
    }


    public void draw(double radiusScale, BufferedImage img){
        if (img.equals(main)){
            mainCenter = center.add(new ComplexBasic(0,0));
            mainRadius = radius * radiusScale;
        } else if (img.equals(preview)) {
            previewCenter = center.add(new ComplexBasic(0,0));
            previewRadius = radius * radiusScale;
        }
        double dx = 2.0*radius*radiusScale/img.getWidth();
        double dy = 2.0*radius*radiusScale/img.getHeight();
        double r;
        double i = center.getI()-radius*radiusScale;
        double ratio;
        Graphics g = img.getGraphics();
        for (int y = 0; y < img.getHeight() ; y++){
            r = center.getR()-radius*radiusScale;
            for (int x = 0 ; x < img.getWidth(); x++){

                ((SolverHasDefault)solver).resetVariable();
                solver.updateVariable(0, new ComplexBasic(r, i));
                ratio = 1;
                for(int iteration = 0; iteration < this.iterations; iteration++){
                    solver.solve();
                    if (solver.readVariable(1).lengthSq() > 40000) {
                        ratio = (1.0+iteration)/this.iterations;
                        iteration = this.iterations;
                    }
                }
                    g.setColor(new Color((int)(255*ratio), (int)(200*ratio), (int)(150*ratio)));
                    g.drawRect(x, y, 1, 1);

                //TODO implement color equations. with new solver?
                r += dx;
            }
            i += dy;
        }
    }


}
