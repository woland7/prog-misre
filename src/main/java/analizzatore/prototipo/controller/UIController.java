package analizzatore.prototipo.controller;

import analizzatore.prototipo.*;
import analizzatore.prototipo.model.Risultato;
import analizzatore.prototipo.model.RisultatoDHCP;
import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextArea;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.apache.commons.scxml.model.Transition;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Set;

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

    @FXML
    private BarChart<String,Number> istogramma;

    @FXML
    private CategoryAxis xAxis;

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
        if(file != null)
            output.appendText("File aperto con successo. Clicca analizza...\n");
    }

    @FXML
    private void handleAnalizza()throws IOException{
        output.appendText("Sto analizzando il file....\n");
        if(choiceProtocol.getValue().equals(DHCP_PROTOCOL_NAME)) {
            DHCP dhcp = new DHCP(file, DHCP_PROTOCOL_NAME);
            try {
                RisultatoDHCP ris = dhcp.run();
                compute(ris);
            } catch (ProtocolMismatchException e) {
                output.appendText(e.getMessage() + "Fai attenzione alla scelta del protocollo.");

            } catch (TransitionNotFoundException e) {
                output.appendText(e.getMessage());
            } catch (TransitionNotValidException e) {
                output.appendText(e.getMessage());
                for(Transition t: e.getTransitions())
                    output.appendText(t.getEvent()+"\n");
            }
        }
        else{
            DHCP http = new DHCP(file, choiceProtocol.getValue().toString());
            try{
                RisultatoDHCP ris = http.run();
            }
            catch(ProtocolMismatchException e){
                output.appendText(e.getMessage() + "Fai attenzione alla scelta del protocollo.");
            }
            catch(TransitionNotFoundException e){

            }
            catch(TransitionNotValidException e){

            }
        }
    }

    private void compute(Risultato ris) throws IOException{
        this.handleResult(ris);
        this.handleIstogramma(ris.getNumberStates());
        if (salvaFile.isSelected())
            this.saveToFile(ris.getResultString());
    }

    private void handleIstogramma(HashMap<String,Integer> numberStates){
        XYChart.Series<String,Number> frequenze = new XYChart.Series();
        Set<String> keySet = numberStates.keySet();
        xAxis.setLabel("Stati");
        for(String s: keySet)
            frequenze.getData().add(new XYChart.Data(s,numberStates.get(s)));
        istogramma.getData().add(frequenze);
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