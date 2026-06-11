package bezier;

import entity.Complex;
import entity.ComplexBasic;

public class BezierSolver {
    public static Complex getPoint(Complex prev, Complex start, Complex end, Complex next, double factor){
        // using the direction from prev to end, and start to next, mutiplied by the min distant to their respective next points * some factor 0 < x < 1, +- original point to find control points.
        Complex startControl = getControl(prev, start, end, 1);
        Complex endControl = getControl(start, end, next, -1);
        //https://en.wikipedia.org/wiki/B%C3%A9zier_curve cubic curve equation
        return start.mul(new ComplexBasic(Math.pow(1-factor, 3), 0)).add(startControl.mul(new ComplexBasic(factor * Math.pow(1-factor, 2) * 3, 0))).add(endControl.mul(new ComplexBasic(Math.pow(factor, 2) * (1-factor) * 3, 0))).add(end.mul(new ComplexBasic(Math.pow(factor, 3), 0)));
    }

    private static Complex getControl(Complex prev, Complex start, Complex end, int dir){
        if (prev == null || end == null)
            return start;
        double minDist = Math.min(start.sub(prev).lengthSq(), start.sub(end).lengthSq());
        minDist = Math.sqrt(minDist) * 0.5 * dir;
        Complex vec = end.sub(prev).div(new ComplexBasic(Math.sqrt(end.sub(prev).lengthSq()), 0)).mul(new ComplexBasic(minDist, 0));
        return start.add(vec);
    }
}
