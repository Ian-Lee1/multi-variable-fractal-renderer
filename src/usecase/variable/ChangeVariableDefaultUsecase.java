package usecase.variable;

import entity.Complex;
import entity.ComplexBasic;
import entity.EquationError;
import usecase.Solver;
import usecase.SolverHasDefault;

public class ChangeVariableDefaultUsecase {
    private final Solver solver;
    public ChangeVariableDefaultUsecase(Solver solver){
        this.solver = solver;
    }
    public void changeDefault(int ind, String r, String i){
        Complex value;
        try{
            if (i.charAt(i.length() - 1) == 'i'){
                i = i.substring(0, i.length()-1);
            }
            value = new ComplexBasic(Double.parseDouble(r), Double.parseDouble(i));
        } catch (NumberFormatException _){
            throw new EquationError("Given value " + r + "+" + i + "i is not a number." );
        }
        if (solver instanceof SolverHasDefault){
            ((SolverHasDefault) solver).setDefault(ind, value);
        }
    }
}
