package usecase;

import entity.*;

import java.util.ArrayList;

public class EquLinkedComponentSolver implements Solver {
    private String equation;
    private EquComponent equationLink;
    private ArrayList<Complex> variables;
    private final char[] operationChars = {'+', '-', '*', '/', '^'};
    private final char[] specialChars = {'c', 'v', '(', ')'};

    public EquLinkedComponentSolver(String equation){
        this.equation = equation;
        this.variables = new ArrayList<>();
        compile();
    }
    @Override
    public void setEquation(String equation) throws EquationError {
        this.equation=equation;
        compile();
    }

    @Override
    public void updateVariable(int i, Complex value) {
        try {
            variables.set(i, value);
        } catch (IndexOutOfBoundsException _) {
            throw new EquationError("Variable " + i + " is not defined.");
        }
    }

    public void addVariable(){
        variables.add(new ComplexBasic(0,0));
    }

    public void removeVariable(int i){
        variables.remove(i);
    }

    @Override
    public Complex solve() {
        return equationLink.solve();
    }

    private void compile(){
        EquLinkedComponent temp = new EquLinkedComponent(null, Operations.ADD, new EquConstantComponent(new ComplexBasic(0,0)));
        compileRecursive(0, temp);
        this.equationLink = temp.getNext();
    }
    private int compileRecursive(int starting, EquComponent last) {
        // v(0) = pixel location, v(1) = last output, v(...) = defined variables
        // c() = constants
        // ()
        // +-*/

        //c(5+6i)/c(7+8i)
        //get and store constant block 5+6i
        //      last term = 5+6i block
        //get new divide block with value being the 5+6i block
        //get
        int lastOp = starting;
        EquComponent lastOpTerm = last;
        EquComponent lastNumTerm = null;
        //create a method that can join terms based on term.prev.next == null
        for (int i = starting; i < equation.length(); i++) {
            if(contains(equation.charAt(i), operationChars)){ // next character is an operation
                //if(lastTerm == null || lastTerm.isComplete()) //check if a term is contained between 2 operations
                //    throw new EquationError("Equation has adjacent operations");

            }

            if(contains(equation.charAt(i), specialChars)) { //next char is a constant, variable, or a parentheses.
                switch (equation.charAt(i)){
                    case 'c':
                        //get the constant as a component. If last op is incomplete, save it to last Num term.
                        //if last op is complete, multiply it to last num term, then set last op's next to the product.
                        System.out.println(getConstant(i).component.solve().getR());
                        System.out.println(getConstant(i).component.solve().getI());
                }
            }

        }
        return 0;
    }

//    private EquComponent joinComponents(EquComponent last, EquComponent curr) {
//        //last is null, new is complete:   last = new
//        //last is incomplete, new is complete:   x+c(5+6i)    last.next = new
//        //last is complete, new is complete, c(5+6i)c(5+6i)
//
//    }
//        x+6*7
//
//                x    last:x (complete)
//                +(x,null)    last: + (incomplete)
//                +(x,6)       last: + (complete)
//                +(x,*(6, null))   op is (+/ /)
//               +(x,*(6,7))
//
//            (5+6)(7+8)
//
//                c()
//                c(5)
    public record ComponentTuple(EquComponent component, int jumpTo) {}

    private ComponentTuple getConstant(int i){
        int jumpTo = 0;
        int pointer = i+2;
        double real = 0;
        double imag = 0;
        boolean hasReal = false;
        if(equation.charAt(i+1) != '(')
            throw new EquationError("Constant decleration should start with (");

        for(int j = i+2; j < equation.length(); j++) {
            if (equation.charAt(j) == ',') {
                if (hasReal)
                    throw new EquationError("Invalid constant formatting. Must only contain one real component.");
                if (pointer >= j)
                    throw new EquationError("Invalid constant formatting. , is included but missing real component.");
                if (equation.charAt(j + 1) == ')')
                    throw new EquationError("Invalid constant formatting. , is included but missing imaginary component.");
                real = Double.parseDouble(equation.substring(pointer, j));
                pointer = j + 1;
                hasReal = true;
            }
            if (equation.charAt(j) == 'i') {
                if (pointer >= j)
                    throw new EquationError("Invalid constant formatting. i is included but missing imaginary component.");
                if (equation.charAt(j + 1) != ')')
                    throw new EquationError("Invalid constant formatting. Constant value must end after imaginary component.");
                imag = Double.parseDouble(equation.substring(pointer, j));
                pointer = j;
            }
            if (equation.charAt(j) == ')') {
                if (pointer == i+2){
                    if (pointer >= j)
                        throw new EquationError("Constant decleration is empty.");
                    real = Double.parseDouble(equation.substring(pointer, j));
                }
                jumpTo = j + 1;
            break;
            }
            if ((contains(equation.charAt(j), specialChars) || contains(equation.charAt(j), operationChars)) && equation.charAt(j) != '-')
                throw new EquationError("Invalid constant formatting. Contains invalid symbol: " + equation.charAt(j));
        }
        return new ComponentTuple(new EquConstantComponent(new ComplexBasic(real, imag)), jumpTo);
    }
    private boolean contains(char c, char[] array) {
        for (char x : array) {
            if (x == c) {
                return true;
            }
        }
        return false;
    }
}
