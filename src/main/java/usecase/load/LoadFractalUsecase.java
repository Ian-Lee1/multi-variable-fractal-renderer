package usecase.load;

import dataaccessentity.JSONLoader;
import entity.ComplexBasic;
import entity.EquationError;
import entity.fractalRenderer.FractalRenderer;
import org.json.JSONArray;
import org.json.JSONObject;
import usecase.Solver;
import usecase.SolverHasDefault;

public class LoadFractalUsecase {
    private final Solver solver;
    private final Solver colorSolver;
    private final FractalRenderer fr;

    public LoadFractalUsecase(Solver solver, Solver colorSolver, FractalRenderer fractalRenderer){
        this.solver = solver;
        this.colorSolver = colorSolver;
        this.fr = fractalRenderer;
    }
    public void loadFractal(String path){
        JSONObject data = JSONLoader.readJSON(path);
        solver.reset();
        colorSolver.reset();

        try {
            //Add default RGB variable for color solver
            colorSolver.addVariable();
            colorSolver.addVariable();
            colorSolver.addVariable();

            fr.setCenter(new ComplexBasic(data.getDouble("centerR"), data.getDouble("centerI")));
            fr.setRadius(data.getDouble("radius"));
            fr.setIterations(data.getInt("maxIter"));
            fr.setBailVariable(data.getInt("bailVar"));
            fr.setBailMin(data.getDouble("bailMin"));
            fr.setBailMax(data.getDouble("bailMax"));

            colorSolver.setEquation(0, data.getString("REquation"));
            colorSolver.setEquation(1, data.getString("GEquation"));
            colorSolver.setEquation(2, data.getString("BEquation"));

            JSONArray varaibleDataArray = data.getJSONArray("variables");
            for (int i = 0; i < varaibleDataArray.length(); i++) {
                solver.addVariable();
                if (colorSolver.getVariableCount() < solver.getVariableCount())
                    colorSolver.addVariable();
            }

            for (int i = 0; i < varaibleDataArray.length(); i++) {
                JSONObject variableData = varaibleDataArray.getJSONObject(i);
                int index = variableData.getInt("index");
                solver.setEquation(index, variableData.getString("equation"));
                ((SolverHasDefault) solver).setDefault(index, new ComplexBasic(variableData.getDouble("defaultR"), variableData.getDouble("defaultI")));
            }
        } catch (Exception e) {
            throw new EquationError("Given file could not be loaded and produced error: " + e.getMessage());
        }

    }
}
