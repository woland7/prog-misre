/**
 * Created by Antonio on 04/06/2016.
 */
import java.io.File;
import java.net.MalformedURLException;

public class Analizzatore {
    public static void main(String[] args){
        System.out.println("Versione di prova");
        DHCP p = null;
        try{
            p = new DHCP(new File("esempio_prova.csv"), new File("dhcp.scxml").toURI().toURL());
        }catch(MalformedURLException e){
            e.printStackTrace();
        }
        p.run();
    }
}