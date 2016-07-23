import org.apache.commons.scxml.env.AbstractStateMachine;
import org.apache.commons.csv.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;

/**
 * Created by Antonio on 04/06/2016.
 */
public class DHCP extends AbstractStateMachine {
    private File f_input;

    public DHCP(File f_input, URL u_input) {
        super(u_input);
        this.f_input = f_input;
    }

    public void run(){
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
    }

    /*Ciascuno dei metodi sottostanti corrisponde a una degli stati dello schema
    della macchina a stati finit.
     */

    public void init(){
        System.out.println("Sono in init");
    }

    public void waiting4Offer(){
        System.out.println("Sono in waiting4Offer");
    }

    public void selecting(){
        System.out.println("Sono in selecting");
    }

    public void waiting4Acceptance(){
        System.out.println("Sono in waiting4Acceptance");
    }

    public void bound(){
        System.out.println("Sono in bound");
    }
}