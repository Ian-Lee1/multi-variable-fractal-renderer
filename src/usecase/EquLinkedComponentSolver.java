package usecase;

import entity.*;

import java.util.ArrayList;

public class EquLinkedComponentSolver implements Solver {
    private String equation;
    private EquComponent equationLink;
    private final ArrayList<Complex> variables;
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
        if (equation.isEmpty())
            throw new EquationError("Equation is empty.");
        if (equation.charAt(0) == '-')
            equation = "c(0)" + equation;
        ComponentTuple temp = compileRecursive(0);
        this.equationLink = temp.component;
    }
    private ComponentTuple compileRecursive(int starting) {
        EquComponent lastTerm = null;
        int i = starting;
        while (i < equation.length()){
            if(contains(equation.charAt(i), operationChars)){ // next character is an operation
                lastTerm = processOperation(lastTerm, i);
            }
            else if(contains(equation.charAt(i), specialChars)) { //next char is a constant, variable, or a parentheses.
                ComponentTuple temp = processSpecial(lastTerm, i, starting);
                if (equation.charAt(i) == ')')
                    return temp;
                lastTerm = temp.component;
                i = temp.jumpTo;
            }
            i++;

        }
        if (starting != 0)
            throw new EquationError("Equation has unclosed parenthesis.");
        lastTerm = findRootTerm(lastTerm);

        if (lastTerm == null || !lastTerm.isComplete())
            throw new EquationError("Equation is incomplete.");
        return new ComponentTuple(lastTerm, i);
    }

    private EquComponent processOperation(EquComponent lastTerm, int i){
        if(lastTerm == null || !lastTerm.isComplete())
            throw new EquationError("Equation has missing operands");
        EquLinkedComponent newComponent = null;
        switch (equation.charAt(i)){
            case '+', '-':
                newComponent = new EquLinkedComponent(getOperation(equation.charAt(i)), lastTerm);
                if (lastTerm instanceof EquLinkedComponent) {
                    lastTerm = findRootTerm(lastTerm);
                    newComponent = new EquLinkedComponent(getOperation(equation.charAt(i)), lastTerm);
                    ((EquLinkedComponent) lastTerm).setPrev(newComponent);
                }
                break;
            case '*', '/', '^':
                if (lastTerm instanceof EquLinkedComponent) {
                    newComponent = new EquLinkedComponent(getOperation(equation.charAt(i)), ((EquLinkedComponent) lastTerm).getNext());
                    ((EquLinkedComponent) lastTerm).setNext(newComponent);
                    newComponent.setPrev(lastTerm);
                }else {
                    newComponent = new EquLinkedComponent(getOperation(equation.charAt(i)), lastTerm);
                }
                break;
        }
        return newComponent;
    }


    private ComponentTuple processSpecial(EquComponent lastTerm, int i, int starting){
        ComponentTuple temp;
        switch (equation.charAt(i)){
            case 'c':
                temp = getConstant(i);
                lastTerm = joinComponents(lastTerm, temp.component);
                i = temp.jumpTo;
                break;
            case 'v':
                temp = getVariable(i);
                lastTerm = joinComponents(lastTerm, temp.component);
                i = temp.jumpTo;
                break;
            case '(':
                temp = compileRecursive(i+1);
                EquParenthesisComponent parenthesis = new EquParenthesisComponent(temp.component);
                lastTerm = joinComponents(lastTerm, parenthesis);
                i = temp.jumpTo;
                break;
            case ')':
                if (starting == 0)
                    throw new EquationError("Unexpecting closing parethesis.");
                return new ComponentTuple(findRootTerm(lastTerm), i);
        }
        return new ComponentTuple(lastTerm, i);
    }

    private EquComponent findRootTerm(EquComponent lastTerm){
        if (!(lastTerm instanceof EquLinkedComponent) || ((EquLinkedComponent) lastTerm).getPrev() == null)
            return lastTerm;
        return findRootTerm(((EquLinkedComponent) lastTerm).getPrev());

    }

    private Operations getOperation(char op){
        return switch (op) {
            case '+' -> Operations.ADD;
            case '-' -> Operations.SUB;
            case '*' -> Operations.MUL;
            case '/' -> Operations.DIV;
            case '^' -> Operations.POW;
            default -> throw new IllegalStateException("Unexpected value: " + op);
        };

    }

    public record ComponentTuple(EquComponent component, int jumpTo) {}

    private EquComponent joinComponents(EquComponent last, EquComponent curr) {
        if (last == null) // last does not exist.
            return curr;
        else if (!last.isComplete()){ // last is incomplete. ex: +(5,null)    *(2.3,null). Must be a linked component.
            ((EquLinkedComponent) last).setNext(curr); //curr must be complete by operation check.
            return last;
        }

        if(last.isComplete() && curr.isComplete()){
            if (last instanceof EquLinkedComponent)
                return joinComponentsWithOrder((EquLinkedComponent)last, curr);

            EquLinkedComponent product = new EquLinkedComponent(Operations.MUL, last);
            product.setNext(curr);
            return product;
        }
        //last.isComplete && !curr.isComplete or both incomplete checked during operation creation
            throw new EquationError("Unknown error, last it :"+ last.isComplete() + " and curr is: " + curr.isComplete());

    }

    private EquComponent joinComponentsWithOrder(EquLinkedComponent last, EquComponent curr){

        EquLinkedComponent product = new EquLinkedComponent(Operations.MUL, last.getNext());
        product.setNext(curr);
        last.setNext(product);
        product.setPrev(last);

        return product;
    }

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
                jumpTo = j;
            break;
            }
            if ((contains(equation.charAt(j), specialChars) || contains(equation.charAt(j), operationChars)) && equation.charAt(j) != '-')
                throw new EquationError("Invalid constant formatting. Contains invalid symbol: " + equation.charAt(j));
        }
        return new ComponentTuple(new EquConstantComponent(new ComplexBasic(real, imag)), jumpTo);
    }

    private ComponentTuple getVariable(int i) {
        int jumpTo = 0;
        int pointer = i + 1;
        EquVariableComponent varComp = null;
        for (int j = i + 1; j < equation.length(); j++) {
            if (contains(equation.charAt(j), operationChars) || contains(equation.charAt(j), specialChars)){
                if (pointer == j)
                    throw new EquationError("No number provided for variable decleration.");
                jumpTo = j;
                break;
            } else if (j == equation.length() - 1) {
                jumpTo = j+1;
            }
        }
        if (pointer == jumpTo)
            throw new EquationError("No number provided for variable decleration.");
        varComp = new EquVariableComponent(variables, Integer.parseInt(equation.substring(pointer, jumpTo)));
        return new ComponentTuple(varComp, jumpTo - 1);
    }

    private boolean contains(char c, char[] array) {
        for (char x : array) {
            if (x == c) {
                return true;
            }
        }
        return false;
    }

    @Override
    public String toString(){
        return equationLink.toString();
    }
}
