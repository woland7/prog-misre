package analizzatore.prototipo.controller;

import analizzatore.prototipo.*;
import analizzatore.prototipo.model.Risultato;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextArea;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import static analizzatore.prototipo.Constants.DHCP_PROTOCOL_NAME;

/**
 * Created by Antonio on 31/07/2016.
 */
public class UIController {
    private AnalizzatoreUI aui;

    private File file = null;

    @FXML
    private TextArea output;

    @FXML
    private ChoiceBox<String> choiceProtocol;

    @FXML
    private CheckBox salvaFile;

    public UIController(){
    }

    @FXML
    private void initialize(){
        output.setText("Nessun file selezionato.\n");
        choiceProtocol.getSelectionModel().selectFirst();
    }

    @FXML
    private void handleNuovo(){
        FileChooser fC = new FileChooser();
        fC.setTitle("Scegli capture da analizzare...");
        FileChooser.ExtensionFilter ef = new FileChooser.ExtensionFilter("CSV", "*.csv");
        fC.setInitialDirectory(new File(System.getProperty("user.home")));
        fC.getExtensionFilters().add(ef);
        file = fC.showOpenDialog(new Stage());
        output.appendText("Clicca analizza...\n");
    }

    @FXML
    private void handleAnalizza()throws IOException{
        output.appendText("Sto analizzando il file....\n");
        Risultato ris = null;
        if(choiceProtocol.getValue().equals(DHCP_PROTOCOL_NAME)) {
            DHCP dhcp = new DHCP(file, DHCP_PROTOCOL_NAME);
            try {
                ris = dhcp.run();
            } catch (ProtocolMismatchException e) {
                output.appendText(e.getMessage() + "Fai attenzione alla scelta del protocollo.");

            } catch (TransitionNotFoundException e) {
                output.appendText(e.getMessage());
            } catch (TransitionNotValidException e) {

            }
        }
        else{
            DHCP http = new DHCP(file, choiceProtocol.getValue().toString());
            try{
                ris = http.run();
            }
            catch(ProtocolMismatchException e){
                output.appendText(e.getMessage() + "Fai attenzione alla scelta del protocollo.");
            }
            catch(TransitionNotFoundException e){

            }
            catch(TransitionNotValidException e){

            }
        }
        this.handleResult(ris);
        if(salvaFile.isSelected()){
            this.saveToFile(ris.getResultString());
        }
    }

    private void saveToFile(String result) throws IOException{
        FileChooser fC = new FileChooser();
        fC.setTitle("Scegli file di output...");
        fC.setInitialFileName("risultato");
        FileChooser.ExtensionFilter ef = new FileChooser.ExtensionFilter("TXT", "*.TXT");
        fC.getExtensionFilters().add(ef);
        fC.setInitialDirectory(new File(System.getProperty("user.home")));
        file = fC.showSaveDialog(new Stage());
        try(BufferedWriter writer = new BufferedWriter(new FileWriter(file))){
            writer.write(result);
        }
    }

    private void handleResult(Risultato ris){
        output.appendText(ris.getResultString());
    }

    @FXML
    private void handlePulisci(){
        output.setText("");
    }

    public void setAnalizzatoreUI(AnalizzatoreUI aui){
        this.aui = aui;
    }
}