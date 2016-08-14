package analizzatore.prototipo.controller;

import analizzatore.prototipo.AnalizzatoreUI;
import analizzatore.prototipo.DHCP;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;


/**
 * Created by Antonio on 31/07/2016.
 */
public class UIController {
    @FXML
    private Label file_analizzato;

    private AnalizzatoreUI aui;

    private File file = null;

    @FXML
    private TextArea output;

    public UIController(){

    }

    @FXML
    private void initialize(){
        file_analizzato.setText("Nessun file selezionato.");
    }

    @FXML
    private void handleNuovo(){
        FileChooser fC = new FileChooser();
        fC.setTitle("Scegli capture file da analizzare");
        FileChooser.ExtensionFilter ef = new FileChooser.ExtensionFilter("CSV", "*.csv");
        fC.setInitialDirectory(new File(System.getProperty("user.home")));
        fC.getExtensionFilters().add(ef);
        file = fC.showOpenDialog(new Stage());
        file_analizzato.setText(file.getName());
    }

    @FXML
    private void handleAnalizza(){
        this.output.setText("Sto analizzando il file....\n");
        DHCP dhcp = new DHCP(file);
        String output = dhcp.run();
        this.output.appendText(output);
    }

    public void setAnalizzatoreUI(AnalizzatoreUI aui){
        this.aui = aui;
    }
}