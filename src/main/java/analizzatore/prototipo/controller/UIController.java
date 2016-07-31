package analizzatore.prototipo.controller;

import analizzatore.prototipo.AnalizzatoreUI;
import javafx.fxml.FXML;
import javafx.scene.control.Label;


/**
 * Created by Antonio on 31/07/2016.
 */
public class UIController {
    @FXML
    private Label file_analizzato;
    @FXML
    private Label output;

    private AnalizzatoreUI aui;

    public UIController(){

    }

    @FXML
    private void initialize(){
        file_analizzato.setText("Nessun file selezionato.");
        output.setText("");
    }

    @FXML
    private void handleNuovo(){

    }

    public void setAnalizzatoreUI(AnalizzatoreUI aui){
        this.aui = aui;
    }
}