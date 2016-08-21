package analizzatore.prototipo.model;

import java.util.HashMap;

import static analizzatore.prototipo.Constants.DHCP_STATES;

/**
 * Created by Antonio on 20/08/2016.
 */
public class RisultatoDHCP extends Risultato {
    private HashMap<String, Integer> states;

    public RisultatoDHCP(){
        super();
    }

    public void createStates(){
        this.states = new HashMap<String, Integer>();
        for(String s: DHCP_STATES)
            states.put(s, 0);
    }

    public void incrementNumberStates(String key){
        states.replace(key, states.get(key) + 1);
    }

    public HashMap<String, Integer> getNumberStates(){
        return this.states;
    }
}