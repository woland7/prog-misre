package analizzatore.prototipo.model;

import static analizzatore.prototipo.Constants.DHCP_STATES;

/**
 * Created by Antonio on 20/08/2016.
 */
public class RisultatoDHCP extends Risultato {

    public RisultatoDHCP(){
        super();
    }

    public void createStates(){
        for(String s: DHCP_STATES)
            super.getNumberStates().put(s, 0);
    }

    public void incrementNumberStates(String key){
        super.getNumberStates().replace(key, super.getNumberStates().get(key) + 1);
    }
}