package usecase.save;

import entity.fractalRenderer.FractalRenderer;
import org.json.JSONArray;
import org.json.JSONObject;
import usecase.Solver;
import usecase.SolverHasDefault;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

public class SaveFractalUsecase {
    private final Solver solver;
    private final Solver colorSolver;
    private final FractalRenderer fr;

    public SaveFractalUsecase(Solver solver, Solver colorSolver, FractalRenderer fractalRenderer){
        this.solver = solver;
        this.colorSolver = colorSolver;
        this.fr = fractalRenderer;
    }

    public void save(String path) throws IOException {
        JSONObject data = new JSONObject();

        data.put("varAmt", solver.getVariableCount());
        data.put("centerR", fr.getCenter().getR());
        data.put("centerI", fr.getCenter().getI());
        data.put("radius", fr.getRadius());
        data.put("maxIter", fr.getMaxIteration());
        data.put("bailVar", fr.getBailVariable());
        data.put("bailMin", fr.getBailMin());
        data.put("bailMax", fr.getBailMax());

        data.put("REquation", colorSolver.getEquation(0));
        data.put("GEquation", colorSolver.getEquation(1));
        data.put("BEquation", colorSolver.getEquation(2));

        JSONArray variableDataArray = new JSONArray();

        for (int i = 0; i < solver.getVariableCount(); i++){
            JSONObject variableData = new JSONObject();
            variableData.put("index", i);
            variableData.put("defaultR", ((SolverHasDefault)solver).getDefault(i).getR());
            variableData.put("defaultI", ((SolverHasDefault)solver).getDefault(i).getI());
            variableData.put("equation", solver.getEquation(i));
            variableDataArray.put(variableData);
        }

        data.put("variables", variableDataArray);

        Path filePath = Path.of(path);
        Path file = Files.createFile(filePath);
        Files.writeString(file, data.toString(2), StandardOpenOption.TRUNCATE_EXISTING);
    }
}
