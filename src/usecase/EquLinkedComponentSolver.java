package usecase;

import entity.*;

import java.util.ArrayList;

public class EquLinkedComponentSolver implements Solver, SolverHasDefault {
    private final ArrayList<Complex> variables;
    private final ArrayList<Complex> tempVariables;
    private final ArrayList<Complex> variablesDefault;
    private final ArrayList<Integer> variableAddress;
    private final ArrayList<String> equations;
    private final ArrayList<EquComponent> equLinks;


    private final char[] operationChars = {'+', '-', '*', '/', '^'};
    private final char[] specialChars = {'c', 'v', '(', ')', 'F'};

    public EquLinkedComponentSolver(){
        this.variables = new ArrayList<>();
        this.equations = new ArrayList<>();
        this.equLinks = new ArrayList<>();
        this.tempVariables = new ArrayList<>();
        this.variableAddress = new ArrayList<>();
        this.variablesDefault = new ArrayList<>();
    }
    @Override
    public void setEquation(int i, String equation) throws EquationError {
        if (i < 0 || i >= variables.size())
            throw new EquationError("Variable "+ i + "has not been declared.");
        int index;
        if (variableAddress.contains(i)) {
            index = variableAddress.indexOf(i);
            equations.set(index, equation);
        }
        else{
            index = equations.size();
            equations.add(equation);
            tempVariables.add(new ComplexBasic(0,0));
            variablesDefault.add(new ComplexBasic(0,0));
            equLinks.add(null);
            variableAddress.add(i);
        }
        compile(index);
    }

    public void setDefault(int i, Complex value){
        if (variableAddress.contains(i)) {
            int index = variableAddress.indexOf(i);
            variablesDefault.set(index, value);
        }
        else
            updateVariable(i, value);
    }

    public void resetVariable(){
        for (int i = 0; i < equations.size(); i++){
            variables.set(variableAddress.get(i), variablesDefault.get(i));
        }
    }

    public Complex getDefault(int i){
        if (variableAddress.contains(i)) {
            int index = variableAddress.indexOf(i);
            return variablesDefault.get(index);
        }
        else
            return readVariable(i);
    }

    public String getEquation(int i){
        if (variableAddress.contains(i)) {
            int index = variableAddress.indexOf(i);
            return equations.get(index);
        }
        else
            return "";
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
        for(int j = 0; j < equations.size(); j++){
            if (variableAddress.get(j) == i){
                int index = variableAddress.indexOf(i);
                variableAddress.remove(index);
                tempVariables.remove(index);
                variablesDefault.remove(index);
                equLinks.remove(index);
                equations.remove(index);
            }
            else if (variableAddress.get(j) > i)
                variableAddress.set(j, variableAddress.get(j) - 1);
        }
    }

    @Override
    public void solve() {
        for(int i = 0; i < equations.size(); i++){
                try{tempVariables.set(i, equLinks.get(i).solve());}
                catch(EquationError e){throw new EquationError("Equation " + variableAddress.get(i) + " produced error:" + e.getMessage());}
        }
        for(int i = 0; i < equations.size(); i++){
            variables.set(variableAddress.get(i), tempVariables.get(i));
        }

    }

    @Override
    public Complex readVariable(int i){
        if (i < 0 || i > variables.size())
            throw new EquationError("Variable " + i + " has not been delcared.");
        return variables.get(i);
    }

    private void compile(int i){
        if (equations.get(i).isEmpty()) {
            equations.remove(i);
            equLinks.remove(i);
            tempVariables.remove(i);
            variableAddress.remove(i);
            variables.set(i, variablesDefault.get(i));
            variablesDefault.remove(i);
            return;
        }
        if (equations.get(i).startsWith("Broken equation!"))
            equations.set(i, equations.get(i).substring(16));
        if (equations.get(i).charAt(0) == '-')
            equations.set(i, "c(0)" + equations.get(i));
        equations.set(i, equations.get(i));
        try{
            ComponentTuple temp = compileRecursive(0, equations.get(i));
            this.equLinks.set(i, temp.component);
        }catch (EquationError e){
            equations.set(i, "Broken equation!" + equations.get(i));
            throw e;
        }

    }
    private ComponentTuple compileRecursive(int starting, String equation) {
        EquComponent lastTerm = null;
        int i = starting;
        while (i < equation.length()){
            if(contains(equation.charAt(i), operationChars)){ // next character is an operation
                lastTerm = processOperation(lastTerm, i, equation);
            }
            else if(contains(equation.charAt(i), specialChars)) { //next char is a constant, variable, or a parentheses.
                ComponentTuple temp = processSpecial(lastTerm, i, starting, equation);
                if (equation.charAt(i) == ')')
                    return temp;
                lastTerm = temp.component;
                i = temp.jumpTo;
            }
            else if (!("" + equation.charAt(i)).matches("\\d+"))
                throw new EquationError("Unexpected letter " + equation.charAt(i) +".");

            i++;

        }
        if (starting != 0)
            throw new EquationError("Equation has unclosed parenthesis.");
        lastTerm = findRootTerm(lastTerm);

        if (lastTerm == null || !lastTerm.isComplete())
            throw new EquationError("Equation is incomplete.");
        return new ComponentTuple(lastTerm, i);
    }

    private EquComponent processOperation(EquComponent lastTerm, int i, String equation){
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

    private record FunctionTuple(Functions func, int jumpTo) {}
    private ComponentTuple processSpecial(EquComponent lastTerm, int i, int starting, String equation){
        ComponentTuple temp;
        Functions func = Functions.NONE;
        switch (equation.charAt(i)){
            case 'c':
                temp = getConstant(i, equation);
                lastTerm = joinComponents(lastTerm, temp.component);
                i = temp.jumpTo;
                break;
            case 'v':
                temp = getVariable(i, equation);
                lastTerm = joinComponents(lastTerm, temp.component);
                i = temp.jumpTo;
                break;
            case 'F':
               FunctionTuple ft = getFunction(i+1, equation.toLowerCase());
               func = ft.func;
               i = ft.jumpTo;
            case '(':
                temp = compileRecursive(i+1, equation);
                EquParenthesisComponent parenthesis = new EquParenthesisComponent(temp.component, func);
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

    private FunctionTuple getFunction(int i, String equation){
            if (equation.startsWith("sin(", i)){
                return new FunctionTuple(Functions.SIN, i+3);
            } else if (equation.startsWith("cos(", i)){
                return new FunctionTuple(Functions.COS, i+3);
            } else if (equation.startsWith("tan(", i)){
                return new FunctionTuple(Functions.TAN, i+3);
            } else if (equation.startsWith("len(", i)){
                return new FunctionTuple(Functions.LEN, i+3);
            } else if (equation.startsWith("len2(", i)){
                return new FunctionTuple(Functions.LEN2, i+4);
            } else if (equation.startsWith("sqrt(", i)){
                return new FunctionTuple(Functions.SQRT, i+4);
            } else throw new EquationError("Unknown function in equation.");
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

    private record ComponentTuple(EquComponent component, int jumpTo) {}

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

    private ComponentTuple getConstant(int i, String equation){
        int jumpTo = 0;
        int pointer = i+2;
        double real = 0;
        double imag = 0;
        boolean hasReal = false;
        if(equation.charAt(i+1) != '(')
            throw new EquationError("Constant decleration should start with (");

        for(int j = i+2; j < equation.length(); j++) {
            if (j == equation.length() - 1 && equation.charAt(j) != ')')
                throw new EquationError("Unclosed constant decleration.");
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
            else if (equation.charAt(j) == 'i') {
                if (pointer >= j)
                    throw new EquationError("Invalid constant formatting. i is included but missing imaginary component.");
                if (equation.charAt(j + 1) != ')')
                    throw new EquationError("Invalid constant formatting. Constant value must end after imaginary component.");
                imag = Double.parseDouble(equation.substring(pointer, j));
                pointer = j;
            }
            else if (equation.charAt(j) == ')') {
                if (pointer == i+2){
                    if (pointer >= j)
                        throw new EquationError("Constant decleration is empty.");
                    real = Double.parseDouble(equation.substring(pointer, j));
                }
                jumpTo = j;
            break;
            }
            else if (!("" + equation.charAt(j)).matches("\\d+")&& equation.charAt(j) != '.' && equation.charAt(j) != '-')
                throw new EquationError("Unexpected letter " + equation.charAt(j) +".");
        }
        return new ComponentTuple(new EquConstantComponent(new ComplexBasic(real, imag)), jumpTo);
    }

    private ComponentTuple getVariable(int i, String equation) {
        int jumpTo = 0;
        int pointer = i + 1;
        EquVariableComponent varComp = null;
        for (int j = i + 1; j < equation.length(); j++) {
            if (contains(equation.charAt(j), operationChars) || contains(equation.charAt(j), specialChars)){
                if (pointer == j)
                    throw new EquationError("No number provided for variable decleration.");
                jumpTo = j;
                break;
            } else if (!("" + equation.charAt(j)).matches("\\d+"))
                throw new EquationError("Unexpected letter " + equation.charAt(j) +".");
            else if (j == equation.length() - 1) {
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
        if (equations.isEmpty())
            return "Solver has " + variables.size() + " variables and no equations.";
        String str = "Variables: \n";
        for(int i = 0; i < variables.size(); i++){
            str += "v" + i + ": " + variables.get(i).toString() + "\n";
        }
        str += "Equations: \n";
        for(int i = 0; i < equations.size(); i++){
            str += "Equ for v" + variableAddress.get(i) + ": " + equLinks.get(i).toString() + "\n";
        }

        return str;
    }
}
