package analizzatore.prototipo;

import analizzatore.prototipo.model.RisultatoHTTP;
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
 */
public class HTTP extends AbstractStateMachine {
    private File f_input;
    private String message = null;
    private String protocol;
    private String fileName;
    private RisultatoHTTP ris;
    private State currentState;
    private int pipeline = 0;

    public HTTP(File f_input, String protocol) {
        super(HTTP.class.getClassLoader().getResource(HTTP_RESOURCE));
        this.f_input = f_input;
        this.protocol = protocol;
        this.fileName = f_input.getAbsolutePath();
    }

    public RisultatoHTTP run() throws ProtocolMismatchException, TransitionNotValidException, TransitionNotFoundException {
        int countPackets = 0;
        try {
            String protocol = null;
            CSVParser parser = new CSVParser(new FileReader(f_input), CSVFormat.DEFAULT.withFirstRecordAsHeader().withSkipHeaderRecord(true));
            for (CSVRecord r : parser) {
                countPackets++;
                protocol = r.get(4);
                if (!protocol.equals(HTTP_PROTOCOL_NAME))
                    throw new ProtocolMismatchException("Esiste un pacchetto di un altro protocollo: " + protocol);
                message = r.get(6);
                if (message.contains("TCP")) {
                    ris.addExtraInfo("Expert message:");
                    ris.addExtraInfo(message);
                } else {
                    String transition = extractTransition(message);
                    if (transition == null)
                        throw new TransitionNotFoundException("Non esiste transizione corrispondente a " + transition + " per il protocollo scelto.");
                    Set<State> states = this.getEngine().getCurrentStatus().getAllStates();
                    boolean check = false;
                    List<Transition> transitions = null;
                    for (State s: states) {
                        System.out.println("CIAO" + s.getId());
                        transitions = s.getTransitionsList();
                        for (Transition t : transitions){
                            System.out.println(t.getEvent());
                            if (transition.contains(t.getEvent())) {
                                check = true;
                                ris.addEvent(transition);
                                ris.addExtraInfo(message);
                            } }
                    }
                    if (!check)
                        throw new TransitionNotValidException("Transizione non valida: " + transition + ". Le transizioni applicabili da questo stato sono:\n", transitions);
                    this.fireEvent(transition.substring(0,1));
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        ris.setFileName(fileName);
        ris.setProtocol(protocol);
        ris.setCountPackets(countPackets);
        return ris;
    }

    public String extractTransition(String message){
        Pattern p = null;
        Matcher m = null;
        for(String s: HTTP_TRANSITIONS){
            p = Pattern.compile(s);
            m = p.matcher(message);
            if(m.find()){
                return m.group();
            }
        }
        return null;
    }

    /*Ciascuno dei metodi sottostanti corrisponde a una degli stati dello schema
    della macchina a stati finiti.
     */

    public void NeedRequest(){
        if(ris == null) {
            ris = new RisultatoHTTP();
            ris.createStates();
        }
        System.out.println("ciao");
        ris.incrementNumberStates("NeedRequest");
        ris.addState("NeedRequest");
    }

    public void SentRequest(){
        pipeline++;
        ris.incrementNumberStates("SentRequest");
        ris.addState("SentRequest");
    }

    public void Information(){
        pipeline--;
        if(pipeline == 0)
            this.fireEvent("no_pipeline");
        else
            this.fireEvent("pipeline");
        ris.incrementNumberStates("Information");
        ris.addState("Information");
    }

    public void Successful(){
        pipeline--;
        if(pipeline == 0)
            this.fireEvent("no_pipeline");
        else
            this.fireEvent("pipeline");
        ris.incrementNumberStates("Successful");
        ris.addState("Successful");
    }

    public void Redirection(){
        pipeline--;
        if(pipeline == 0)
            this.fireEvent("no_pipeline");
        else
            this.fireEvent("pipeline");
        ris.incrementNumberStates("Redirection");
        ris.addState("Redirection");
    }

    public void ClientError(){
        pipeline--;
        if(pipeline == 0)
            this.fireEvent("no_pipeline");
        else
            this.fireEvent("pipeline");
        ris.incrementNumberStates("ClientError");
        ris.addState("ServerError");
    }

    public void ServerError(){
        pipeline--;
        if(pipeline == 0)
            this.fireEvent("no_pipeline");
        else
            this.fireEvent("pipeline");
        ris.incrementNumberStates("ServerError");
        ris.addState("ServerError");
    }
}