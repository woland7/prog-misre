/**
 * Created by Antonio on 31/07/2016.
 */

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.IOException;

public class Analizzatore_UI extends Application {
    private Stage primaryStage;
    private BorderPane ui;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("Analizzatore-Prototipo");

        initUI();
    }

    public void initUI(){
        try{
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Analizzatore_UI.class.getResource("/ui.fxml"));
            ui = (BorderPane) loader.load();

            Scene scene = new Scene(ui);
            primaryStage.setScene(scene);
            primaryStage.show();
        }catch(IOException e){
            e.printStackTrace();
        }
    }

    public Stage getPrimaryStage(){
        return primaryStage;
    }
}
