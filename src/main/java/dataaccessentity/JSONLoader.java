package dataaccessentity;
import entity.EquationError;
import org.json.JSONObject;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public class JSONLoader {
    public static JSONObject readJSON(String path){
        if (!Files.exists(Path.of(path)))
            throw new EquationError("File path is invalid.");
        try {
            String raw = Files.readString(Path.of(path), StandardCharsets.UTF_8).trim();
            return new JSONObject(raw);
        } catch (Exception e) {
            throw new EquationError("Given file could not be read properly and gave error: " + e.getMessage());
        }
    }
}
