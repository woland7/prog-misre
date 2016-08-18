package analizzatore.prototipo;

import analizzatore.prototipo.model.Risultato;
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
import java.util.Iterator;
import java.util.List;
import java.util.Set;


/**
 * Created by Antonio on 04/06/2016.
 */
public class DHCP extends AbstractStateMachine {
    private File f_input;
    private String message = null;
    private String protocol;
    private final String protocolException = "C'è la presenza di un pacchetto di un altro protocollo.";

    public DHCP(File f_input, String protocol) {
        super(DHCP.class.getClassLoader().getResource("\\dhcp.scxml"));
        this.f_input = f_input;
        this.protocol = protocol;
    }

    public Risultato run() throws ProtocolMismatchException{
        Risultato ris = new Risultato();
        try
        {
            String protocol = null;
            CSVParser parser = new CSVParser(new FileReader(f_input), CSVFormat.DEFAULT.withFirstRecordAsHeader().withSkipHeaderRecord(true));
            for(CSVRecord r: parser){
                protocol = r.get(4);
                if(!protocol.equals(this.protocol))
                    throw new ProtocolMismatchException(protocolException + ": " + protocol);
                message = r.get(6);
                int lastIndex = message.lastIndexOf('-');
                message = message.substring(0,lastIndex);
                message.trim();
                this.getCurrentState();
                State state = this.getCurrentState();
                List<Transition> list = state.getTransitionsList();
                for(Transition t: list){
                    System.out.println(t.getEvent());
                    if(t.getEvent().equals(message))
                        this.fireEvent(message);
                }
            }
        }catch(FileNotFoundException e)
        {
            e.printStackTrace();
        }
        catch(IOException e) {
            e.printStackTrace();
        }
        return ris;
    }

    public State getCurrentState() {
        Set s = this.getEngine().getCurrentStatus().getStates();
        Iterator iter = s.iterator();
        State st = (State) iter.next();
        System.out.println(st.getId());
        return st;
    }

    /*Ciascuno dei metodi sottostanti corrisponde a una degli stati dello schema
    della macchina a stati finiti.
     */

    public void init(){
        System.out.println("Init-> ");
    }

    public void selecting(){
        selecting++;
        System.out.println("Transition: " + message);
        if(selecting <= 2)
            System.out.println("Selecting->");
        else
            System.out.println("I've received more than one offers and I'm collecting them. Still in Selecting->");



    }

    public void requesting(){
        requesting++;
        System.out.println("Transition: " + message);
        System.out.println("Requesting->");
    }

    public void bound(){
        bound++;
        System.out.println("Transition: " + message);
        System.out.println("Bound->");
    }

    public void renewing(){
        renewing++;
        System.out.println("Transition: " + message);
        System.out.println("Renewing->");
    }

    public void rebinding(){
        rebinding++;
        System.out.println("Transition: " + message);
        System.out.println("Rebinding->");
    }

    public int getSelecting(){
        return selecting;
    }

    public int getRequesting(){
        return requesting;
    }

    public int getBound(){
        return bound;
    }

    public int getRenewing(){
        return renewing;
    }

    public int getRebinding(){
        return rebinding;
    }

    private int selecting = 0, requesting = 0, bound = 0, renewing = 0, rebinding = 0;
}