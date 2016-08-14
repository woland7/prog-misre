package analizzatore.prototipo;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.scxml.env.AbstractStateMachine;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;


/**
 * Created by Antonio on 04/06/2016.
 */
public class DHCP extends AbstractStateMachine {
    private File f_input;
    private String message = null;

    public DHCP(File f_input) {
        super(DHCP.class.getClassLoader().getResource("/dhcp.scxml"));
        this.f_input = f_input;
    }

    public void run(){
        try
        {
            CSVParser parser = new CSVParser(new FileReader(f_input), CSVFormat.DEFAULT.withFirstRecordAsHeader().withSkipHeaderRecord(true));
            for(CSVRecord r: parser){
                message = r.get(6);
                int lastIndex = message.lastIndexOf('-');
                message = message.substring(0,lastIndex);
                message.trim();
                this.fireEvent(message);
            }
        }catch(FileNotFoundException e)
        {
            e.printStackTrace();
        }
        catch(IOException e) {
            e.printStackTrace();
        }
    }

    /*Ciascuno dei metodi sottostanti corrisponde a una degli stati dello schema
    della macchina a stati finit.
     */

    public void init(){
        System.out.println("Init-> ");
        System.out.println("");
    }

    public void selecting(){
        selecting++;
        System.out.println("Selecting->");

    }

    public void requesting(){
        requesting++;
        System.out.println("Requesting->");
    }

    public void bound(){
        bound++;
        System.out.println("Bound->");
    }

    public void renewing(){
        renewing++;
        System.out.println("Renewing->");
    }

    public void rebinding(){
        rebinding++;
        System.out.println("Rebinding->");
    }

    public int getInit(){
        return init;
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

    private int init = 0, selecting = 0, requesting = 0, bound = 0, renewing = 0, rebinding = 0;
}