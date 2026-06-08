package usecase.load;

import dataaccessentity.JSONLoader;

public class LoadFractalUsecase {
    public LoadFractalUsecase(){

    }
    public void loadFractal(String path){
        JSONLoader.readJSON(path);
    }
}
