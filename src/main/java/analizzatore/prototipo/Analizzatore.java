/**
 * Created by Antonio on 04/06/2016.
 */
package analizzatore.prototipo;

import java.io.File;
import java.net.MalformedURLException;

public class Analizzatore {
    public static void main(String[] args) {
        System.out.println("Versione di prova");
        DHCP p = new DHCP(new File("build\\resources\\main\\esempio_prova.csv"));
        p.run();
    }
}