package analizzatore.prototipo;

import analizzatore.prototipo.model.ResultHTTP;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.scxml.env.AbstractStateMachine;
import org.apache.commons.scxml.model.State;
import org.apache.commons.scxml.model.Transition;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static analizzatore.prototipo.Constants.HTTP_PROTOCOL_NAME;
import static analizzatore.prototipo.Constants.HTTP_RESOURCE;
import static analizzatore.prototipo.Constants.HTTP_TRANSITIONS;


/**
 * Created by Antonio on 04/06/2016.
 * Estende la classe AbstractStateMachine e consente la verifica di tracce appartenenti al protocollo HTTP.
 */
public class HTTP extends AbstractStateMachine {
    private File f_input;
    private String message;
    private ResultHTTP ris;
    private String currentState;
    private int pipeline = 0;

    public HTTP(File f_input) {
        super(HTTP.class.getClassLoader().getResource(HTTP_RESOURCE));
        this.f_input = f_input;
    }

    public ResultHTTP run() throws ProtocolMismatchException, TransitionNotValidException, TransitionNotFoundException {
        int countPackets = 0;
        boolean check1 = false;
        try {
            String protocol = null;
            CSVParser parser = new CSVParser(new FileReader(f_input), CSVFormat.DEFAULT.withFirstRecordAsHeader().withSkipHeaderRecord(true));
            for (CSVRecord r : parser) {
                countPackets++;
                protocol = r.get(4);
                if (!protocol.equals(HTTP_PROTOCOL_NAME))
                    throw new ProtocolMismatchException("Esiste un pacchetto di un altro protocollo: " + protocol
                            + "\nFai attenzione alla scelta del protocollo.");
                message = r.get(6);
                if (message.contains("TCP")) {
                    ris.addExtraInfo("Condizione particolare:");
                    ris.addExtraInfo(message);
                } else if(message.contains("Continuation")){
                    ris.addExtraInfo("Condizione particolare:");
                    ris.addExtraInfo(message);
                } else {
                    String transition = extractTransition(message);
                    if (transition == null)
                        throw new TransitionNotFoundException("La transizione " + message + " non esiste per il protocollo scelto.\n");
                    Set<State> states = this.getEngine().getCurrentStatus().getAllStates();
                    boolean check = false;
                    if(pipeline > 1 && check1 == false){
                        ris.addExtraInfo("Inizio pipeline.");
                        check1 = true;
                    }
                    List<Transition> transitions = null;
                    for (State s: states) {
                        transitions = s.getTransitionsList();
                        for (Transition t : transitions)
                            if (transition.contains(t.getEvent())) {
                                check = true;
                                ris.addEvent(transition);
                                ris.addExtraInfo(message);
                            }
                    }
                    if (!check)
                        throw new TransitionNotValidException("Stato: "
                            + currentState + ". Transizione non valida: " + message + ". Le transizioni applicabili da questo stato sono:\n", transitions);

                    this.fireEvent(transition.substring(0,1));

                    if(!currentState.equals("NeedRequest") || !currentState.equals("SentRequest")){
                        if(pipeline == 0) {
                            if(check1){
                                System.out.println("ciao");
                                check1 = false;
                                ris.addExtraInfo("Fine pipeline.");
                            }
                            this.fireEvent("no_pipeline");
                        }
                        else
                            this.fireEvent("pipeline");
                    }
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        ris.setFileName(f_input.getAbsolutePath());
        ris.setProtocol(HTTP_PROTOCOL_NAME);
        ris.setCountPackets(countPackets);
        ris.addNumberStatesInfo();
        return ris;
    }

    public String extractTransition(String message){
        Pattern p = null;
        Matcher m = null;
        for(String s: HTTP_TRANSITIONS){
            p = Pattern.compile(s);
            m = p.matcher(message);
            if(m.find())
                return m.group();
        }
        return null;
    }

    /*Ciascuno dei metodi sottostanti corrisponde a una degli stati dello schema
    della macchina a stati finiti.
     */

    public void NeedRequest(){
        if(ris == null) {
            ris = new ResultHTTP();
            ris.createStates();
        }
        ris.addState("NeedRequest");
        currentState = "NeedRequest";
    }

    public void SentRequest(){
        if(currentState.equals("NeedRequest") || currentState.equals("SentRequest"))
            pipeline++;
        ris.addState("SentRequest");
        currentState = "SentRequest";
    }

    public void Information(){
        ris.addState("Information");
        pipeline--;
        currentState = "Information";
    }

    public void Successful(){
        ris.addState("Successful");
        pipeline--;
        currentState = "Successful";
    }

    public void Redirection(){
        ris.addState("Redirection");
        pipeline--;
        currentState = "Redirection";
    }

    public void ClientError(){
        ris.addState("ServerError");
        pipeline--;
        currentState = "ClientError";
    }

    public void ServerError(){
        ris.addState("ServerError");
        pipeline--;
        currentState = "ServerError";
    }
}