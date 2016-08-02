package analizzatore.prototipo.controller;

import analizzatore.prototipo.AnalizzatoreUI;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;


/**
 * Created by Antonio on 31/07/2016.
 */
public class UIController {
    @FXML
    private Label file_analizzato;
    @FXML
    private Label output;

    private AnalizzatoreUI aui;

    private File file = null;

    public UIController(){

    }

    @FXML
    private void initialize(){
        file_analizzato.setText("Nessun file selezionato.");
        output.setText("");
    }

    @FXML
    private void handleNuovo(){
        FileChooser fC = new FileChooser();
        fC.setTitle("Scegli capture file da analizzare");
        FileChooser.ExtensionFilter ef = new FileChooser.ExtensionFilter("CSV", "*.csv");
        fC.setInitialDirectory(new File(System.getProperty("user.home")));
        fC.getExtensionFilters().add(ef);
        file = fC.showOpenDialog(new Stage());
    }

    public void setAnalizzatoreUI(AnalizzatoreUI aui){
        this.aui = aui;
    }
}