package analizzatore.prototipo;

import analizzatore.prototipo.model.ResultDHCP;
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
 * Estende la classe AbstractStateMachine e consente la verifica di tracce appartenenti al protocollo DHCP.
 */
public class DHCP extends AbstractStateMachine {
    private File f_input;
    private String message = null;
    private ResultDHCP ris;
    private String currentState;
    private String initialState;

    public DHCP(File f_input) {
        super(DHCP.class.getClassLoader().getResource(DHCP_RESOURCE)); //costruttore della superclasse
        this.f_input = f_input; //file di input, da esaminare
    }

    public ResultDHCP run() throws ProtocolMismatchException, TransitionNotValidException, TransitionNotFoundException{
        int countPackets = 0; //contatore dei pacchetti
        try
        {
            String protocol = null;
            CSVParser parser = new CSVParser(new FileReader(f_input), CSVFormat.DEFAULT.withFirstRecordAsHeader().withSkipHeaderRecord(true));
            for(CSVRecord r: parser){
                protocol = r.get(4); //estrazione del campo relativo al tipo di protocollo
                if(!protocol.equals(DHCP_PROTOCOL_NAME)) //controllo sul tipo di protocollo
                    throw new ProtocolMismatchException("Esiste un pacchetto di un altro protocollo: " + protocol
                            + "\nBisogna porre attenzione all'utilizzo del filtro su Wireshark.\n" +
                            "Oppure bisogna porre attenzione al protocollo selezionato dal menu dell'interfaccia.\n");
                countPackets++; //incremento del numero dei pacchetti
                message = r.get(6); //estrazione del campo relativo alle informazioni
                /*
                Si sfrutta la formattazione di Wireshark.
                 */
                int lastIndex = message.lastIndexOf('-');
                message = message.substring(0,lastIndex).trim();
                if(!DHCP_TRANSITIONS.contains(message)) //controllo che la transizione sia presente
                    throw new TransitionNotFoundException("La transizione " + message + " non esiste per il protocollo scelto.\n");
                Set<State> states = this.getEngine().getCurrentStatus().getAllStates(); //insieme degli stati attivi in quel momento
                boolean check = false;
                List<Transition> transitions = null;
                for(State s: states){ //per ogni stato si ottiene la lista delle transizioni
                    currentState = s.getId();
                    transitions = s.getTransitionsList();
                    for(Transition t: transitions) //per ogni transizione si verifica l'applicabilità dell'evento presente in message
                        if (message.equals(t.getEvent())) { //qualora si verifichi ciò
                            check = true;
                            ris.addEvent(message); //si aggiunge l'evento ai risultati
                            ris.addExtraInfo(DHCP_EXTRAINFO.get(currentState+"-"+message)); //si aggiungono informazioni extra caratterizzanti quell'evento
                        }
                }
                if(!check) //qualora l'evento non sia applicabile. Viene anche stampata la lista degli eventi possibili da quello stato.
                    throw new TransitionNotValidException("Stato: "
                            + currentState + ". Transizione non valida: " + message +". Le transizioni applicabili da questo stato sono:\n", transitions);
                this.fireEvent(message);
            }
        }catch(FileNotFoundException e){
            e.printStackTrace();
        }
        catch(IOException e) {
            e.printStackTrace();
        }
        /*
        Impostazione di parametri per il risultato.
         */
        ris.setFileName(f_input.getAbsolutePath());
        ris.setProtocol(DHCP_PROTOCOL_NAME);
        ris.setCountPackets(countPackets);
        ris.addNumberStatesInfo();
        return ris;
    }

    /*Ciascuno dei metodi sottostanti corrisponde a una degli stati dello schema
    della macchina a stati finiti.
     */

    /*
    Dal momento che
     */
    public void Init(){
        if(ris == null) {
            ris = new ResultDHCP();
            ris.createStates();
            initialState = this.getEngine().getStateMachine().getInitial();
        }
        ris.addState(initialState);
    }

    public void Selecting(){
        ris.addState("Selecting");
        if(ris.getNumberStates().get("Selecting") > 1)
            ris.addExtraInfo("Si stanno collezionando più offerte.");
    }

    public void Requesting(){
        ris.addState("Requesting");
    }

    public void Bound(){
        ris.addState("Bound");
    }

    public void Renewing(){
        ris.addState("Renewing");
    }

    public void Rebinding(){
        ris.addState("Rebinding");
    }
}