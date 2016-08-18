package analizzatore.prototipo.controller;

import analizzatore.prototipo.AnalizzatoreUI;
import analizzatore.prototipo.DHCP;
import analizzatore.prototipo.ProtocolMismatchException;
import analizzatore.prototipo.model.Risultato;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
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

    @FXML
    private ChoiceBox<String> choiceProtocol;

    public UIController(){

    }

    @FXML
    private void initialize(){
        file_analizzato.setText("Nessun file selezionato.");
        choiceProtocol.getSelectionModel().selectFirst();
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
        Risultato ris = null;
        switch(choiceProtocol.getValue()){
            case DHCP:  DHCP dhcp = new DHCP(file, choiceProtocol.getValue().toString());
                        try{
                            ris = dhcp.run();
                        }
                        catch(ProtocolMismatchException e){
                            e.getMessage();
                            System.exit(1);
                        }
                        break;
            case HTTP:  DHCP http = new DHCP(file, choiceProtocol.getValue().toString());
                        try{
                            ris = http.run();
                        }
                        catch(ProtocolMismatchException e){
                            e.getMessage();
                            System.exit(1);
                        }
                        break;
        }
        this.handleResult(ris);

    }

    private void handleResult(Risultato ris){

    }

    public void setAnalizzatoreUI(AnalizzatoreUI aui){
        this.aui = aui;
    }

    enum String{DHCP, HTTP}
}