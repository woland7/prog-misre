package analizzatore.prototipo.controller;

import analizzatore.prototipo.*;
import analizzatore.prototipo.model.Result;
import analizzatore.prototipo.model.ResultDHCP;
import analizzatore.prototipo.model.ResultHTTP;
import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
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
    private AnalyserUI aui;

    private File file = null;

    @FXML
    private TextArea output;

    @FXML
    private ChoiceBox<String> choiceProtocol;

    @FXML
    private CheckBox saveFile;

    @FXML
    private BarChart<String,Number> histogram;

    @FXML
    private CategoryAxis xAxis;

    @FXML
    private NumberAxis yAxis;

    @FXML
    private Label label_file;

    public UIController(){
    }

    @FXML
    private void initialize(){
        choiceProtocol.getSelectionModel().selectFirst();
        xAxis.setLabel("Stati");
        yAxis.setTickUnit(1);
    }

    @FXML
    private void handleNuovo(){
        FileChooser fC = new FileChooser();
        fC.setTitle("Scegli capture da analizzare...");
        FileChooser.ExtensionFilter ef = new FileChooser.ExtensionFilter("CSV", "*.csv");
        fC.setInitialDirectory(new File(System.getProperty("user.home")));
        fC.getExtensionFilters().add(ef);
        file = fC.showOpenDialog(new Stage());
        label_file.setText(file.getName());
        if(file != null)
            output.appendText("File aperto con successo. Clicca analizza...\n");
    }

    @FXML
    private void handleAnalizza()throws IOException{
        handlePulisci();
        output.appendText("Sto analizzando il file....\n");
        if(choiceProtocol.getValue().equals(DHCP_PROTOCOL_NAME)) {
            DHCP dhcp = new DHCP(file);
            try {
                ResultDHCP ris = dhcp.run();
                compute(ris);
            } catch (ProtocolMismatchException e) {
                output.appendText(e.getMessage());
            } catch (TransitionNotFoundException e) {
                output.appendText(e.getMessage());
            } catch (TransitionNotValidException e) {
                output.appendText(e.getMessage());
                if(e.getTransitions() != null)
                    for(Transition t: e.getTransitions())
                        output.appendText(t.getEvent()+"\n");
            }
        }
        else{
            HTTP http = new HTTP(file);
            try{
                ResultHTTP ris = http.run();
                compute(ris);
            } catch (ProtocolMismatchException e) {
                output.appendText(e.getMessage());
            } catch (TransitionNotFoundException e) {
                output.appendText(e.getMessage());
            } catch (TransitionNotValidException e) {
                output.appendText(e.getMessage());
                for(Transition t: e.getTransitions())
                    output.appendText(t.getEvent()+"\n");
            }
        }
    }

    private void compute(Result ris) throws IOException{
        this.handleResult(ris);
        this.handleIstogramma(ris.getNumberStates());
        if (saveFile.isSelected())
            this.saveToFile(ris.getResultString());
    }

    private void handleIstogramma(HashMap<String,Integer> numberStates){
        XYChart.Series<String,Number> frequenze = new XYChart.Series();
        Set<String> keySet = numberStates.keySet();
        for(String s: keySet)
            frequenze.getData().add(new XYChart.Data(s,numberStates.get(s)));
        histogram.getData().add(frequenze);
    }

    private void saveToFile(String result) throws IOException{
        FileChooser fC = new FileChooser();
        fC.setTitle("Scegli file di output...");
        fC.setInitialFileName("risultato");
        FileChooser.ExtensionFilter ef = new FileChooser.ExtensionFilter("TXT", "*.txt");
        fC.getExtensionFilters().add(ef);
        fC.setInitialDirectory(new File(System.getProperty("user.home")));
        file = fC.showSaveDialog(new Stage());
        try(BufferedWriter writer = new BufferedWriter(new FileWriter(file))){
            writer.write(result);
        }
    }

    private void handleResult(Result ris){
        output.appendText(ris.getResultString());
    }

    @FXML
    private void handlePulisci(){
        output.setText("");
        histogram.getData().clear();
    }

    @FXML
    private void handleAbout(){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("About");
        alert.setHeaderText("Prototipo di analizzatore");
        alert.setContentText("Progetto sviluppato da\nAntonio De Iasio\nGianluca Giso\nLuca Miranda\n" +
                "Protocolli supportati: DHCP, HTTP");
        alert.showAndWait();
    }
}