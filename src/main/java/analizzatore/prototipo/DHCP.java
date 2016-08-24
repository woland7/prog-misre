package analizzatore.prototipo;

import analizzatore.prototipo.model.RisultatoDHCP;
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

import static analizzatore.prototipo.Constants.*;


/**
 * Created by Antonio on 04/06/2016.
 */
public class DHCP extends AbstractStateMachine {
    private File f_input;
    private String message = null;
    private String protocol;
    private String fileName;
    private RisultatoDHCP ris;
    private State currentState;

    public DHCP(File f_input, String protocol) {
        super(DHCP.class.getClassLoader().getResource(DHCP_RESOURCE));
        this.f_input = f_input;
        this.protocol = protocol;
        this.fileName = f_input.getAbsolutePath();
    }

    public RisultatoDHCP run() throws ProtocolMismatchException, TransitionNotValidException, TransitionNotFoundException{
        int countPackets = 0;
        try
        {
            String protocol = null;
            CSVParser parser = new CSVParser(new FileReader(f_input), CSVFormat.DEFAULT.withFirstRecordAsHeader().withSkipHeaderRecord(true));
            for(CSVRecord r: parser){
                countPackets++;
                protocol = r.get(4);
                if(!protocol.equals(DHCP_PROTOCOL_NAME))
                    throw new ProtocolMismatchException("Esiste un pacchetto di un altro protocollo: " + protocol);
                message = r.get(6);
                int lastIndex = message.lastIndexOf('-');
                message = message.substring(0,lastIndex).trim();
                if(!DHCP_TRANSITIONS.contains(message))
                    throw new TransitionNotFoundException("La transizione " + message + " non esiste per il protocollo scelto.");
                Set<State> states = this.getEngine().getCurrentStatus().getAllStates();
                boolean check = false;
                List<Transition> transitions = null;
                for(State s: states){
                    transitions = s.getTransitionsList();
                    for(Transition t: transitions)
                        if (message.equals(t.getEvent())) {
                            check = true;
                            ris.addEvent(message);
                            ris.addExtraInfo(DHCP_EXTRAINFO.get(s.getId()+"-"+message));
                        }
                }
                if(!check)
                    throw new TransitionNotValidException("Transizione non valida: " + message +". Le transizioni applicabili da questo stato sono:\n", transitions);
                this.fireEvent(message);
            }
        }catch(FileNotFoundException e){
            e.printStackTrace();
        }
        catch(IOException e) {
            e.printStackTrace();
        }
        ris.setFileName(fileName);
        ris.setProtocol(protocol);
        ris.setCountPackets(countPackets);
        return ris;
    }

    /*Ciascuno dei metodi sottostanti corrisponde a una degli stati dello schema
    della macchina a stati finiti.
     */

    public void Init(){
        if(ris == null) {
            ris = new RisultatoDHCP();
            ris.createStates();
        }
        ris.incrementNumberStates("Init");
        ris.addState("Init");
    }

    public void Selecting(){
        if(ris.getNumberStates().get("Selecting") <= 1)
            ris.addState("Selecting");
        else{
            ris.addState("Selecting");
            ris.addExtraInfo("Si stanno collezionando più offerte.");
        }
        ris.incrementNumberStates("Selecting");
    }

    public void Requesting(){
        ris.incrementNumberStates("Requesting");
        ris.addState("Requesting");
    }

    public void Bound(){
        ris.incrementNumberStates("Bound");
        ris.addState("Bound");
    }

    public void Renewing(){
        ris.incrementNumberStates("Renewing");
        ris.addState("Renewing");
    }

    public void Rebinding(){
        ris.incrementNumberStates("Rebinding");
        ris.addState("Rebinding");
    }
}