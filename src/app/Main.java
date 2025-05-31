package app;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import utils.SceneManager;

public class Main extends Application {
    @Override
   
public void start(Stage primaryStage) {
    SceneManager.getInstance().init(primaryStage);
    SceneManager.getInstance().showLogin();
}


    public static void main(String[] args) {
        launch(args);
    }
}


    

