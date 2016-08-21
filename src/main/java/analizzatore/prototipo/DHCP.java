package analizzatore.prototipo;

import analizzatore.prototipo.model.Risultato;
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
import static analizzatore.prototipo.Constants.DHCP_TRANSITIONS;


/**
 * Created by Antonio on 04/06/2016.
 */
public class DHCP extends AbstractStateMachine {
    private File f_input;
    private String message = null;
    private String protocol;
    private String fileName;
    private RisultatoDHCP ris;

    public DHCP(File f_input, String protocol) {
        super(DHCP.class.getClassLoader().getResource("dhcp.scxml"));
        this.f_input = f_input;
        this.protocol = protocol;
        this.fileName = f_input.getAbsolutePath();
    }

    public Risultato run() throws ProtocolMismatchException, TransitionNotValidException, TransitionNotFoundException{
        int countPackets = 0;
        try
        {
            String protocol = null;
            CSVParser parser = new CSVParser(new FileReader(f_input), CSVFormat.DEFAULT.withFirstRecordAsHeader().withSkipHeaderRecord(true));
            for(CSVRecord r: parser){
                countPackets++;
                protocol = r.get(4);
                if(!protocol.equals("DHCP"))
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
                        }
                }
                if(!check)
                    throw new TransitionNotValidException("Not valid transition: " + message, transitions);
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

    public void init(){
        if(ris == null) {
            ris = new RisultatoDHCP();
            ris.createStates();
        }
        ris.incrementNumberStates("Init");
        ris.addState("Init");
    }

    public void selecting(){
        if(ris.getNumberStates().get("Selecting") <= 1)
            ris.addState("Selecting");
        else{
            ris.addState("Selecting");
            ris.addExtraInfo("Collecting more than one offer.");
        }
        ris.incrementNumberStates("Selecting");
    }

    public void requesting(){
        ris.incrementNumberStates("Requesting");
        ris.addState("Requesting");
    }

    public void bound(){
        ris.incrementNumberStates("Bound");
        ris.addState("Bound");
    }

    public void renewing(){
        ris.incrementNumberStates("Renewing");
        ris.addState("Renewing");
    }

    public void rebinding(){
        ris.incrementNumberStates("Rebinding");
        ris.addState("Rebinding");
    }
}