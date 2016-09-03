package analizzatore.prototipo.model;

import static analizzatore.prototipo.Constants.DHCP_STATES;

/**
 * Created by Antonio on 20/08/2016.
 */
public class ResultDHCP extends Result {

    public ResultDHCP(){
        super();
    }

    public void createStates(){
        for(String s: DHCP_STATES)
            super.getNumberStates().put(s, 0);
    }
}