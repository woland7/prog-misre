package analizzatore.prototipo;

import org.apache.commons.scxml.env.AbstractStateMachine;

import java.io.File;

/**
 * Created by Antonio on 13/08/2016.
 */
public class HTTP extends AbstractStateMachine {
    private File f_input;

    public HTTP(File f_input, String s_input) {
        super(HTTP.class.getClassLoader().getResource(s_input));
        this.f_input = f_input;
    }
}
