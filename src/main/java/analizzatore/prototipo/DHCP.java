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
    private StringBuilder sb = new StringBuilder();

    public DHCP(File f_input) {
        super(DHCP.class.getClassLoader().getResource("dhcp.scxml"));
        this.f_input = f_input;
    }

    public String run(){
        try
        {
            CSVParser parser = new CSVParser(new FileReader(f_input), CSVFormat.DEFAULT.withFirstRecordAsHeader().withSkipHeaderRecord(true));
            for(CSVRecord r: parser){
                String message = r.get(6);
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
        return sb.toString();
    }

    /*Ciascuno dei metodi sottostanti corrisponde a una degli stati dello schema
    della macchina a stati finiti.
     */

    public void init(){
        sb.append("Sono in init...\n");
    }

    public void waiting4Offer() {
        sb.append("Sono in waiting4Offer\n");
    }

    public void selecting() {
        sb.append("Sono in selecting\n");
    }

    public void waiting4Acceptance() {
        sb.append("Sono in waiting4Acceptance\n");
    }

    public void bound() {
        sb.append("Sono in bound\n");
    }
}